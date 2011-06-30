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

    String printDbProperties();

    String printDbProperties(String prefix);

    String getDbProperty(String name);

    String setDbProperty(String name, String value);

    String removeDbProperty(String name);

    void clearCache();

    String printAppProperties();

    String printAppProperties(String prefix);

    String getAppProperty(String name);

    String setAppProperty(String name, String value);
}
