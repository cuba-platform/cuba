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
 */

package com.haulmont.cuba.restapi;

import java.io.Serializable;
import java.util.Date;

/**
 * Cluster message containing an information about the REST API access token to be stored
 */
public class TokenStoreAddAccessTokenMsg implements Serializable {
    protected String accessTokenValue;
    protected String refreshTokenValue;
    protected byte[] accessTokenBytes;
    protected String authenticationKey;
    protected byte[] authenticationBytes;
    protected Date tokenExpiry;
    protected String userLogin;

    public TokenStoreAddAccessTokenMsg(String accessTokenValue,
                                       byte[] accessTokenBytes,
                                       String authenticationKey,
                                       byte[] authenticationBytes,
                                       Date tokenExpiry,
                                       String userLogin,
                                       String refreshTokenValue) {
        this.accessTokenValue = accessTokenValue;
        this.accessTokenBytes = accessTokenBytes;
        this.authenticationKey = authenticationKey;
        this.authenticationBytes = authenticationBytes;
        this.tokenExpiry = tokenExpiry;
        this.userLogin = userLogin;
        this.refreshTokenValue = refreshTokenValue;
    }

    public String getAccessTokenValue() {
        return accessTokenValue;
    }

    public byte[] getAccessTokenBytes() {
        return accessTokenBytes;
    }

    public String getAuthenticationKey() {
        return authenticationKey;
    }

    public byte[] getAuthenticationBytes() {
        return authenticationBytes;
    }

    public Date getTokenExpiry() {
        return tokenExpiry;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public String getRefreshTokenValue() {
        return refreshTokenValue;
    }
}
