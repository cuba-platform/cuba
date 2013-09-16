/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.jmx;

import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;

/**
 * JMX interface for {@link com.haulmont.cuba.core.app.cache.ObjectsCacheManagerAPI}.
 *
 * @author artamonov
 * @version $Id$
 */
public interface ObjectsCacheManagerMBean {

    int getCacheCount();

    String printActiveCaches();

    @ManagedOperationParameters({@ManagedOperationParameter(name = "cacheName", description = "")})
    String printStatsByName(String cacheName);

    @ManagedOperationParameters({@ManagedOperationParameter(name = "cacheName", description = "")})
    String reloadByName(String cacheName);
}