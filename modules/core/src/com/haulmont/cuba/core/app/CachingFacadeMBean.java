/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 27.11.2009 18:47:41
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

public interface CachingFacadeMBean {

    void clearGroovyCache();

    void clearMessagesCache();

    void clearResourceRepositoryCache();

    void clearConfigStorageCache();

    void clearEntityLogCache();
}
