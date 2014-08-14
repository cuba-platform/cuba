/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.javacl;

/**
 * @author degtyarjov
 * @version $Id$
 */
public interface ClassLoaderManagerMBean {
    String reloadClass(String className) throws ClassNotFoundException;

    String loadClass(String className) throws ClassNotFoundException;

    String removeClass(String className);

    String getClassDependencies(String className);

    String clearCache();
}
