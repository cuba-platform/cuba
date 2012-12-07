/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.security.jmx;

/**
 * JMX interface for {@link com.haulmont.cuba.security.app.EntityLogAPI}
 *
 * @author krivopustov
 * @version $Id$
 */
public interface EntityLogMBean {
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
