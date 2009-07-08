/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 08.07.2009 14:25:48
 *
 * $Id$
 */
package com.haulmont.cuba.security.sys;

public interface UserSessionsMBean {

    String OBJECT_NAME = "haulmont.cuba:service=UserSessions";

    UserSessionsAPI getAPI();

    int getExpirationTimeout();
    void setExpirationTimeout(int value);

    int getCount();

    String printSessions();

    void processEviction();
}
