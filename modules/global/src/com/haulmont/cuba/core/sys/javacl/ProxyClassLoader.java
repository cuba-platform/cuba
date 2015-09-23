/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.sys.javacl;

import java.util.HashMap;
import java.util.Map;

/**
 * @author degtyarjov
 * @version $Id$
 */
public class ProxyClassLoader extends ClassLoader {
    Map<String, TimestampClass> compiled;
    ThreadLocal<Map<String, TimestampClass>> removedFromCompilation = new ThreadLocal<>();

    ProxyClassLoader(ClassLoader parent, Map<String, TimestampClass> compiled) {
        super(parent);
        this.compiled = compiled;
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        TimestampClass tsClass = compiled.get(name);
        if (tsClass != null) {
            return tsClass.clazz;
        } else {
            return super.loadClass(name, resolve);
        }
    }

    public TimestampClass removeFromCache(String className) {
        Map<String, TimestampClass> removedFromCompilationMap = removedFromCompilation.get();
        if (removedFromCompilationMap == null) {
            removedFromCompilationMap = new HashMap<>();
            removedFromCompilation.set(removedFromCompilationMap);
        }

        TimestampClass timestampClass = compiled.get(className);
        if (timestampClass != null) {
            removedFromCompilationMap.put(className, timestampClass);
            compiled.remove(className);
            return timestampClass;
        }

        return null;
    }

    public void restoreRemoved() {
        Map<String, TimestampClass> map = removedFromCompilation.get();
        if (map != null) {
            compiled.putAll(map);
        }
        removedFromCompilation.remove();
    }

    public void cleanupRemoved() {
        removedFromCompilation.remove();
    }

    public boolean contains(String className) {
        return compiled.containsKey(className);
    }
}
