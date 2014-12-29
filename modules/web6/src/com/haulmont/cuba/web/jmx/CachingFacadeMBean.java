/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.jmx;

/**
 * <p>$Id$</p>
 *
 * @author Alexander Budarov
 */
public interface CachingFacadeMBean {

    int getMessagesCacheSize();

    void clearGroovyCache();

    void clearMessagesCache();

    void clearPersistenceManagerClientCache();

    void clearViewRepositoryCache();

    void clearWindowConfig();

    void clearMenuConfig();
}
