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
import com.haulmont.cuba.core.config.defaults.DefaultBoolean;

@Source(type = SourceType.SYSTEM)
public interface GlobalConfig extends Config {

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
}
