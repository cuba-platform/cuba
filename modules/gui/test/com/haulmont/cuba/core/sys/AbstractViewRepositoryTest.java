/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.client.testsupport.CubaClientTestCase;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.data.impl.testmodel1.TestMasterEntity;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertFalse;

/**
 * @author gorelov
 * @version $Id$
 */
public class AbstractViewRepositoryTest extends CubaClientTestCase {

    private MetaClass testMasterEntity;

    @Before
    public void setUp() {
        addEntityPackage("com.haulmont.cuba.gui.data.impl.testmodel1");
        setViewConfig("/com/haulmont/cuba/gui/data/impl/testmodel1/test-views.xml");
        setupInfrastructure();

        testMasterEntity = metadata.getClassNN(TestMasterEntity.class);
    }

    @Test
    public void getView() {
        assertNotNull(metadata.getViewRepository().getView(testMasterEntity, View.LOCAL));
        assertNotNull(metadata.getViewRepository().getView(testMasterEntity, View.MINIMAL));
        assertNotNull(metadata.getViewRepository().getView(testMasterEntity, "withDetails"));
        assertNotNull(metadata.getViewRepository().getView(testMasterEntity, "withDetail"));
    }

    @Test
    public void getViewNames() {
        Collection<String> views = metadata.getViewRepository().getViewNames(testMasterEntity);
        assertFalse(views.contains(View.LOCAL));
        assertFalse(views.contains(View.MINIMAL));

        assertNotNull(metadata.getViewRepository().getView(testMasterEntity, View.LOCAL));
        assertNotNull(metadata.getViewRepository().getView(testMasterEntity, View.MINIMAL));
    }
}
