/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core;

import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.UserRole;
import com.haulmont.cuba.testsupport.TestSupport;

import java.util.UUID;

/**
 * @author Konstantin Krivopustov
 * @version $Id$
 */
public class DataManagerCommitTest extends CubaTestCase {

    private DataManager dataManager;
    private UUID userId;
    private UUID groupId = UUID.fromString("0fa2b1a5-1d68-4d69-9fbd-dff348347f93");
    private UUID userRoleId;
    private View view;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        dataManager = AppBeans.get(DataManager.class);

        try (Transaction tx = persistence.createTransaction()) {
            EntityManager em = persistence.getEntityManager();

            Group group = em.find(Group.class, groupId);
            Role role = em.find(Role.class, UUID.fromString("0c018061-b26f-4de2-a5be-dff348347f93"));

            User user = new User();
            userId = user.getId();
            user.setName("testUser");
            user.setLogin("login" + userId);
            user.setPassword("000");
            user.setGroup(group);
            em.persist(user);

            UserRole userRole = new UserRole();
            userRoleId = userRole.getId();
            userRole.setRole(role);
            userRole.setUser(user);
            em.persist(userRole);

            tx.commit();
        }

        view = new View(User.class, true)
                .addProperty("login")
                .addProperty("loginLowerCase")
                .addProperty("name")
                .addProperty("password")
                .addProperty("group", new View(Group.class).addProperty("name"))
                .addProperty("userRoles", new View(UserRole.class));
    }

    @Override
    protected void tearDown() throws Exception {
        deleteRecord("SEC_USER_ROLE", userRoleId);
        deleteRecord("SEC_USER", userId);
        super.tearDown();
    }

    public void testViewAfterCommit() throws Exception {
        LoadContext<User> loadContext = LoadContext.create(User.class).setId(userId).setView(view);
        User user = dataManager.load(loadContext);
        assertNotNull(user);
        user = TestSupport.reserialize(user);
        assertEquals(groupId, user.getGroup().getId());
        assertEquals(1, user.getUserRoles().size());
        assertEquals(userRoleId, user.getUserRoles().get(0).getId());

        Integer version = user.getVersion();
        user.setName("testUser-changed");
        user = dataManager.commit(user, view);
        assertNotNull(user);
        user = TestSupport.reserialize(user);

        assertTrue(PersistenceHelper.isDetached(user));
        assertTrue(!PersistenceHelper.isNew(user));
        assertTrue(!PersistenceHelper.isManaged(user));

        assertEquals(version + 1, (int) user.getVersion());
        assertEquals("testUser-changed", user.getName());
        assertEquals(groupId, user.getGroup().getId());
        assertEquals(1, user.getUserRoles().size());
        assertEquals(userRoleId, user.getUserRoles().get(0).getId());

        assertTrue(PersistenceHelper.isLoaded(user, "group"));
        assertTrue(PersistenceHelper.isLoaded(user, "userRoles"));
        assertTrue(!PersistenceHelper.isLoaded(user, "substitutions"));
    }
}
