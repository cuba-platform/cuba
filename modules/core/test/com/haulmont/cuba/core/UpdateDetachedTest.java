/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 26.02.2009 18:33:47
 *
 * $Id$
 */
package com.haulmont.cuba.core;

import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.security.entity.Permission;
import com.haulmont.cuba.security.entity.PermissionType;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.global.DataServiceRemote;
import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.entity.Entity;

import java.util.UUID;
import java.util.Collections;
import java.util.Map;

import org.apache.openjpa.enhance.PersistenceCapable;

public class UpdateDetachedTest extends CubaTestCase
{
    private UUID roleId, role2Id, permissionId;

    protected void setUp() throws Exception {
        super.setUp();
        Transaction tx = Locator.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();

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

    protected void tearDown() throws Exception {
        Transaction tx = Locator.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();

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
        super.tearDown();
    }

    public void test() {
        Permission p;
        Transaction tx = Locator.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();

            em.setView(new View(Permission.class)
                    .addProperty("target")
                    .addProperty("role",
                        new View(Role.class)
                            .addProperty("name")
                    )
            );

            p = em.find(Permission.class, permissionId);
            tx.commitRetaining();

            p.setTarget("newTarget");

            em = PersistenceProvider.getEntityManager();
            p = em.merge(p);

            tx.commit();
        } finally {
            tx.end();
        }

        assertNotNull(((PersistenceCapable) p).pcGetDetachedState());
        assertNotNull(p.getRole());
        assertNotNull(((PersistenceCapable) p.getRole()).pcGetDetachedState());
    }

    public void testRollback() {
        Permission p = null;
        Transaction tx = Locator.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();

            em.setView(new View(Permission.class)
                    .addProperty("target")
                    .addProperty("role",
                        new View(Role.class)
                            .addProperty("name")
                    )
            );

            p = em.find(Permission.class, permissionId);
            tx.commitRetaining();

            p.setTarget("newTarget");

            em = PersistenceProvider.getEntityManager();
            p = em.merge(p);

            throwException();
            tx.commit();
        } catch (RuntimeException e) {
            // ok
        } finally {
            tx.end();
            assertNotNull(p);
            assertNull(((PersistenceCapable) p).pcGetDetachedState());
        }
    }

    private void throwException() {
        throw new RuntimeException();
    }

    public void testDataService() {
        Permission p;
        DataService ds = Locator.lookupLocal(DataService.JNDI_NAME);

        DataService.LoadContext ctx = new DataServiceRemote.LoadContext(Permission.class);
        ctx.setId(permissionId);
        ctx.setView(new View(Permission.class)
                .addProperty("target")
                .addProperty("role",
                    new View(Role.class)
                        .addProperty("name")
                )
        );
        p = ds.load(ctx);

        p.setTarget("newTarget");

        DataServiceRemote.CommitContext commitCtx = new DataService.CommitContext(Collections.singleton(p));
        Map<Entity,Entity> map = ds.commit(commitCtx);

        p = (Permission) map.get(p);
        assertNotNull(((PersistenceCapable) p).pcGetDetachedState());
        assertNotNull(p.getRole());
        assertNotNull(((PersistenceCapable) p.getRole()).pcGetDetachedState());
    }

    public void testUpdateNotLoaded() {
        Permission p;
        Transaction tx = Locator.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();

            em.setView(new View(Permission.class)
                    .addProperty("target")
            );

            p = em.find(Permission.class, permissionId);
            tx.commitRetaining();

            em = PersistenceProvider.getEntityManager();
            Role role = em.find(Role.class, role2Id);
            tx.commitRetaining();

            p.setRole(role); // change Role in detached object

            em = PersistenceProvider.getEntityManager();
            p = em.merge(p);
            tx.commit();
        } finally {
            tx.end();
        }
        assertNull(p.getRole());

        tx = Locator.createTransaction();
        try {
            EntityManager em = PersistenceProvider.getEntityManager();

            em.setView(new View(Permission.class)
                    .addProperty("target")
                    .addProperty("role")
            );
            p = em.find(Permission.class, permissionId);
            tx.commit();
        } finally {
            tx.end();
        }
        assertEquals(roleId, p.getRole().getId()); // Role has not been changed
    }

}
