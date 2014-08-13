/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.javacl;

import javax.annotation.ManagedBean;
import javax.inject.Inject;

/**
 * @author degtyarjov
 * @version $Id$
 */
@ManagedBean("cuba_ClassLoaderManagement")
public class ClassLoaderManagement implements ClassLoaderManagementMBean {
    @Inject
    protected JavaClassLoader javaClassLoader;

    @Override
    public Class loadClass(String className) throws ClassNotFoundException {
        return javaClassLoader.loadClass(className);
    }

    @Override
    public Class removeClass(String className) {
        TimestampClass removed = javaClassLoader.compiled.remove(className);
        if (removed != null) {
            for (String dependent : removed.dependent) {
                removeClass(dependent);
            }
        }
        return removed != null ? removed.clazz : null;
    }

    @Override
    public Class reloadClass(String className) throws ClassNotFoundException {
        removeClass(className);
        return loadClass(className);
    }

    @Override
    public String getClassDependencies(String className) {
        TimestampClass timestampClass = javaClassLoader.compiled.get(className);
        if (timestampClass != null) {
            return String.format("Dependencies \n%s\nDependent \n%s", timestampClass.dependencies, timestampClass.dependent);
        }

        return null;
    }

    @Override
    public void clearCache() {
        javaClassLoader.clearCache();
    }
}
