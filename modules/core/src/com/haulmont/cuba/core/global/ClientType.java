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
    WEB("W"),
    DESKTOP("D");

    private String id;

    ClientType(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
