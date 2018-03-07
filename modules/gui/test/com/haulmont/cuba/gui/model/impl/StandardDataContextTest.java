/*
 * Copyright (c) 2008-2017 Haulmont.
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

package com.haulmont.cuba.gui.model.impl;

import com.haulmont.cuba.client.testsupport.CubaClientTestCase;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.EntityStates;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.UserRole;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

import static org.junit.Assert.*;

public class StandardDataContextTest extends CubaClientTestCase {

    private Metadata metadata;
    private DataManager dataManager;
    private EntityStates entityStates;

    @Before
    public void setUp() throws Exception {
        addEntityPackage("com.haulmont.cuba");
        setupInfrastructure();

        metadata = AppBeans.get(Metadata.class);
        dataManager = AppBeans.get(DataManager.class);
        entityStates = AppBeans.get(EntityStates.class);
    }

    @Test
    public void testMerge() throws Exception {
        StandardDataContext context = new StandardDataContext(metadata, dataManager, entityStates);

        User user1 = new User();
        user1.setLogin("u1");
        user1.setName("User 1");

        User mergedUser1 = context.merge(user1);
        assertTrue(mergedUser1 == context.find(User.class, user1.getId()));

        User user11 = new User();
        user11.setLogin("u11");
        user11.setName("User 11");
        user11.setId(user1.getId());

        User mergedUser11 = context.merge(user11);
        assertTrue(mergedUser11 == mergedUser1);
        assertTrue(mergedUser11 != user11);
    }

    @Test
    public void testMergeGraph() throws Exception {
        StandardDataContext context = new StandardDataContext(metadata, dataManager, entityStates);

        // an object being merged
        User user1 = new User();
        entityStates.makeDetached(user1);
        user1.setLogin("u1");
        user1.setName("User 1");
        user1.setUserRoles(new ArrayList<>());

        Role role1 = new Role();
        entityStates.makeDetached(role1);
        role1.setName("Role 1");

        UserRole user1Role1 = new UserRole();
        entityStates.makeDetached(user1Role1);
        user1Role1.setUser(user1);
        user1Role1.setRole(role1);

        user1.getUserRoles().add(user1Role1);

        // somewhere in the object graph another object with the same id
        User user11 = new User();
        entityStates.makeDetached(user11);
        user11.setLogin("u11");
        user11.setName("User 11");
        user11.setId(user1.getId());

        UserRole user11Role1 = new UserRole();
        entityStates.makeDetached(user11Role1);
        user11Role1.setUser(user11);
        user11Role1.setRole(role1);

        user1.getUserRoles().add(user11Role1);

        User mergedUser1 = context.merge(user1);

        // instance of the first object
        assertTrue(mergedUser1 == context.find(User.class, user1.getId()));

        // local attributes of the second object
        assertEquals("u11", mergedUser1.getLogin());
        assertEquals("User 11", mergedUser1.getName());

        assertTrue(user1Role1 == context.find(UserRole.class, user1Role1.getId()));
        assertTrue(user11Role1 == context.find(UserRole.class, user11Role1.getId()));
        assertTrue(role1 == context.find(Role.class, role1.getId()));

        assertTrue(mergedUser1.getUserRoles().get(1).getUser() == mergedUser1);

        assertFalse(context.hasChanges());
    }

    @Test
    public void testMergeGraph_firstObjectWithNullCollection() throws Exception {
        StandardDataContext context = new StandardDataContext(metadata, dataManager, entityStates);

        User user1 = new User();
        user1.setLogin("u1");
        user1.setName("User 1");

        Role role1 = new Role();
        role1.setName("Role 1");

        User user11 = new User();
        user11.setLogin("u11");
        user11.setName("User 11");
        user11.setId(user1.getId());

        UserRole user11Role1 = new UserRole();
        user11Role1.setUser(user11);
        user11Role1.setRole(role1);
        user11.setUserRoles(new ArrayList<>());
        user11.getUserRoles().add(user11Role1);

        User mergedUser1 = context.merge(user1);

        User mergedUser11 = context.merge(user11);

        assertTrue(mergedUser11 == mergedUser1);

        // instance of the first object
        assertTrue(mergedUser1 == context.find(User.class, user1.getId()));

        // local attributes of the second object
        assertEquals("u11", mergedUser1.getLogin());
        assertEquals("User 11", mergedUser1.getName());

        // collection of the second object
        assertNotNull(mergedUser1.getUserRoles());
        assertTrue(mergedUser1.getUserRoles().get(0).getUser() == mergedUser1);

        assertTrue(user11Role1 == context.find(UserRole.class, user11Role1.getId()));
        assertTrue(role1 == context.find(Role.class, role1.getId()));
    }

    @Test
    public void testMergedNew() throws Exception {
        StandardDataContext context = new StandardDataContext(metadata, dataManager, entityStates) {
            @Override
            protected Set<Entity> performCommit() {
                return Collections.emptySet();
            }
        };

        User user1 = new User();
        user1.setLogin("u1");
        user1.setName("User 1");
        user1.setUserRoles(new ArrayList<>());

        Role role1 = new Role();
        role1.setName("Role 1");

        Role role2 = new Role();
        role1.setName("Role 2");

        UserRole user1Role1 = new UserRole();
        user1Role1.setUser(user1);
        user1Role1.setRole(role1);

        user1.getUserRoles().add(user1Role1);

        UserRole user1Role2 = new UserRole();
        user1Role2.setUser(user1);
        user1Role2.setRole(role2);

        user1.getUserRoles().add(user1Role2);

        context.merge(user1);

        context.addPreCommitListener(e -> {
            assertEquals(5, e.getModifiedInstances().size());
            assertTrue(e.getModifiedInstances().contains(user1));
            assertTrue(e.getModifiedInstances().contains(role1));
            assertTrue(e.getModifiedInstances().contains(role2));
            assertTrue(e.getModifiedInstances().contains(user1Role1));
            assertTrue(e.getModifiedInstances().contains(user1Role2));

            assertEquals(0, e.getRemovedInstances().size());
        });

        context.commit();
    }

    @Test
    public void testModified() throws Exception {
        StandardDataContext context = new StandardDataContext(metadata, dataManager, entityStates) {
            @Override
            protected Set<Entity> performCommit() {
                return Collections.emptySet();
            }
        };

        User user1 = new User();
        user1.setLogin("u1");
        user1.setName("User 1");
        user1.setUserRoles(new ArrayList<>());
        entityStates.makeDetached(user1);

        Role role1 = new Role();
        role1.setName("Role 1");
        entityStates.makeDetached(role1);

        Role role2 = new Role();
        role1.setName("Role 2");
        entityStates.makeDetached(role2);

        UserRole user1Role1 = new UserRole();
        user1Role1.setUser(user1);
        user1Role1.setRole(role1);
        entityStates.makeDetached(user1Role1);

        user1.getUserRoles().add(user1Role1);

        UserRole user1Role2 = new UserRole();
        user1Role2.setUser(user1);
        user1Role2.setRole(role2);
        entityStates.makeDetached(user1Role2);

        user1.getUserRoles().add(user1Role2);

        context.merge(user1);

        role1.setName("Role 1 modified");

        user1.getUserRoles().remove(user1Role2);
        context.remove(user1Role2);

        context.addPreCommitListener(e -> {
            assertEquals(2, e.getModifiedInstances().size());
            assertTrue(e.getModifiedInstances().contains(role1));
            assertTrue(e.getModifiedInstances().contains(user1));

            assertEquals(1, e.getRemovedInstances().size());
            assertTrue(e.getRemovedInstances().contains(user1Role2));
        });

        context.commit();
    }

    @Test
    public void testCopyState() throws Exception {
        StandardDataContext context = new StandardDataContext(metadata, dataManager, entityStates);

        User src, dst;
        // (1) src.new > dst.new : copy all non-null

        src = new User();
        src.setLogin("u-src");

        dst = new User();
        dst.setId(src.getId());
        dst.setLogin("u-dst");
        dst.setName("Dest User");
        dst.setUserRoles(new ArrayList<>());

        context.copyState(src, dst);

        assertTrue(entityStates.isNew(dst));
        assertNull(dst.getVersion());
        assertEquals("u-src", dst.getLogin());
        assertEquals("Dest User", dst.getName());
        assertNotNull(dst.getUserRoles());

        // (2) src.new -> dst.det : do nothing

        src = new User();
        src.setLogin("u-src");

        dst = new User();
        dst.setId(src.getId());
        dst.setVersion(1);
        dst.setLogin("u-dst");
        dst.setName("Dest User");
        dst.setUserRoles(new ArrayList<>());
        entityStates.makeDetached(dst);

        context.copyState(src, dst);

        assertTrue(entityStates.isDetached(dst));
        assertNotNull(dst.getVersion());
        assertEquals("u-dst", dst.getLogin());
        assertEquals("Dest User", dst.getName());
        assertNotNull(dst.getUserRoles());

        // (3) src.det -> dst.new : copy all loaded, make detached

        src = new User();
        src.setVersion(1);
        src.setLogin("u-src");
        entityStates.makeDetached(src);

        dst = new User();
        dst.setId(src.getId());
        dst.setLogin("u-dst");
        dst.setName("Dest User");
        dst.setUserRoles(new ArrayList<>());

        context.copyState(src, dst);

        assertTrue(entityStates.isDetached(dst));
        assertEquals(Integer.valueOf(1), dst.getVersion());
        assertEquals("u-src", dst.getLogin());
        assertNull(dst.getName());
        assertNotNull(dst.getUserRoles());

        // (4) src.det -> dst.det : if src.version >= dst.version, copy all loaded

        src = new User();
        src.setVersion(2);
        src.setLogin("u-src");
        entityStates.makeDetached(src);

        dst = new User();
        dst.setId(src.getId());
        dst.setVersion(1);
        dst.setLogin("u-dst");
        dst.setName("Dest User");
        dst.setUserRoles(new ArrayList<>());
        entityStates.makeDetached(dst);

        context.copyState(src, dst);

        assertTrue(entityStates.isDetached(dst));
        assertEquals(Integer.valueOf(2), dst.getVersion());
        assertEquals("u-src", dst.getLogin());
        assertNull(dst.getName());
        assertNotNull(dst.getUserRoles());

        // (4) src.det -> dst.det : if src.version < dst.version, do nothing

        src = new User();
        src.setVersion(1);
        src.setLogin("u-src");
        entityStates.makeDetached(src);

        dst = new User();
        dst.setId(src.getId());
        dst.setVersion(2);
        dst.setLogin("u-dst");
        dst.setName("Dest User");
        dst.setUserRoles(new ArrayList<>());
        entityStates.makeDetached(dst);

        context.copyState(src, dst);

        assertTrue(entityStates.isDetached(dst));
        assertEquals(Integer.valueOf(2), dst.getVersion());
        assertEquals("u-dst", dst.getLogin());
        assertEquals("Dest User", dst.getName());
        assertNotNull(dst.getUserRoles());
    }

    @Test
    public void testParentContext() throws Exception {
        StandardDataContext context = new StandardDataContext(metadata, dataManager, entityStates) {
            @Override
            protected Set<Entity> commitToDataManager() {
                return Collections.emptySet();
            }
        };

        User user1 = new User();
        user1.setLogin("u1");
        user1.setName("User 1");
        user1.setUserRoles(new ArrayList<>());

        context.merge(user1);

        StandardDataContext childContext = new StandardDataContext(metadata, dataManager, entityStates);

        childContext.setParent(context);

        User user1InChild = childContext.find(User.class, user1.getId());
        assertNotNull(user1InChild);
        assertTrue(user1InChild != user1);

        UserRole user1Role1 = new UserRole();
        user1Role1.setUser(user1InChild);
        assertNotNull(user1InChild.getUserRoles());
        user1InChild.getUserRoles().add(user1Role1);

        childContext.merge(user1Role1);

        childContext.addPreCommitListener(e -> {
            assertEquals(2, e.getModifiedInstances().size());
            assertTrue(e.getModifiedInstances().stream().anyMatch(entity -> entity == user1InChild));
            assertTrue(e.getModifiedInstances().stream().anyMatch(entity -> entity == user1Role1));
        });

        childContext.commit();

        assertNotNull(user1.getUserRoles());
        assertEquals(1, user1.getUserRoles().size());

        context.addPreCommitListener(e -> {
            assertEquals(2, e.getModifiedInstances().size());
            assertTrue(e.getModifiedInstances().stream().anyMatch(entity -> entity == user1));
            assertTrue(e.getModifiedInstances().stream().anyMatch(entity -> entity == user1Role1));
        });

        context.commit();
    }
}