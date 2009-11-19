/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 22.12.2008 12:42:07
 *
 * $Id$
 */
package com.haulmont.cuba.core.global;

import com.haulmont.cuba.core.CubaTestCase;
import com.haulmont.cuba.security.entity.User;

import java.io.InputStream;

public class ViewRepositoryTest extends CubaTestCase
{
    private ViewRepository repository;

    protected void setUp() throws Exception {
        super.setUp();
        repository = MetadataProvider.getViewRepository();

        InputStream stream = ViewRepositoryTest.class.getResourceAsStream("test.view.xml");
        repository.deployViews(stream);
    }

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

    public void testInheritance() {
        View view = repository.getView(User.class, "testInheritance");
        assertNotNull(view);
        assertNotNull(view.getProperty("name"));
        assertNotNull(view.getProperty("login"));
        assertNotNull(view.getProperty("userRoles"));
        assertNull(view.getProperty("substitutions"));
        assertNull(view.getProperty("defaultSubstitutedUser"));
    }
}
