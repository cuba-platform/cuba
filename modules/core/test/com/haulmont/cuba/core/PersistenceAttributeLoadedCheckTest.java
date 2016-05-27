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

import com.haulmont.cuba.core.entity.ScheduledTask;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.UserRole;
import com.haulmont.cuba.testsupport.TestContainer;
import com.haulmont.cuba.testsupport.TestSupport;
import org.junit.*;

import java.util.UUID;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class PersistenceAttributeLoadedCheckTest {
    @ClassRule
    public static final TestContainer cont = TestContainer.Common.INSTANCE;

    private DataManager dataManager;
    private Persistence persistence;
    private UUID taskId;
    private UUID userId;
    private UUID groupId = UUID.fromString("0fa2b1a5-1d68-4d69-9fbd-dff348347f93");
    private View taskView;
    private View userView;

    @Before
    public void setUp() throws Exception {
        dataManager = AppBeans.get(DataManager.class);
        persistence = AppBeans.get(Persistence.class);

        try (Transaction tx = persistence.createTransaction()) {
            EntityManager em = persistence.getEntityManager();

            ScheduledTask task = new ScheduledTask();
            task.setBeanName("BeanName");
            task.setMethodName("MethodName");
            taskId = task.getId();

            User user = new User();
            userId = user.getId();
            user.setName("testUser");
            user.setLogin("login" + userId);
            user.setPassword("000");
            user.setGroup(em.find(Group.class, groupId));
            em.persist(user);

            em.persist(task);
            em.persist(user);
            tx.commit();
        }

        taskView = new View(ScheduledTask.class, true)
                .addProperty("beanName");

        userView = new View(User.class, true)
                .addProperty("login")
                .addProperty("loginLowerCase")
                .addProperty("name")
                .addProperty("password")
                .addProperty("group", new View(Group.class).addProperty("name"))
                .addProperty("userRoles", new View(UserRole.class));
    }

    @After
    public void tearDown() throws Exception {
        cont.deleteRecord("SEC_USER", userId);
        cont.deleteRecord("SYS_SCHEDULED_TASK", taskId);
    }

    @Test
    public void testIsLoadedLogic() throws Exception {
        LoadContext<User> userContext = LoadContext.create(User.class).setId(userId).setView(userView);
        LoadContext<ScheduledTask> taskContext = LoadContext.create(ScheduledTask.class).setId(taskId).setView(taskView);
        User user = dataManager.load(userContext);
        ScheduledTask task = dataManager.load(taskContext);

        assertNotNull(user);
        assertNotNull(task);

        assertTrue(PersistenceHelper.isLoaded(user, "group"));//if attribute is in the view - it should be loaded
        assertTrue(PersistenceHelper.isLoaded(user, "userRoles"));//if attribute is in the view - it should be loaded
        assertTrue(!PersistenceHelper.isLoaded(user, "substitutions"));//if attribute is not in the view - it should not be loaded
        try {
            PersistenceHelper.isLoaded(user, "notExistingAttribute");
            Assert.fail("Should throw an exception for not existing attribute");
        } catch (Exception ignored) {
        }

        assertTrue(PersistenceHelper.isLoaded(task, "beanName"));//if attribute is in the view - it should be loaded
        assertTrue(!PersistenceHelper.isLoaded(task, "methodName"));//if attribute is not in the view - it should not be loaded
        assertTrue(PersistenceHelper.isLoaded(task, "methodParametersString"));//meta properties should be marked as loaded

        user = TestSupport.reserialize(user);
        task = TestSupport.reserialize(task);

        assertTrue(PersistenceHelper.isLoaded(user, "group"));//if attribute is in the view - it should be loaded
        assertTrue(PersistenceHelper.isLoaded(user, "userRoles"));//if attribute is in the view - it should be loaded
        assertTrue(!PersistenceHelper.isLoaded(user, "substitutions"));//if attribute is not in the view - it should not be loaded
        try {
            PersistenceHelper.isLoaded(user, "notExistingAttribute");
            Assert.fail("Should throw an exception for not existing attribute");
        } catch (Exception ignored) {
        }

        assertTrue(PersistenceHelper.isLoaded(task, "beanName"));//if attribute is in the view - it should be loaded
        assertTrue(!PersistenceHelper.isLoaded(task, "methodName"));//if attribute is not in the view - it should not be loaded
        assertTrue(PersistenceHelper.isLoaded(task, "methodParametersString"));//meta properties should be marked as loaded
    }

    @Test
    public void testManagedInstance() throws Exception {
        try (Transaction tx = cont.persistence().createTransaction()) {
            User user = cont.entityManager().find(User.class, userId);

            assertTrue(PersistenceHelper.isLoaded(user, "name"));
            assertFalse(PersistenceHelper.isLoaded(user, "group"));

            tx.commit();
        }
    }
}