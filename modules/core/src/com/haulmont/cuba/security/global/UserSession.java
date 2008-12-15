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
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.Profile;
import com.haulmont.cuba.security.entity.ProfileRole;

import java.util.UUID;
import java.util.List;
import java.util.Arrays;
import java.util.Locale;
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

    public UserSession(User user, String[] roles, Locale locale) {
        id = UuidProvider.createUuid();
        userId = user.getId();
        login = user.getLogin();
        name = user.getName();

        this.roles = roles;
        Arrays.sort(this.roles);

        this.locale = locale;
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

    public String toString() {
        return "UserSession{" +
                "id=" + id +
                ", login='" + login + '\'' +
                '}';
    }
}
