/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core;

import com.haulmont.bali.db.QueryRunner;
import com.haulmont.bali.db.ResultSetHandler;
import com.haulmont.cuba.core.entity.EntitySnapshot;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

/**
 * @author krivopustov
 * @version $Id$
 */
public class ViewTest extends CubaTestCase {

    private TimeSource timeSource;

    private UUID userId;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        timeSource = AppBeans.get(TimeSource.NAME);

        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();

            Group group = em.find(Group.class, UUID.fromString("0fa2b1a5-1d68-4d69-9fbd-dff348347f93"));

            User user = new User();
            userId = user.getId();
            user.setName("testUser");
            user.setLogin("login" + userId);
            user.setPassword("000");
            user.setGroup(group);
            em.persist(user);

            tx.commit();
        } finally {
            tx.end();
        }
    }

    @Override
    protected void tearDown() throws Exception {
        deleteRecord("SEC_USER", userId);
        super.tearDown();
    }

    public void testQuery() {
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            Query q = em.createQuery("select u from sec$User u where u.id = ?1");
            q.setParameter(1, userId);

            View view = new View(User.class)
                    .addProperty("name")
                    .addProperty("login")
                    .addProperty("group",
                            new View(Group.class)
                                    .addProperty("name")
                    );
            q.setView(view);

            User user = (User) q.getSingleResult();

            tx.commit();

            assertNull(user.getPassword());
            assertNotNull(user.getGroup().getName());
        } finally {
            tx.end();
        }
    }

    public void testEntityManager() {
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();

            View view = new View(User.class)
                    .addProperty("name")
                    .addProperty("login")
                    .addProperty("group",
                            new View(Group.class)
                                    .addProperty("name")
                    );
            em.setView(view);

            User user = em.find(User.class, userId);

            tx.commit();

            assertNull(user.getPassword());
            assertNotNull(user.getGroup().getName());
            assertNotNull(user.getCreateTs());
            assertNotNull(user.getGroup().getCreateTs());
        } finally {
            tx.end();
        }
    }

    public void testViewWithoutSystemProperties() {
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();

            View view = new View(User.class, false)
                    .addProperty("name")
                    .addProperty("login")
                    .addProperty("group",
                            new View(Group.class, false)
                                    .addProperty("name")
                    );
            em.setView(view);

            User user = em.find(User.class, userId);

            tx.commit();

            assertNull(user.getPassword());
            assertNotNull(user.getGroup().getName());
            assertNull(user.getCreateTs());
            assertNull(user.getGroup().getCreateTs());
        } finally {
            tx.end();
        }
    }

    public void testViewWithoutSystemProperties_update() throws Exception {

        View view = new View(User.class, false)
                .addProperty("name")
                .addProperty("login")
                .addProperty("group",
                        new View(Group.class, false)
                                .addProperty("name")
                );

        Transaction tx = persistence.createTransaction();
        try {
            // First stage: change managed

            Date ts = timeSource.currentTimestamp();
            Thread.sleep(1000);

            EntityManager em = persistence.getEntityManager();
            em.setView(view);
            User user = em.find(User.class, userId);
            user.setName(new Date().toString());

            tx.commitRetaining();

            assertTrue(getCurrentUpdateTs().after(ts));

            // Second stage: change detached

            ts = timeSource.currentTimestamp();
            Thread.sleep(1000);

            em = persistence.getEntityManager();
            em.setView(view);
            user = em.find(User.class, userId);

            tx.commitRetaining();

            user.setName(new Date().toString());
            em = persistence.getEntityManager();
            em.merge(user);

            tx.commit();

            assertTrue(getCurrentUpdateTs().after(ts));

        } finally {
            tx.end();
        }
    }

    /*
     * Test that entity which is loaded with view, can lazily fetch not-loaded attributes until transaction ends.
     */
    public void testLazyLoadAfterLoadWithView() {
        View view = new View(User.class, false).addProperty("name");

        User user;
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            user = em.find(User.class, userId, view);
            tx.commit();
        } finally {
            tx.end();
        }
        assertNull(user.getLogin()); // login is not loaded after transaction is finished

        tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            user = em.find(User.class, userId, view);

            em.setView(null);
            assertNotNull(user.getLogin()); // field is loaded lazily

            tx.commit();
        } finally {
            tx.end();
        }
    }

    private Date getCurrentUpdateTs() {
        String sql = "select UPDATE_TS from SEC_USER where ID = '" + userId.toString() + "'";
        QueryRunner runner = new QueryRunner(persistence.getDataSource());
        try {
            return runner.query(sql, new ResultSetHandler<Date>() {
                @Override
                public Date handle(ResultSet rs) throws SQLException {
                    rs.next();
                    return rs.getTimestamp("UPDATE_TS");
                }
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void testNoTransientPropertiesInLocalView() throws Exception {
        View view = metadata.getViewRepository().getView(EntitySnapshot.class, View.LOCAL);
        ViewProperty prop = view.getProperty("label");
        assertNull(prop);
    }

    public void testViewCopy() throws Exception {
        ViewRepository viewRepository = metadata.getViewRepository();
        View view = viewRepository.getView(User.class, View.LOCAL);
        view.addProperty("group", viewRepository.getView(Group.class, View.MINIMAL));

        assertNotNull(view.getProperty("group"));
        assertNull(viewRepository.getView(User.class, View.LOCAL).getProperty("group"));
    }
}
