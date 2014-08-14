/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.javacl;

import org.apache.commons.lang.exception.ExceptionUtils;

import javax.annotation.ManagedBean;
import javax.inject.Inject;

import static java.lang.String.format;

/**
 * @author degtyarjov
 * @version $Id$
 */
@ManagedBean("cuba_ClassLoaderManager")
public class ClassLoaderManager implements ClassLoaderManagerMBean {
    @Inject
    protected JavaClassLoader javaClassLoader;

    @Override
    public String loadClass(String className) {
        try {
            Class<?> aClass = javaClassLoader.loadClass(className);
            return format("Loaded %s", aClass.toString());
        } catch (Exception e) {
            return ExceptionUtils.getStackTrace(e);
        }
    }

    @Override
    public String removeClass(String className) {
        try {
            TimestampClass removed = javaClassLoader.compiled.remove(className);
            if (removed != null) {
                for (String dependent : removed.dependent) {
                    removeClass(dependent);
                }
            }
            return removed != null ? format("Removed %s", removed.clazz.toString()) : "No such class in cache";
        } catch (Exception e) {
            return ExceptionUtils.getStackTrace(e);
        }
    }

    @Override
    public String reloadClass(String className) {
        try {
            removeClass(className);
            return loadClass(className);
        } catch (Exception e) {
            return ExceptionUtils.getStackTrace(e);
        }
    }

    @Override
    public String getClassDependencies(String className) {
        try {
            TimestampClass timestampClass = javaClassLoader.compiled.get(className);
            if (timestampClass != null) {
                return format("Dependencies \n%s\nDependent \n%s", timestampClass.dependencies, timestampClass.dependent);
            }

            return "No such class in cache";
        } catch (Exception e) {
            return ExceptionUtils.getStackTrace(e);
        }
    }

    @Override
    public String clearCache() {
        try {
            javaClassLoader.clearCache();
            return "Done";
        } catch (Exception e) {
            return ExceptionUtils.getStackTrace(e);
        }
    }
}
