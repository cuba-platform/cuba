/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 25.12.2008 11:40:39
 *
 * $Id$
 */
package com.haulmont.cuba.core.global;

public enum ClientType
{
    WEB("W", "web"),
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
