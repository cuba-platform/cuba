/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Eugeniy Degtyarjov
 * Created: 30.12.2009 13:03:19
 *
 * $Id$
 */
package com.haulmont.cuba.core.sys.javacl.compiler;

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
    private final Map<String, JavaFileObject> classes = new HashMap<String, JavaFileObject>();

    ClassLoaderImpl(final ClassLoader parentClassLoader) {
        super(parentClassLoader);
    }

    /**
     * @return An collection of JavaFileObject instances for the classes in the
     *         class loader.
     */
    Collection<JavaFileObject> files() {
        return Collections.unmodifiableCollection(classes.values());
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
    protected synchronized Class<?> loadClass(final String name, final boolean resolve)
            throws ClassNotFoundException {
        Class clazz = findClass(name);
        if (clazz != null)
            return clazz;
        else
            return super.loadClass(name, resolve);
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
