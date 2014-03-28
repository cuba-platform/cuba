/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.global;

import com.haulmont.cuba.core.CubaTestCase;
import com.haulmont.cuba.core.sys.AbstractViewRepository;
import com.haulmont.cuba.security.entity.UserRole;

import java.io.InputStream;

/**
 * @author artamonov
 * @version $Id$
 */
public class ViewRepositoryOverwriteTest extends CubaTestCase {

    private ViewRepository repository;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        repository = metadata.getViewRepository();

        InputStream stream = ViewRepositoryTest.class.getResourceAsStream("test-overwrite.view.xml");
        ((AbstractViewRepository) repository).deployViews(stream);
    }

    public void testDependentUpdated() {
        View dependentView = repository.getView(UserRole.class, "dependent");
        View userView = dependentView.getProperty("user").getView();

        ViewProperty groupProperty = userView.getProperty("group");
        assertNotNull(groupProperty);
        assertEquals(groupProperty.getView().getName(), "_local");
    }
}