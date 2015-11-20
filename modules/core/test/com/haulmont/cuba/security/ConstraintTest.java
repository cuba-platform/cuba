/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.security;

import com.haulmont.cuba.core.CubaTestCase;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.entity.Server;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.security.app.LoginWorker;
import com.haulmont.cuba.security.entity.*;
import com.haulmont.cuba.security.global.ConstraintData;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.testsupport.TestUserSessionSource;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * @author krivopustov
 * @version $Id$
 */
public class ConstraintTest extends CubaTestCase {
    private static final String USER_LOGIN = "testUser";
    private static final String USER_PASSW = "testUser";

    private UUID serverConstraintId, userRoleConstraintId,  parentConstraintId, groupId, parentGroupId,
            userId, user2Id, userRoleId, roleId;
    private UUID serverId;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();

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
            em = persistence.getEntityManager();

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

            User user2 = new User();
            user2Id = user2.getId();
            user2.setLogin("someOtherUser");
            em.persist(user2);

            UserRole userRole = new UserRole();
            userRoleId = userRole.getId();
            userRole.setUser(user2);
            Role role = new Role();
            roleId = role.getId();
            em.persist(role);
            userRole.setRole(role);

            em.persist(userRole);

            tx.commit();
        } finally {
            tx.end();
        }
    }

    @Override
    protected void tearDown() throws Exception {
        deleteRecord("SYS_SERVER", serverId);
        deleteRecord("SEC_USER_ROLE", userRoleId);
        deleteRecord("SEC_ROLE", roleId);
        deleteRecord("SEC_USER", userId, user2Id);
        deleteRecord("SEC_CONSTRAINT", "ID", parentConstraintId, serverConstraintId, userRoleConstraintId);
        deleteRecord("SEC_GROUP_HIERARCHY", "GROUP_ID", groupId);
        deleteRecord("SEC_GROUP", groupId);
        deleteRecord("SEC_GROUP", parentGroupId);

        super.tearDown();
    }

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
