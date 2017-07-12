/*
 * Copyright (c) 2008-2017 Haulmont.
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
 */

package com.haulmont.cuba.security;

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.security.app.LoginWorker;
import com.haulmont.cuba.security.entity.*;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.testsupport.TestContainer;
import com.haulmont.cuba.testsupport.TestUserSessionSource;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.Locale;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;

@SuppressWarnings("IncorrectCreateEntity")
public class DistinctConstraintTest {
    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    private static final String USER_LOGIN = "testUser";
    private static final String USER_PASSW = "testUser";

    private UUID userConstraintId,
            groupId, parentGroupId,
            user1Id, user2Id,
            userRole1Id, userRole2Id,
            role1Id, role2Id;

    private PasswordEncryption passwordEncryption;

    @Before
    public void setUp() throws Exception {
        passwordEncryption = AppBeans.get(PasswordEncryption.class);

        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();

            Group parentGroup = new Group();
            parentGroupId = parentGroup.getId();
            parentGroup.setName("testParentGroup");
            em.persist(parentGroup);

            tx.commitRetaining();
            em = cont.persistence().getEntityManager();

            Group group = new Group();
            groupId = group.getId();
            group.setName("testGroup");
            group.setParent(parentGroup);
            em.persist(group);

            Constraint userConstraint = new Constraint();
            userConstraintId = userConstraint.getId();
            userConstraint.setEntityName("sec$User");
            userConstraint.setJoinClause("join {E}.userRoles ur");
            userConstraint.setWhereClause("{E}.id is not null");
            userConstraint.setGroup(group);
            em.persist(userConstraint);

            User user = new User();
            user1Id = user.getId();
            user.setLogin(USER_LOGIN);

            String pwd = passwordEncryption.getPasswordHash(user1Id, USER_PASSW);
            user.setPassword(pwd);

            user.setGroup(group);
            em.persist(user);

            User user2 = new User();
            user2.setGroup(parentGroup);
            user2Id = user2.getId();
            user2.setLogin("someOtherUser");
            em.persist(user2);

            Role role1 = new Role();
            role1.setName("TestRole1");
            role1Id = role1.getId();
            em.persist(role1);

            Role role2 = new Role();
            role2.setName("TestRole2");
            role2Id = role2.getId();
            em.persist(role2);

            UserRole userRole1 = new UserRole();
            userRole1Id = userRole1.getId();
            userRole1.setUser(user2);
            userRole1.setRole(role1);
            em.persist(userRole1);

            UserRole userRole2 = new UserRole();
            userRole2Id = userRole2.getId();
            userRole2.setUser(user2);
            userRole2.setRole(role2);
            em.persist(userRole2);

            tx.commit();
        } finally {
            tx.end();
        }
    }

    @After
    public void tearDown() throws Exception {
        cont.deleteRecord("SEC_USER_ROLE", userRole1Id, userRole2Id);
        cont.deleteRecord("SEC_ROLE", role1Id, role2Id);
        cont.deleteRecord("SEC_USER", user1Id, user2Id);
        cont.deleteRecord("SEC_CONSTRAINT", "ID", userConstraintId);
        cont.deleteRecord("SEC_GROUP_HIERARCHY", "GROUP_ID", groupId);
        cont.deleteRecord("SEC_GROUP_HIERARCHY", "GROUP_ID", parentGroupId);
        cont.deleteRecord("SEC_GROUP", groupId);
        cont.deleteRecord("SEC_GROUP", parentGroupId);
    }

    @Test
    public void test() throws LoginException {
        LoginWorker lw = AppBeans.get(LoginWorker.NAME);

        UserSession userSession = lw.login(USER_LOGIN, passwordEncryption.getPlainHash(USER_PASSW), Locale.getDefault());
        assertNotNull(userSession);

        UserSessionSource uss = AppBeans.get(UserSessionSource.class);
        UserSession savedUserSession = uss.getUserSession();
        ((TestUserSessionSource) uss).setUserSession(userSession);
        try {
            DataManager dm = AppBeans.get(DataManager.NAME);
            LoadContext<User> loadContext = new LoadContext<>(User.class).setId(user2Id).setView(View.LOCAL);

            User user = dm.load(loadContext);

            assertNotNull(user);

        } finally {
            ((TestUserSessionSource) uss).setUserSession(savedUserSession);
        }
    }
}
