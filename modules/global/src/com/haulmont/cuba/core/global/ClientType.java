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
package com.haulmont.cuba.core.global;

/**
 */
public enum ClientType {
    WEB("W", "web"),
    PORTAL("P", "portal"),
    DESKTOP("D", "desktop");

    private String id;
    private String configPath;

    ClientType(String id, String configPath) {
        this.id = id;
        this.configPath = configPath;
    }

    public String getId() {
        return id;
    }

    public String getConfigPath() {
        return configPath;
    }

    public static ClientType fromId(String id) {
        if ("W".equals(id)) {
            return WEB;
        } else if ("D".equals(id)) {
            return DESKTOP;
        } else {
            return null;
        }
    }
}