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

import com.haulmont.cuba.core.entity.BaseGenericIdEntity;
import com.haulmont.cuba.core.entity.Server;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.testmodel.cascadedelete.CascadeEntity;
import com.haulmont.cuba.testmodel.sales.Customer;
import com.haulmont.cuba.testmodel.sales.Order;
import com.haulmont.cuba.testsupport.TestContainer;
import com.haulmont.cuba.testsupport.TestSupport;
import org.apache.commons.lang.time.DateUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;

import static org.junit.Assert.*;

public class NonDetachedTest {

    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    private Persistence persistence;
    private Metadata metadata;
    private User user;
    private Group companyGroup;
    private Customer customer;
    private Order order;
    private View orderView;
    private CascadeEntity cascadeEntity1;
    private CascadeEntity cascadeEntity2;

    @Before
    public void setUp() throws Exception {
        persistence = cont.persistence();
        metadata = cont.metadata();

        try (Transaction tx = persistence.createTransaction()) {
            EntityManager em = persistence.getEntityManager();

            user = metadata.create(User.class);
            user.setName("testUser");
            user.setLogin("testLogin");
            companyGroup = em.find(Group.class, TestSupport.COMPANY_GROUP_ID);
            user.setGroup(companyGroup);
            em.persist(user);

            customer = metadata.create(Customer.class);
            customer.setName("test customer");
            em.persist(customer);

            order = metadata.create(Order.class);
            order.setDate(new Date());
            order.setAmount(BigDecimal.TEN);
            order.setCustomer(customer);
            order.setUser(user);
            em.persist(order);

            cascadeEntity1 = metadata.create(CascadeEntity.class);
            cascadeEntity1.setName("cascadeEntity1");
            em.persist(cascadeEntity1);

            cascadeEntity2 = metadata.create(CascadeEntity.class);
            cascadeEntity2.setName("cascadeEntity2");
            cascadeEntity2.setFather(cascadeEntity1);
            em.persist(cascadeEntity2);

            cascadeEntity1.setFirstChild(cascadeEntity2);

            tx.commit();
        }

        orderView = new View(Order.class)
                .addProperty("date")
                .addProperty("amount")
                .addProperty("customer", new View(Customer.class).addProperty("name"))
                .addProperty("user", new View(User.class).addProperty("login").addProperty("name"));
    }

    @After
    public void tearDown() throws Exception {
        cont.deleteRecord(order, customer, user, cascadeEntity2, cascadeEntity1);
    }

    @Test
    public void testSaveNotDetached_DM() throws Exception {
        Group companyGroupCopy = metadata.getTools().copy(companyGroup);
        assertTrue(PersistenceHelper.isNew(companyGroupCopy));
        PersistenceHelper.makeDetached(companyGroupCopy);

        User user = persistence.callInTransaction((em) -> em.find(User.class, this.user.getId()));
        User userCopy = metadata.getTools().copy(user);
        assertNull(userCopy.getGroup());

        assertTrue(PersistenceHelper.isNew(userCopy));
        PersistenceHelper.makeDetached(userCopy);

        userCopy.setGroup(companyGroupCopy);
        userCopy.setName("new name");
        AppBeans.get(DataManager.class).commit(userCopy);

        user = persistence.callInTransaction((em) -> em.find(User.class, this.user.getId()));
        assertEquals("new name", user.getName());
    }

    @Test
    public void testCreateNew_DM() throws Exception {
        // check versioned entity
        Group companyGroupCopy = metadata.getTools().copy(companyGroup);
        assertTrue(PersistenceHelper.isNew(companyGroupCopy));
        PersistenceHelper.makeDetached(companyGroupCopy);

        User user = persistence.callInTransaction((em) -> em.find(User.class, this.user.getId()));
        User userCopy = metadata.getTools().copy(user);
        assertNull(userCopy.getGroup());

        assertTrue(PersistenceHelper.isNew(userCopy));
        PersistenceHelper.makeDetached(userCopy);

        userCopy.setId(UuidProvider.createUuid());
        userCopy.setVersion(null);
        userCopy.setGroup(companyGroupCopy);
        userCopy.setName("new name");
        try {
            AppBeans.get(DataManager.class).commit(userCopy);

            user = persistence.callInTransaction((em) -> em.find(User.class, userCopy.getId()));
            assertEquals("new name", user.getName());
        } finally {
            cont.deleteRecord(userCopy);
        }

        // check non-versioned entity
        Server server = metadata.create(Server.class);
        server.setName("server-" + server.getId());
        assertTrue(PersistenceHelper.isNew(server));
        PersistenceHelper.makeDetached(server);
        try {
            AppBeans.get(DataManager.class).commit(server);
            Server loaded = persistence.callInTransaction(em -> em.find(Server.class, server.getId()));
            assertNotNull(loaded);
        } finally {
            cont.deleteRecord(server);
        }
    }

