/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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
}
