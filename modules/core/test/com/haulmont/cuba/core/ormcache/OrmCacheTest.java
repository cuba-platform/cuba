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

package com.haulmont.cuba.core.ormcache;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.haulmont.cuba.core.Query;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.TypedQuery;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.security.entity.*;
import com.haulmont.cuba.testsupport.TestAppender;
import com.haulmont.cuba.testsupport.TestContainer;
import com.haulmont.cuba.testsupport.TestNamePrinter;
import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.internal.jpa.EntityManagerFactoryDelegate;
import org.eclipse.persistence.jpa.JpaCache;
import org.eclipse.persistence.sessions.server.ServerSession;
import org.junit.*;
import org.junit.rules.TestRule;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManagerFactory;
import java.util.Arrays;
import java.util.List;

import static com.haulmont.cuba.testsupport.TestSupport.assertFail;
import static com.haulmont.cuba.testsupport.TestSupport.reserialize;
import static org.junit.Assert.*;

/**
 * Tests of EclipseLink shared cache.
 *
 */
@Ignore
public class OrmCacheTest {

    @ClassRule
    public static TestContainer cont = new TestContainer()
            .setAppPropertiesFiles(Arrays.asList("cuba-app.properties", "com/haulmont/cuba/core/ormcache/test-ormcache-app.properties"));

    @Rule
    public TestRule testNamePrinter = new TestNamePrinter();

    private JpaCache cache;

    private final TestAppender appender;
    private Group group;
    private User user;
    private User user2;
    private Role role, role1;
    private UserRole userRole;
    private User user1;
    private UserSetting userSetting;

    public OrmCacheTest() {
        appender = new TestAppender();
        appender.start();
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger logger = context.getLogger("eclipselink.sql");
        logger.addAppender(appender);
    }

    @Before
    public void setUp() throws Exception {
        assertTrue(cont.getSpringAppContext() == AppContext.getApplicationContext());

        try (Transaction tx = cont.persistence().createTransaction()) {
            EntityManagerFactory emf = cont.entityManager().getDelegate().getEntityManagerFactory();

            assertTrue(cont.metadata().getTools().isCacheable(cont.metadata().getClassNN(User.class)));
            assertFalse(cont.metadata().getTools().isCacheable(cont.metadata().getClassNN(UserSubstitution.class)));

            ServerSession serverSession = ((EntityManagerFactoryDelegate) emf).getServerSession();
            ClassDescriptor descriptor = serverSession.getDescriptor(User.class);
            assertEquals(500, descriptor.getCachePolicy().getIdentityMapSize());

//            assertTrue(Boolean.valueOf((String) emf.getProperties().get("eclipselink.cache.shared.default")));
//            assertTrue(Boolean.valueOf((String) emf.getProperties().get("eclipselink.cache.shared.sec$User")));


            this.cache = (JpaCache) emf.getCache();

            group = cont.metadata().create(Group.class);
            group.setName("group-" + group.getId());
            cont.entityManager().persist(group);

            user = cont.metadata().create(User.class);
            user.setLogin("OrmCacheTest-" + user.getId());
            user.setPassword("111");
            user.setGroup(group);
            cont.entityManager().persist(user);

            user2 = cont.metadata().create(User.class);
            user2.setLogin("OrmCacheTest-" + user2.getId());
            user2.setPassword("111");
            user2.setGroup(group);
            cont.entityManager().persist(user2);

            role = cont.metadata().create(Role.class);
            role.setName("Test role");
            role.setDescription("Test role descr");
            cont.entityManager().persist(role);

            userRole = cont.metadata().create(UserRole.class);
            userRole.setRole(role);
            userRole.setUser(user);
            cont.entityManager().persist(userRole);

            userSetting = cont.metadata().create(UserSetting.class);
            userSetting.setUser(user);
            cont.entityManager().persist(userSetting);

            tx.commit();
        }
        cache.clear();
    }

    @After
    public void tearDown() throws Exception {
        cont.deleteRecord(userSetting, userRole, role, user, user2);
        if (role1 != null)
            cont.deleteRecord(role1);
        if (user1 != null)
            cont.deleteRecord(user1);
        cont.deleteRecord(group);
    }

