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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * PL-9325 For new entity, DataManager.commit() does not fetch attributes of related entity by supplied view
 */
public class DataManagerCommit2Test {

    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    private DataManager dataManager;
    private UUID userId;
    private UUID groupId = UUID.fromString("0fa2b1a5-1d68-4d69-9fbd-dff348347f93");
    private UUID userRoleId;
    private View view;

    private Metadata metadata;
    private Persistence persistence;
    private Group group2;

    @Before
    public void setUp() throws Exception {
        metadata = cont.metadata();
        persistence = cont.persistence();

        dataManager = AppBeans.get(DataManager.class);

        try (Transaction tx = persistence.createTransaction()) {
            EntityManager em = persistence.getEntityManager();

            Group group = em.find(Group.class, groupId);
            Role role = em.find(Role.class, UUID.fromString("0c018061-b26f-4de2-a5be-dff348347f93"));

            group2 = metadata.create(Group.class);
            group2.setName("Group2-" + group2.getId());
            em.persist(group2);

            User user = metadata.create(User.class);
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
        cont.deleteRecord(group2);
    }

    @Test
    public void testViewAfterCommitNew() throws Exception {
        Group group = dataManager.load(LoadContext.create(Group.class).setId(TestSupport.COMPANY_GROUP_ID).setView(View.MINIMAL));
        assertFalse(PersistenceHelper.isLoaded(group, "createTs"));

        User user = metadata.create(User.class);
        try {
            user.setName("testUser");
            user.setLogin("login" + user.getId());
            user.setGroup(group);

            View userView = new View(User.class, true)
                    .addProperty("login")
                    .addProperty("name")
                    .addProperty("group", new View(Group.class, false)
                            .addProperty("name")
                            .addProperty("createTs"));

            User committedUser = dataManager.commit(user, userView);
            assertTrue(PersistenceHelper.isLoaded(committedUser.getGroup(), "createTs"));
        } finally {
            cont.deleteRecord(user);
        }
    }

    @Test
    public void testViewAfterCommitModified() throws Exception {
        Group group2 = dataManager.load(LoadContext.create(Group.class).setId(this.group2.getId()).setView(View.MINIMAL));
        assertFalse(PersistenceHelper.isLoaded(group2, "createTs"));

        LoadContext<User> loadContext = LoadContext.create(User.class).setId(userId).setView(view);
        User user = dataManager.load(loadContext);

        user.setName("testUser-changed");
        user.setGroup(group2);

        View userView = new View(User.class, true)
                .addProperty("login")
                .addProperty("name")
                .addProperty("group", new View(Group.class)
                        .addProperty("name")
                        .addProperty("createTs"));

        User committedUser = dataManager.commit(user, userView);
        assertTrue(PersistenceHelper.isLoaded(committedUser.getGroup(), "createTs"));
    }
}
