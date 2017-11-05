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

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import com.haulmont.bali.db.QueryRunner;
import com.haulmont.bali.db.ResultSetHandler;
import com.haulmont.cuba.core.entity.EntitySnapshot;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.security.entity.*;
import com.haulmont.cuba.testmodel.multiplelinks.LinkEntity;
import com.haulmont.cuba.testmodel.multiplelinks.MultiLinkEntity;
import com.haulmont.cuba.testmodel.selfinherited.ChildEntity;
import com.haulmont.cuba.testmodel.selfinherited.RootEntity;
import com.haulmont.cuba.testmodel.selfinherited.RootEntityDetail;
import com.haulmont.cuba.testsupport.TestContainer;
import org.eclipse.persistence.queries.FetchGroupTracker;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

import static com.haulmont.cuba.testsupport.TestSupport.assertFail;
import static com.haulmont.cuba.testsupport.TestSupport.reserialize;
import static org.junit.Assert.*;

public class ViewTest {

    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;
    
    private TimeSource timeSource;

    private UUID userId;
    private RootEntity rootEntity;
    private ChildEntity childEntity;
    private MultiLinkEntity multiLinkEntity;
    private LinkEntity linkEntity1, linkEntity2, linkEntity3;
    private Persistence persistence;
    private Metadata metadata;

