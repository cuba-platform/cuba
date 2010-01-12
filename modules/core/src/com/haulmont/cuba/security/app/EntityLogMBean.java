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
 */
public interface EntityLogMBean
{
    String OBJECT_NAME = "haulmont.cuba:service=EntityLog";

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
