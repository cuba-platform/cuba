/*
 * Copyright (c) 2008-2018 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/commercial-software-license for details.
 */

package com.haulmont.idp;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import java.util.Objects;

/**
 * IDP authentication mode.
 */
public enum IdpAuthMode implements EnumClass<String> {
    /**
     * Authenticate users with built-in logins and passwords.
     */
    STANDARD("STANDARD"),
    /**
     * Delegate authentication to LDAP server.
     */
    LDAP("LDAP");

    private String id;

    IdpAuthMode(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    public static IdpAuthMode fromId(String id) {
        for (IdpAuthMode type : IdpAuthMode.values()) {
            if (Objects.equals(id, type.getId()))
                return type;
        }
        return null; // unknown id
    }
}