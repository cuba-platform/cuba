/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.security.jmx;

import com.haulmont.cuba.security.app.UserSessionsAPI;
import org.apache.commons.lang.text.StrBuilder;

import org.springframework.stereotype.Component;
import javax.inject.Inject;
import java.util.UUID;

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
    public int getSendTimeoutSec() {
        return userSessions.getSendTimeoutSec();
    }

    @Override
    public void setSendTimeoutSec(int timeout) {
        userSessions.setSendTimeoutSec(timeout);
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