/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.javacl;

/**
 * @author degtyarjov
 * @version $Id$
 */
public interface ClassLoaderManagementMBean {
    Class reloadClass(String className) throws ClassNotFoundException;

    Class loadClass(String className) throws ClassNotFoundException;

    Class removeClass(String className);

    String getClassDependencies(String className);

    void clearCache();
}
