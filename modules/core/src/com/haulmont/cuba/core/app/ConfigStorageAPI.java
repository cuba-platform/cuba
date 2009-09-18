/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 18.05.2009 12:42:42
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

/**
 * API of {@link ConfigStorage} MBean.<br>
 * Reference to this interface must be obtained through {@link ConfigStorageMBean#getAPI()} method
 */
public interface ConfigStorageAPI
{
    String getConfigProperty(String name);

    void setConfigProperty(String name, String value);
}
