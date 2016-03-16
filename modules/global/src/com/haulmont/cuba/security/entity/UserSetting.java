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
package com.haulmont.cuba.security.entity;

import com.haulmont.cuba.core.entity.BaseUuidEntity;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import com.haulmont.cuba.core.global.ClientType;

import javax.persistence.*;

/**
 * Record for user setting.
 * Used by <code>UserSettingServiceBean</code>.
 *
 */
@Entity(name = "sec$UserSetting")
@Table(name = "SEC_USER_SETTING")
@SystemLevel
public class UserSetting extends BaseUuidEntity {

    private static final long serialVersionUID = -4324101071593066529L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @Column(name = "CLIENT_TYPE", length = 1)
    private String clientType;

    @Column(name = "NAME", length = 255)
    private String name;

    @Lob
    @Column(name = "VALUE")
    private String value;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ClientType getClientType() {
        return ClientType.fromId(clientType);
    }

    public void setClientType(ClientType clientType) {
        this.clientType = clientType == null ? null : clientType.getId();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}