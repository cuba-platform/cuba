/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 02.12.2008 15:30:51
 *
 * $Id$
 */
package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.SecurityProvider;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Locale;
import java.util.UUID;

public class TestSecurityProvider extends SecurityProvider
{
    public static final String USER_ID = "60885987-1b61-4247-94c7-dff348347f93";

    protected UserSession __currentUserSession() {
        User user = new User();
        user.setId(UUID.fromString(USER_ID));
        user.setLogin("test_admin");
        user.setName("Test Administrator");
        user.setPassword(DigestUtils.md5Hex("test_admin"));

        UserSession session = new UserSession(user, new String[]{"Administrators"}, Locale.getDefault());
        session.addConstraint("sec$Group", "", "a.createdBy = :currentUserLogin");

        return session;
    }
}
