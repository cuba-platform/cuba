/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.sys.javacl.compiler;

import com.haulmont.cuba.core.sys.javacl.ProxyClassLoader;
import org.apache.commons.lang.StringUtils;

import javax.tools.JavaFileObject;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A custom ClassLoader which maps class names to JavaFileObjectImpl instances.
 */
final class ClassLoaderImpl extends ClassLoader {
    private final Map<String, JavaFileObject> classes = new HashMap<>();
    private final ProxyClassLoader proxyClassLoader;

    ClassLoaderImpl(final ProxyClassLoader proxyClassLoader) {
        super(proxyClassLoader);
        this.proxyClassLoader = proxyClassLoader;
    }

    /**
     * @return An collection of JavaFileObject instances for the classes in the
     *         class loader.
     */
    Collection<JavaFileObject> files() {
        return Collections.unmodifiableCollection(classes.values());
    }

    Collection<String> classNames() {
        return Collections.unmodifiableCollection(classes.keySet());
    }

    @Override
    protected Class<?> findClass(final String qualifiedClassName)
            throws ClassNotFoundException {
        JavaFileObject file = classes.get(qualifiedClassName);
        if (file != null) {
            JavaFileObjectImpl castedFile = (JavaFileObjectImpl) file;
            if (castedFile.definedClass != null) {
                return castedFile.definedClass;
            } else {

                byte[] bytes = castedFile.getByteCode();
                Class<?> justDefinedClass = defineClass(qualifiedClassName, bytes, 0, bytes.length);
                castedFile.definedClass = justDefinedClass;
                return justDefinedClass;
            }
        }
        // Workaround for "feature" in Java 6
        // see http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6434149
        try {
            Class<?> c = Class.forName(qualifiedClassName);
            return c;
        } catch (ClassNotFoundException nf) {
            // Ignore and fall through
        }
        return super.findClass(qualifiedClassName);
    }

    /**
     * Add a class name/JavaFileObject mapping
     *
     * @param qualifiedClassName the name
     * @param javaFile           the file associated with the name
     */
    void add(final String qualifiedClassName, final JavaFileObject javaFile) {
        classes.put(qualifiedClassName, javaFile);
    }

    @Override
    protected synchronized Class<?> loadClass(final String qualifiedClassName, final boolean resolve)
            throws ClassNotFoundException {
        if (!cacheContainsFirstLevelClass(qualifiedClassName)) {
            Class clazz = findClass(qualifiedClassName);
            if (clazz != null) {
                return clazz;
            } else {
                return super.loadClass(qualifiedClassName, resolve);
            }
        } else {
            return super.loadClass(qualifiedClassName, resolve);
        }
    }

    private boolean cacheContainsFirstLevelClass(String qualifiedClassName) {
        String outerClassName = StringUtils.substringBefore(qualifiedClassName, "$");
        return proxyClassLoader.contains(outerClassName);
    }

    @Override
    public InputStream getResourceAsStream(final String name) {
        if (name.endsWith(".class")) {
            String qualifiedClassName = name.substring(0,
                    name.length() - ".class".length()).replace('/', '.');
            JavaFileObjectImpl file = (JavaFileObjectImpl) classes.get(qualifiedClassName);
            if (file != null) {
                return new ByteArrayInputStream(file.getByteCode());
            }
        }
        return super.getResourceAsStream(name);
    }
}