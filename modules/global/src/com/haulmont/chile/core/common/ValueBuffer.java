/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.chile.core.common;

public interface ValueBuffer {
    <T> T getValue(String name);
    void setValue(String name, Object value);

    <T> T getValueEx(String propertyPath);
    void setValueEx(String propertyPath, Object value);

//    <T> T asClass(Class<T> clazz);
}
