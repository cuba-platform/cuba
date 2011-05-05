/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
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
