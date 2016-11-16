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
 */

package com.haulmont.cuba.core;

import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.ValueLoadContext;
import com.haulmont.cuba.testsupport.TestContainer;
import com.haulmont.cuba.testsupport.TestSupport;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NonEntityQueryTest {

    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    private DataManager dataManager;

    @Before
    public void setUp() throws Exception {
        dataManager = AppBeans.get(DataManager.class);
    }

    @Test
    public void testScalars() throws Exception {
        ValueLoadContext context = ValueLoadContext.create()
                .setQuery(ValueLoadContext.createQuery("select u.id, u.login from sec$User u where u.id = :id1 or u.id = :id2 order by u.login")
                    .setParameter("id1", TestSupport.ADMIN_USER_ID)
                    .setParameter("id2", TestSupport.ANONYMOUS_USER_ID))
                .addProperty("userId").addProperty("login");

        List<KeyValueEntity> list = dataManager.loadValues(context);

        assertEquals(2, list.size());
        KeyValueEntity e = list.get(0);
        assertEquals(TestSupport.ADMIN_USER_ID, e.getValue("userId"));
        assertEquals("admin", e.getValue("login"));
        e = list.get(1);
        assertEquals(TestSupport.ANONYMOUS_USER_ID, e.getValue("userId"));
        assertEquals("anonymous", e.getValue("login"));
    }

    @Test
    public void testAggregates() throws Exception {
        ValueLoadContext context = ValueLoadContext.create();
        ValueLoadContext.Query query = context.setQueryString("select count(u) from sec$User u where u.id = :id1 or u.id = :id2");
        query.setParameter("id1", TestSupport.ADMIN_USER_ID);
        query.setParameter("id2", TestSupport.ANONYMOUS_USER_ID);
        context.addProperty("count");

        List<KeyValueEntity> list = dataManager.loadValues(context);

        assertEquals(1, list.size());
        KeyValueEntity e = list.get(0);
        assertEquals(Long.valueOf(2), e.getValue("count"));
    }
}
