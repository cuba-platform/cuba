/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app.cache;

import java.util.Collection;

/**
 * Select items from CacheSet
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public interface CacheSelector {
    Collection select(CacheSet cacheSet);
}
