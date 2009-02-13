/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 25.12.2008 17:39:53
 *
 * $Id$
 */
package com.haulmont.cuba.security;

import com.haulmont.cuba.core.*;
import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.security.entity.*;
import com.haulmont.cuba.security.app.LoginWorker;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.security.global.LoginException;

import java.util.UUID;
import java.util.Locale;
import java.util.List;
import java.util.Date;

import org.apache.commons.codec.digest.DigestUtils;

public class ConstraintTest extends CubaTestCase
{
    private static final String ADMIN_NAME = "admin";
    private static final String ADMIN_PASSW = DigestUtils.md5Hex("admin");
    private static final String PROFILE_NAME = "testProfile";

    private UUID constraintId, parentConstraintId, groupId, parentGroupId, profileId, subjectId;

    protected void setUp() throws Exception {
        super.setUp();

        Transaction tx = Locator.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();

            User user = em.find(User.class, UUID.fromString("60885987-1b61-4247-94c7-dff348347f93"));

            Group parentGroup = new Group();
            parentGroupId = parentGroup.getId();
            parentGroup.setName("testParentGroup");
            em.persist(parentGroup);

            tx.commitRetaining();
            em = PersistenceProvider.getEntityManager();

            Constraint parentConstraint = new Constraint();
            parentConstraintId = parentConstraint.getId();
            parentConstraint.setEntityName("core$Server");
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
            constraint.setEntityName("core$Server");
            constraint.setWhereClause("name = 'localhost'");
            constraint.setGroup(group);
            em.persist(constraint);

            Profile profile = new Profile();
            profileId = profile.getId();
            profile.setName(PROFILE_NAME);
            profile.setGroup(group);
            em.persist(profile);

            Subject subject = new Subject();
            subjectId = subject.getId();
            subject.setUser(user);
            subject.setProfile(profile);
            em.persist(subject);

            tx.commit();
        } finally {
            tx.end();
        }
    }

    protected void tearDown() throws Exception {
        Transaction tx = Locator.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();

            Query q = em.createNativeQuery("delete from SEC_SUBJECT where ID = ?");
            q.setParameter(1, subjectId.toString());
            q.executeUpdate();

            q = em.createNativeQuery("delete from SEC_PROFILE where ID = ?");
            q.setParameter(1, profileId.toString());
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
        LoginWorker lw = Locator.lookupLocal(LoginWorker.JNDI_NAME);

        UserSession userSession = lw.login(ADMIN_NAME, ADMIN_PASSW, PROFILE_NAME, Locale.getDefault());
        assertNotNull(userSession);

        List<String> constraints = userSession.getConstraints("core$Server");
        assertEquals(2, constraints.size());

        DataService bs = Locator.lookupLocal(DataService.JNDI_NAME);

        DataService.CollectionLoadContext ctx = new DataService.CollectionLoadContext(Group.class);
        ctx.setQueryString("select g from sec$Group g where g.createTs <= :createTs").addParameter("createTs", new Date());

        List<Group> list = bs.loadList(ctx);
        assertTrue(list.size() > 0);
    }
}
