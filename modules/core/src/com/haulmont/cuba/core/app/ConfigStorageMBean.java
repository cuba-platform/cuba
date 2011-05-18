/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 13.01.2009 17:59:42
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

/**
 * Management interface of the {@link ConfigStorageService} MBean.<br>
 */
public interface ConfigStorageMBean
{
    String OBJECT_NAME = "haulmont.cuba:service=ConfigStorage";

    String printProperties();

    String printProperties(String prefix);

    String getProperty(String name);

    String setProperty(String name, String value);

    String removeProperty(String name);

    void clearCache();
}
