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

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.security.entity.Group;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

/**
 */
@SuppressWarnings("IncorrectCreateEntity")
public class HsqlLikeNullFailTest {

    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    @Before
    public void setUp() throws Exception {
        DataManager dataManager = AppBeans.get(DataManager.NAME);

        Group group = dataManager.load(new LoadContext<>(Group.class)
                .setId(UUID.fromString("0fa2b1a5-1d68-4d69-9fbd-dff348347f93")));

        User user = new User();
        user.setGroup(group);
        user.setId(UUID.fromString("de0f39d2-e60a-11e1-9b55-3860770d7eaf"));
        user.setName("Test");
        user.setLogin("tEst");
        user.setLoginLowerCase("test");

        dataManager.commit(user);
    }

    @After
    public void tearDown() throws Exception {
        cont.deleteRecord("SEC_USER", UUID.fromString("de0f39d2-e60a-11e1-9b55-3860770d7eaf"));
    }

    @Test
    public void testLoadListCaseInsensitive() {
        LoadContext<User> loadContext = LoadContext.create(User.class);
        loadContext.setQueryString("select u from sec$User u " +
                "where u.name like :custom_searchString or u.login like :custom_searchString")
                .setParameter("custom_searchString", null);

        DataManager dataManager = AppBeans.get(DataManager.NAME);
        List<User> list = dataManager.loadList(loadContext);
        assertEquals(0, list.size());
    }
}