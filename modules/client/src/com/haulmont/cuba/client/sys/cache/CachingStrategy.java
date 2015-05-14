/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.client.sys.cache;

import java.util.concurrent.locks.ReadWriteLock;

/**
* @author degtyarjov
* @version $Id$
*/
public interface CachingStrategy {
    void init();

    Object getObject();

    Object loadObject();

    ReadWriteLock lock();

    boolean needToReload();
}
