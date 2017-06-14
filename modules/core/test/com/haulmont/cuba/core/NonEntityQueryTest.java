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
 */

package com.haulmont.cuba.core;

import com.haulmont.bali.db.QueryRunner;
import com.haulmont.cuba.core.app.ConfigStorageService;
import com.haulmont.cuba.core.entity.EntitySnapshot;
import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.core.entity.Server;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.security.app.LoginWorker;
import com.haulmont.cuba.security.entity.*;
import com.haulmont.cuba.security.global.UserSession;
import com.haulmont.cuba.testsupport.TestContainer;
import com.haulmont.cuba.testsupport.TestSupport;
import com.haulmont.cuba.testsupport.TestUserSessionSource;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static org.junit.Assert.*;

public class NonEntityQueryTest {

    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    private static final String USER_NAME_1 = "queryTestUser1";
    private static final String USER_NAME_2 = "queryTestUser2";
    private static final String USER_PASSWORD = "testUser";

    private DataManager dataManager;
    private PasswordEncryption passwordEncryption;

    private UUID serverId, role1Id,
            permission1Id, permission2Id,
            user1Id, user2Id,
            group1Id, group2Id,
            constraint1Id, constraint2Id,
            userRole1Id,
            entitySnapshotId;

    @Before
    public void setUp() throws Exception {
        dataManager = AppBeans.get(DataManager.class);
        passwordEncryption = AppBeans.get(PasswordEncryption.class);

        QueryRunner runner = new QueryRunner(cont.persistence().getDataSource());
        runner.update("delete from SYS_SERVER");

        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();
            UserSessionSource uss = AppBeans.get(UserSessionSource.class);
            UserSession userSession = uss.getUserSession();

            Server server = new Server();
            server.setName("someServer");
            server.setRunning(false);
            serverId = server.getId();
            em.persist(server);

            EntitySnapshot entitySnapshot = cont.metadata().create(EntitySnapshot.class);
            entitySnapshot.setSnapshotDate(new Date());
            entitySnapshot.setAuthor(userSession.getCurrentOrSubstitutedUser());
            entitySnapshotId = entitySnapshot.getId();
            em.persist(entitySnapshot);

            Role role1 = new Role();
            role1Id = role1.getId();
            role1.setName("testRole1");
            em.persist(role1);

            Permission permission1 = new Permission();
            permission1Id = permission1.getId();
            permission1.setRole(role1);
            permission1.setType(PermissionType.ENTITY_ATTR);
            permission1.setTarget("sys$Server:name");
            permission1.setValue(0);
            em.persist(permission1);

            Permission permission2 = new Permission();
            permission2Id = permission2.getId();
            permission2.setRole(role1);
            permission2.setType(PermissionType.ENTITY_OP);
            permission2.setTarget("sys$EntitySnapshot:read");
            permission2.setValue(0);
            em.persist(permission2);

            Group group1 = new Group();
            group1Id = group1.getId();
            group1.setName("testGroup1");
            em.persist(group1);

            User user1 = new User();
            user1Id = user1.getId();
            user1.setName(USER_NAME_1);
            user1.setLogin(USER_NAME_1);
            user1.setPassword(passwordEncryption.getPasswordHash(user1Id, USER_PASSWORD));
            user1.setGroup(group1);
            em.persist(user1);

            UserRole userRole1 = new UserRole();
            userRole1Id = userRole1.getId();
            userRole1.setUser(user1);
            userRole1.setRole(role1);
            em.persist(userRole1);

            Group group2 = new Group();
            group2Id = group2.getId();
            group2.setName("testGroup2");
            em.persist(group2);

            Constraint constraint1 = new Constraint();
            constraint1Id = constraint1.getId();
            constraint1.setEntityName("sys$Server");
            constraint1.setWhereClause("{E}.running = true");
            constraint1.setGroup(group2);
            em.persist(constraint1);

            Constraint constraint2 = new Constraint();
            constraint2Id = constraint2.getId();
            constraint2.setEntityName("sys$EntitySnapshot");
            constraint2.setCheckType(ConstraintCheckType.MEMORY);
            constraint2.setGroovyScript("{E}.viewXml = 'xml'");
            constraint2.setGroup(group2);
            em.persist(constraint2);

            User user2 = new User();
            user2Id = user2.getId();
            user2.setName(USER_NAME_2);
            user2.setLogin(USER_NAME_2);
            user2.setPassword(passwordEncryption.getPasswordHash(user2Id, USER_PASSWORD));
            user2.setGroup(group2);
            em.persist(user2);

            tx.commit();
        } finally {
            tx.end();
        }
    }

    @After
    public void tearDown() throws Exception {
        cont.deleteRecord("SYS_SERVER", serverId);
        cont.deleteRecord("SEC_USER_ROLE", userRole1Id);
        cont.deleteRecord("SEC_PERMISSION", permission1Id, permission2Id);
        cont.deleteRecord("SEC_ROLE", role1Id);
        cont.deleteRecord("SEC_USER", user1Id, user2Id);
        cont.deleteRecord("SEC_CONSTRAINT", constraint1Id, constraint2Id);
        cont.deleteRecord("SEC_GROUP", group1Id, group2Id);
        cont.deleteRecord("SYS_ENTITY_SNAPSHOT", entitySnapshotId);
    }

    @Test
    public void testScalars() throws Exception {
        ValueLoadContext context = ValueLoadContext.create()
                .setQuery(ValueLoadContext.createQuery("select u.id, u.login from sec$User u where u.id = :id1 or u.id = :id2 order by u.login")
                        .setParameter("id1", TestSupport.ADMIN_USER_ID)
                        .setParameter("id2", TestSupport.ANONYMOUS_USER_ID))
                .addProperty("user1Id").addProperty("login");

        List<KeyValueEntity> list = dataManager.loadValues(context);

        assertEquals(2, list.size());
        KeyValueEntity e = list.get(0);
        assertEquals(TestSupport.ADMIN_USER_ID, e.getValue("user1Id"));
        assertEquals("admin", e.getValue("login"));
        e = list.get(1);
        assertEquals(TestSupport.ANONYMOUS_USER_ID, e.getValue("user1Id"));
        assertEquals("anonymous", e.getValue("login"));
    }

    @Test
    public void testExtract() throws Exception {
        ValueLoadContext context = ValueLoadContext.create()
                .setQuery(ValueLoadContext.createQuery("select extract(DAY from u.createTs), count(u.id) from sec$User u group by extract(DAY from u.createTs)"))
                .addProperty("date").addProperty("count");
        List<KeyValueEntity> list = dataManager.loadValues(context);
    }

    @Test
    public void testAggregates() throws Exception {
        ValueLoadContext context = ValueLoadContext.create();
        ValueLoadContext.Query query = context.setQueryString("select count(u) from sec$User u where u.id = :id1 or u.id = :id2");
        query.setParameter("id1", TestSupport.ADMIN_USER_ID);
        query.setParameter("id2", TestSupport.ANONYMOUS_USER_ID);
        context.addProperty("count");

        List<KeyValueEntity> list = dataManager.loadValues(context);

        assertEquals(1, list.size());
        KeyValueEntity e = list.get(0);
        assertEquals(Long.valueOf(2), e.getValue("count"));
    }

    @Test
    public void testIdentificationVariable() throws Exception {
        ValueLoadContext context = ValueLoadContext.create();
        ValueLoadContext.Query query = context.setQueryString("select u, u.id from sec$User u where u.id = :id1");
        query.setParameter("id1", TestSupport.ADMIN_USER_ID);
        context.addProperty("user");
        context.addProperty("id");

        List<KeyValueEntity> list = dataManager.secure().loadValues(context);

        assertEquals(1, list.size());
        KeyValueEntity e = list.get(0);
        assertEquals(TestSupport.ADMIN_USER_ID, ((User) e.getValue("user")).getId());
    }

    @Test
    public void testScalarWithSubQuery() throws Exception {
        ValueLoadContext context = ValueLoadContext.create();
        context.setQueryString("select s.name from sys$Server s where s.name in (select u.login from sec$User u) ");
        context.addProperty("name");

        List<KeyValueEntity> list = dataManager.secure().loadValues(context);

        assertEquals(0, list.size());
    }


    @Test
    public void testDeniedAttribute() throws Exception {
        LoginWorker lw = AppBeans.get(LoginWorker.NAME);
        UserSession userSession = lw.login(USER_NAME_1, passwordEncryption.getPlainHash(USER_PASSWORD), Locale.getDefault());
        assertNotNull(userSession);

        UserSessionSource uss = AppBeans.get(UserSessionSource.class);
        UserSession savedUserSession = uss.getUserSession();
        ((TestUserSessionSource) uss).setUserSession(userSession);
        try {
            ValueLoadContext context = ValueLoadContext.create();
            context.setQueryString("select s.name from sys$Server s");
            context.addProperty("name");

            List<KeyValueEntity> list = dataManager.secure().loadValues(context);

            assertEquals(1, list.size());
            KeyValueEntity e = list.get(0);
            assertNull(e.getValue("name"));

            context = ValueLoadContext.create();
            context.setQueryString("select count(s.id) from sys$Server s where s.name = 'someServer'");
            context.addProperty("count");

            try {
                dataManager.secure().loadValues(context);
                fail();
            } catch (AccessDeniedException e1) {
            }

            context = ValueLoadContext.create();
            context.setQueryString("select s.name, count(s.id) from sys$Server s group by s.name");
            context.addProperty("name");
            context.addProperty("count");

            try {
                dataManager.secure().loadValues(context);
                fail();
            } catch (AccessDeniedException e1) {
            }

            context = ValueLoadContext.create();
            context.setQueryString("select count(s.id) from sys$Server s join sec$User u on u.login = s.name");
            context.addProperty("count");

            try {
                dataManager.secure().loadValues(context);
                fail();
            } catch (AccessDeniedException e1) {
            }

            context = ValueLoadContext.create();
            context.setQueryString("select count(sn) from sys$Server s, sys$EntitySnapshot sn");
            context.addProperty("count");

            list = dataManager.secure().loadValues(context);
            assertEquals(0, list.size());
        } finally {
            ((TestUserSessionSource) uss).setUserSession(savedUserSession);
        }
    }

    @Test
    public void testConstraints() throws Exception {
        LoginWorker lw = AppBeans.get(LoginWorker.NAME);
        UserSession userSession = lw.login(USER_NAME_2, passwordEncryption.getPlainHash(USER_PASSWORD), Locale.getDefault());
        assertNotNull(userSession);

        UserSessionSource uss = AppBeans.get(UserSessionSource.class);
        UserSession savedUserSession = uss.getUserSession();
        ((TestUserSessionSource) uss).setUserSession(userSession);
        try {
            ValueLoadContext context = ValueLoadContext.create();
            context.setQueryString("select s.name from sys$Server s");
            context.addProperty("name");

            List<KeyValueEntity> list = dataManager.secure().loadValues(context);
            assertEquals(0, list.size());
        } finally {
            ((TestUserSessionSource) uss).setUserSession(savedUserSession);
        }
    }

    @Test
    public void testInMemoryAndSeveralConstraints() throws Exception {
        ConfigStorageService configStorageService = AppBeans.get(ConfigStorageService.class);
        configStorageService.setDbProperty("cuba.disableLoadValuesIfConstraints", "false");

        LoginWorker lw = AppBeans.get(LoginWorker.NAME);
        UserSession userSession = lw.login(USER_NAME_2, passwordEncryption.getPlainHash(USER_PASSWORD), Locale.getDefault());
        assertNotNull(userSession);

        UserSessionSource uss = AppBeans.get(UserSessionSource.class);
        UserSession savedUserSession = uss.getUserSession();
        ((TestUserSessionSource) uss).setUserSession(userSession);
        try {
            ValueLoadContext context = ValueLoadContext.create();
            context.setQueryString("select s.viewXml from sys$EntitySnapshot s");
            context.addProperty("viewXml");

            List<KeyValueEntity> list = dataManager.secure().loadValues(context);
            assertEquals(1, list.size());

            context = ValueLoadContext.create();
            context.setQueryString("select s.name, sn.viewXml from sys$Server s, sys$EntitySnapshot sn");
            context.addProperty("name");
            context.addProperty("viewXml");

            list = dataManager.secure().loadValues(context);
            assertEquals(0, list.size());
        } finally {
            ((TestUserSessionSource) uss).setUserSession(savedUserSession);
        }
    }

    @Test
    public void testInMemoryAndSeveralConstraintsWithError() throws Exception {
        ConfigStorageService configStorageService = AppBeans.get(ConfigStorageService.class);
        configStorageService.setDbProperty("cuba.disableLoadValuesIfConstraints", "true");

        LoginWorker lw = AppBeans.get(LoginWorker.NAME);
        UserSession userSession = lw.login(USER_NAME_2, passwordEncryption.getPlainHash(USER_PASSWORD), Locale.getDefault());
        assertNotNull(userSession);

        UserSessionSource uss = AppBeans.get(UserSessionSource.class);
        UserSession savedUserSession = uss.getUserSession();
        ((TestUserSessionSource) uss).setUserSession(userSession);
        try {
            ValueLoadContext context = ValueLoadContext.create();
            context.setQueryString("select s.viewXml from sys$EntitySnapshot s");
            context.addProperty("viewXml");

            try {
                dataManager.secure().loadValues(context);
                fail();
            } catch (RowLevelSecurityException e) {
            }

            context = ValueLoadContext.create();
            context.setQueryString("select s.name, sn.viewXml from sys$Server s, sys$EntitySnapshot sn");
            context.addProperty("name");
            context.addProperty("viewXml");
            try {
                dataManager.secure().loadValues(context);
                fail();
            } catch (RowLevelSecurityException e) {
            }
        } finally {
            ((TestUserSessionSource) uss).setUserSession(savedUserSession);
        }
    }

    @Test
    public void testIdentificationVariableDeniedSecurity() throws Exception {
        ConfigStorageService configStorageService = AppBeans.get(ConfigStorageService.class);
        configStorageService.setDbProperty("cuba.disableLoadValuesIfConstraints", "true");

        LoginWorker lw = AppBeans.get(LoginWorker.NAME);
        UserSession userSession = lw.login(USER_NAME_1, passwordEncryption.getPlainHash(USER_PASSWORD), Locale.getDefault());
        assertNotNull(userSession);

        UserSessionSource uss = AppBeans.get(UserSessionSource.class);
        UserSession savedUserSession = uss.getUserSession();
        ((TestUserSessionSource) uss).setUserSession(userSession);
        try {
            ValueLoadContext context = ValueLoadContext.create();
            context.setQueryString("select s from sys$EntitySnapshot s");
            context.addProperty("snapshot");

            List result = dataManager.secure().loadValues(context);

            assertTrue(result.isEmpty());
        } finally {
            ((TestUserSessionSource) uss).setUserSession(savedUserSession);
        }
    }

    @Test
    public void testIncorrectPathInWhere() throws Exception {
        ConfigStorageService configStorageService = AppBeans.get(ConfigStorageService.class);
        configStorageService.setDbProperty("cuba.disableLoadValuesIfConstraints", "true");

        LoginWorker lw = AppBeans.get(LoginWorker.NAME);
        UserSession userSession = lw.login(USER_NAME_1, passwordEncryption.getPlainHash(USER_PASSWORD), Locale.getDefault());
        assertNotNull(userSession);

        UserSessionSource uss = AppBeans.get(UserSessionSource.class);
        UserSession savedUserSession = uss.getUserSession();
        ((TestUserSessionSource) uss).setUserSession(userSession);
        try {
            ValueLoadContext context = ValueLoadContext.create();
            context.setQueryString("select s.viewXml from sys$EntitySnapshot s where s.name1 = '1'");
            context.addProperty("viewXml");

            dataManager.secure().loadValues(context);
        } catch (NullPointerException e) {
            fail("Handling property path error");
        } catch (IllegalStateException e) {
            if (!"query path 's.name1' is unresolved".equals(e.getMessage())) {
                throw e;
            }
        }
        finally {
            ((TestUserSessionSource) uss).setUserSession(savedUserSession);
        }
    }
}
