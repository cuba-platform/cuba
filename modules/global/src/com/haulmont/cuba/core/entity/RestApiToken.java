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

package com.haulmont.cuba.core.entity;

import com.haulmont.cuba.core.entity.annotation.SystemLevel;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Entity;
import java.util.Date;

/**
 */
@Entity(name = "sys$RestApiToken")
@Table(name = "SYS_REST_API_TOKEN")
@SystemLevel
public class RestApiToken extends BaseUuidEntity implements Creatable{

    @Column(name = "CREATE_TS")
    protected Date createTs;

    @Column(name = "CREATED_BY", length = 50)
    protected String createdBy;

    @Column(name = "ACCESS_TOKEN_VALUE")
    protected String accessTokenValue;

    @Column(name = "ACCESS_TOKEN_BYTES")
    protected byte[] accessTokenBytes;

    @Column(name = "AUTHENTICATION_KEY")
    protected String authenticationKey;

    @Column(name = "AUTHENTICATION_BYTES")
    protected byte[] authenticationBytes;

    @Column(name = "EXPIRY")
    protected Date expiry;

    public String getAccessTokenValue() {
        return accessTokenValue;
    }

    public void setAccessTokenValue(String accessTokenValue) {
        this.accessTokenValue = accessTokenValue;
    }

    public byte[] getAccessTokenBytes() {
        return accessTokenBytes;
    }

    public void setAccessTokenBytes(byte[] accessTokenBytes) {
        this.accessTokenBytes = accessTokenBytes;
    }

    public String getAuthenticationKey() {
        return authenticationKey;
    }

    public void setAuthenticationKey(String authenticationKey) {
        this.authenticationKey = authenticationKey;
    }

    public byte[] getAuthenticationBytes() {
        return authenticationBytes;
    }

    public void setAuthenticationBytes(byte[] authenticationBytes) {
        this.authenticationBytes = authenticationBytes;
    }

    public Date getExpiry() {
        return expiry;
    }

    public void setExpiry(Date expiry) {
        this.expiry = expiry;
    }

    @Override
    public Date getCreateTs() {
        return createTs;
    }

    @Override
    public void setCreateTs(Date createTs) {
        this.createTs = createTs;
    }

    @Override
    public String getCreatedBy() {
        return createdBy;
    }

    @Override
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
