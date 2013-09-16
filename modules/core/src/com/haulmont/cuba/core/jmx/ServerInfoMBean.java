/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.jmx;

import org.springframework.jmx.export.annotation.ManagedResource;

/**
 * JMX interface for {@link com.haulmont.cuba.core.app.ServerInfoAPI}.
 *
 * @author krivopustov
 * @version $Id$
 */
@ManagedResource(description = "Provides basic information about this Middleware unit")
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
