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

/**
 * Cluster message containing an information about the REST API token to be stored
 */
public class TokenStoreAddTokenMsg implements Serializable {
    protected String tokenValue;
    protected byte[] accessTokenBytes;
    protected String authenticationKey;
    protected byte[] authenticationBytes;

    public TokenStoreAddTokenMsg(String tokenValue, byte[] accessTokenBytes, String authenticationKey, byte[] authenticationBytes) {
        this.tokenValue = tokenValue;
        this.accessTokenBytes = accessTokenBytes;
        this.authenticationKey = authenticationKey;
        this.authenticationBytes = authenticationBytes;
    }

    public String getTokenValue() {
        return tokenValue;
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
}