    @Test
    public void testFind() throws Exception {
        appender.clearMessages();

        loadUserAlone();

        assertEquals(1, appender.filterMessages(m -> m.startsWith("SELECT")).count());
        appender.clearMessages();

        loadUserAlone();

        assertEquals(0, appender.filterMessages(m -> m.startsWith("SELECT")).count());
    }

    @Test
    public void testQueryById() throws Exception {
        appender.clearMessages();

        User u;

        try (Transaction tx = cont.persistence().createTransaction()) {
            TypedQuery<User> query = cont.entityManager().createQuery("select u from sec$User u where u.id = ?1", User.class);
            query.setParameter(1, this.user.getId());
            query.setViewName("user.browse");
            u = query.getSingleResult();

            tx.commit();
        }
        assertEquals(this.user.getLogin(), u.getLogin());
        assertEquals(this.group, u.getGroup());

        assertEquals(2, appender.filterMessages(m -> m.startsWith("SELECT")).count()); // User, Group
        appender.clearMessages();

        try (Transaction tx = cont.persistence().createTransaction()) {
            TypedQuery<User> query = cont.entityManager().createQuery("select u from sec$User u where u.id = ?1", User.class);
            query.setParameter(1, this.user.getId());
            query.setViewName("user.browse");
            u = query.getSingleResult();

            tx.commit();
        }
        assertEquals(this.user.getLogin(), u.getLogin());
        assertEquals(this.group, u.getGroup());

        assertEquals(0, appender.filterMessages(m -> m.startsWith("SELECT")).count());
    }

    @Test
    public void testFindWithView1() throws Exception {
        appender.clearMessages();
        User user;

        try (Transaction tx = cont.persistence().createTransaction()) {
            user = cont.entityManager().find(User.class, this.user.getId(), "user.browse");
            assertNotNull(user);

            tx.commit();
        }
        user = reserialize(user);
        assertNotNull(user.getGroup());

        assertEquals(2, appender.filterMessages(m -> m.startsWith("SELECT")).count()); // user, group
        appender.clearMessages();

        try (Transaction tx = cont.persistence().createTransaction()) {
            user = cont.entityManager().find(User.class, this.user.getId(), "user.browse");
            assertNotNull(user);

            tx.commit();
        }
        user = reserialize(user);
        Group g = user.getGroup();
        assertEquals(this.group, g);
        assertNotNull(g.getName());
        assertFail(g::getParent);

        assertEquals(0, appender.filterMessages(m -> m.startsWith("SELECT")).count());
    }

    @Test
    public void testFindWithView2() throws Exception {
        appender.clearMessages();

        User u;
        try (Transaction tx = cont.persistence().createTransaction()) {
            u = cont.entityManager().find(User.class, user.getId(), "user.edit");
            tx.commit();
        }
        checkUser(u);

        assertEquals(5, appender.filterMessages(m -> m.startsWith("SELECT")).count()); // User, Group, UserRoles, Role, UserSubstitution
        appender.clearMessages();

        try (Transaction tx = cont.persistence().createTransaction()) {
            u = cont.entityManager().find(User.class, user.getId(), "user.edit");
            tx.commit();
        }
        checkUser(u);

        assertEquals(1, appender.filterMessages(m -> m.startsWith("SELECT")).count()); // 1 because UserSubstitution is not cached
    }

    private void checkUser(User u) throws Exception {
        u = reserialize(u);
        assertNotNull(u);

        assertEquals(group, u.getGroup());
        assertNotNull(u.getGroup().getName());
        assertFail(u.getGroup()::getParent);

        assertFalse(u.getUserRoles().isEmpty());
        UserRole ur = u.getUserRoles().iterator().next();
        assertEquals(userRole, ur);

        Role r = ur.getRole();
        assertEquals(this.role, r);
        assertNotNull(r.getName());
        assertNotNull(r.getDescription());

        assertEquals(0, u.getSubstitutions().size());
    }

