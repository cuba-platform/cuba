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

import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.security.entity.Permission;
import com.haulmont.cuba.security.entity.PermissionType;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

import static com.haulmont.cuba.testsupport.TestSupport.reserialize;
import static org.junit.Assert.*;


/**
 */
public class UpdateDetachedTest {

    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    private UUID roleId, role2Id, permissionId;

    @Before
    public void setUp() throws Exception {
        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();

            Role role = new Role();
            roleId = role.getId();
            role.setName("testRole");
            em.persist(role);

            Role role2 = new Role();
            role2Id = role2.getId();
            role2.setName("testRole2");
            em.persist(role2);

            Permission permission = new Permission();
            permissionId = permission.getId();
            permission.setRole(role);
            permission.setType(PermissionType.SCREEN);
            permission.setTarget("testTarget");
            em.persist(permission);

            tx.commit();
        } finally {
            tx.end();
        }
    }

    @After
    public void tearDown() throws Exception {
        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();

            Query q;
            q = em.createNativeQuery("delete from SEC_PERMISSION where ID = ?");
            q.setParameter(1, permissionId.toString());
            q.executeUpdate();

            q = em.createNativeQuery("delete from SEC_ROLE where ID = ? or ID = ?");
            q.setParameter(1, roleId.toString());
            q.setParameter(2, role2Id.toString());
            q.executeUpdate();

            tx.commit();
        } finally {
            tx.end();
        }
    }

    @Test
    public void test() throws Exception {
        Permission p;
        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();

            View view = new View(Permission.class)
                    .addProperty("target")
                    .addProperty("role",
                            new View(Role.class)
                                    .addProperty("name")
                    );

            p = em.find(Permission.class, permissionId, view);
            tx.commitRetaining();

            assertNotNull(p);
            p.setTarget("newTarget");

            em = cont.persistence().getEntityManager();
            p = em.merge(p);

            tx.commit();
        } finally {
            tx.end();
        }
        p = reserialize(p);
        assertTrue(PersistenceHelper.isDetached(p));
        assertNotNull(p.getRole());
        assertTrue(PersistenceHelper.isDetached(p.getRole()));
        assertTrue(PersistenceHelper.isLoaded(p, "role"));
    }

    @Test
    public void testRollback() {
        Permission p = null;
        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();

            View view = new View(Permission.class)
                    .addProperty("target")
                    .addProperty("role",
                            new View(Role.class)
                                    .addProperty("name")
                    );

            p = em.find(Permission.class, permissionId, view);
            tx.commitRetaining();

            p.setTarget("newTarget");

            em = cont.persistence().getEntityManager();
            p = em.merge(p);

            throwException();
            tx.commit();
        } catch (RuntimeException e) {
            // ok
        } finally {
            tx.end();
            assertNotNull(p);
//            assertNull(((PersistenceCapable) p).pcGetDetachedState());
        }
    }

    private void throwException() {
        throw new RuntimeException();
    }

    @Test
    public void testDataService() throws Exception {
        Permission p;
        DataService ds = AppBeans.get(DataService.NAME);

        LoadContext<Permission> ctx = new LoadContext<>(Permission.class);
        ctx.setId(permissionId);
        ctx.setView(new View(Permission.class)
                .addProperty("target")
                .addProperty("role",
                    new View(Role.class)
                        .addProperty("name")
                )
        );
        p = ds.load(ctx);

        assertNotNull(p);
        p.setTarget("newTarget");

        CommitContext commitCtx = new CommitContext(Collections.singleton(p));
        Set<Entity> entities = ds.commit(commitCtx);

        Permission result = null;
        for (Entity entity : entities) {
            if (entity.equals(p))
                result = (Permission) entity;
        }
        result = reserialize(result);
        assertTrue(PersistenceHelper.isDetached(result));
        assertNotNull(result.getRole());
        assertTrue(PersistenceHelper.isDetached(result.getRole()));
        assertTrue(PersistenceHelper.isLoaded(result, "role"));
    }

    @Test
    public void testUpdateNotLoaded() throws Exception {
        Permission p;
        Role role;
        Transaction tx = cont.persistence().createTransaction();
        try {
            EntityManager em = cont.persistence().getEntityManager();

            p = em.find(Permission.class, permissionId, new View(Permission.class).addProperty("target"));
            tx.commitRetaining();

            em = cont.persistence().getEntityManager();
            role = em.find(Role.class, role2Id);
            tx.commit();
        } finally {
            tx.end();
        }
        p = reserialize(p);

        assertFalse(PersistenceHelper.isLoaded(p, "role"));
        assertFalse(PersistenceHelper.isLoaded(p, "value"));
    }
}
