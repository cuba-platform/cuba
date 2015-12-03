/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.security.entity;

import com.haulmont.cuba.core.entity.BaseUuidEntity;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;

import javax.persistence.*;

/**
 * @author artamonov
 * @version $Id$
 */
@Entity(name = "sec$RememberMeToken")
@Table(name = "SEC_REMEMBER_ME")
@SystemLevel
public class RememberMeToken extends BaseUuidEntity {

    private static final long serialVersionUID = -3757776319150532739L;

    public static final int TOKEN_LENGTH = 32;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "USER_ID")
    protected User user;

    @Column(name = "TOKEN", nullable = false, length = TOKEN_LENGTH)
    protected String token;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}