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
import com.haulmont.cuba.security.entity.Subject;
import com.haulmont.cuba.security.entity.Profile;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Locale;
import java.util.UUID;

public class TestSecurityProvider extends SecurityProvider
{
    protected UserSession __currentUserSession() {
        User user = new User();
        user.setId(UUID.fromString("60885987-1b61-4247-94c7-dff348347f93"));
        user.setLogin("test_admin");
        user.setName("Test Administrator");
        user.setPassword(DigestUtils.md5Hex("test_admin"));

        Profile profile = new Profile();
        profile.setId(UUID.fromString("bf83541f-f610-46f4-a268-dff348347f93"));
        profile.setName("Default");

        Subject subject = new Subject();
        subject.setId(UUID.fromString("05d9d689-da68-4622-8952-f94dfb36ca07"));
        subject.setUser(user);
        subject.setProfile(profile);
        
        UserSession session = new UserSession(user, subject, new String[]{"Administrators"}, Locale.getDefault());
        session.addConstraint("sec$Group", "a.createdBy = :currentSubjectId");

        return session;
    }
}
