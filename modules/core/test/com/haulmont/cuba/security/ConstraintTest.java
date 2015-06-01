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
import com.haulmont.cuba.security.entity.Constraint;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.User;
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

    private UUID constraintId, parentConstraintId, groupId, parentGroupId, userId;
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

            Constraint constraint = new Constraint();
            constraintId = constraint.getId();
            constraint.setEntityName("sys$Server");
            constraint.setWhereClause("{E}.name = 'localhost'");
            constraint.setGroup(group);
            em.persist(constraint);

            User user = new User();
            userId = user.getId();
            user.setLogin(USER_LOGIN);

            String pwd = passwordEncryption.getPasswordHash(userId, USER_PASSW);
            user.setPassword(pwd);

            user.setGroup(group);
            em.persist(user);

            tx.commit();
        } finally {
            tx.end();
        }
    }

    @Override
    protected void tearDown() throws Exception {
        deleteRecord("SYS_SERVER", serverId);
        deleteRecord("SEC_USER", userId);
        deleteRecord("SEC_CONSTRAINT", "ID", parentConstraintId, constraintId);
        deleteRecord("SEC_GROUP_HIERARCHY", "GROUP_ID", groupId);
        deleteRecord("SEC_GROUP", groupId);
        deleteRecord("SEC_GROUP", parentGroupId);

        super.tearDown();
    }

    public void test() throws LoginException {
        LoginWorker lw = AppBeans.get(LoginWorker.NAME);

        UserSession userSession = lw.login(USER_LOGIN, passwordEncryption.getPlainHash(USER_PASSW), Locale.getDefault());
        assertNotNull(userSession);

        List<String[]> constraints = userSession.getConstraints("sys$Server");
        assertEquals(2, constraints.size());

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
        } finally {
            ((TestUserSessionSource) uss).setUserSession(savedUserSession);
        }
    }
}
