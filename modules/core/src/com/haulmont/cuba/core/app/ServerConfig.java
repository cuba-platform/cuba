/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 04.07.2009 12:34:03
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.Property;
import com.haulmont.cuba.core.config.Source;
import com.haulmont.cuba.core.config.SourceType;
import com.haulmont.cuba.core.config.type.Factory;
import com.haulmont.cuba.core.config.defaults.DefaultInt;
import com.haulmont.cuba.core.sys.ConfigDirFactory;

/**
 * Common server configuration parameters
 */
@Source(type = SourceType.SYSTEM)
public interface ServerConfig extends Config {

    /**
     * Config directory. Root of all not deployable application configuration and logic.
     * Does not end with "/"
     */
    @Factory(factory = ConfigDirFactory.class)
    String getServerConfDir();

    /**
     * Logs directory. Place app-specific log files here.
     * Does not end with "/"
     */
    @Property("jboss.server.log.dir")
    String getServerLogDir();

    /**
     * Temporary files directory. Place app-specific temp files under this directory.
     * Does not end with "/"
     */
    @Property("jboss.server.temp.dir")
    String getServerTempDir();

    /**
     * Data directory. Place persistent app-specific data files under this directory.
     * Does not end with "/"
     */
    @Property("jboss.server.data.dir")
    String getServerDataDir();

    /**
     * User session expiration timeout in seconds.
     * Not the same as HTTP session timeout, but should have the same value.
     */
    @Property("cuba.userSessionExpirationTimeoutSec")
    @DefaultInt(1800)
    int getUserSessionExpirationTimeoutSec();

    /**
     * User session expiration timeout in seconds.
     * Not the same as HTTP session timeout, but should have the same value.
     */
    void setUserSessionExpirationTimeoutSec(int timeout);
}
