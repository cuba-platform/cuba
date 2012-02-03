/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 19.12.2008 12:03:22
 *
 * $Id$
 */
package com.haulmont.cuba.core;

import com.haulmont.bali.db.QueryRunner;
import com.haulmont.bali.db.ResultSetHandler;
import com.haulmont.cuba.core.global.TimeProvider;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.Group;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

public class ViewTest extends CubaTestCase
{
    private UUID userId;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        Transaction tx = PersistenceProvider.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();

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
        Transaction tx = PersistenceProvider.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();
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
        Transaction tx = PersistenceProvider.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();

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
        Transaction tx = PersistenceProvider.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();

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

        Transaction tx = PersistenceProvider.createTransaction();
        try {
            // First stage: change managed

            Date ts = TimeProvider.currentTimestamp();
            Thread.sleep(1000);

            EntityManager em = PersistenceProvider.getEntityManager();
            em.setView(view);
            User user = em.find(User.class, userId);
            user.setName(new Date().toString());

            tx.commitRetaining();

            assertTrue(getCurrentUpdateTs().after(ts));

            // Second stage: change detached

            ts = TimeProvider.currentTimestamp();
            Thread.sleep(1000);

            em = PersistenceProvider.getEntityManager();
            em.setView(view);
            user = em.find(User.class, userId);
            
            tx.commitRetaining();
            
            user.setName(new Date().toString());
            em = PersistenceProvider.getEntityManager();
            em.merge(user);

            tx.commit();

            assertTrue(getCurrentUpdateTs().after(ts));

        } finally {
            tx.end();
        }
    }

    private Date getCurrentUpdateTs() {
        String sql = "select UPDATE_TS from SEC_USER where ID = '" + userId.toString() + "'";
        QueryRunner runner = new QueryRunner(Locator.getDataSource());
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
}
