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
package com.haulmont.cuba.security;

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.entity.Server;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.security.app.LoginWorker;
import com.haulmont.cuba.security.entity.*;
import com.haulmont.cuba.security.global.ConstraintData;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.testsupport.TestContainer;
import com.haulmont.cuba.testsupport.TestUserSessionSource;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 */
@SuppressWarnings("IncorrectCreateEntity")
public class ConstraintTest {

    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    private static final String USER_LOGIN = "testUser";
    private static final String USER_PASSW = "testUser";

    private UUID serverConstraintId, userRoleConstraintId,  parentConstraintId, groupId, otherGroupId, parentGroupId,
            userId, user2Id, userRoleId, roleId;
    private UUID serverId;

    private PasswordEncryption passwordEncryption;

    @Before
    public void setUp() throws Exception {
        passwordEncryption = AppBeans.get(PasswordEncryption.class);

        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();

            Server server = new Server();
            server.setName("someServer");
            server.setRunning(false);
            serverId = server.getId();
            em.persist(server);

            Group parentGroup = new Group();
            parentGroupId = parentGroup.getId();
            parentGroup.setName("testParentGroup");
            em.persist(parentGroup);

            tx.commitRetaining();
            em = cont.persistence().getEntityManager();

            Constraint parentConstraint = new Constraint();
            parentConstraintId = parentConstraint.getId();
            parentConstraint.setEntityName("sys$Server");
            parentConstraint.setWhereClause("{E}.running = true");
            parentConstraint.setGroup(parentGroup);
            em.persist(parentConstraint);

            Group group = new Group();
            groupId = group.getId();
            group.setName("testGroup");
            group.setParent(parentGroup);
            em.persist(group);

            Constraint serverConstraint = new Constraint();
            serverConstraintId = serverConstraint.getId();
            serverConstraint.setEntityName("sys$Server");
            serverConstraint.setWhereClause("{E}.name = 'localhost'");
            serverConstraint.setGroup(group);
            em.persist(serverConstraint);

            Constraint userRoleConstraint = new Constraint();
            userRoleConstraintId = userRoleConstraint.getId();
            userRoleConstraint.setEntityName("sec$UserRole");
            userRoleConstraint.setWhereClause("{E}.user.id = :session$userId");
            userRoleConstraint.setGroup(group);
            em.persist(userRoleConstraint);

            User user = new User();
            userId = user.getId();
            user.setLogin(USER_LOGIN);

            String pwd = passwordEncryption.getPasswordHash(userId, USER_PASSW);
            user.setPassword(pwd);

            user.setGroup(group);
            em.persist(user);

            Group otherGroup = new Group();
            otherGroupId = otherGroup.getId();
            otherGroup.setName("otherGroup");
            otherGroup.setParent(parentGroup);
            em.persist(otherGroup);

            User user2 = new User();
            user2.setGroup(otherGroup);
            user2Id = user2.getId();
            user2.setLogin("someOtherUser");
            em.persist(user2);

            UserRole userRole = new UserRole();
            userRoleId = userRole.getId();
            userRole.setUser(user2);
            Role role = new Role();
            role.setName("TestRole");
            roleId = role.getId();
            em.persist(role);
            userRole.setRole(role);

            em.persist(userRole);

            tx.commit();
        } finally {
            tx.end();
        }
    }

    @After
    public void tearDown() throws Exception {
        cont.deleteRecord("SYS_SERVER", serverId);
        cont.deleteRecord("SEC_USER_ROLE", userRoleId);
        cont.deleteRecord("SEC_ROLE", roleId);
        cont.deleteRecord("SEC_USER", userId, user2Id);
        cont.deleteRecord("SEC_CONSTRAINT", "ID", parentConstraintId, serverConstraintId, userRoleConstraintId);
        cont.deleteRecord("SEC_GROUP_HIERARCHY", "GROUP_ID", groupId, otherGroupId);
        cont.deleteRecord("SEC_GROUP_HIERARCHY", "GROUP_ID", otherGroupId);
        cont.deleteRecord("SEC_GROUP", groupId, otherGroupId);
        cont.deleteRecord("SEC_GROUP", parentGroupId);
    }

    @Test
    public void test() throws LoginException {
        LoginWorker lw = AppBeans.get(LoginWorker.NAME);

        UserSession userSession = lw.login(USER_LOGIN, passwordEncryption.getPlainHash(USER_PASSW), Locale.getDefault());
        assertNotNull(userSession);

        List<ConstraintData> constraints = userSession.getConstraints("sys$Server");
        assertEquals(2, constraints.size());

        List<ConstraintData> roleConstraints = userSession.getConstraints("sec$UserRole");
        assertEquals(1, roleConstraints.size());

        UserSessionSource uss = AppBeans.get(UserSessionSource.class);
        UserSession savedUserSession = uss.getUserSession();
        ((TestUserSessionSource) uss).setUserSession(userSession);
        try {
            DataManager dm = AppBeans.get(DataManager.NAME);
            LoadContext loadContext = new LoadContext(Server.class)
                    .setQuery(new LoadContext.Query("select s from sys$Server s"));
            List<Server> list = dm.loadList(loadContext);
            for (Server server : list) {
                if (server.getId().equals(serverId))
                    fail("Constraints have not taken effect for some reason");
            }

            //test constraint that contains session parameter
            loadContext = new LoadContext(UserRole.class)
                    .setQuery(new LoadContext.Query("select ur from sec$UserRole ur"));
            List<UserRole> userRoles = dm.loadList(loadContext);
            if (!userRoles.isEmpty()) {
                fail("Constraint with session attribute failed");
            }
        } finally {
            ((TestUserSessionSource) uss).setUserSession(savedUserSession);
        }
    }
}
