/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.global;

import com.haulmont.cuba.core.sys.AbstractViewRepository;
import com.haulmont.cuba.security.entity.UserRole;
import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author artamonov
 * @version $Id$
 */
public class ViewRepositoryOverwriteTest {

    @ClassRule
    public static TestContainer cont = TestContainer.Common.INSTANCE;

    private ViewRepository repository;

    @Before
    public void setUp() throws Exception {
        repository = cont.metadata().getViewRepository();

        InputStream stream = ViewRepositoryTest.class.getResourceAsStream("test-overwrite.view.xml");
        ((AbstractViewRepository) repository).deployViews(stream);
    }

    @Test
    public void testDependentUpdated() {
        View dependentView = repository.getView(UserRole.class, "dependent");
        View userView = dependentView.getProperty("user").getView();

        ViewProperty groupProperty = userView.getProperty("group");
        assertNotNull(groupProperty);
        assertEquals(groupProperty.getView().getName(), "_local");
    }
}