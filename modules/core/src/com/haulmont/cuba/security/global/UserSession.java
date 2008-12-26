/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 26.11.2008 13:27:53
 *
 * $Id$
 */
package com.haulmont.cuba.security.global;

import com.haulmont.cuba.core.global.UuidProvider;
import com.haulmont.cuba.security.entity.*;

import java.util.*;
import java.io.Serializable;

public class UserSession implements Serializable
{
    private static final long serialVersionUID = -8248326616891177382L;

    private final UUID id;
    private final UUID userId;
    private final String login;
    private final String name;
    private final String[] roles;
    private final Locale locale;

    private final Map<String, Integer>[] permissions;
    private final Map<String, List<String>> constraints;

    public UserSession(User user, String[] roles, Locale locale) {
        id = UuidProvider.createUuid();
        userId = user.getId();
        login = user.getLogin();
        name = user.getName();

        this.roles = roles;
        Arrays.sort(this.roles);

        this.locale = locale;

        permissions = new Map[PermissionType.values().length];
        for (int i = 0; i < permissions.length; i++) {
            permissions[i] = new HashMap<String, Integer>();
        }

        constraints = new HashMap<String, List<String>>();
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getLogin() {
        return login;
    }

    public String getName() {
        return name;
    }

    public String[] getRoles() {
        return roles;
    }

    public Locale getLocale() {
        return locale;
    }

    public void addPermission(PermissionType type, String target, int value) {
        permissions[type.ordinal()].put(target, value);
    }

    public Integer getPermissionValue(PermissionType type, String target) {
        return permissions[type.ordinal()].get(target);
    }
    
    public boolean isPermitted(PermissionType type, String target) {
        return isPermitted(type, target, 1);
    }

    public boolean isPermitted(PermissionType type, String target, int value) {
        Integer p = permissions[type.ordinal()].get(target);
        return p == null || p >= value;
    }

    public void addConstraint(String entityName, String constraint) {
        List<String> list = constraints.get(entityName);
        if (list == null) {
            list = new ArrayList<String>();
            constraints.put(entityName, list);
        }
        list.add(constraint);
    }

    public List<String> getConstraints(String entityName) {
        List<String> list = constraints.get(entityName);
        return list != null ? list : Collections.<String>emptyList();
    }

    public String toString() {
        return "UserSession{" +
                "id=" + id +
                ", login='" + login + '\'' +
                '}';
    }
}
