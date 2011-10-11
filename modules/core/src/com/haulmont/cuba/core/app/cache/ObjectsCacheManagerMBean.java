/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.app.cache;

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

    String printStatsByName(String cacheName);

    String reloadByName(String cacheName);
}