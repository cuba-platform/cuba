/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
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
}
