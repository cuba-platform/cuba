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
package com.haulmont.cuba.core;

import com.haulmont.bali.db.QueryRunner;
import com.haulmont.bali.db.ResultSetHandler;
import com.haulmont.cuba.core.entity.EntitySnapshot;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.security.entity.*;
import com.haulmont.cuba.testsupport.TestContainer;
import org.eclipse.persistence.queries.FetchGroupTracker;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

import static com.haulmont.cuba.testsupport.TestSupport.reserialize;
import static org.junit.Assert.*;

/**
 */
public class ViewTest {

    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;
    
    private TimeSource timeSource;

    private UUID userId;

    @Before
    public void setUp() throws Exception {
        timeSource = AppBeans.get(TimeSource.NAME);

        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();

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

    @After
    public void tearDown() throws Exception {
        cont.deleteRecord("SEC_USER", userId);
    }

    @Test
    public void testQuery() throws Exception {
        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();
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

    @Test
    public void testEntityManager() throws Exception {
        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();

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

    @Test
    public void testViewWithoutSystemProperties() throws Exception {
        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();

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

    @Test
    public void testViewWithoutSystemProperties_update() throws Exception {

        View view = new View(User.class, false)
                .addProperty("name")
                .addProperty("login")
                .addProperty("loginLowerCase")
                .addProperty("group",
                        new View(Group.class, false)
                                .addProperty("name")
                );

        Transaction tx = cont.persistence().createTransaction();
        try {
            // First stage: change managed

            long ts = timeSource.currentTimeMillis();
            Thread.sleep(200);

            EntityManager em = cont.persistence().getEntityManager();
            User user = em.find(User.class, userId, view);
            user.setName(new Date().toString());

            try {
                tx.commitRetaining();

                assertTrue(getCurrentUpdateTs() > ts);
            } catch (Exception e) {
                // todo el: commit is failed because updateTs & updatedBy can not be changed (https://bugs.eclipse.org/bugs/show_bug.cgi?id=466841)
                tx = cont.persistence().createTransaction();
            }

            // Second stage: change detached

            ts = timeSource.currentTimeMillis();
            Thread.sleep(200);

            em = cont.persistence().getEntityManager();
            user = em.find(User.class, userId, view);

            tx.commitRetaining();

            user.setName(new Date().toString());
            em = cont.persistence().getEntityManager();
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
     * 6.0: Not loaded (unfetched) attributes cannot be loaded lazily. Otherwise, previously loaded reference attributes are lost.
     */
    @Test
    public void testLazyLoadAfterLoadWithView() throws Exception {
        View view = new View(User.class, false)
                .addProperty("name")
                .addProperty("group", new View(Group.class)
                    .addProperty("name"));

        User user;
        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();
            user = em.find(User.class, userId, view);
            tx.commit();
        } finally {
            tx.end();
        }

        user = reserialize(user);
        assertNotNull(user.getGroup().getName());

        // login is not loaded after transaction is finished
        try {
            user.getLogin();
            fail();
        } catch (Exception ignored) {
        }

        tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();
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
        user = reserialize(user);
        assertNotNull(user.getGroup().getName());
    }


    @Test
    public void testLazyProperty() throws Exception {
        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();
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
        QueryRunner runner = new QueryRunner(cont.persistence().getDataSource());
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

    @Test
    public void testNoTransientPropertiesInLocalView() throws Exception {
        View view = cont.metadata().getViewRepository().getView(EntitySnapshot.class, View.LOCAL);
        ViewProperty prop = view.getProperty("label");
        assertNull(prop);
    }

    @Test
    public void testViewCopy() throws Exception {
        ViewRepository viewRepository = cont.metadata().getViewRepository();
        View view = viewRepository.getView(User.class, View.LOCAL);
        view.addProperty("group", viewRepository.getView(Group.class, View.MINIMAL));

        assertNotNull(view.getProperty("group"));
        assertNull(viewRepository.getView(User.class, View.LOCAL).getProperty("group"));
    }

    @Test
    public void testFetchGroupIsAbsentIfViewIsFull() throws Exception {
        ViewRepository viewRepository = cont.metadata().getViewRepository();
        View view = viewRepository.getView(User.class, View.LOCAL);
        view.addProperty("group", new View(Group.class)
                .addProperty("name"))
            .addProperty("userRoles", new View(UserRole.class)
                .addProperty("role", new View(Role.class)
                            .addProperty("name")))
            .addProperty("substitutions", new View(UserSubstitution.class)
                .addProperty("startDate")
                .addProperty("substitutedUser", new View(User.class)
                        .addProperty("login")
                        .addProperty("name")));

        User u;
        try (Transaction tx = cont.persistence().createTransaction()) {
            EntityManager em = cont.persistence().getEntityManager();
            u = em.find(User.class, userId, view);
            tx.commit();
        }
        assertNotNull(u);
        assertTrue(PersistenceHelper.isLoaded(u, "login"));
        assertTrue(PersistenceHelper.isLoaded(u, "group"));
        assertTrue(PersistenceHelper.isLoaded(u, "userRoles"));
        assertTrue(PersistenceHelper.isLoaded(u, "substitutions"));

        assertTrue(u instanceof FetchGroupTracker);
        assertNull(((FetchGroupTracker) u)._persistence_getFetchGroup());
    }
}
