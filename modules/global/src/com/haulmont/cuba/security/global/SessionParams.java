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

package com.haulmont.cuba.security.global;

/**
 * List of parameters that could be passed in map when user session is being created
 */
public enum SessionParams {
    IP_ADDERSS("ipAddress"),
    CLIENT_INFO("clientInfo"),
    CLIENT_TYPE("clientType"),
    HOST_NAME("hostName");

    private String id;

    SessionParams(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public static SessionParams fromId(String id) {
        for (SessionParams val : SessionParams.values()) {
            if (id.equals(val.getId())) {
                return val;
            }
        }
        return null;
    }
}
