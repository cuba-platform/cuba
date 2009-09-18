/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 17.03.2009 16:09:14
 *
 * $Id$
 */
package com.haulmont.cuba.security.app;

/**
 * Management interface of the {@link com.haulmont.cuba.security.app.EntityLog} MBean.<br>
 * Use {@link #getAPI()} method to obtain a direct reference to application interface.<br>
 * <p>
 * Reference to this interface can be obtained through {@link com.haulmont.cuba.core.Locator#lookupMBean(Class, String)} method
 */
public interface EntityLogMBean
{
    String OBJECT_NAME = "haulmont.cuba:service=EntityLog";

    /**
     * Get direct reference to application interface. Direct means no proxies or container interceptors.
     */
    EntityLogAPI getAPI();

    /**
     * Is logging enabled?
     */
    boolean isEnabled();

    /**
     * Enables or disables logging
     */
    void setEnabled(boolean enabled);

    /**
     * Invalidates configuration cache.
     * The configuration will be recreated from the database on next lifecycle event.
     */
    void invalidateCache();
}
