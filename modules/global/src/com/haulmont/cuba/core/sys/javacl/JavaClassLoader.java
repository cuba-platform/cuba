/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.sys.javacl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Multimap;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.core.sys.CubaClassPathXmlApplicationContext;
import com.haulmont.cuba.core.sys.javacl.compiler.CharSequenceCompiler;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.perf4j.log4j.Log4JStopWatch;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@ManagedBean("cuba_JavaClassLoader")
public class JavaClassLoader extends URLClassLoader implements BeanFactoryAware, ApplicationContextAware {
    private static final String JAVA_CLASSPATH = System.getProperty("java.class.path");
    private static final String PATH_SEPARATOR = System.getProperty("path.separator");
    private static final String JAR_EXT = ".jar";

    private static Log log = LogFactory.getLog(JavaClassLoader.class);

    protected final String cubaClassPath;
    protected final String classPath;

    protected final String rootDir;

    protected final Map<String, TimestampClass> compiled = new ConcurrentHashMap<>();
    protected final ConcurrentHashMap<String, Lock> locks = new ConcurrentHashMap<>();

    protected final ProxyClassLoader proxyClassLoader;
    protected final SourceProvider sourceProvider;

    protected CubaClassPathXmlApplicationContext applicationContext;
    protected DefaultListableBeanFactory beanFactory;

    @Inject
    private TimeSource timeSource;

    @Inject
    public JavaClassLoader(Configuration configuration) {
        super(new URL[0], Thread.currentThread().getContextClassLoader());

        this.proxyClassLoader = new ProxyClassLoader(Thread.currentThread().getContextClassLoader(), compiled);
        GlobalConfig config = configuration.getConfig(GlobalConfig.class);
        this.rootDir = config.getConfDir() + "/";
        this.cubaClassPath = config.getCubaClasspathDirectories();
        this.classPath = buildClasspath();
        this.sourceProvider = new SourceProvider(rootDir);
    }

    //Please use this constructor only in tests
    JavaClassLoader(ClassLoader parent, String rootDir, String cubaClassPath) {
        super(new URL[0], parent);

        Preconditions.checkNotNull(rootDir);
        Preconditions.checkNotNull(cubaClassPath);

        this.proxyClassLoader = new ProxyClassLoader(parent, compiled);
        this.rootDir = rootDir;
        this.cubaClassPath = cubaClassPath;
        this.classPath = buildClasspath();
        this.sourceProvider = new SourceProvider(rootDir);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        if (beanFactory instanceof DefaultListableBeanFactory) {
            this.beanFactory = (DefaultListableBeanFactory) beanFactory;
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (applicationContext instanceof CubaClassPathXmlApplicationContext) {
//            ((DefaultResourceLoader) applicationContext).setClassLoader(this);
            this.applicationContext = (CubaClassPathXmlApplicationContext) applicationContext;
        }
    }

    public void clearCache() {
        compiled.clear();
    }

    public Class loadClass(final String fullClassName, boolean resolve) throws ClassNotFoundException {
        String containerClassName = StringUtils.substringBefore(fullClassName, "$");

        Log4JStopWatch loadingWatch = new Log4JStopWatch("LoadClass");
        try {
            lock(containerClassName);
            Class clazz;

            if (!sourceProvider.getSourceFile(containerClassName).exists()) {
                clazz = super.loadClass(fullClassName, resolve);
                return clazz;
            }

            CompilationScope compilationScope = new CompilationScope(this, containerClassName);
            if (!compilationScope.compilationNeeded()) {
                return getTimestampClass(fullClassName).clazz;
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

                updateSpringContext(compiledClasses.values());

                return clazz;
            } catch (Exception e) {
                proxyClassLoader.restoreRemoved();
                throw new RuntimeException(e);
            } finally {
                proxyClassLoader.cleanupRemoved();
            }
        } finally {
            unlock(containerClassName);
            loadingWatch.stop();
        }
    }

    private void updateSpringContext(Collection<Class> classes) {
        if (beanFactory != null) {
            boolean needToRefreshRemotingContext = false;
            for (Class clazz : classes) {
                Service serviceAnnotation = (Service) clazz.getAnnotation(Service.class);
                ManagedBean managedBeanAnnotation = (ManagedBean) clazz.getAnnotation(ManagedBean.class);
                Component componentAnnotation = (Component) clazz.getAnnotation(Component.class);
                Controller controllerAnnotation = (Controller) clazz.getAnnotation(Controller.class);

                String beanName = null;
                if (serviceAnnotation != null) {
                    beanName = serviceAnnotation.value();
                } else if (managedBeanAnnotation != null) {
                    beanName = managedBeanAnnotation.value();
                } else if (componentAnnotation != null) {
                    beanName = componentAnnotation.value();
                } else if (controllerAnnotation != null) {
                    beanName = controllerAnnotation.value();
                }

                if (StringUtils.isNotBlank(beanName)) {
                    GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
                    beanDefinition.setBeanClass(clazz);
                    beanFactory.registerBeanDefinition(beanName, beanDefinition);
                }

                if (StringUtils.isNotBlank(beanName)) {
                    needToRefreshRemotingContext = true;
                }
            }

            if (needToRefreshRemotingContext) {
                ApplicationContext remotingContext = RemotingContextHolder.getRemotingApplicationContext();
                if (remotingContext != null && remotingContext instanceof ConfigurableApplicationContext) {
                    ((ConfigurableApplicationContext) remotingContext).refresh();
                }
            }
        }
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
    private void linkDependencies(Map<String, TimestampClass> compiledTimestampClasses, Multimap<String, String> dependecies) {
        for (Map.Entry<String, TimestampClass> entry : compiledTimestampClasses.entrySet()) {
            String className = entry.getKey();
            TimestampClass timestampClass = entry.getValue();

            Collection<String> dependencyClasses = dependecies.get(className);
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