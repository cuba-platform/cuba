/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Collections;
import java.util.Locale;
import java.util.UUID;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class TestUserSessionSource extends AbstractUserSessionSource {

    public static final String USER_ID = "60885987-1b61-4247-94c7-dff348347f93";

    @Override
    public boolean checkCurrentUserSession() {
        return true;
    }

    @Override
    public UserSession getUserSession() {
        User user = new User();
        user.setId(UUID.fromString(USER_ID));
        user.setLogin("test_admin");
        user.setName("Test Administrator");
        user.setPassword(DigestUtils.md5Hex("test_admin"));

        UserSession session = new UserSession(user, Collections.<Role>emptyList(), Locale.getDefault(), false);
        session.addConstraint("sec$Group", "", "a.createdBy = :currentUserLogin");

        return session;
    }
}
