/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys;

import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;

/**
 * @author degtyarjov
 * @version $Id$
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
