/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.security.entity;

import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;

/**
 * Link between users and roles.
 *
 * @author krivopustov
 * @version $Id$
 */
@Entity(name = "sec$UserRole")
@Table(name = "SEC_USER_ROLE")
@SystemLevel
public class UserRole extends StandardEntity {

    private static final long serialVersionUID = 8543853035155300992L;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "USER_ID")
    @OnDeleteInverse(DeletePolicy.CASCADE)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ROLE_ID")
    @OnDeleteInverse(DeletePolicy.CASCADE)
    private Role role;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
