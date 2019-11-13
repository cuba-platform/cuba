/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.haulmont.cuba.core.sys.javacl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Multimap;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.core.sys.SpringBeanLoader;
import com.haulmont.cuba.core.sys.javacl.compiler.CharSequenceCompiler;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.perf4j.StopWatch;
import org.perf4j.slf4j.Slf4JStopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component("cuba_JavaClassLoader")
public class JavaClassLoader extends URLClassLoader {
    private static final String JAVA_CLASSPATH = System.getProperty("java.class.path");
    private static final String PATH_SEPARATOR = System.getProperty("path.separator");
    private static final String JAR_EXT = ".jar";

    private static final Logger log = LoggerFactory.getLogger(JavaClassLoader.class);

    protected final String cubaClassPath;
    protected final String classPath;

    protected final String rootDir;

    protected final Map<String, TimestampClass> compiled = new ConcurrentHashMap<>();
    protected final ConcurrentHashMap<String, Lock> locks = new ConcurrentHashMap<>();

    protected final ProxyClassLoader proxyClassLoader;
    protected final SourceProvider sourceProvider;
    protected final ClassFilesProvider classFilesProvider;

    @Inject
    protected TimeSource timeSource;
    @Inject
    protected SpringBeanLoader springBeanLoader;

    @Inject
    public JavaClassLoader(Configuration configuration) {
        super(new URL[0], Thread.currentThread().getContextClassLoader());

        this.proxyClassLoader = new ProxyClassLoader(Thread.currentThread().getContextClassLoader(), compiled);
        GlobalConfig config = configuration.getConfig(GlobalConfig.class);
        this.rootDir = config.getConfDir() + "/";
        this.cubaClassPath = config.getCubaClasspathDirectories();
        this.classPath = buildClasspath();
        this.sourceProvider = new SourceProvider(rootDir);
        this.classFilesProvider = new ClassFilesProvider(rootDir);
    }

    //Please use this constructor only in tests
    JavaClassLoader(ClassLoader parent, String rootDir, String cubaClassPath, SpringBeanLoader springBeanLoader) {
        super(new URL[0], parent);

        Preconditions.checkNotNull(rootDir);
        Preconditions.checkNotNull(cubaClassPath);

        this.proxyClassLoader = new ProxyClassLoader(parent, compiled);
        this.springBeanLoader = springBeanLoader;
        this.rootDir = rootDir;
        this.cubaClassPath = cubaClassPath;
        this.classPath = buildClasspath();
        this.sourceProvider = new SourceProvider(rootDir);
        this.classFilesProvider = new ClassFilesProvider(rootDir);
    }

    public void clearCache() {
        compiled.clear();
    }

    @Override
    public Class loadClass(final String fullClassName, boolean resolve) throws ClassNotFoundException {
        String containerClassName = StringUtils.substringBefore(fullClassName, "$");

        StopWatch loadingWatch = new Slf4JStopWatch("LoadClass");
        try {
            lock(containerClassName);
            Class clazz;

            //first check if there is a ".class" file in the "conf" directory
            File classFile = classFilesProvider.getClassFile(containerClassName);
            if (classFile.exists()) {
                return loadClassFromClassFile(fullClassName, containerClassName, classFile);
            }

            //then check if a ".java" source file is deployed to the "conf" directory
            if (sourceProvider.getSourceFile(containerClassName).exists()) {
                return loadClassFromJavaSources(fullClassName, containerClassName);
            }

            //default class loading
            clazz = super.loadClass(fullClassName, resolve);
            return clazz;
        } finally {
            unlock(containerClassName);
            loadingWatch.stop();
        }
    }

