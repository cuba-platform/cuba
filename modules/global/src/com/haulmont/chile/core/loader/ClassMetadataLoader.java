/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.chile.core.loader;

import com.haulmont.chile.core.model.Session;

public interface ClassMetadataLoader {
    Session loadPackage(String modelName, String packageName);
    Session loadClass(String modelName, Class<?> clazz);
    Session loadClass(String modelName, String className);

    Session getSession();
}