    @Test
    public void testFindWithView3() throws Exception {
        appender.clearMessages();

        User user;

        // no name in group
        View groupView = new View(Group.class, false);
        View userView = new View(User.class)
                .addProperty("login")
                .addProperty("group", groupView)
                .setLoadPartialEntities(true);

        try (Transaction tx = cont.persistence().createTransaction()) {
            user = cont.entityManager().find(User.class, this.user.getId(), userView);
            assertNotNull(user);

            tx.commit();
        }
        user = reserialize(user);
        Group g = user.getGroup();
        assertNotNull(user.getGroup());
        assertEquals(this.group, g);
        assertNotNull(g.getName()); // due to caching, we load all attributes anyway

        assertEquals(2, appender.filterMessages(m -> m.startsWith("SELECT")).count()); // user, group
        appender.clearMessages();

        // second time - from cache
        try (Transaction tx = cont.persistence().createTransaction()) {
            user = cont.entityManager().find(User.class, this.user.getId(), userView);
            assertNotNull(user);

            tx.commit();
        }
        user = reserialize(user);
        g = user.getGroup();
        assertEquals(this.group, g);
        assertNotNull(g.getName());

        assertEquals(0, appender.filterMessages(m -> m.startsWith("SELECT")).count()); // user, group
        appender.clearMessages();

        // name in group - from cache again
        View groupView1 = new View(Group.class, true)
                .addProperty("name");
        View userView1 = new View(User.class)
                .addProperty("login")
                .addProperty("group", groupView1)
                .setLoadPartialEntities(true);

        try (Transaction tx = cont.persistence().createTransaction()) {
            user = cont.entityManager().find(User.class, this.user.getId(), userView1);
            assertNotNull(user);

            tx.commit();
        }
        user = reserialize(user);
        g = user.getGroup();
        assertEquals(this.group, g);
        assertNotNull(g.getName());

        assertEquals(0, appender.filterMessages(m -> m.startsWith("SELECT")).count()); // user, group
        appender.clearMessages();
    }

    @Test
    public void testStaleData_insert() throws Exception {
        appender.clearMessages();

        loadUser();
        assertEquals(2, appender.filterMessages(m -> m.startsWith("SELECT")).count()); // User, Group
        appender.clearMessages();

        loadUser();
        assertEquals(0, appender.filterMessages(m -> m.startsWith("SELECT")).count());
        appender.clearMessages();

        User newUser = cont.metadata().create(User.class);
        newUser.setLogin("new user");
        newUser.setGroup(this.group);
        try (Transaction tx = cont.persistence().createTransaction()) {
            cont.entityManager().persist(newUser);
            tx.commit();
        }

        loadUser();
        assertEquals(0, appender.filterMessages(m -> m.startsWith("SELECT")).count()); // inserting new entities does not affect existing in cache

        cont.deleteRecord(newUser);
    }

    @Test
    public void testStaleData_update() throws Exception {
        appender.clearMessages();

        loadUser();
        assertEquals(2, appender.filterMessages(m -> m.startsWith("SELECT")).count()); // User, Group
        appender.clearMessages();

        loadUser();
        assertEquals(0, appender.filterMessages(m -> m.startsWith("SELECT")).count());
        appender.clearMessages();


        try (Transaction tx = cont.persistence().createTransaction()) {
            User u = cont.entityManager().find(User.class, this.user.getId());
            u.setName("new name");
            tx.commit();
        }

        User u = loadUser();
        assertEquals("new name", u.getName());
        assertEquals(0, appender.filterMessages(m -> m.startsWith("SELECT")).count()); // no DB requests - the User has been updated in cache
        appender.clearMessages();

        loadUser();
        assertEquals(0, appender.filterMessages(m -> m.startsWith("SELECT")).count());
    }