    protected Class loadClassFromJavaSources(String fullClassName, String containerClassName) throws ClassNotFoundException {
        Class clazz;
        CompilationScope compilationScope = new CompilationScope(this, containerClassName);
        if (!compilationScope.compilationNeeded()) {
            TimestampClass timestampClass = getTimestampClass(fullClassName);
            if (timestampClass == null) {
                throw new ClassNotFoundException(fullClassName);
            }
            return timestampClass.clazz;
        }

        String src;
        try {
            src = sourceProvider.getSourceString(containerClassName);
        } catch (IOException e) {
            throw new ClassNotFoundException("Could not load java sources for class " + containerClassName);
        }

        try {
            log.debug("Compiling " + containerClassName);
            final DiagnosticCollector<JavaFileObject> errs = new DiagnosticCollector<>();

            SourcesAndDependencies sourcesAndDependencies = new SourcesAndDependencies(rootDir, this);
            sourcesAndDependencies.putSource(containerClassName, src);
            sourcesAndDependencies.collectDependencies(containerClassName);
            Map<String, CharSequence> sourcesForCompilation = sourcesAndDependencies.collectSourcesForCompilation(containerClassName);

            @SuppressWarnings("unchecked")
            Map<String, Class> compiledClasses = createCompiler().compile(sourcesForCompilation, errs);

            Map<String, TimestampClass> compiledTimestampClasses = wrapCompiledClasses(compiledClasses);
            compiled.putAll(compiledTimestampClasses);
            linkDependencies(compiledTimestampClasses, sourcesAndDependencies.dependencies);

            clazz = compiledClasses.get(fullClassName);

            springBeanLoader.updateContext(compiledClasses.values());

            return clazz;
        } catch (Exception e) {
            proxyClassLoader.restoreRemoved();
            throw new RuntimeException(e);
        } finally {
            proxyClassLoader.cleanupRemoved();
        }
    }

    protected Class loadClassFromClassFile(String fullClassName, String containerClassName, File classFile) {
        TimestampClass timestampClass = compiled.get(containerClassName);
        if (timestampClass != null && !FileUtils.isFileNewer(classFile, timestampClass.timestamp)) {
            return timestampClass.clazz;
        }
        Map<String, Class> loadedClasses = new HashMap<>();
        Map<String, Path> modifiedClassFiles = collectModifiedClassFiles(rootDir);
        FileClassLoader fileClassLoader = new FileClassLoader(proxyClassLoader, rootDir, modifiedClassFiles.keySet());
        for (String fqn : modifiedClassFiles.keySet()) {
            Class clazz;
            try {
                clazz = fileClassLoader.loadClass(fqn);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Class not found", e);
            }
            loadedClasses.put(fqn, clazz);
            compiled.put(fqn, new TimestampClass(clazz, getCurrentTimestamp()));
        }
        springBeanLoader.updateContext(loadedClasses.values());
        return loadedClasses.get(fullClassName);
    }

