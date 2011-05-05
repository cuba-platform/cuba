/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.app.cache;

import java.util.Collection;

/**
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public interface ObjectsCacheManagerAPI {
    String NAME = "cuba_ObjectsCacheManager";

    void registerCache(ObjectsCacheInstance objectsCache);

    Collection<ObjectsCacheInstance> getActiveInstances();

    ObjectsCacheInstance getCache(String cacheName);
}
