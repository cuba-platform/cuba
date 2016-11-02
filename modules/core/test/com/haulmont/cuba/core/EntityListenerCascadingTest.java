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

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.listener.TestCascadingEntityListener;
import com.haulmont.cuba.core.sys.listener.EntityListenerManager;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.testsupport.TestContainer;
import com.haulmont.cuba.testsupport.TestSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import static org.junit.Assert.*;

public class EntityListenerCascadingTest {

    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    private Metadata metadata;
    private Persistence persistence;

    private EntityListenerManager entityListenerManager;

    private User user;

    @Before
    public void setUp() throws Exception {
        persistence = cont.persistence();
        metadata = cont.metadata();

        try (Transaction tx = persistence.createTransaction()) {
            user = metadata.create(User.class);
            user.setGroup(persistence.getEntityManager().find(Group.class, TestSupport.COMPANY_GROUP_ID));
            user.setLogin("user-" + user.getId());
            persistence.getEntityManager().persist(user);
            tx.commit();
        }

        entityListenerManager = AppBeans.get(EntityListenerManager.class);
        entityListenerManager.addListener(User.class, TestCascadingEntityListener.class);

        TestCascadingEntityListener.events.clear();
    }

    @After
    public void tearDown() throws Exception {
        entityListenerManager.removeListener(User.class, TestCascadingEntityListener.class);
        cont.deleteRecord(user);
    }

    @Test
    public void testQueryInListener() throws Exception {
        TestCascadingEntityListener.withNewTx = false;
        TestCascadingEntityListener.withView = false;
        doQueryInListener();
    }

    @Test
    public void testQueryWithViewInListener() throws Exception {
        TestCascadingEntityListener.withNewTx = false;
        TestCascadingEntityListener.withView = true;
        doQueryInListener();
    }

    @Test
    public void testQueryWithViewInNewTxInListener() throws Exception {
        TestCascadingEntityListener.withNewTx = true;
        TestCascadingEntityListener.withView = true;
        doQueryInListener();
    }

    private void doQueryInListener() {
        try (Transaction tx = persistence.createTransaction()) {
            User u = persistence.getEntityManager().find(User.class, user.getId());
            assertNotNull(u);
            u.setName(u.getLogin());
            tx.commit();
        }
        System.out.println("\n" + TestCascadingEntityListener.events + "\n");
        assertEquals(2, TestCascadingEntityListener.events.size());
        assertTrue(TestCascadingEntityListener.events.get(0).contains("onBeforeUpdate"));
        assertTrue(TestCascadingEntityListener.events.get(1).contains("onAfterUpdate"));
    }

    @Test
    public void testUpdateBySecondListener() throws Exception {
        TestCascadingEntityListener.withNewTx = false;
        TestCascadingEntityListener.withView = true;
        try (Transaction tx = persistence.createTransaction()) {
            User u = persistence.getEntityManager().find(User.class, user.getId());
            assertNotNull(u);
            u.setLogin("1-NEW-" + user.getId());

            tx.commit();
        }
        System.out.println("\n" + TestCascadingEntityListener.events + "\n");
        assertEquals(3, TestCascadingEntityListener.events.size());
        assertTrue(TestCascadingEntityListener.events.get(0).contains("onBeforeUpdate"));
        assertTrue(TestCascadingEntityListener.events.get(1).contains("onAfterUpdate"));
        // second onAfterUpdate because UserEntityListener changes loginLowerCase and the instance becomes dirty
        assertTrue(TestCascadingEntityListener.events.get(2).contains("onAfterUpdate"));

        try (Transaction tx = persistence.createTransaction()) {
            User u = persistence.getEntityManager().find(User.class, user.getId());
            assertNotNull(u);
            assertEquals("1-new-" + u.getId(), u.getLoginLowerCase());
        }
    }

    @Test
    public void testChangeThenQuery() throws Exception {
        TestCascadingEntityListener.withNewTx = false;
        TestCascadingEntityListener.withView = true;
        try (Transaction tx = persistence.createTransaction()) {
            User u = persistence.getEntityManager().find(User.class, user.getId());
            assertNotNull(u);
            u.setLogin("1-NEW-" + user.getId());

            TypedQuery<User> query = persistence.getEntityManager().createQuery("select u from sec$User u where u.login = 'admin'", User.class);
            query.setViewName(View.MINIMAL);
            User admin = query.getSingleResult();
            System.out.println(admin.getLogin());

            tx.commit();
        }
        System.out.println("\n" + TestCascadingEntityListener.events + "\n");
        assertEquals(3, TestCascadingEntityListener.events.size());
        // on flush by query
        assertTrue(TestCascadingEntityListener.events.get(0).contains("onBeforeUpdate"));
        assertTrue(TestCascadingEntityListener.events.get(1).contains("onAfterUpdate"));
        // second onAfterUpdate because UserEntityListener changes loginLowerCase and the instance becomes dirty
        assertTrue(TestCascadingEntityListener.events.get(2).contains("onAfterUpdate"));

        try (Transaction tx = persistence.createTransaction()) {
            User u = persistence.getEntityManager().find(User.class, user.getId());
            assertNotNull(u);
            assertEquals("1-new-" + u.getId(), u.getLoginLowerCase());
        }
    }

    @Test
    public void testChangeThenQueryThenChange() throws Exception {
        TestCascadingEntityListener.withNewTx = false;
        TestCascadingEntityListener.withView = true;
        try (Transaction tx = persistence.createTransaction()) {
            User u = persistence.getEntityManager().find(User.class, user.getId());
            assertNotNull(u);
            u.setLogin("1-NEW-" + user.getId());

            TypedQuery<User> query = persistence.getEntityManager().createQuery("select u from sec$User u where u.login = 'admin'", User.class);
            query.setViewName(View.MINIMAL);
            User admin = query.getSingleResult();
            System.out.println(admin.getLogin());

            u.setLogin("2-NEW-" + user.getId());

            tx.commit();
        }
        System.out.println("\n" + TestCascadingEntityListener.events + "\n");
        assertEquals(6, TestCascadingEntityListener.events.size());
        // on flush by query
        assertTrue(TestCascadingEntityListener.events.get(0).contains("onBeforeUpdate"));
        assertTrue(TestCascadingEntityListener.events.get(1).contains("onAfterUpdate"));
        // second onAfterUpdate because UserEntityListener changes loginLowerCase and the instance becomes dirty
        assertTrue(TestCascadingEntityListener.events.get(2).contains("onAfterUpdate"));
        // on commit because of change after flush
        assertTrue(TestCascadingEntityListener.events.get(0).contains("onBeforeUpdate"));
        assertTrue(TestCascadingEntityListener.events.get(1).contains("onAfterUpdate"));
        // second onAfterUpdate because UserEntityListener changes loginLowerCase and the instance becomes dirty
        assertTrue(TestCascadingEntityListener.events.get(2).contains("onAfterUpdate"));

        try (Transaction tx = persistence.createTransaction()) {
            User u = persistence.getEntityManager().find(User.class, user.getId());
            assertNotNull(u);
            assertEquals("2-new-" + u.getId(), u.getLoginLowerCase());
        }
    }
}
