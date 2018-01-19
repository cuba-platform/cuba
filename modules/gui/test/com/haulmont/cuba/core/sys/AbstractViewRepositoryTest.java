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