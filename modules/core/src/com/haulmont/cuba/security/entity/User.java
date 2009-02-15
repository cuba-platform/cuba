/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 14.11.2008 13:04:23
 *
 * $Id$
 */
package com.haulmont.cuba.security.entity;

import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "sec$User")
@Table(name = "SEC_USER")
public class User extends StandardEntity
{
    private static final long serialVersionUID = 5007187642916030394L;

    @Column(name = "LOGIN", length = 20)
    private String login;

    @Column(name = "PASSWORD", length = 32)
    private String password;

    @Column(name = "NAME", length = 100)
    private String name;

    @Column(name = "AD_USER", length = 100)
    private String activeDirectoryUser;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "GROUP_ID")
    @OnDeleteInverse(DeletePolicy.DENY)
    private Group group;

    @OneToMany(mappedBy = "user")
    private Set<UserRole> userRoles;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getActiveDirectoryUser() {
        return activeDirectoryUser;
    }

    public void setActiveDirectoryUser(String activeDirectoryUser) {
        this.activeDirectoryUser = activeDirectoryUser;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Set<UserRole> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(Set<UserRole> userRoles) {
        this.userRoles = userRoles;
    }

    public String toString() {
        return login;
    }
}
