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
import com.haulmont.cuba.core.config.defaults.DefaultBoolean;
import com.haulmont.cuba.core.config.type.Factory;
import com.haulmont.cuba.core.config.defaults.DefaultInt;
import com.haulmont.cuba.core.config.defaults.DefaultString;

/**
 * Common server configuration parameters
 */
@Source(type = SourceType.APP)
public interface ServerConfig extends Config {

    /**
     * DB scripts directory.
     * Does not end with "/"
     */
    @Property("cuba.dbDir")
    String getDbDir();

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

    /**
     * Whether the server will try to init/update database on each start
     */
    @Property("cuba.automaticDatabaseUpdate")
    @DefaultBoolean(false)
    boolean getAutomaticDatabaseUpdate();

    /**
     * returns path to installed Open Office 
     */
    @Property("cuba.reporting.openoffice.path")
    String getOpenOfficePath();

    /*
     * Set it to true if you're using openoffice reporting formatter on a *nix server without X server running
     */
    @Property("cuba.reporting.displayDeviceUnavailable")
    @DefaultBoolean(false)
    boolean getDisplayDeviceUnavailable();

}
