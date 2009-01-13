/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 12.01.2009 15:44:10
 *
 * $Id$
 */
package com.haulmont.cuba.core.config;

public interface ConfigPersister
{
    String getProperty(SourceType sourceType, String name);

    void setProperty(SourceType sourceType, String name, String value);
}
