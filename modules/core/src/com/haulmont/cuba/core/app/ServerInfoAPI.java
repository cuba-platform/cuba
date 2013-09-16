/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app;

/**
 * Interface to provide basic information about the middleware.
 *
 * @author krivopustov
 * @version $Id$
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