    @Before
    public void setUp() throws Exception {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        context.getLogger("com.haulmont.cuba.core.sys.FetchGroupManager").setLevel(Level.TRACE);

        persistence = cont.persistence();
        metadata = cont.metadata();
        timeSource = AppBeans.get(TimeSource.NAME);

        Transaction tx = persistence.createTransaction();
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

            childEntity = metadata.create(ChildEntity.class);
            childEntity.setName("childEntityName");
            childEntity.setDescription("childEntityDescription");
            em.persist(childEntity);

            rootEntity = metadata.create(RootEntity.class);
            rootEntity.setDescription("rootEntityDescription");
            rootEntity.setEntity(childEntity);
            em.persist(rootEntity);

            RootEntityDetail detail1 = metadata.create(RootEntityDetail.class);
            detail1.setInfo("detail1");
            detail1.setMaster(childEntity);
            em.persist(detail1);

            RootEntityDetail detail2 = metadata.create(RootEntityDetail.class);
            detail2.setInfo("detail2");
            detail2.setMaster(childEntity);
            em.persist(detail2);

            linkEntity1 = metadata.create(LinkEntity.class);
            linkEntity1.setName("A");
            em.persist(linkEntity1);

            linkEntity2 = metadata.create(LinkEntity.class);
            linkEntity2.setName("B");
            em.persist(linkEntity2);

            linkEntity3 = metadata.create(LinkEntity.class);
            linkEntity3.setName("C");
            em.persist(linkEntity3);

            multiLinkEntity = metadata.create(MultiLinkEntity.class);
            multiLinkEntity.setA(linkEntity1);
            multiLinkEntity.setB(linkEntity2);
            multiLinkEntity.setC(linkEntity3);
            em.persist(multiLinkEntity);

            tx.commit();
        } finally {
            tx.end();
        }
    }

    @After
    public void tearDown() throws Exception {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        context.getLogger("com.haulmont.cuba.core.sys.FetchGroupManager").setLevel(Level.DEBUG);

        cont.deleteRecord("SEC_USER", userId);
        cont.deleteRecord("TEST_ROOT_ENTITY_DETAIL");
        cont.deleteRecord("TEST_CHILD_ENTITY");
        cont.deleteRecord("TEST_ROOT_ENTITY");
        cont.deleteRecord("TEST_MULTI_LINK_ENTITY");
        cont.deleteRecord("TEST_LINK_ENTITY");
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
                    )
                    .setLoadPartialEntities(true);
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
                    ).setLoadPartialEntities(true);

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
                    ).setLoadPartialEntities(true);

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
        view.setLoadPartialEntities(true);

        try (Transaction tx = cont.persistence().createTransaction()) {
            // First stage: change managed

            long ts = timeSource.currentTimeMillis();
            Thread.sleep(200);

            EntityManager em = cont.persistence().getEntityManager();
            User user = em.find(User.class, userId, view);

            assertFalse(PersistenceHelper.isLoaded(user, "updateTs"));

            user.setName(new Date().toString());

            tx.commitRetaining();

            assertTrue(getCurrentUpdateTs() > ts);

            // Second stage: change detached

            ts = timeSource.currentTimeMillis();
            Thread.sleep(1000);

            em = cont.persistence().getEntityManager();
            user = em.find(User.class, userId, view);

            tx.commitRetaining();

            assertFalse(PersistenceHelper.isLoaded(user, "updateTs"));

            user.setName(new Date().toString());
            em = cont.persistence().getEntityManager();
            em.merge(user);

            tx.commit();

            assertTrue(getCurrentUpdateTs() > ts);
        }

        // test _minimal
        try (Transaction tx = cont.persistence().createTransaction()) {
            long ts = timeSource.currentTimeMillis();
            Thread.sleep(1000);

            View minimalView = cont.metadata().getViewRepository().getView(User.class, View.MINIMAL);
            minimalView.setLoadPartialEntities(true);

            EntityManager em = cont.persistence().getEntityManager();
            User user = em.find(User.class, userId, minimalView);

            tx.commitRetaining();

            assertFalse(PersistenceHelper.isLoaded(user, "updateTs"));

            user.setName(new Date().toString());
            em = cont.persistence().getEntityManager();
            em.merge(user);

            tx.commit();

            assertTrue(getCurrentUpdateTs() > ts);

            tx.commit();
        }

        // test DataManager
        long ts = timeSource.currentTimeMillis();
        Thread.sleep(1000);

        DataManager dataManager = AppBeans.get(DataManager.class).secure();
        User user = dataManager.load(LoadContext.create(User.class).setId(userId).setView(view));

        assertFalse(PersistenceHelper.isLoaded(user, "updateTs"));

        user.setName(new Date().toString());
        dataManager.commit(user);

        assertTrue(getCurrentUpdateTs() > ts);
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
                    .addProperty("name"))
                .setLoadPartialEntities(true);

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

            // field is loaded lazily but the object becomes not partial and references loaded by previous view are cleared
            user.getLogin();

            tx.commit();
        } finally {
            tx.end();
        }
        user = reserialize(user);
        assertFail(user::getGroup);
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
                    ).setLoadPartialEntities(true);
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

    @Test
    public void testSelfReferenceInView() {
        ViewRepository viewRepository = cont.metadata().getViewRepository();
        View view = viewRepository.getView(RootEntity.class, View.LOCAL);
        view.addProperty("entity", new View(ChildEntity.class)
                .addProperty("name").addProperty("description"), FetchMode.AUTO);
        RootEntity e;
        try (Transaction tx = cont.persistence().createTransaction()) {
            EntityManager em = cont.persistence().getEntityManager();
            e = em.find(RootEntity.class, rootEntity.getId(), view);
            tx.commit();
        }
        assertNotNull(e);
        assertNotNull(e.getEntity());
        assertEquals("rootEntityDescription", e.getDescription());
        assertEquals("childEntityDescription", e.getEntity().getDescription());
        assertEquals("childEntityName", e.getEntity().getName());
    }

    @Test
    public void testNestedCollectionInJoinedInheritance() throws Exception {
        View childEntityView = new View(ChildEntity.class, false)
                .addProperty("description")
                .addProperty("name")
                .addProperty("details", new View(RootEntityDetail.class, false)
                        .addProperty("info"));
        childEntityView.setLoadPartialEntities(true);

        ChildEntity loaded;
        try (Transaction tx = persistence.createTransaction()) {
            EntityManager em = persistence.getEntityManager();
            TypedQuery<ChildEntity> query = em.createQuery("select e from test$ChildEntity e where e.id = ?1", ChildEntity.class);
            query.setParameter(1, childEntity.getId());
            query.setView(childEntityView);
            loaded = query.getSingleResult();
            tx.commit();
        }
        assertEquals(childEntity, loaded);
        assertNotNull(loaded.getDetails());
        assertEquals(2, loaded.getDetails().size());
    }

    @Test
    public void testMultiLinkInEntity() throws Exception {
        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();
            View linkView = new View(LinkEntity.class).addProperty("name");
            View view = new View(MultiLinkEntity.class)
                    .addProperty("a", linkView)
                    .addProperty("b", linkView)
                    .addProperty("c", linkView)
                    .setLoadPartialEntities(true);

            MultiLinkEntity reloadEntity = em.find(MultiLinkEntity.class, multiLinkEntity.getId(), view);

            tx.commit();
            reloadEntity = reserialize(reloadEntity);
            assertEquals("A", reloadEntity.getA().getName());
            assertEquals("B", reloadEntity.getB().getName());
            assertEquals("C", reloadEntity.getC().getName());
        } finally {
            tx.end();
        }
    }
}