/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.client.sys.cache;

import java.util.concurrent.locks.ReadWriteLock;

/**
 * Describes cache storage and invalidation policy
 *
* @author degtyarjov
* @version $Id$
*/
public interface CachingStrategy {
    /**
     * Method for strategy initialization
     * Invoked at first login, so security context is available
     */
    void init();

    /**
     * Return cached object
     */
    Object getObject();

    /**
     * Refresh cached object
     */
    Object loadObject();

    /**
     * Return lock used to provide caching thread safety
     */
    ReadWriteLock lock();

    /**
     * Indicate whether cached object should be refreshed or not
     */
    boolean needToReload();
}
