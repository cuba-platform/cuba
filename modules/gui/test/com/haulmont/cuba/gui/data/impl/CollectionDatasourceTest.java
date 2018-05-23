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
import mockit.Expectations;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

public class CollectionDatasourceTest extends CubaClientTestCase {

    @Mocked
    protected BackgroundWorker backgroundWorker;

    @Before
    public void setUp() throws Exception {
        addEntityPackage("com.haulmont.cuba");
        setupInfrastructure();

        new Expectations() {
            {
                backgroundWorker.checkUIAccess(); result = null; minTimes = 0;
                AppBeans.get(BackgroundWorker.NAME); result = backgroundWorker; minTimes = 0;
                AppBeans.get(BackgroundWorker.class); result = backgroundWorker; minTimes = 0;
                AppBeans.get(BackgroundWorker.NAME, BackgroundWorker.class); result = backgroundWorker; minTimes = 0;
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

    @Test
    @SuppressWarnings("IncorrectCreateEntity")
    public void testSuspendListeners() {
        ArrayList<TestMasterEntity> itemsToRemove = new ArrayList<>(2);
        itemsToRemove.add(new TestMasterEntity());
        itemsToRemove.add(new TestMasterEntity());

        ArrayList<TestMasterEntity> itemsToAdd = new ArrayList<>(3);
        itemsToAdd.add(new TestMasterEntity());
        itemsToAdd.add(new TestMasterEntity());
        itemsToAdd.add(new TestMasterEntity());

        CollectionDatasourceImpl<TestMasterEntity, UUID> cds = new CollectionDatasourceImpl<>();
        cds.setMetaClass(metadata.getClassNN(TestMasterEntity.class));
        cds.setRefreshMode(CollectionDatasource.RefreshMode.NEVER);
        cds.valid();

        TestMasterEntity entity = new TestMasterEntity();
        cds.data.put(entity.getId(), entity);
        itemsToRemove.forEach(testMasterEntity -> cds.data.put(testMasterEntity.getId(), testMasterEntity));

        ArrayList<CollectionDatasource.Operation> operations = new ArrayList<>(2);
        ArrayList<TestMasterEntity> removedItems = new ArrayList<>();
        ArrayList<TestMasterEntity> addedItems = new ArrayList<>();

        cds.addCollectionChangeListener(e -> {
            assertFalse("CollectionChange listener worked, when they they are suspended", cds.listenersSuspended);
            if (CollectionDatasource.Operation.ADD.equals(e.getOperation())) {
                addedItems.clear();
                addedItems.addAll(e.getItems());
            } else if (CollectionDatasource.Operation.REMOVE.equals(e.getOperation())) {
                removedItems.clear();
                removedItems.addAll(e.getItems());
            }
            operations.add(e.getOperation());
        });

        cds.suspendListeners();
        itemsToRemove.forEach(cds::removeItem);
        itemsToAdd.forEach(cds::addItem);
        cds.resumeListeners();

        assertEquals(2, operations.size());
        assertTrue("Not right order of operations", operations.get(0).equals(CollectionDatasource.Operation.REMOVE)
                && operations.get(1).equals(CollectionDatasource.Operation.ADD));
        assertTrue("Not all removed items passed on resume", removedItems.containsAll(itemsToRemove));
        assertTrue("Not all added items passed on resume", addedItems.containsAll(itemsToAdd));
    }
}
