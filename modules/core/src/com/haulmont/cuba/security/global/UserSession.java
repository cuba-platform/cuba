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

import java.util.UUID;
import java.io.Serializable;

public class UserSession implements Serializable
{
    private final UUID id;
    private final UUID userId;
    private final String login;
    private final String name;

    public UserSession(User user) {
        id = UuidProvider.createUuid();
        userId = user.getId();
        login = user.getLogin();
        name = user.getName();
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

    public String toString() {
        return "UserSession{" +
                "id=" + id +
                ", login='" + login + '\'' +
                '}';
    }
}