    interface Saver {
        void save(BaseGenericIdEntity entity);
    }

    private Order loadChangeAndSave(Saver saver) {
        Order order = persistence.callInTransaction(em -> em.find(Order.class, this.order.getId()));
        Order orderCopy = metadata.getTools().copy(order);

        Customer customerCopy = metadata.getTools().copy(this.customer);

        Date date = DateUtils.addDays(orderCopy.getDate(), 1);
        orderCopy.setDate(date);
        orderCopy.setAmount(null);
        orderCopy.setCustomer(customerCopy);
        assertNull(orderCopy.getUser());

        assertTrue(PersistenceHelper.isNew(orderCopy));
        saver.save(orderCopy);

        order = persistence.callInTransaction(em -> em.find(Order.class, this.order.getId(), orderView));
        assertEquals(date, order.getDate());

        return order;
    }

    @Test
    public void testSaveNulls_DM() throws Exception {
        Order order = loadChangeAndSave(entity -> {
            PersistenceHelper.makeDetached(entity);
            AppBeans.get(DataManager.class).commit(entity);
        });
        assertNull(order.getAmount());
        assertNotNull(order.getCustomer());
        assertNull(order.getUser());
    }

    @Test
    public void testSaveNulls_EM() throws Exception {
        Order order = loadChangeAndSave(entity -> {
            PersistenceHelper.makeDetached(entity);
            persistence.runInTransaction(em -> em.merge(entity));
        });
        assertNull(order.getAmount());
        assertNotNull(order.getCustomer());
        assertNull(order.getUser());
    }

    @Test
    public void testDoNotSaveNulls_DM() throws Exception {
        Order order = loadChangeAndSave(entity -> {
            PersistenceHelper.makePatch(entity);
            AppBeans.get(DataManager.class).commit(entity);
        });
        assertNotNull(order.getAmount());
        assertNotNull(order.getCustomer());
        assertNotNull(order.getUser());
    }

    @Test
    public void testDoNotSaveNulls_EM() throws Exception {
        Order order = loadChangeAndSave(entity -> {
            PersistenceHelper.makePatch(entity);
            persistence.runInTransaction(em -> em.merge(entity));
        });
        assertNotNull(order.getAmount());
        assertNotNull(order.getCustomer());
        assertNotNull(order.getUser());
    }

    @Test
    public void testDoNotSaveNulls_EM_new() throws Exception {
        Order order = loadChangeAndSave(entity -> {
            persistence.runInTransaction(em -> em.merge(entity));
        });
        assertNotNull(order.getAmount());
        assertNotNull(order.getCustomer());
        assertNotNull(order.getUser());
    }

    @Test
    public void testRecursiveObjects() throws Exception {
        CascadeEntity e1 = metadata.create(CascadeEntity.class);
        e1.setName("cascadeEntity1");

        CascadeEntity e2 = metadata.create(CascadeEntity.class);
        e2.setName("cascadeEntity2");
        e2.setFather(cascadeEntity1);

        e1.setChildren(new HashSet<>());
        e1.getChildren().add(e2);
        e1.setFirstChild(e2);

        AppBeans.get(EntityStates.class).makePatch(e1);

        CascadeEntity mergedEntity = persistence.callInTransaction(em -> {
            return em.merge(e1);
        });

        assertEquals(cascadeEntity1.getName(), mergedEntity.getName());
    }
}
