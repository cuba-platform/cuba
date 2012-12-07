/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.core.jmx;

/**
 * @author krivopustov
 * @version $Id$
 */
public interface CachingFacadeMBean {

    int getMessagesCacheSize();

    void clearGroovyCache();

    void clearMessagesCache();

    void clearConfigStorageCache();

    void clearEntityLogCache();

    void clearViewsCache();
}
