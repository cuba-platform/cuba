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

package com.haulmont.idp.model;

import java.io.Serializable;

public class AuthResponse implements Serializable {
    private String errorCode;
    private String serviceProviderUrl;

    public AuthResponse() {
    }

    public static AuthResponse authenticated(String serviceProviderUrl) {
        AuthResponse response = new AuthResponse();
        response.setServiceProviderUrl(serviceProviderUrl);
        return response;
    }

    public static AuthResponse failed(String errorCode) {
        AuthResponse response = new AuthResponse();
        response.setErrorCode(errorCode);
        return response;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getServiceProviderUrl() {
        return serviceProviderUrl;
    }

    public void setServiceProviderUrl(String serviceProviderUrl) {
        this.serviceProviderUrl = serviceProviderUrl;
    }
}