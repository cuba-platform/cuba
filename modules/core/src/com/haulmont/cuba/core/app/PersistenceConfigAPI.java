/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 22.05.2009 14:43:03
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

/**
 * API of {@link PersistenceConfig} MBean.<br>
 * Reference to this interface must be obtained through {@link PersistenceConfigMBean#getAPI()} method
 */
public interface PersistenceConfigAPI
{
    boolean isSoftDeleteFor(String table);
}