    /**
     * Collects class files that were modified or not loaded yet from the "conf" directory
     */
    protected Map<String, Path> collectModifiedClassFiles(String rootDir) {
        Map<String, Path> result = new HashMap<>();
        Path root = Paths.get(rootDir);
        try {
            Files.walk(root)
                    .forEach(path -> {
                        if (Files.isDirectory(path) || !path.toString().endsWith(".class")) {
                            return;
                        }
                        String fqn = root.relativize(path).toString();
                        fqn = fqn.substring(0, fqn.length() - 6).replace(File.separator, ".");
                        TimestampClass timeStampClass = getTimestampClass(fqn);
                        if (timeStampClass == null || FileUtils.isFileNewer(path.toFile(), timeStampClass.timestamp)) {
                            result.put(fqn, path);
                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException("Error on traversing the directory " + rootDir, e);
        }
        return result;
    }

    /**
     * Class loader is used for building class instances from ".class" files. Loading from file happens only for classes with FQN from the {@code
     * modifiedFQNs} collection passed to the constructor. Loading of all other classes is delegated to parent class loaders.
     */
    protected static class FileClassLoader extends ClassLoader {

        private final Map<String, Class> loadedClasses = new HashMap<>();
        private String rootDir;
        private Set<String> modifiedFQNs;

        /**
         * @param parent parent class loader
         * @param rootDir a root directory (usually a "conf" dir)
         * @param modifiedFQNs a set of classes fully qualified names that should be loaded by this class loader
         */
        FileClassLoader(ClassLoader parent, String rootDir, Set<String> modifiedFQNs) {
            super(parent);
            this.rootDir = rootDir;
            this.modifiedFQNs = modifiedFQNs;
        }

        @Override
        protected Class<?> loadClass(String fqn, boolean resolve) throws ClassNotFoundException {
            Class clazz = loadedClasses.get(fqn);
            if (clazz != null) return clazz;
            if (modifiedFQNs.contains(fqn)) {
                Path pathToClassFile = fqnToPath(fqn);
                if (Files.exists(pathToClassFile)) {
                    try {
                        byte[] bytes = Files.readAllBytes(pathToClassFile);
                        clazz = defineClass(fqn, bytes, 0, bytes.length);
                        loadedClasses.put(fqn, clazz);
                    } catch (IOException e) {
                        throw new RuntimeException("Error on reading file content", e);
                    }
                }
            }
            return super.loadClass(fqn, resolve);
        }

        private Path fqnToPath(String fqn) {
            String[] packageNameParts = fqn.split("\\.");
            packageNameParts[packageNameParts.length - 1] = packageNameParts[packageNameParts.length - 1] + ".class";
            return Paths.get(rootDir, packageNameParts);
        }
    }

    public boolean removeClass(String className) {
        TimestampClass removed = compiled.remove(className);
        if (removed != null) {
            for (String dependent : removed.dependent) {
                removeClass(dependent);
            }
        }
        return removed != null;
    }

    public boolean isLoadedClass(String className) {
        return compiled.containsKey(className);
    }

    public Collection<String> getClassDependencies(String className) {
        TimestampClass timestampClass = compiled.get(className);
        if (timestampClass != null) {
            return timestampClass.dependencies;
        }
        return Collections.emptyList();
    }

    public Collection<String> getClassDependent(String className) {
        TimestampClass timestampClass = compiled.get(className);
        if (timestampClass != null) {
            return timestampClass.dependent;
        }
        return Collections.emptyList();
    }

    @Override
    public URL findResource(String name) {
        if (name.startsWith("/"))
            name = name.substring(1);
        File file = new File(rootDir, name);
        if (file.exists()) {
            try {
                return file.toURI().toURL();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        } else
            return null;
    }

    @Override
    public URL getResource(String name) {
        URL resource = findResource(name);
        if (resource != null)
            return resource;
        else
            return super.getResource(name);
    }

    protected Date getCurrentTimestamp() {
        return timeSource.currentTimestamp();
    }

    TimestampClass getTimestampClass(String name) {
        return compiled.get(name);
    }

    /**
     * Wrap each compiled class with TimestampClass
     */
    private Map<String, TimestampClass> wrapCompiledClasses(Map<String, Class> compiledClasses) {
        Map<String, TimestampClass> compiledTimestampClasses = new HashMap<>();

        for (Map.Entry<String, Class> entry : compiledClasses.entrySet()) {
            compiledTimestampClasses.put(entry.getKey(), new TimestampClass(entry.getValue(), getCurrentTimestamp()));
        }

        return compiledTimestampClasses;
    }

    /**
     * Add dependencies for each class and ALSO add each class to dependent for each dependency
     */
    private void linkDependencies(Map<String, TimestampClass> compiledTimestampClasses,
                                  Multimap<String, String> dependencies) {
        for (Map.Entry<String, TimestampClass> entry : compiledTimestampClasses.entrySet()) {
            String className = entry.getKey();
            TimestampClass timestampClass = entry.getValue();

            Collection<String> dependencyClasses = dependencies.get(className);
            timestampClass.dependencies.addAll(dependencyClasses);

            for (String dependencyClassName : timestampClass.dependencies) {
                TimestampClass dependencyClass = compiled.get(dependencyClassName);
                if (dependencyClass != null) {
                    dependencyClass.dependent.add(className);
                }
            }
        }
    }

    private CharSequenceCompiler createCompiler() {
        return new CharSequenceCompiler(
                proxyClassLoader,
                Arrays.asList("-classpath", classPath, "-g")
        );
    }

    private void unlock(String name) {
        locks.get(name).unlock();
    }

    private void lock(String name) {//not sure it's right, but we can not use synchronization here
        locks.putIfAbsent(name, new ReentrantLock());
        locks.get(name).lock();
    }

    private String buildClasspath() {
        StringBuilder classpathBuilder = new StringBuilder(JAVA_CLASSPATH).append(PATH_SEPARATOR);

        if (cubaClassPath != null) {
            String[] directories = cubaClassPath.split(";");
            for (String directoryPath : directories) {
                if (StringUtils.isNotBlank(directoryPath)) {
                    classpathBuilder.append(directoryPath).append(PATH_SEPARATOR);
                    File directory = new File(directoryPath);
                    File[] directoryFiles = directory.listFiles();
                    if (directoryFiles != null) {
                        for (File file : directoryFiles) {
                            if (file.getName().endsWith(JAR_EXT)) {
                                classpathBuilder.append(file.getAbsolutePath()).append(PATH_SEPARATOR);
                            }
                        }
                    }
                }
            }
        }
        return classpathBuilder.toString();
    }
}