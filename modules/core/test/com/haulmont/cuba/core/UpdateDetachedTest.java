/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core;

import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.security.entity.Permission;
import com.haulmont.cuba.security.entity.PermissionType;
import com.haulmont.cuba.security.entity.Role;
import org.apache.openjpa.enhance.PersistenceCapable;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

/**
 * @author krivopustov
 * @version $Id$
 */
public class UpdateDetachedTest extends CubaTestCase
{
    private UUID roleId, role2Id, permissionId;

    protected void setUp() throws Exception {
        super.setUp();
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();

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
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();

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
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();

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

            em = persistence.getEntityManager();
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
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();

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

            em = persistence.getEntityManager();
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

    public void testDataService() {
        Permission p;
        DataService ds = AppBeans.get(DataService.NAME);

        LoadContext ctx = new LoadContext(Permission.class);
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

        CommitContext commitCtx = new CommitContext(Collections.singleton(p));
        Set<Entity> entities = ds.commit(commitCtx);

        Permission result = null;
        for (Entity entity : entities) {
            if (entity.equals(p))
                result = (Permission) entity;
        }
        assertNotNull(((PersistenceCapable) result).pcGetDetachedState());
        assertNotNull(result.getRole());
        assertNotNull(((PersistenceCapable) result.getRole()).pcGetDetachedState());
    }

    public void testUpdateNotLoaded() {
        Permission p;
        Transaction tx = persistence.createTransaction();
        try {
            EntityManager em = persistence.getEntityManager();

            em.setView(new View(Permission.class)
                    .addProperty("target")
            );

            p = em.find(Permission.class, permissionId);
            tx.commitRetaining();

            em = persistence.getEntityManager();
            Role role = em.find(Role.class, role2Id);
            tx.commit();

            assertNull(p.getRole());
            try {
                p.setRole(role); // try to change Role in detached object
                fail();
            } catch (Exception e) {
                // ok
            }

        } finally {
            tx.end();
        }
    }
}
