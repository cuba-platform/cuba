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

import com.haulmont.chile.core.annotations.Aggregation;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.entity.annotation.Listeners;
import com.haulmont.cuba.core.global.DeletePolicy;

import javax.persistence.*;
import java.util.Set;

/**
 * User
 */
@Entity(name = "sec$User")
@Table(name = "SEC_USER")
@Listeners("com.haulmont.cuba.security.listener.UserEntityListener")
@NamePattern("%s [%s]|name,login")
public class User extends StandardEntity
{
    private static final long serialVersionUID = 5007187642916030394L;

    @Column(name = "LOGIN", length = LOGIN_FIELD_LEN)
    private String login;

    @Column(name = "LOGIN_LC", length = LOGIN_FIELD_LEN)
    private String loginLowerCase;

    @Column(name = "PASSWORD", length = 32)
    private String password;

    @Column(name = "NAME", length = 100)
    private String name;

    @Column(name = "FIRST_NAME", length = 100)
    private String firstName;

    @Column(name = "LAST_NAME", length = 100)
    private String lastName;

    @Column(name = "MIDDLE_NAME", length = 100)
    private String middleName;

    @Column(name = "POSITION", length = 100)
    private String position;

    @Column(name = "EMAIL", length = 100)
    private String email;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "GROUP_ID")
    @OnDeleteInverse(DeletePolicy.DENY)
    private Group group;

    @OneToMany(mappedBy = "user")
    @Aggregation
    private Set<UserRole> userRoles;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEFAULT_SUBSTITUTED_USER_ID")
    private User defaultSubstitutedUser;

    @OneToMany(mappedBy = "user")
    @Aggregation
    private Set<UserSubstitution> substitutions;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getLoginLowerCase() {
        return loginLowerCase;
    }

    public void setLoginLowerCase(String loginLowerCase) {
        this.loginLowerCase = loginLowerCase;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Set<UserSubstitution> getSubstitutions() {
        return substitutions;
    }

    public void setSubstitutions(Set<UserSubstitution> substitutions) {
        this.substitutions = substitutions;
    }

    public User getDefaultSubstitutedUser() {
        return defaultSubstitutedUser;
    }

    public void setDefaultSubstitutedUser(User defaultSubstitutedUser) {
        this.defaultSubstitutedUser = defaultSubstitutedUser;
    }
}
