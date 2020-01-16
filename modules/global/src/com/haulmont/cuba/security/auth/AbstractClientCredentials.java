/*
 * Copyright (c) 2008-2017 Haulmont.
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
 */

package com.haulmont.cuba.security.auth;

import com.haulmont.cuba.core.global.ClientType;

import java.io.Serializable;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public abstract class AbstractClientCredentials extends AbstractCredentials
        implements SyncSessionCredentials, SessionAttributesProvider, TimeZoneProvider {

    private String clientInfo;
    private String ipAddress;
    private String hostName;
    private ClientType clientType;
    private String securityScope;
    private boolean syncNewUserSessionReplication = false;

    private boolean checkClientPermissions = true;

    private Map<String, Serializable> sessionAttributes;
    private TimeZone timeZone;

    public AbstractClientCredentials(Locale locale, Map<String, Object> params) {
        super(locale, params);
    }

    public AbstractClientCredentials() {
    }

    public String getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(String clientInfo) {
        this.clientInfo = clientInfo;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public ClientType getClientType() {
        return clientType;
    }

    public void setClientType(ClientType clientType) {
        this.clientType = clientType;
    }

    public boolean isCheckClientPermissions() {
        return checkClientPermissions;
    }

    public void setCheckClientPermissions(boolean checkClientPermissions) {
        this.checkClientPermissions = checkClientPermissions;
    }

    /**
     * @return user identifier that represents to user login, not necessarily equal to login/email of a user.
     */
    public abstract String getUserIdentifier();

    @Override
    public boolean isSyncNewUserSessionReplication() {
        return syncNewUserSessionReplication;
    }

    public void setSyncNewUserSessionReplication(boolean syncNewUserSessionReplication) {
        this.syncNewUserSessionReplication = syncNewUserSessionReplication;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    @Override
    public Map<String, Serializable> getSessionAttributes() {
        return sessionAttributes;
    }

    public void setSessionAttributes(Map<String, Serializable> sessionAttributes) {
        this.sessionAttributes = sessionAttributes;
    }

    @Override
    public TimeZone getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    public String getSecurityScope() {
        return securityScope;
    }

    public void setSecurityScope(String securityScope) {
        this.securityScope = securityScope;
    }
}