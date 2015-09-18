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
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.UserRole;
import org.eclipse.persistence.jpa.jpql.parser.ExpressionVisitorWrapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import static com.haulmont.cuba.testsupport.TestSupport.reserialize;

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

    public void testQuery() throws Exception {
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
            user = reserialize(user);

            try {
                user.getPassword();
                fail();
            } catch (Exception ignored) {
            }
            assertNotNull(user.getGroup().getName());
        } finally {
            tx.end();
        }
    }

    public void testEntityManager() throws Exception {
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

            User user = em.find(User.class, userId, view);

            tx.commit();
            user = reserialize(user);

            try {
                user.getPassword();
                fail();
            } catch (Exception ignored) {
            }
            assertNotNull(user.getGroup().getName());
            assertNotNull(user.getCreateTs());
            assertNotNull(user.getGroup().getCreateTs());
        } finally {
            tx.end();
        }
    }

    public void testViewWithoutSystemProperties() throws Exception {
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

            User user = em.find(User.class, userId, view);

            tx.commit();
            user = reserialize(user);

            try {
                user.getPassword();
                fail();
            } catch (Exception ignored) {
            }
            try {
                user.getCreateTs();
                fail();
            } catch (Exception ignored) {
            }
            assertNotNull(user.getGroup().getName());
            try {
                user.getGroup().getCreateTs();
                fail();
            } catch (Exception ignored) {
            }
        } finally {
            tx.end();
        }
    }

    public void testViewWithoutSystemProperties_update() throws Exception {

        View view = new View(User.class, false)
                .addProperty("name")
                .addProperty("login")
                .addProperty("loginLowerCase")
                .addProperty("group",
                        new View(Group.class, false)
                                .addProperty("name")
                );

        Transaction tx = persistence.createTransaction();
        try {
            // First stage: change managed

            long ts = timeSource.currentTimeMillis();
            Thread.sleep(200);

            EntityManager em = persistence.getEntityManager();
            User user = em.find(User.class, userId, view);
            user.setName(new Date().toString());

            try {
                tx.commitRetaining();

                assertTrue(getCurrentUpdateTs() > ts);
            } catch (Exception e) {
                // todo el: commit is failed because updateTs & updatedBy can not be changed (https://bugs.eclipse.org/bugs/show_bug.cgi?id=466841)
                tx = persistence.createTransaction();
            }

            // Second stage: change detached

            ts = timeSource.currentTimeMillis();
            Thread.sleep(200);

            em = persistence.getEntityManager();
            user = em.find(User.class, userId, view);

            tx.commitRetaining();

            user.setName(new Date().toString());
            em = persistence.getEntityManager();
            em.merge(user);

            tx.commit();

            assertTrue(getCurrentUpdateTs() > ts);

        } finally {
            tx.end();
        }
    }

    /*
     * Pre 6.0: Test that entity which is loaded with view, can lazily fetch not-loaded attributes until transaction ends.
     *
     * 6.0: Not loaded (unfetched) attributes cannot be loaded lazily.
     */
    public void testLazyLoadAfterLoadWithView() throws Exception {
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

        user = reserialize(user);

        // login is not loaded after transaction is finished
        try {
            user.getLogin();
            fail();
        } catch (Exception ignored) {
        }

        tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            user = em.find(User.class, userId, view);
            assertNotNull(user);

            // field is not loaded lazily
            try {
                user.getLogin();
                fail();
            } catch (Exception ignored) {
            }

            tx.commit();
        } finally {
            tx.end();
        }
    }


    public void testLazyProperty() throws Exception {
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();
            Query q = em.createQuery("select u from sec$User u where u.id = ?1");
            q.setParameter(1, userId);

            View userRoleView = new View(UserRole.class).addProperty("role", new View(Role.class).addProperty("name"));
            View view = new View(User.class)
                    .addProperty("name")
                    .addProperty("login")
                    .addProperty("userRoles", userRoleView, true)
                    .addProperty("group",
                            new View(Group.class)
                                    .addProperty("name")
                    );
            q.setView(view);

            User user = (User) q.getSingleResult();

            tx.commit();
            user = reserialize(user);

            user.getUserRoles().size();

            try {
                user.getPassword();
                fail();
            } catch (Exception ignored) {
            }
            assertNotNull(user.getGroup().getName());
        } finally {
            tx.end();
        }
    }

    private long getCurrentUpdateTs() {
        String sql = "select UPDATE_TS from SEC_USER where ID = '" + userId.toString() + "'";
        QueryRunner runner = new QueryRunner(persistence.getDataSource());
        try {
            return runner.query(sql, new ResultSetHandler<Long>() {
                @Override
                public Long handle(ResultSet rs) throws SQLException {
                    rs.next();
                    return rs.getTimestamp("UPDATE_TS").getTime();
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
