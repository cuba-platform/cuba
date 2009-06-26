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
import java.util.concurrent.ConcurrentHashMap;
import java.io.Serializable;

public class UserSession implements Serializable
{
    private static final long serialVersionUID = -8248326616891177382L;

    private UUID userId;
    private UUID id;
    private String login;
    private String name;
    private String[] roles;
    private Locale locale;

    private Map<String, Integer>[] permissions;
    private Map<String, List<String[]>> constraints;

    private final Map<String, Serializable> attributes;

    public UserSession(User user, String[] roles, Locale locale) {
        this.id = UuidProvider.createUuid();
        this.userId = user.getId();
        this.login = user.getLogin();
        this.name = user.getName();

        this.roles = roles;
        Arrays.sort(this.roles);

        this.locale = locale;

        permissions = new Map[PermissionType.values().length];
        for (int i = 0; i < permissions.length; i++) {
            permissions[i] = new HashMap<String, Integer>();
        }

        constraints = new HashMap<String, List<String[]>>();
        attributes = new ConcurrentHashMap<String, Serializable>();
    }
    
    public UserSession(UserSession src, User user, String[] roles, Locale locale) {
        this(user, roles, locale);
        this.id = src.id;
        this.userId = src.userId;
        this.login = src.login;
        this.name = src.name;
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

    public void addConstraint(String entityName, String joinClause, String whereClause) {
        List<String[]> list = constraints.get(entityName);
        if (list == null) {
            list = new ArrayList<String[]>();
            constraints.put(entityName, list);
        }
        list.add(new String[] {joinClause, whereClause});
    }

    public List<String[]> getConstraints(String entityName) {
        List<String[]> list = constraints.get(entityName);
        return list != null ? list : Collections.<String[]>emptyList();
    }

    public <T extends Serializable> T getAttribute(String name) {
        return (T) attributes.get(name);
    }

    public void setAttribute(String name, Serializable value) {
        attributes.put(name, value);
    }

    public String toString() {
        return "UserSession{" +
                "id=" + id +
                ", login='" + login + '\'' +
                '}';
    }
}
