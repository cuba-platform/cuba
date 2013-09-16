/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.client.testsupport;

import com.haulmont.cuba.core.sys.AbstractUserSessionSource;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Collections;
import java.util.Locale;
import java.util.UUID;

/**
 * <p>$Id: TestUserSessionSource.java 8266 2012-06-26 11:59:00Z krivopustov $</p>
 *
 * @author krivopustov
 */
public class TestUserSessionSource extends AbstractUserSessionSource {

    public static final String USER_ID = "60885987-1b61-4247-94c7-dff348347f93";

    private UserSession session;

    @Override
    public boolean checkCurrentUserSession() {
        return true;
    }

    @Override
    public synchronized UserSession getUserSession() {
        if (session == null) {
            User user = new User();
            user.setId(UUID.fromString(USER_ID));
            user.setLogin("test_admin");
            user.setName("Test Administrator");
            user.setPassword(DigestUtils.md5Hex("test_admin"));

            session = new UserSession(UUID.randomUUID(), user, Collections.<Role>emptyList(), Locale.getDefault(), false);
        }
        return session;
    }
}
