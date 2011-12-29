/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.security.app;

/**
 * User sessions distributed cache JMX interface.
 *
 * @version $Id$
 *
 * @author krivopustov
 */
public interface UserSessionsMBean {

    /**
     * User session expiration timeout. Not the same as HTTP session timeout, but should have the same value.
     * @return  timeout in seconds
     */
    int getExpirationTimeoutSec();

    /**
     * Set user session expiration timeout for the current server session.
     * @param value timeout in seconds
     */
    void setExpirationTimeoutSec(int value);

    int getCount();

    String printSessions();

    void processEviction();
}
