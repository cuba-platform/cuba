/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.security.entity.User;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * @author artamonov
 * @version $Id$
 */
public class HsqlLikeNullFailTest extends CubaTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        User user = new User();
        user.setId(UUID.fromString("de0f39d2-e60a-11e1-9b55-3860770d7eaf"));
        user.setName("Test");
        user.setLogin("tEst");
        user.setLoginLowerCase("test");

        DataManager dataManager = AppBeans.get(DataManager.NAME);
        dataManager.commit(new CommitContext(Collections.<Entity>singleton(user)));
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        deleteRecord("SEC_USER", UUID.fromString("de0f39d2-e60a-11e1-9b55-3860770d7eaf"));
    }

    public void testLoadListCaseInsensitive() {
        LoadContext<User> loadContext = LoadContext.create(User.class);
        loadContext.setQueryString("select u from sec$User u where u.name like :custom_searchString or u.login like :custom_searchString")
                .setParameter("custom_searchString", null);

        DataManager dataManager = AppBeans.get(DataManager.NAME);
        List<User> list = dataManager.loadList(loadContext);
        assertEquals(1, list.size());
    }
}