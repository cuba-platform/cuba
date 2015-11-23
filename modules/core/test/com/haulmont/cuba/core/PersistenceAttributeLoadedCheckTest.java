/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core;

import com.haulmont.cuba.core.entity.ScheduledTask;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.entity.UserRole;
import com.haulmont.cuba.testsupport.TestSupport;
import org.junit.Assert;

import java.util.UUID;

/**
 * @author degtyarjov
 * @version $Id$
 */
public class PersistenceAttributeLoadedCheckTest extends CubaTestCase {

    private DataManager dataManager;
    private UUID taskId;
    private UUID userId;
    private UUID groupId = UUID.fromString("0fa2b1a5-1d68-4d69-9fbd-dff348347f93");
    private View taskView;
    private View userView;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        dataManager = AppBeans.get(DataManager.class);

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

    @Override
    protected void tearDown() throws Exception {
        deleteRecord("SEC_USER", userId);
        deleteRecord("SYS_SCHEDULED_TASK", taskId);
        super.tearDown();
    }

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
}
