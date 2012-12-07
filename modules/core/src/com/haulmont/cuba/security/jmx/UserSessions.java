/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.security.jmx;

import com.haulmont.cuba.security.app.UserSessionsAPI;
import org.apache.commons.lang.text.StrBuilder;

import javax.annotation.ManagedBean;
import javax.inject.Inject;

/**
 * @author krivopustov
 * @version $Id$
 */
@ManagedBean("cuba_UserSessionsMBean")
public class UserSessions implements UserSessionsMBean {

    @Inject
    protected UserSessionsAPI userSessions;

    @Override
    public int getExpirationTimeoutSec() {
        return userSessions.getExpirationTimeoutSec();
    }

    @Override
    public void setExpirationTimeoutSec(int value) {
        userSessions.setExpirationTimeoutSec(value);
    }

    @Override
    public int getCount() {
        return userSessions.getUserSessionInfo().size();
    }

    @Override
    public String printSessions() {
        StrBuilder sb = new StrBuilder();
        sb.appendWithSeparators(userSessions.getUserSessionInfo(), "\n");
        return sb.toString();
    }

    @Override
    public void processEviction() {
        userSessions.processEviction();
    }
}
