/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.portal.sys.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

/**
 * @author artamonov
 * @version $Id$
 */
public class RoleGrantedAuthority implements GrantedAuthority {

    private static final long serialVersionUID = -8973027786139485184L;

    private static final String ROLE_USER = "ROLE_USER";

    protected final String role;

    public RoleGrantedAuthority() {
        this.role = ROLE_USER;
    }

    public RoleGrantedAuthority(String role) {
        Assert.hasText(role, "A granted authority textual representation is required");
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return role;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof RoleGrantedAuthority) {
            return role.equals(((RoleGrantedAuthority) obj).role);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return this.role.hashCode();
    }

    @Override
    public String toString() {
        return this.role;
    }
}