    @Test
    public void testStaleData_update_DM() throws Exception {
        appender.clearMessages();

        loadUser();
        assertEquals(2, appender.filterMessages(m -> m.startsWith("SELECT")).count()); // User, Group
        appender.clearMessages();

        loadUser();
        assertEquals(0, appender.filterMessages(m -> m.startsWith("SELECT")).count());
        appender.clearMessages();

        DataManager dataManager = AppBeans.get(DataManager.class);
        User u = dataManager.load(LoadContext.create(User.class).setId(this.user.getId()).setView("user.browse"));
        u.setName("new name");
        dataManager.commit(u);
        assertEquals(0, appender.filterMessages(m -> m.startsWith("SELECT")).count()); // no DB requests - the User has been updated in cache
        appender.clearMessages();

        u = loadUser();
        assertEquals("new name", u.getName());
        assertEquals(0, appender.filterMessages(m -> m.startsWith("SELECT")).count()); // no DB requests - the User has been updated in cache
        appender.clearMessages();

        loadUser();
        assertEquals(0, appender.filterMessages(m -> m.startsWith("SELECT")).count());
    }

    @Test
    public void testStaleData_updateCollection_add() throws Exception {
        appender.clearMessages();

        loadUserWithRoles();
        assertEquals(4, appender.filterMessages(m -> m.startsWith("SELECT")).count()); // User, Group, UserRoles, Role
        appender.clearMessages();

        loadUserWithRoles();
        assertEquals(0, appender.filterMessages(m -> m.startsWith("SELECT")).count());
        appender.clearMessages();

        Role newRole;
        UserRole newUserRole;

        try (Transaction tx = cont.persistence().createTransaction()) {
            User u = cont.entityManager().find(User.class, this.user.getId());

            newRole = cont.metadata().create(Role.class);
            newRole.setName("new role");
            cont.entityManager().persist(newRole);

            newUserRole = cont.metadata().create(UserRole.class);
            newUserRole.setRole(newRole);
            newUserRole.setUser(u);
            cont.entityManager().persist(newUserRole);

            tx.commit(); // User should be evicted from cache to update collection of UserRoles - see OrmCacheSupport.evictMasterEntity()
        }

        User u = loadUserWithRoles();
        assertEquals(2, u.getUserRoles().size());
        assertTrue(u.getUserRoles().stream()
                .map(UserRole::getRole)
                .anyMatch(r -> r.getName().equals("new role")));

        assertEquals(2, appender.filterMessages(m -> m.startsWith("SELECT")).count()); // User, UserRoles

        cont.deleteRecord(newUserRole, newRole);
    }

    @Test
    public void testStaleData_updateCollection_remove() throws Exception {
        appender.clearMessages();

        loadUserWithRoles();
        assertEquals(4, appender.filterMessages(m -> m.startsWith("SELECT")).count()); // User, Group, UserRoles, Role
        appender.clearMessages();

        loadUserWithRoles();
        assertEquals(0, appender.filterMessages(m -> m.startsWith("SELECT")).count());
        appender.clearMessages();

        try (Transaction tx = cont.persistence().createTransaction()) {
            UserRole ur = cont.entityManager().find(UserRole.class, this.userRole.getId());
            cont.entityManager().remove(ur);

            tx.commit(); // User should be evicted from cache to update collection of UserRoles - see OrmCacheSupport.evictMasterEntity()
        }

        User u = loadUserWithRoles();
        assertEquals(0, u.getUserRoles().size());

        assertEquals(2, appender.filterMessages(m -> m.startsWith("SELECT")).count()); // User, UserRoles
    }

    @Test
    public void testStaleData_updateCollection_changeMaster() throws Exception {
        appender.clearMessages();

        loadUserWithRoles();
        assertEquals(4, appender.filterMessages(m -> m.startsWith("SELECT")).count()); // User, Group, UserRoles, Role
        appender.clearMessages();

        loadUserWithRoles();
        assertEquals(0, appender.filterMessages(m -> m.startsWith("SELECT")).count());
        appender.clearMessages();

        try (Transaction tx = cont.persistence().createTransaction()) {
            UserRole ur = cont.entityManager().find(UserRole.class, this.userRole.getId());
            user1 = cont.metadata().create(User.class);
            user1.setLogin("user1-" + user1.getId());
            user1.setGroup(group);
            cont.entityManager().persist(user1);
            ur.setUser(user1);

            tx.commit(); // User should be evicted from cache to update collection of UserRoles - see OrmCacheSupport.evictMasterEntity()
        }

        User u = loadUserWithRoles();
        assertEquals(0, u.getUserRoles().size());

        assertEquals(2, appender.filterMessages(m -> m.startsWith("SELECT")).count()); // User, UserRoles
    }

