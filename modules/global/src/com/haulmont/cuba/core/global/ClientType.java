/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.global;

public enum ClientType
{
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
        if ("W".equals(id))
            return WEB;
        else if ("D".equals(id))
            return DESKTOP;
        else
            return null;
    }
}
