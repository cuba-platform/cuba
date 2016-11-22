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

package com.haulmont.cuba.gui.data.impl;

import com.haulmont.cuba.client.testsupport.CubaClientTestCase;
import com.haulmont.cuba.core.entity.BaseEntityInternalAccess;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.impl.testmodel1.TestDetailEntity;
import com.haulmont.cuba.gui.data.impl.testmodel1.TestMasterEntity;
import com.haulmont.cuba.gui.executors.BackgroundWorker;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class CollectionDatasourceTest extends CubaClientTestCase {

    @Mocked
    protected BackgroundWorker backgroundWorker;

    @Before
    public void setUp() throws Exception {
        addEntityPackage("com.haulmont.cuba");
        setupInfrastructure();

        new NonStrictExpectations() {
            {
                backgroundWorker.checkUIAccess(); result = null;
                AppBeans.get(BackgroundWorker.NAME); result = backgroundWorker;
                AppBeans.get(BackgroundWorker.class); result = backgroundWorker;
                AppBeans.get(BackgroundWorker.NAME, BackgroundWorker.class); result = backgroundWorker;
            }
        };
    }

    @Test
    public void testRemoveThenAdd() throws Exception {
        CollectionDatasourceImpl<TestMasterEntity, UUID> cds = new CollectionDatasourceImpl<>();
        cds.setMetaClass(metadata.getClassNN(TestMasterEntity.class));
        cds.setRefreshMode(CollectionDatasource.RefreshMode.NEVER);

        TestMasterEntity entity = new TestMasterEntity();
        entity.setMasterName("master");

        BaseEntityInternalAccess.setNew(entity, false);
        BaseEntityInternalAccess.setDetached(entity, true);

        cds.data.put(entity.getId(), entity);

        cds.removeItem(entity);
        cds.addItem(entity);

        assertEquals(0, cds.itemsToCreate.size());
        assertEquals(1, cds.itemsToUpdate.size());
        assertEquals(0, cds.itemsToDelete.size());
    }

    @Test
    public void testRemoveThenAddToPropertyDs() throws Exception {
        DatasourceImpl<TestMasterEntity> ds = new DatasourceImpl<>();
        ds.setMetaClass(metadata.getClassNN(TestMasterEntity.class));
        ds.initialized();

        CollectionPropertyDatasourceImpl<TestDetailEntity, UUID> cpds = new CollectionPropertyDatasourceImpl<>();
        cpds.masterDs = ds;
        cpds.metaProperty = ds.getMetaClass().getProperty("details");

        TestMasterEntity masterEntity = new TestMasterEntity();
        masterEntity.setMasterName("master");
        masterEntity.setDetails(new HashSet<>());

        ds.setItem(masterEntity);

        TestDetailEntity detailEntity = new TestDetailEntity();
        BaseEntityInternalAccess.setNew(detailEntity, false);
        BaseEntityInternalAccess.setDetached(detailEntity, true);
        detailEntity.setMaster(masterEntity);
        detailEntity.setDetailName("detail");
        masterEntity.getDetails().add(detailEntity);

        cpds.removeItem(detailEntity);
        cpds.addItem(detailEntity);

        assertEquals(0, cpds.itemsToCreate.size());
        assertEquals(1, cpds.itemsToUpdate.size());
        assertEquals(0, cpds.itemsToDelete.size());
    }
}
