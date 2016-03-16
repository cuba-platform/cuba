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

package com.haulmont.cuba.core.sys;

import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;

/**
 */
public class CubaSingleAppClassLoader extends URLClassLoader {
    public CubaSingleAppClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    public CubaSingleAppClassLoader(URL[] urls) {
        super(urls);
    }

    public CubaSingleAppClassLoader(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory) {
        super(urls, parent, factory);
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        try {
            Class<?> aClass = findLoadedClass(name);

            if (aClass == null) {
                aClass = findClass(name);
            }

            if (resolve) {
                resolveClass(aClass);
            }

            return aClass;
        } catch (ClassNotFoundException e) {
            return super.loadClass(name, resolve);
        }
    }
}
