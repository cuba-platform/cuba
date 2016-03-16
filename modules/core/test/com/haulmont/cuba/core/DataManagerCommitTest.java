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

import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.Role;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.UserRole;
import com.haulmont.cuba.testsupport.TestContainer;
import com.haulmont.cuba.testsupport.TestSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

/**
 */
public class DataManagerCommitTest {

    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;
    
    private DataManager dataManager;
    private UUID userId;
    private UUID groupId = UUID.fromString("0fa2b1a5-1d68-4d69-9fbd-dff348347f93");
    private UUID userRoleId;
    private View view;

    @Before
    public void setUp() throws Exception {
        dataManager = AppBeans.get(DataManager.class);

        try (Transaction tx = cont.persistence().createTransaction()) {
            EntityManager em = cont.persistence().getEntityManager();

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

    @After
    public void tearDown() throws Exception {
        cont.deleteRecord("SEC_USER_ROLE", userRoleId);
        cont.deleteRecord("SEC_USER", userId);
    }

    @Test
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

        //do check loaded before serialization
        assertTrue(PersistenceHelper.isLoaded(user, "group"));
        assertTrue(PersistenceHelper.isLoaded(user, "userRoles"));
        assertTrue(!PersistenceHelper.isLoaded(user, "substitutions"));
        //do second check to make sure isLoaded did not affect attribute fetch status
        assertTrue(!PersistenceHelper.isLoaded(user, "substitutions"));

        user = TestSupport.reserialize(user);

        assertTrue(PersistenceHelper.isDetached(user));
        assertTrue(!PersistenceHelper.isNew(user));
        assertTrue(!PersistenceHelper.isManaged(user));

        assertEquals(version + 1, (int) user.getVersion());
        assertEquals("testUser-changed", user.getName());
        assertEquals(groupId, user.getGroup().getId());
        assertEquals(1, user.getUserRoles().size());
        assertEquals(userRoleId, user.getUserRoles().get(0).getId());

        //do check loaded after serialization
        assertTrue(PersistenceHelper.isLoaded(user, "group"));
        assertTrue(PersistenceHelper.isLoaded(user, "userRoles"));
        assertTrue(!PersistenceHelper.isLoaded(user, "substitutions"));
        //do second check to make sure isLoaded did not affect attribute fetch status
        assertTrue(!PersistenceHelper.isLoaded(user, "substitutions"));
    }
}
