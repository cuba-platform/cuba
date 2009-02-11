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
        assertNotNull(view.getProperty("subjects"));

        View subjectView = view.getProperty("subjects").getView();
        assertNotNull(subjectView);
        assertNotNull(subjectView.getProperty("profile"));

        View profileView = subjectView.getProperty("profile").getView();
        assertNotNull(profileView);
        assertNotNull(profileView.getProperty("name"));

    }

}
