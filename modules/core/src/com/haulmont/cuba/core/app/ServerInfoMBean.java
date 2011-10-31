/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.core.app;

/**
 * JMX interface to provide basic information about the middleware.
 */
public interface ServerInfoMBean
{
    /**
     * @return  release number
     */
    String getReleaseNumber();

    /**
     * @return  release timestamp
     */
    String getReleaseTimestamp();

    /**
     * @return  this middleware instance identifier
     * @see     com.haulmont.cuba.core.app.ServerInfoAPI#getServerId()
     */
    String getServerId();
}