    @Test
    public void testStaleData_updateCollectionElement() throws Exception {
        appender.clearMessages();

        loadUserWithRoles();
        assertEquals(4, appender.filterMessages(m -> m.startsWith("SELECT")).count()); // User, Group, UserRoles, Role
        appender.clearMessages();

        loadUserWithRoles();
        assertEquals(0, appender.filterMessages(m -> m.startsWith("SELECT")).count());
        appender.clearMessages();

        try (Transaction tx = cont.persistence().createTransaction()) {
            User u = cont.entityManager().find(User.class, this.user.getId());

            role1 = cont.metadata().create(Role.class);
            role1.setName("new role");
            cont.entityManager().persist(role1);

            UserRole userRole = u.getUserRoles().get(0);
            userRole.setRole(role1);

            tx.commit();
        }

        User u = loadUserWithRoles();
        assertTrue(u.getUserRoles().get(0).getRole().getName().equals("new role"));

        assertEquals(0, appender.filterMessages(m -> m.startsWith("SELECT")).count());
    }

    @Test
    public void testSoftDeleteToOne() throws Exception {
        appender.clearMessages();

        loadUserSetting();
        assertEquals(2, appender.filterMessages(m -> m.startsWith("SELECT")).count()); // UserSetting, User
        appender.clearMessages();

        loadUserSetting();
        assertEquals(0, appender.filterMessages(m -> m.startsWith("SELECT")).count());
        appender.clearMessages();

        User u;
        try (Transaction tx = cont.persistence().createTransaction()) {
            u = cont.entityManager().find(User.class, this.user.getId());
            cont.entityManager().remove(u);
            tx.commit();
        }
        appender.clearMessages();

        UserSetting us = loadUserSetting();
        assertTrue(us.getUser().isDeleted());
        appender.clearMessages();

        us = loadUserSetting();
        assertTrue(us.getUser().isDeleted());
        assertEquals(0, appender.filterMessages(m -> m.startsWith("SELECT")).count());
    }

    @Test
    public void testQuery() throws Exception {
        appender.clearMessages();

        User u1, u2;
        List<User> list;

        try (Transaction tx = cont.persistence().createTransaction()) {
            TypedQuery<User> query = cont.entityManager().createQuery("select u from sec$User u where u.login like ?1", User.class);
            query.setParameter(1, "OrmCacheTest%");
            query.setViewName("user.browse");
            list = query.getResultList();

            tx.commit();
        }
        u1 = list.stream().filter(u -> u.getId().equals(this.user.getId())).findFirst().get();
        assertEquals(this.user.getLogin(), u1.getLogin());
        assertEquals(this.group, u1.getGroup());
        u2 = list.stream().filter(user -> user.getId().equals(this.user2.getId())).findFirst().get();
        assertEquals(this.user2.getLogin(), u2.getLogin());
        assertEquals(this.group, u2.getGroup());

        assertEquals(2, appender.filterMessages(m -> m.startsWith("SELECT")).count()); // User, Group
        appender.clearMessages();

        try (Transaction tx = cont.persistence().createTransaction()) {
            TypedQuery<User> query = cont.entityManager().createQuery("select u from sec$User u where u.login like ?1", User.class);
            query.setParameter(1, "OrmCacheTest%");
            query.setViewName("user.browse");
            list = query.getResultList();

            tx.commit();
        }
        u1 = list.stream().filter(u -> u.getId().equals(this.user.getId())).findFirst().get();
        assertEquals(this.user.getLogin(), u1.getLogin());
        assertEquals(this.group, u1.getGroup());
        u2 = list.stream().filter(user -> user.getId().equals(this.user2.getId())).findFirst().get();
        assertEquals(this.user2.getLogin(), u2.getLogin());
        assertEquals(this.group, u2.getGroup());

        assertEquals(1, appender.filterMessages(m -> m.startsWith("SELECT")).count()); // User only
    }

