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

package com.haulmont.cuba.restapi;

import java.io.Serializable;
import java.util.Date;

/**
 * Cluster message containing an information about the REST API refresh token to be stored
 */
public class TokenStoreAddRefreshTokenMsg implements Serializable {
    protected String tokenValue;
    protected byte[] tokenBytes;
    protected byte[] authenticationBytes;
    protected Date tokenExpiry;
    protected String userLogin;

    public TokenStoreAddRefreshTokenMsg(String tokenValue,
                                        byte[] tokenBytes,
                                        byte[] authenticationBytes,
                                        Date tokenExpiry,
                                        String userLogin,
                                        String refreshTokenValue) {
        this.tokenValue = tokenValue;
        this.tokenBytes = tokenBytes;
        this.authenticationBytes = authenticationBytes;
        this.tokenExpiry = tokenExpiry;
        this.userLogin = userLogin;
    }

    public String getTokenValue() {
        return tokenValue;
    }

    public byte[] getTokenBytes() {
        return tokenBytes;
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
}
