/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 02.12.2008 15:30:51
 *
 * $Id$
 */
package com.haulmont.cuba.core.impl;

import com.haulmont.cuba.core.global.SecurityProvider;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.entity.User;

import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;

public class TestSecurityProvider extends SecurityProvider
{
    protected UserSession __currentUserSession() {
        User user = new User();
        user.setId(UUID.fromString("60885987-1b61-4247-94c7-dff348347f93"));
        user.setLogin("test_admin");
        user.setName("Test Administrator");
        user.setPassword(DigestUtils.md5Hex("test_admin"));

        UserSession session = new UserSession(user);
        return session;
    }
}
