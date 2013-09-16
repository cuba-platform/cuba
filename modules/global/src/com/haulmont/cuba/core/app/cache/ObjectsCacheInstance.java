/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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
