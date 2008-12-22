/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 22.12.2008 12:42:07
 *
 * $Id$
 */
package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.CubaTestCase;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.security.entity.User;

import java.io.InputStream;

public class ViewRepositoryTest extends CubaTestCase
{
    private ViewRepository repository;

    protected void setUp() throws Exception {
        super.setUp();
        repository = ViewRepository.getInstance();

        InputStream stream = ViewRepositoryTest.class.getResourceAsStream("test.view.xml");
        repository.deployViews(stream);
    }

    public void testGetView() {
        View view = repository.getView(User.class, "test");
        assertNotNull(view);
        assertEquals("name", view.getProperties().get(0).getName());
        assertEquals("login", view.getProperties().get(1).getName());
        assertEquals("profiles", view.getProperties().get(2).getName());

        View profileView = view.getProperties().get(2).getView();
        assertNotNull(profileView);
        assertEquals("name", profileView.getProperties().get(0).getName());
    }

}
