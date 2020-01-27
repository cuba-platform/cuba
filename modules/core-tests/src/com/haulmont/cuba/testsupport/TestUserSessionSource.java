/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.testsupport;

import com.haulmont.cuba.core.sys.AbstractUserSessionSource;
import com.haulmont.cuba.security.entity.Access;
import com.haulmont.cuba.security.entity.EntityAttrAccess;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.NoUserSessionException;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.role.*;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Collections;
import java.util.Locale;
import java.util.UUID;

public class TestUserSessionSource extends AbstractUserSessionSource {

    public static final String USER_ID = "60885987-1b61-4247-94c7-dff348347f93";

    private UserSession session;
    private boolean exceptionOnGetUserSession;

    @Override
    public boolean checkCurrentUserSession() {
        return true;
    }

    public void setExceptionOnGetUserSession(boolean exceptionOnGetUserSession) {
        this.exceptionOnGetUserSession = exceptionOnGetUserSession;
    }

    @Override
    public synchronized UserSession getUserSession() {
        if (exceptionOnGetUserSession) {
            throw new NoUserSessionException(UUID.fromString(USER_ID));
        }
        if (session == null) {
            User user = new User();
            user.setId(UUID.fromString(USER_ID));
            user.setLogin("test_admin");
            user.setName("Test Administrator");
            user.setPassword(DigestUtils.md5Hex("test_admin"));

            session = new UserSession(UUID.randomUUID(), user, Collections.emptyList(), Locale.forLanguageTag("en"), false);
            session.setJoinedRole(new TestFullAccessRole());
        }
        return session;
    }

    public void setUserSession(UserSession session) {
        this.session = session;
    }

    public static class TestFullAccessRole implements RoleDefinition {

        private EntityPermissionsContainer entityPermissions;
        private EntityAttributePermissionsContainer entityAttributePermissions;
        private SpecificPermissionsContainer specificPermissions;
        private ScreenPermissionsContainer screenPermissions;
        private ScreenComponentPermissionsContainer screenElementsPermissions;

        public TestFullAccessRole() {
            entityPermissions = new EntityPermissionsContainer();
            entityAttributePermissions = new EntityAttributePermissionsContainer();
            specificPermissions = new SpecificPermissionsContainer();
            screenPermissions = new ScreenPermissionsContainer();
            screenElementsPermissions = new ScreenComponentPermissionsContainer();

            entityPermissions.setDefaultEntityCreateAccess(Access.ALLOW);
            entityPermissions.setDefaultEntityReadAccess(Access.ALLOW);
            entityPermissions.setDefaultEntityUpdateAccess(Access.ALLOW);
            entityPermissions.setDefaultEntityDeleteAccess(Access.ALLOW);
            entityAttributePermissions.setDefaultEntityAttributeAccess(EntityAttrAccess.MODIFY);
            specificPermissions.setDefaultSpecificAccess(Access.ALLOW);
            screenPermissions.setDefaultScreenAccess(Access.ALLOW);
        }

        @Override
        public String getName() {
            return "system-test-full-access";
        }

        @Override
        public EntityPermissionsContainer entityPermissions() {
            return entityPermissions;
        }

        @Override
        public EntityAttributePermissionsContainer entityAttributePermissions() {
            return entityAttributePermissions;
        }

        @Override
        public SpecificPermissionsContainer specificPermissions() {
            return specificPermissions;
        }

        @Override
        public ScreenPermissionsContainer screenPermissions() {
            return screenPermissions;
        }

        @Override
        public ScreenComponentPermissionsContainer screenComponentPermissions() {
            return screenElementsPermissions;
        }
    }
}