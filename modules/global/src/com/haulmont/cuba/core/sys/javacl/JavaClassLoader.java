/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Eugeniy Degtyarjov
 * Created: 28.12.2009 17:30:25
 *
 * $Id$
 */
package com.haulmont.cuba.core.sys.javacl;

import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.javacl.compiler.CharSequenceCompiler;
import org.apache.commons.io.FileUtils;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.global.ConfigProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaFileObject;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class JavaClassLoader extends URLClassLoader {

    private static class TimestampClass {
        Class clazz;
        Date timestamp;

        private TimestampClass(Class clazz, Date timestamp) {
            this.clazz = clazz;
            this.timestamp = timestamp;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            TimestampClass that = (TimestampClass) o;

            if (clazz != null ? !clazz.equals(that.clazz) : that.clazz != null) return false;
            if (timestamp != null ? !timestamp.equals(that.timestamp) : that.timestamp != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = clazz != null ? clazz.hashCode() : 0;
            result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
            return result;
        }
    }

    private Log log = LogFactory.getLog(JavaClassLoader.class);

    protected CharSequenceCompiler compiler;

    private String rootDir;

    private final Map<String, TimestampClass> compiled = new ConcurrentHashMap<String, TimestampClass>();

    private final static JavaClassLoader jcl = new JavaClassLoader(
            Thread.currentThread().getContextClassLoader(),
            ConfigProvider.getConfig(GlobalConfig.class).getConfDir() + "/"
    );

    public static JavaClassLoader getInstance() {
        return jcl;
    }

    private JavaClassLoader(ClassLoader parent, String rootDir) {
        super(new URL[0], parent);
        this.rootDir = rootDir;
        compiler = new CharSequenceCompiler(
                Thread.currentThread().getContextClassLoader(),
                Arrays.asList("-g", "-classpath", buildClasspath())
        );
    }

    private TimestampClass getTimestampClass(String name) {
        return compiled.get(name);
    }

    private void setTimestampClass(String name, TimestampClass clazz) {
        compiled.put(name, clazz);
    }

    @Deprecated
    private Map<String, CharSequence> collectDependentClasses(String name, String src) {
        Map<String, CharSequence> classes = new HashMap<String, CharSequence>();
        classes.put(name, src);
        String[] dependence = src.split("import");
        if (dependence.length > 1) {
            for (int i = 1, length = dependence.length - 1; i < length; i++) {
                String className = dependence[i].trim().replaceAll(";", "");
                if (validSource(className))
                    classes.put(className, getSourceString(className));
            }
            String last = dependence[dependence.length - 1];
            int importsEnd = last.indexOf(";");
            String lastDependent = last.substring(0, importsEnd).trim();
            if (validSource(lastDependent)) {
                String lastDependentSource = getSourceString(lastDependent);
                classes.put(lastDependent, lastDependentSource);
            }
        }
        return classes;
    }

    private Map<String, Boolean> importedClasses = new ConcurrentHashMap<String, Boolean>();
    
    private boolean validSource(String className) {
        Boolean valid = importedClasses.get(className);
        if (valid != null)
            return valid;

        String path = className.replace('.', '/');
        File file = new File(rootDir + "/" + path + ".java");
        valid = file.exists();
        importedClasses.put(className, valid);
        return valid;
    }

    //todo: draft of cyclic classloader. reimplement.
    private Map<String, CharSequence> collectDependentClasses(Map<String, CharSequence> loaded) {
        List<String> loadedNames = new ArrayList<String>();
        loadedNames.addAll(loaded.keySet());
        for (String className : loadedNames) {
            CharSequence src = loaded.get(className);
            List<String> imports = getImports(src);
            for (String importedClassName : imports) {
                if (!loaded.containsKey(importedClassName)) {
                    loaded.put(importedClassName, getSourceString(importedClassName));
                    collectDependentClasses(loaded);
                }
            }

        }
        return loaded;
    }

    private List<String> getImports(CharSequence src) {
        List<String> importedClassNames = new ArrayList<String>();

        String[] dependence = src.toString().split("import");
        if (dependence.length > 1) {
            for (int i = 1, length = dependence.length - 1; i < length; i++) {
                String dependentClassName = dependence[i].trim().replaceAll(";", "");
                if (validSource(dependentClassName)) {
                    importedClassNames.add(dependentClassName);
                }
            }
            String last = dependence[dependence.length - 1];
            int importsEnd = last.indexOf(";");
            String lastDependentClassName = last.substring(0, importsEnd).trim();
            if (validSource(lastDependentClassName)) {
                importedClassNames.add(lastDependentClassName);
            }
        }
        return importedClassNames;
    }

    public Class loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class clazz = null;
        try {
            clazz = super.loadClass(name, resolve);
        } catch (ClassNotFoundException e) {
            //
        }

        if (clazz != null)
            return clazz;

        File srcFile = getSourceFile(name);
        if (!srcFile.exists())
            throw new ClassNotFoundException("Java source for " + name + " not found");

        TimestampClass timeStampClazz = getTimestampClass(name);
        if (timeStampClazz != null) {
            if (!FileUtils.isFileNewer(srcFile, timeStampClazz.timestamp))
                return timeStampClazz.clazz;
        }

        try {
            log.debug("Compiling " + name);

            String src = FileUtils.readFileToString(srcFile);
            final DiagnosticCollector<JavaFileObject> errs = new DiagnosticCollector<JavaFileObject>();

            HashMap<String, CharSequence> sources = new HashMap<String, CharSequence>();
            sources.put(name, src);

            Map compiledClasses = compiler.compile(collectDependentClasses(sources), errs);

            clazz = (Class) compiledClasses.get(name);
            setTimestampClass(name, new TimestampClass(clazz, new Date(srcFile.lastModified())));

            return clazz;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String buildClasspath() {
        StringBuilder sb = new StringBuilder(System.getProperty("java.class.path") + System.getProperty("path.separator"));

        String directoriesStr = AppContext.getProperty("cuba.classpath.directories");
        String[] directories = directoriesStr.split(";");
        for (String directory : directories) {
            if (!"".equalsIgnoreCase(directory)) {
                sb.append(directory);
                File _dir = new File(directory);
                for (File file : _dir.listFiles()) {
                    if (file.getName().endsWith(".jar")) {
                        sb.append(file.getAbsolutePath()).append(System.getProperty("path.separator"));
                    }
                }
            }
        }
        return sb.toString();
    }

    private File getSourceFile(String name) {
        String path = name.replace(".", "/");
        File srcFile = new File(rootDir + path + ".java");
        return srcFile;
    }

    private String getSourceString(String name) {
        try {
            return FileUtils.readFileToString(getSourceFile(name));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
