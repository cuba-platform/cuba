/*
 * Author: Konstantin Krivopustov
 * Created: 22.11.2009 18:05:54
 * 
 * $Id$
 */
package com.haulmont.cuba.core.global;

import com.haulmont.cuba.core.config.Config;
import com.haulmont.cuba.core.config.Property;
import com.haulmont.cuba.core.config.Source;
import com.haulmont.cuba.core.config.SourceType;
import com.haulmont.cuba.core.config.defaults.DefaultInt;
import com.haulmont.cuba.core.config.type.Factory;
import com.haulmont.cuba.core.config.defaults.DefaultBoolean;
import com.haulmont.cuba.core.config.defaults.DefaultString;
import com.haulmont.cuba.core.sys.ConfigDirFactory;

@Source(type = SourceType.SYSTEM)
public interface GlobalConfig extends Config {

    @Property("cuba.webHostName")
    @DefaultString("localhost")
    String getWebHostName();

    @Property("cuba.webPort")
    @DefaultString("8080")
    String getWebPort();

    @Property("cuba.webContextName")
    @DefaultString("cuba")
    String getWebContextName();

    /**
     * Config directory. Root of all not deployable application configuration and logic.
     * Does not end with "/"
     */
    @Factory(factory = ConfigDirFactory.class)
    String getConfDir();

    /**
     * Logs directory. Place app-specific log files here.
     * Does not end with "/"
     */
    @Property("jboss.server.log.dir")
    String getLogDir();

    /**
     * Temporary files directory. Place app-specific temp files under this directory.
     * Does not end with "/"
     */
    @Property("jboss.server.temp.dir")
    String getTempDir();

    /**
     * Data directory. Place persistent app-specific data files under this directory.
     * Does not end with "/"
     */
    @Property("jboss.server.data.dir")
    String getDataDir();

    /**
     * Used to support automatic testing
     */
    @Property("cuba.testMode")
    @DefaultBoolean(false)
    boolean getTestMode();

    /**
     * Enable class and resource loading by Groovy
     */
    @Property("cuba.groovyClassLoaderEnabled")
    @DefaultBoolean(true)
    boolean isGroovyClassLoaderEnabled();

    /**
     * User session expiration timeout in seconds.
     * Not the same as HTTP session timeout, but should have the same value.
     */
    @Property("cuba.userSessionExpirationTimeoutSec")
    @DefaultInt(1800)
    int getUserSessionExpirationTimeoutSec();
}
