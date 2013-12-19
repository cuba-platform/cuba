/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.security.entity;

import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.core.entity.annotation.Listeners;
import com.haulmont.cuba.core.entity.annotation.OnDeleteInverse;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import com.haulmont.cuba.core.entity.annotation.TrackEditScreenHistory;
import com.haulmont.cuba.core.global.DeletePolicy;
import com.haulmont.cuba.core.sys.AppContext;
import org.apache.commons.lang.StringUtils;

import javax.persistence.*;
import java.text.MessageFormat;
import java.util.List;

/**
 * User
 *
 * @author krivopustov
 * @version $Id$
 */
@Entity(name = "sec$User")
@Table(name = "SEC_USER")
@Listeners("com.haulmont.cuba.security.listener.UserEntityListener")
@NamePattern("#getCaption|login,name")
@TrackEditScreenHistory
public class User extends StandardEntity {

    private static final long serialVersionUID = 5007187642916030394L;

    @Column(name = "LOGIN", length = LOGIN_FIELD_LEN, nullable = false)
    protected String login;

    @SystemLevel
    @Column(name = "LOGIN_LC", length = LOGIN_FIELD_LEN, nullable = false)
    protected String loginLowerCase;

    @SystemLevel
    @Column(name = "PASSWORD", length = 255)
    protected String password;

    @Column(name = "NAME", length = 100)
    protected String name;

    @Column(name = "FIRST_NAME", length = 255)
    protected String firstName;

    @Column(name = "LAST_NAME", length = 255)
    protected String lastName;

    @Column(name = "MIDDLE_NAME", length = 255)
    protected String middleName;

    @Column(name = "POSITION_", length = 255)
    protected String position;

    @Column(name = "EMAIL", length = 100)
    protected String email;

    @Column(name = "LANGUAGE_", length = 20)
    protected String language;

    @Column(name = "ACTIVE")
    protected Boolean active = true;

    @Column(name = "CHANGE_PASSWORD_AT_LOGON")
    protected Boolean changePasswordAtNextLogon = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GROUP_ID")
    @OnDeleteInverse(DeletePolicy.DENY)
    protected Group group;

    @OneToMany(mappedBy = "user")
    @OrderBy("createTs")
    @Composition
    protected List<UserRole> userRoles;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEFAULT_SUBSTITUTED_USER_ID")
    protected User defaultSubstitutedUser;

    @OneToMany(mappedBy = "user")
    @OrderBy("createTs")
    @Composition
    protected List<UserSubstitution> substitutions;

    @Column(name = "IP_MASK", length = 200)
    protected String ipMask;

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

    public List<UserRole> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(List<UserRole> userRoles) {
        this.userRoles = userRoles;
    }

    @Override
    public String toString() {
        return login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
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

    public List<UserSubstitution> getSubstitutions() {
        return substitutions;
    }

    public void setSubstitutions(List<UserSubstitution> substitutions) {
        this.substitutions = substitutions;
    }

    public User getDefaultSubstitutedUser() {
        return defaultSubstitutedUser;
    }

    public void setDefaultSubstitutedUser(User defaultSubstitutedUser) {
        this.defaultSubstitutedUser = defaultSubstitutedUser;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getIpMask() {
        return ipMask;
    }

    public void setIpMask(String ipMask) {
        this.ipMask = ipMask;
    }

    public String getCaption() {
        String pattern = AppContext.getProperty("cuba.user.namePattern");
        if (StringUtils.isBlank(pattern)) {
            pattern = "{1} [{0}]";
        }
        MessageFormat fmt = new MessageFormat(pattern);
        return StringUtils.trimToEmpty(fmt.format(new Object[]{
                StringUtils.trimToEmpty(login),
                StringUtils.trimToEmpty(name)
        }));
    }

    public Boolean getChangePasswordAtNextLogon() {
        return changePasswordAtNextLogon;
    }

    public void setChangePasswordAtNextLogon(Boolean changePasswordAtNextLogon) {
        this.changePasswordAtNextLogon = changePasswordAtNextLogon;
    }

    @Transient
    public String getSalt() {
        return id != null ? id.toString() : "";
    }
}