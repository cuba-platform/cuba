/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.app;

/**
 * Interface to provide basic information about the middleware.
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public interface ServerInfoAPI {

    String NAME = "cuba_ServerInfo";

    /**
     * @return  release number
     */
    String getReleaseNumber();

    /**
     * @return  release timestamp
     */
    String getReleaseTimestamp();

    /**
     * This middleware instance identifier (unique in the current cluster).
     * The identifier has the form <code>host:port/context</code> and is built from the following configuration
     * parameters:
     * <ul>
     *     <li>{@link com.haulmont.cuba.core.global.GlobalConfig#getWebHostName()}</li>
     *     <li>{@link com.haulmont.cuba.core.global.GlobalConfig#getWebPort()}</li>
     *     <li>{@link com.haulmont.cuba.core.global.GlobalConfig#getWebContextName()}</li>
     * </ul>
     * @return  this middleware instance identifier
     */
    String getServerId();
}
