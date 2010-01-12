/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 08.07.2009 14:25:48
 *
 * $Id$
 */
package com.haulmont.cuba.security.app;

/**
 * Management interface of the {@link UserSessions} MBean.<br>
 */

public interface UserSessionsMBean {

    String OBJECT_NAME = "haulmont.cuba:service=UserSessions";

    /**
     * Get direct reference to application interface. Direct means no proxies or container interceptors.
     * <p>DEPRECATED - lookup API directly
     */
    @Deprecated
    UserSessionsAPI getAPI();

    /**
     * User session expiration timeout in seconds.
     * Not the same as HTTP session timeout, but should have the same value.
     */
    int getExpirationTimeoutSec();

    /**
     * Set user session expiration timeout in seconds.
     * Not the same as HTTP session timeout, but should have the same value.<br>
     * Persists the value in database using {@link com.haulmont.cuba.core.app.ServerConfig}
     * configuration parameter.
     */
    void setExpirationTimeoutSec(int value);

    int getCount();

    String printSessions();

    void processEviction();
}
