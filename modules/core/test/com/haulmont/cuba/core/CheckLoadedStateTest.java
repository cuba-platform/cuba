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
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.testmodel.checkview.UserRelatedNews;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CheckLoadedStateTest {
    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    private UUID userRelatedNewsId = null;
    private List<UUID> recursiveUserRelatedIds = null;

    @Test
    public void checkLocalProperties() {
        DataManager dataManager = AppBeans.get(DataManager.class);
        EntityStates entityStates = AppBeans.get(EntityStates.class);

        User user = dataManager.load(LoadContext.create(User.class)
                .setId(UUID.fromString("60885987-1b61-4247-94c7-dff348347f93"))
                .setView(View.LOCAL));

        entityStates.checkLoaded(user, "login", "name", "active");

        try {
            entityStates.checkLoaded(user, "group");

            Assert.fail("user.group is not loaded");
        } catch (IllegalArgumentException e) {
            Assert.assertThat(e.getMessage(), containsString("group is not loaded"));
        }
    }

    @Test
    public void checkMinimalProperties() {
        DataManager dataManager = AppBeans.get(DataManager.class);
        EntityStates entityStates = AppBeans.get(EntityStates.class);

        User user = dataManager.load(LoadContext.create(User.class)
                .setId(UUID.fromString("60885987-1b61-4247-94c7-dff348347f93"))
                .setView(View.MINIMAL));

        entityStates.checkLoaded(user, "login", "name");

        try {
            entityStates.checkLoaded(user, "group");

            Assert.fail("user.group is not loaded");
        } catch (IllegalArgumentException e) {
            Assert.assertThat(e.getMessage(), containsString("group is not loaded"));
        }

        try {
            entityStates.checkLoaded(user, "password");

            Assert.fail("user.password is not loaded");
        } catch (IllegalArgumentException e) {
            Assert.assertThat(e.getMessage(), containsString("password is not loaded"));
        }

        try {
            entityStates.checkLoaded(user, "email");

            Assert.fail("user.email is not loaded");
        } catch (IllegalArgumentException e) {
            Assert.assertThat(e.getMessage(), containsString("email is not loaded"));
        }
    }

    @Test
    public void checkLocalView() {
        DataManager dataManager = AppBeans.get(DataManager.class);
        EntityStates entityStates = AppBeans.get(EntityStates.class);

        User user = dataManager.load(LoadContext.create(User.class)
                .setId(UUID.fromString("60885987-1b61-4247-94c7-dff348347f93"))
                .setView(View.LOCAL));

        assertTrue(entityStates.isLoadedWithView(user, View.LOCAL));

        entityStates.checkLoadedWithView(user, View.LOCAL);

        User userMinimal = dataManager.load(LoadContext.create(User.class)
                .setId(UUID.fromString("60885987-1b61-4247-94c7-dff348347f93"))
                .setView(View.MINIMAL));

        try {
            assertFalse(entityStates.isLoadedWithView(userMinimal, View.LOCAL));

            entityStates.checkLoadedWithView(userMinimal, View.LOCAL);

            Assert.fail("user local attributes are not loaded");
        } catch (IllegalArgumentException e) {
            Assert.assertThat(e.getMessage(), containsString(" is not loaded"));
        }
    }

    @Test
    public void checkDeepView() {
        DataManager dataManager = AppBeans.get(DataManager.class);
        EntityStates entityStates = AppBeans.get(EntityStates.class);

        User user = dataManager.load(LoadContext.create(User.class)
                .setId(UUID.fromString("60885987-1b61-4247-94c7-dff348347f93"))
                .setView("user.browse"));

        try {
            assertFalse(entityStates.isLoadedWithView(user, "user.edit"));

            entityStates.checkLoadedWithView(user, "user.edit");

            Assert.fail("user edit attributes are not loaded");
        } catch (IllegalArgumentException e) {
            Assert.assertThat(e.getMessage(), containsString(" is not loaded"));
        }

        User userEdit = dataManager.load(LoadContext.create(User.class)
                .setId(UUID.fromString("60885987-1b61-4247-94c7-dff348347f93"))
                .setView("user.edit"));

        assertTrue(entityStates.isLoadedWithView(userEdit, "user.edit"));

        entityStates.checkLoadedWithView(userEdit, "user.edit");
    }

    @Test
    public void checkRelatedView() {
        DataManager dataManager = AppBeans.get(DataManager.class);
        EntityStates entityStates = AppBeans.get(EntityStates.class);

        User user = dataManager.load(LoadContext.create(User.class)
                .setId(UUID.fromString("60885987-1b61-4247-94c7-dff348347f93"))
                .setView(View.MINIMAL));

        UserRelatedNews record = cont.metadata().create(UserRelatedNews.class);

        userRelatedNewsId = record.getId();

        record.setName("Test");
        record.setUser(user);

        UserRelatedNews testRecord = dataManager.commit(record);

        View view = new View(UserRelatedNews.class, false);
        view.addProperty("userLogin");
        view.addProperty("name");

        entityStates.checkLoadedWithView(testRecord, view);

        assertTrue(entityStates.isLoadedWithView(testRecord, view));

        UserRelatedNews minimalRecord = dataManager.load(LoadContext.create(UserRelatedNews.class)
                .setId(userRelatedNewsId)
                .setView(View.MINIMAL));

        try {
            assertFalse(entityStates.isLoadedWithView(minimalRecord, view));

            entityStates.checkLoadedWithView(minimalRecord, view);

            Assert.fail("minimal record attributes are not loaded");
        } catch (IllegalArgumentException e) {
            Assert.assertThat(e.getMessage(), containsString("userLogin is not loaded"));
        }
    }

    @Test
    public void checkRecursiveView() {
        DataManager dataManager = AppBeans.get(DataManager.class);
        EntityStates entityStates = AppBeans.get(EntityStates.class);

        User user = dataManager.load(LoadContext.create(User.class)
                .setId(UUID.fromString("60885987-1b61-4247-94c7-dff348347f93"))
                .setView(View.MINIMAL));

        recursiveUserRelatedIds = new ArrayList<>();

        UserRelatedNews parentRecord = cont.metadata().create(UserRelatedNews.class);
        parentRecord.setName("Test");
        parentRecord.setUser(user);

        UserRelatedNews committedParentRecord = dataManager.commit(parentRecord);
        recursiveUserRelatedIds.add(committedParentRecord.getId());

        UserRelatedNews record = cont.metadata().create(UserRelatedNews.class);
        record.setName("Test");
        record.setUser(user);
        record.setParent(committedParentRecord);

        UserRelatedNews committedRecord = dataManager.commit(record);
        recursiveUserRelatedIds.add(committedRecord.getId());

        View view = new View(UserRelatedNews.class, false);
        view.addProperty("parent");
        view.addProperty("name");

        assertTrue(entityStates.isLoadedWithView(committedRecord, view));

        entityStates.checkLoadedWithView(committedRecord, view);

        UserRelatedNews localRecord = dataManager.load(LoadContext.create(UserRelatedNews.class)
                .setId(committedRecord.getId())
                .setView(View.LOCAL));

        try {
            assertFalse(entityStates.isLoadedWithView(localRecord, view));

            entityStates.checkLoadedWithView(localRecord, view);

            Assert.fail("local record attributes are not loaded");
        } catch (IllegalArgumentException e) {
            Assert.assertThat(e.getMessage(), containsString("parent is not loaded"));
        }
    }

    @After
    public void tearDown() {
        if (userRelatedNewsId != null) {
            cont.deleteRecord("TEST_USER_RELATED_NEWS", userRelatedNewsId);
            userRelatedNewsId = null;
        }

        if (recursiveUserRelatedIds != null) {
            Collections.reverse(recursiveUserRelatedIds);

            for (UUID id : recursiveUserRelatedIds) {
                cont.deleteRecord("TEST_USER_RELATED_NEWS", id);
            }
        }
    }
}