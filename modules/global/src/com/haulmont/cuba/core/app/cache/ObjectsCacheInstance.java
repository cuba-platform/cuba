/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.app.cache;

import org.apache.commons.collections.Predicate;

import java.util.Collection;

/**
 * Objects cache interface
 *
 * @author artamonov
 * @version $Id$
 */
public interface ObjectsCacheInstance {
    String getName();

    CacheStatistics getStatistics();

    Collection execute(CacheSelector cacheSelector);

    int count(Predicate... selectors);
}
