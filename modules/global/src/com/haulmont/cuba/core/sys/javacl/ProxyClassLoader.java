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

import java.util.HashMap;
import java.util.Map;

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