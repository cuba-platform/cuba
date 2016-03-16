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

package com.haulmont.cuba.core.sys.serialization;

import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.global.ViewRepository;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.UserRole;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.*;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 */
public class SerilaizationTest {
    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    private UUID userId;
    private UUID userRoleId;

    @Before
    public void setUp() throws Exception {
        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();

            User user = new User();
            userId = user.getId();
            user.setName("testUser");
            user.setLogin("testLogin");
            user.setPosition("manager");
            user.setGroup(em.find(Group.class, UUID.fromString("0fa2b1a5-1d68-4d69-9fbd-dff348347f93")));
            em.persist(user);

            UserRole userRole = new UserRole();
            userRoleId = userRole.getId();
            userRole.setUser(user);
            userRole.setRole(em.find(Role.class, UUID.fromString("0c018061-b26f-4de2-a5be-dff348347f93")));
            em.persist(userRole);

            tx.commit();
        } finally {
            tx.end();
        }
    }

    @After
    public void tearDown() throws Exception {
        cont.deleteRecord("SEC_USER_ROLE", userRoleId);
        cont.deleteRecord("SEC_USER", userId);
    }

    @Test
    public void testCompareSpeedAndSize() throws Exception {
        View view = getView();
        User user;
        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();
            user = em.find(User.class, userId, view);
            assertNotNull(user);
            tx.commit();
        } finally {
            tx.end();
        }

        KryoSerialization kryoSerialization = new KryoSerialization();
        kryoSerialization.serialize(user);//warm up
        StandardSerialization standardSerialization = new StandardSerialization();
        standardSerialization.serialize(user);//warm up

        int kryoLength = 0;
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            kryoLength = kryoSerialization.serialize(user).length;
        }
        long end = System.currentTimeMillis();
        long kryoTime = end - start;
        System.out.printf("Kryo:\t\tSize [%d]. Time [%d]\n", kryoLength, kryoTime);

        int standardLength = 0;
        start = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            standardLength = standardSerialization.serialize(user).length;
        }
        end = System.currentTimeMillis();
        long standardTime = end - start;
        System.out.printf("Standard:\t\tSize [%d]. Time [%d]\n", standardLength, standardTime);
        assertTrue(kryoTime < standardTime);
        assertTrue(kryoLength < standardLength);
    }

    @Test
    public void testDetachedSerialization() throws Exception {
        View view = getView();
        User user;
        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();
            user = em.find(User.class, userId, view);
            assertNotNull(user);
            tx.commit();
        } finally {
            tx.end();
        }

        KryoSerialization kryoSerialization = new KryoSerialization();
        User kryoUser = (User) kryoSerialization.deserialize(kryoSerialization.serialize(user));

        StandardSerialization standardSerialization = new StandardSerialization();
        User standardUser = (User) standardSerialization.deserialize(standardSerialization.serialize(user));

        assertEquals(standardUser, kryoUser);
        assertEquals(standardUser.getLogin(), kryoUser.getLogin());
        assertEquals(standardUser.getName(), kryoUser.getName());
        assertEquals(standardUser.getPosition(), kryoUser.getPosition());
        assertEquals(standardUser.getGroup(), kryoUser.getGroup());
        assertEquals(standardUser.getGroup().getName(), kryoUser.getGroup().getName());
        assertEquals(standardUser.getUserRoles().get(0), kryoUser.getUserRoles().get(0));
        assertEquals(standardUser.getUserRoles().get(0).getRole(), kryoUser.getUserRoles().get(0).getRole());
        assertEquals(standardUser.getUserRoles().get(0).getRole().getName(), kryoUser.getUserRoles().get(0).getRole().getName());
    }

    @Test
    public void testManagedSerialization() throws Exception {
        View view = getView();
        User user;
        User kryoUser;
        User standardUser;
        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();
            user = em.find(User.class, userId, view);
            assertNotNull(user);

            KryoSerialization kryoSerialization = new KryoSerialization();
            kryoUser = (User) kryoSerialization.deserialize(kryoSerialization.serialize(user));

            tx.commit();
        } finally {
            tx.end();
        }

        tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();
            user = em.find(User.class, userId, view);
            assertNotNull(user);

            StandardSerialization standardSerialization = new StandardSerialization();
            standardUser = (User) standardSerialization.deserialize(standardSerialization.serialize(user));

            tx.commit();
        } finally {
            tx.end();
        }

        assertEquals(standardUser, kryoUser);
        assertEquals(standardUser.getLogin(), kryoUser.getLogin());
        assertEquals(standardUser.getName(), kryoUser.getName());
        assertEquals(standardUser.getPosition(), kryoUser.getPosition());
        assertEquals(standardUser.getGroup(), kryoUser.getGroup());
        assertEquals(standardUser.getGroup().getName(), kryoUser.getGroup().getName());
        assertEquals(standardUser.getUserRoles().get(0), kryoUser.getUserRoles().get(0));
        assertEquals(standardUser.getUserRoles().get(0).getRole(), kryoUser.getUserRoles().get(0).getRole());
        assertEquals(standardUser.getUserRoles().get(0).getRole().getName(), kryoUser.getUserRoles().get(0).getRole().getName());
    }

    @Test
    public void testEmptyDetachedSerialization() throws Exception {
        User user;
        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();
            user = em.find(User.class, userId);
            assertNotNull(user);
            tx.commit();
        } finally {
            tx.end();
        }

        KryoSerialization kryoSerialization = new KryoSerialization();
        User kryoUser = (User) kryoSerialization.deserialize(kryoSerialization.serialize(user));

        StandardSerialization standardSerialization = new StandardSerialization();
        User standardUser = (User) standardSerialization.deserialize(standardSerialization.serialize(user));

        assertEquals(standardUser, kryoUser);
        assertEquals(standardUser.getLogin(), kryoUser.getLogin());
        assertEquals(standardUser.getName(), kryoUser.getName());
        assertEquals(standardUser.getPosition(), kryoUser.getPosition());

        try {
            standardUser.getGroup();
            Assert.fail();
        } catch (Exception ignored) {
        }

        try {
            kryoUser.getGroup();
            Assert.fail();
        } catch (Exception ignored) {
        }

        try {
            standardUser.getUserRoles().size();
            Assert.fail();
        } catch (Exception ignored) {
        }

        try {
            kryoUser.getUserRoles().size();
            Assert.fail();
        } catch (Exception ignored) {
        }
    }

    protected View getView() {Metadata metadata = AppBeans.get(Metadata.NAME);
        ViewRepository viewRepository = metadata.getViewRepository();

        View userLocalView = viewRepository.getView(User.class, View.LOCAL);
        View roleLocalView = viewRepository.getView(Role.class, View.LOCAL);
        return new View(userLocalView, "test", true)
                .addProperty("group", viewRepository.getView(Group.class, View.LOCAL))
                .addProperty("userRoles", new View(UserRole.class)
                        .addProperty("user", userLocalView)
                        .addProperty("role", roleLocalView));
    }


}
