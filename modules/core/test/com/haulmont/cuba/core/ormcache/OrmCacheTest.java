/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.ormcache;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.TypedQuery;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.UserRole;
import com.haulmont.cuba.testsupport.TestAppender;
import com.haulmont.cuba.testsupport.TestContainer;
import com.haulmont.cuba.testsupport.TestNamePrinter;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.eclipse.persistence.jpa.JpaCache;
import org.junit.*;
import org.junit.rules.TestRule;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManagerFactory;
import java.util.Arrays;
import java.util.UUID;

import static com.haulmont.cuba.testsupport.TestSupport.reserialize;
import static com.haulmont.cuba.testsupport.TestSupport.assertFail;
import static org.junit.Assert.*;

/**
 * Tests of EclipseLink shared cache.
 *
 * @author Konstantin Krivopustov
 * @version $Id$
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
    private Role role;
    private UserRole userRole;

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
            assertTrue(Boolean.valueOf((String) emf.getProperties().get("eclipselink.cache.shared.default")));

            cache = (JpaCache) emf.getCache();

            group = cont.entityManager().find(Group.class, UUID.fromString("0fa2b1a5-1d68-4d69-9fbd-dff348347f93"));

            user = cont.metadata().create(User.class);
            user.setLogin("user1");
            user.setPassword("111");
            user.setGroup(group);
            cont.entityManager().persist(user);

            role = cont.metadata().create(Role.class);
            role.setName("Test role");
            cont.entityManager().persist(role);

            userRole = cont.metadata().create(UserRole.class);
            userRole.setRole(role);
            userRole.setUser(user);
            cont.entityManager().persist(userRole);

            tx.commit();
        }
        cache.evictAll();
    }

    @After
    public void tearDown() throws Exception {
        cont.deleteRecord(userRole, role, user);
    }

    @Test
    public void testFind() throws Exception {
        appender.clearMessages();

        try (Transaction tx = cont.persistence().createTransaction()) {
            User user = cont.entityManager().find(User.class, this.user.getId());
            assertNotNull(user);

            tx.commit();
        }

        assertEquals(1, appender.filterMessages(m -> m.startsWith("SELECT")).count());
        appender.clearMessages();

        try (Transaction tx = cont.persistence().createTransaction()) {
            User user = cont.entityManager().find(User.class, this.user.getId());
            assertNotNull(user);

            tx.commit();
        }

        assertEquals(0, appender.filterMessages(m -> m.startsWith("SELECT")).count());
    }

    @Test
    public void testQuery() throws Exception {
        appender.clearMessages();

        try (Transaction tx = cont.persistence().createTransaction()) {
            TypedQuery<User> query = cont.entityManager().createQuery("select u from sec$User u where u.id = ?1", User.class);
            query.setParameter(1, user.getId());
            User user = query.getSingleResult();
            assertEquals("user1", user.getLogin());

            tx.commit();
        }

        assertEquals(1, appender.filterMessages(m -> m.startsWith("SELECT")).count());
        appender.clearMessages();

        try (Transaction tx = cont.persistence().createTransaction()) {
            TypedQuery<User> query = cont.entityManager().createQuery("select u from sec$User u where u.id = ?1", User.class);
            query.setParameter(1, user.getId());
            User user = query.getSingleResult();
            assertEquals("user1", user.getLogin());

            tx.commit();
        }

        assertEquals(0, appender.filterMessages(m -> m.startsWith("SELECT")).count());
    }

    /**
     * Does not populate to-one relations when reading from cache
     */
    @Test
    public void testFindWithView1() throws Exception {
        User user;

        try (Transaction tx = cont.persistence().createTransaction()) {
            user = cont.entityManager().find(User.class, this.user.getId(), "user.browse");
            assertNotNull(user);

            tx.commit();
        }
        user = reserialize(user);
        assertNotNull(user.getGroup());

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
    }

    /**
     * Fails on quering with the view containing to-many relations
     */
    @Test
    public void testFindWithView2() throws Exception {
        User u;
        try (Transaction tx = cont.persistence().createTransaction()) {
            u = cont.entityManager().find(User.class, user.getId(), "user.edit");
            tx.commit();
        }
        u = reserialize(u);
        assertNotNull(u);

        assertEquals(group, u.getGroup());
        assertNotNull(group.getName());
        assertFail(group::getParent);

        assertFalse(u.getUserRoles().isEmpty());
        UserRole ur = u.getUserRoles().iterator().next();
        assertEquals(userRole, ur);

        Role r = ur.getRole();
        assertEquals(this.role, r);
        assertNotNull(r.getName());
        // fails due to PL-6594 Loading objects with some views does not set FetchGroup to the entities
        //assertFail(r::getDescription);
    }
}
