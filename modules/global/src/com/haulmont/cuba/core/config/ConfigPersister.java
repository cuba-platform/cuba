/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.core.config;

public interface ConfigPersister
{
    String getProperty(SourceType sourceType, String name);

    void setProperty(SourceType sourceType, String name, String value);
}
