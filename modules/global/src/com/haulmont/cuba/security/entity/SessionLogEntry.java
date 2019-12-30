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

package com.haulmont.cuba.security.entity;

import com.haulmont.cuba.core.entity.TenantEntity;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.global.ClientType;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Table(name = "SEC_SESSION_LOG")
@Entity(name = "sec$SessionLogEntry")
public class SessionLogEntry extends StandardEntity implements TenantEntity {
    private static final long serialVersionUID = -2218273202879030900L;

    public static final String DEFAULT_VIEW = "sessionLogEntry-view";

    @Column(name = "SESSION_ID", nullable = false)
    protected UUID sessionId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "SUBSTITUTED_USER_ID")
    protected User substitutedUser;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "USER_ID")
    protected User user;

    @Lob
    @Column(name = "USER_DATA")
    protected String userData;

    @Column(name = "LAST_ACTION", nullable = false)
    protected Integer lastAction;

    @Column(name = "CLIENT_INFO")
    protected String clientInfo;

    @Column(name = "ADDRESS")
    protected String address;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "STARTED_TS")
    protected Date startedTs;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FINISHED_TS")
    protected Date finishedTs;

    @Column(name = "CLIENT_TYPE")
    protected String clientType;

    @Column(name = "SERVER_ID")
    protected String server;

    @Column(name = "SYS_TENANT_ID")
    protected String sysTenantId;

    public void setLastAction(SessionAction lastAction) {
        this.lastAction = lastAction == null ? null : lastAction.getId();
    }

    public SessionAction getLastAction() {
        return lastAction == null ? null : SessionAction.fromId(lastAction);
    }

    public void setStartedTs(Date startedTs) {
        this.startedTs = startedTs;
    }

    public Date getStartedTs() {
        return startedTs;
    }

    public void setFinishedTs(Date finishedTs) {
        this.finishedTs = finishedTs;
    }

    public Date getFinishedTs() {
        return finishedTs;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setClientInfo(String clientInfo) {
        this.clientInfo = clientInfo;
    }

    public String getClientInfo() {
        return clientInfo;
    }

    public void setClientType(ClientType clientType) {
        this.clientType = clientType == null ? null : clientType.getId();
    }

    public ClientType getClientType() {
        return clientType == null ? null : ClientType.fromId(clientType);
    }

    public void setSessionId(UUID sessionId) {
        this.sessionId = sessionId;
    }

    public UUID getSessionId() {
        return sessionId;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUserData(String userData) {
        this.userData = userData;
    }

    public String getUserData() {
        return userData;
    }

    public User getSubstitutedUser() {
        return substitutedUser;
    }

    public void setSubstitutedUser(User substitutedUser) {
        this.substitutedUser = substitutedUser;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getSysTenantId() {
        return sysTenantId;
    }

    public void setSysTenantId(String sysTenantId) {
        this.sysTenantId = sysTenantId;
    }
}