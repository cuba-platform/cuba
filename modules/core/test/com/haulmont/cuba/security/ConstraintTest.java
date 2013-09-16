/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.security;

import com.haulmont.cuba.core.CubaTestCase;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.security.app.LoginWorker;
import com.haulmont.cuba.security.entity.Constraint;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.LoginException;
import com.haulmont.cuba.security.global.UserSession;

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

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();

            Group parentGroup = new Group();
            parentGroupId = parentGroup.getId();
            parentGroup.setName("testParentGroup");
            em.persist(parentGroup);

            tx.commitRetaining();
            em = persistence.getEntityManager();

            Constraint parentConstraint = new Constraint();
            parentConstraintId = parentConstraint.getId();
            parentConstraint.setEntityName("sys$Server");
            parentConstraint.setWhereClause("address = '127.0.0.1'");
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
            constraint.setWhereClause("name = 'localhost'");
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
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();

            Query q;

            q = em.createNativeQuery("delete from SEC_USER where ID = ?");
            q.setParameter(1, userId.toString());
            q.executeUpdate();

            q = em.createNativeQuery("delete from SEC_CONSTRAINT where ID = ? or ID = ?");
            q.setParameter(1, parentConstraintId.toString());
            q.setParameter(2, constraintId.toString());
            q.executeUpdate();

            q = em.createNativeQuery("delete from SEC_GROUP_HIERARCHY where GROUP_ID = ?");
            q.setParameter(1, groupId.toString());
            q.executeUpdate();

            q = em.createNativeQuery("delete from SEC_GROUP where ID = ?");
            q.setParameter(1, groupId.toString());
            q.executeUpdate();

            q = em.createNativeQuery("delete from SEC_GROUP_HIERARCHY where GROUP_ID = ?");
            q.setParameter(1, groupId.toString());
            q.executeUpdate();

            q = em.createNativeQuery("delete from SEC_GROUP where ID = ?");
            q.setParameter(1, parentGroupId.toString());
            q.executeUpdate();

            tx.commit();
        } finally {
            tx.end();
        }
        super.tearDown();
    }

    public void test() throws LoginException {
        LoginWorker lw = AppBeans.get(LoginWorker.NAME);

        UserSession userSession = lw.login(USER_LOGIN, passwordEncryption.getPlainHash(USER_PASSW), Locale.getDefault());
        assertNotNull(userSession);

        List<String[]> constraints = userSession.getConstraints("sys$Server");
        assertEquals(2, constraints.size());

//        DataService bs = Locator.lookupLocal(DataService.JNDI_NAME);
//
//        DataService.CollectionLoadContext ctx = new DataService.CollectionLoadContext(Group.class);
//        ctx.setQueryString("select g from sec$Group g where g.createTs <= :createTs").addParameter("createTs", new Date());
//
//        List<Group> list = bs.loadList(ctx);
//        assertTrue(list.size() > 0);
    }
}