    private User loadUser() throws Exception {
        User user;
        try (Transaction tx = cont.persistence().createTransaction()) {
            user = cont.entityManager().find(User.class, this.user.getId(), "user.browse");
            assertNotNull(user);

            tx.commit();
        }
        user = reserialize(user);
        Group g = user.getGroup();
        assertEquals(this.group, g);
        assertNotNull(g.getName());
        assertFail(g::getParent);
        return user;
    }

    private User loadUserWithRoles() throws Exception {
        View roleView = new View(Role.class)
                .addProperty("name");
        View userRoleView = new View(UserRole.class)
                .addProperty("role", roleView);
        View groupView = new View(Group.class)
                .addProperty("name");
        View userView = new View(User.class)
                .addProperty("login")
                .addProperty("name")
                .addProperty("userRoles", userRoleView)
                .addProperty("group", groupView);

        User user;
        try (Transaction tx = cont.persistence().createTransaction()) {
            user = cont.entityManager().find(User.class, this.user.getId(), userView);
            assertNotNull(user);

            tx.commit();
        }
        user = reserialize(user);
        Group g = user.getGroup();
        assertEquals(this.group, g);
        assertNotNull(g.getName());
        assertFail(g::getParent);
        user.getUserRoles().size();
        return user;
    }

    private UserSetting loadUserSetting() throws Exception {
        UserSetting us;
        View usView = new View(UserSetting.class)
                .addProperty("name")
                .addProperty("user", new View(User.class)
                        .addProperty("login"));
        try (Transaction tx = cont.persistence().createTransaction()) {
            us = cont.entityManager().find(UserSetting.class, this.userSetting.getId(), usView);
            assertNotNull(us);
            tx.commit();
        }
        us = reserialize(us);
        assertEquals(userSetting, us);
        assertEquals(user, us.getUser());
        return us;
    }


    @Test
    public void testUpdateQuery() throws Exception {
        appender.clearMessages();

        loadUser();

        assertEquals(2, appender.filterMessages(m -> m.startsWith("SELECT")).count()); // User, Group
        appender.clearMessages();

        try (Transaction tx = cont.persistence().createTransaction()) {
            Query query = cont.entityManager().createQuery("update sec$User u set u.position = ?1 where u.loginLowerCase = ?2");
            query.setParameter(1, "new position");
            query.setParameter(2, this.user.getLoginLowerCase());
            query.executeUpdate();
            tx.commit();
        }
        appender.clearMessages();

        User u = loadUser();
        assertEquals("new position", u.getPosition());

        assertEquals(1, appender.filterMessages(m -> m.startsWith("SELECT")).count()); // Group
    }

    @Test
    public void testNativeQuery() throws Exception {
        appender.clearMessages();

        loadUser();

        assertEquals(2, appender.filterMessages(m -> m.startsWith("SELECT")).count()); // User, Group
        appender.clearMessages();

        try (Transaction tx = cont.persistence().createTransaction()) {
            Query query = cont.entityManager().createNativeQuery("update sec_user set position_ = ? where login_lc = ?");
            query.setParameter(1, "new position");
            query.setParameter(2, this.user.getLoginLowerCase());
            query.executeUpdate(); // all evicted here
            tx.commit();
        }
        appender.clearMessages();

        User u;
        try (Transaction tx = cont.persistence().createTransaction()) {
            u = cont.entityManager().find(User.class, this.user.getId(), "user.browse");
            assertNotNull(u);
            tx.commit();
        }
        u = reserialize(u);
        Group g = u.getGroup();
        assertEquals(this.group, g);
        assertNotNull(g.getName());
        assertEquals("new position", u.getPosition());

        assertEquals(2, appender.filterMessages(m -> m.startsWith("SELECT")).count()); // User,Group
    }

    private void loadUserAlone() {
        try (Transaction tx = cont.persistence().createTransaction()) {
            User user = cont.entityManager().find(User.class, this.user.getId());
            assertNotNull(user);

            tx.commit();
        }
    }

}
