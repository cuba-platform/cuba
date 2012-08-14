/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.app.cache;

import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;

/**
 * MBean interface for ObjectsCacheManager
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public interface ObjectsCacheManagerMBean {

    String OBJECT_NAME = "haulmont.cuba:service=ObjectsCacheManager";

    int getCacheCount();

    String printActiveCaches();

    @ManagedOperationParameters({@ManagedOperationParameter(name = "cacheName", description = "")})
    String printStatsByName(String cacheName);

    @ManagedOperationParameters({@ManagedOperationParameter(name = "cacheName", description = "")})
    String reloadByName(String cacheName);
}