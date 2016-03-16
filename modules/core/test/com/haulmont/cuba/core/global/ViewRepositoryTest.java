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
package com.haulmont.cuba.core.global;

import com.haulmont.cuba.core.sys.AbstractViewRepository;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ViewRepositoryTest {

    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    private ViewRepository repository;

    @Before
    public void setUp() throws Exception {
        repository = cont.metadata().getViewRepository();

        InputStream stream = ViewRepositoryTest.class.getResourceAsStream("test.view.xml");
        ((AbstractViewRepository) repository).deployViews(stream);
    }

    @Test
    public void testGetView() {
        View view = repository.getView(User.class, "test");
        assertNotNull(view);
        assertNotNull(view.getProperty("name"));
        assertNotNull(view.getProperty("login"));
        assertNotNull(view.getProperty("userRoles"));

        View userRolesView = view.getProperty("userRoles").getView();
        assertNotNull(userRolesView);
        assertNotNull(userRolesView.getProperty("role"));

        View roleView = userRolesView.getProperty("role").getView();
        assertNotNull(roleView);
        assertNotNull(roleView.getProperty("name"));
    }

    @Test
    public void testDefaultViews() {
        View localView = repository.getView(User.class, View.LOCAL);
        assertNotNull(localView);
        assertNotNull(localView.getProperty("name"));
        assertNotNull(localView.getProperty("login"));
        assertNotNull(localView.getProperty("email"));
        assertNull(localView.getProperty("userRoles"));

        View minView = repository.getView(User.class, View.MINIMAL);
        assertNotNull(minView);
        assertNotNull(minView.getProperty("name"));
        assertNotNull(minView.getProperty("login"));
        assertNull(minView.getProperty("email"));
        assertNull(minView.getProperty("userRoles"));
    }

    @Test
    public void testInheritance() {
        View view = repository.getView(User.class, "testInheritance");
        assertNotNull(view);
        assertNotNull(view.getProperty("name"));
        assertNotNull(view.getProperty("login"));
        assertNotNull(view.getProperty("userRoles"));
        assertNull(view.getProperty("substitutions"));
    }

    @Test
    public void testAnonymous() {
        View view = repository.getView(User.class, "anonymousTest");
        assertNotNull(view);
        assertNotNull(view.getProperty("group"));
        assertNotNull(view.getProperty("group").getView());
        assertNull(view.getProperty("name"));

        View groupView = view.getProperty("group").getView();
        assertNotNull(groupView.getProperty("constraints"));
        assertNotNull(groupView.getProperty("name"));
        assertNull(groupView.getProperty("hierarchyList"));

        assertNotNull(groupView.getProperty("constraints").getView());
    }
}