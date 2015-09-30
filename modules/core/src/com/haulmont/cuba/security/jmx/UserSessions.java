/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.security.jmx;

import com.haulmont.cuba.security.app.UserSessionsAPI;
import org.apache.commons.lang.text.StrBuilder;

import org.springframework.stereotype.Component;
import javax.inject.Inject;
import java.util.UUID;

/**
 * @author krivopustov
 * @version $Id$
 */
@Component("cuba_UserSessionsMBean")
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

    @Override
    public String killSession(String id) {
        UUID sessionId;
        try {
            sessionId = UUID.fromString(id);
        } catch (IllegalArgumentException ex) {
            return "Invalid session Id format: use UUID";
        }
        userSessions.killSession(sessionId);
        return "OK";
    }
}
