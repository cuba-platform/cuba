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

package com.haulmont.cuba.client.testsupport;

import com.haulmont.cuba.core.sys.AbstractUserSessionSource;
import com.haulmont.cuba.security.entity.Access;
import com.haulmont.cuba.security.entity.EntityAttrAccess;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.role.*;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Collections;
import java.util.Locale;
import java.util.UUID;

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
            session = createTestSession();
        }
        return session;
    }

    public UserSession createTestSession() {
        User user = createTestUser();

        UserSession userSession = new UserSession(UUID.randomUUID(), user, Collections.emptyList(), Locale.ENGLISH, false);
        userSession.setJoinedRole(new TestFullAccessRole());
        return userSession;
    }

    public User createTestUser() {
        User user = new User();
        user.setId(UUID.fromString(USER_ID));
        user.setLogin("test_admin");
        user.setName("Test Administrator");
        user.setPassword(DigestUtils.md5Hex("test_admin"));
        return user;
    }

    public UserSession getSession() {
        return session;
    }

    public void setSession(UserSession session) {
        this.session = session;
    }

    public static class TestFullAccessRole implements RoleDefinition {

        private EntityPermissionsContainer entityPermissions;
        private EntityAttributePermissionsContainer entityAttributePermissions;
        private SpecificPermissionsContainer specificPermissions;
        private ScreenPermissionsContainer screenPermissions;
        private ScreenElementsPermissionsContainer screenElementsPermissions;

        private TestFullAccessRole() {
            entityPermissions = new EntityPermissionsContainer();
            entityAttributePermissions = new EntityAttributePermissionsContainer();
            specificPermissions = new SpecificPermissionsContainer();
            screenPermissions = new ScreenPermissionsContainer();
            screenElementsPermissions = new ScreenElementsPermissionsContainer();

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
        public ScreenElementsPermissionsContainer screenElementsPermissions() {
            return screenElementsPermissions;
        }
    }
}