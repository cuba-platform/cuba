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

package com.haulmont.cuba.gui.data.impl;

import com.haulmont.chile.core.model.Session;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.client.testsupport.CubaClientTestCase;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.PersistenceHelper;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsBuilder;
import com.haulmont.cuba.gui.data.impl.testmodel1.TestDetailEntity;
import com.haulmont.cuba.gui.data.impl.testmodel1.TestEmbeddableEntity;
import com.haulmont.cuba.gui.data.impl.testmodel1.TestMasterEntity;
import com.haulmont.cuba.gui.data.impl.testmodel1.TestPartEntity;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * <pre>
 * masterDsContext
 *      masterDs
 *      detailsDs
 *      masterPartsDs
 * detailDsContext
 *      detailDs (parent=detailsDs)
 *      embeddableDs
 *      partsDs (parent=masterPartsDs)
 * partDsContext
 *      partDs (parent=partsDs)
 * </pre>
 *
 *
 */
public class DsContextTest extends CubaClientTestCase {

    private Session metadataSession;
    private TestDataSupplier dataService;

    private DsContextImplementation masterDsContext;
    private DsContextImplementation detailDsContext;
    private DsContextImplementation partDsContext;

    private TestMasterEntity master;
    private TestDetailEntity detail1;
    private TestEmbeddableEntity embeddable1;
    private TestPartEntity part1;
    private TestDetailEntity detail2;
    private Datasource<TestMasterEntity> masterDs;
    private CollectionDatasource<TestDetailEntity,UUID> detailsDs;
    private CollectionDatasource<TestPartEntity,UUID> masterPartsDs;
    private Datasource<TestDetailEntity> detailDs;
    private Datasource<TestEmbeddableEntity> embeddableDs;
    private CollectionDatasource<TestPartEntity,UUID> partsDs;
    private Datasource<TestPartEntity> partDs;

    @Mocked ClientConfig clientConfig;
    @Mocked PersistenceHelper persistenceHelper;

    @Before
    public void setUp() throws Exception {
        addEntityPackage("com.haulmont.cuba");
        setViewConfig("/com/haulmont/cuba/gui/data/impl/testmodel1/test-views.xml");
        setupInfrastructure();

        metadataSession = metadata.getSession();
        dataService = new TestDataSupplier();

        dataService.commitCount = 0;

        new NonStrictExpectations() {
            {
                configuration.getConfig(ClientConfig.class); result = clientConfig;

                clientConfig.getCollectionDatasourceDbSortEnabled(); result = true;

                persistenceManager.getMaxFetchUI(anyString); result = 10000;

                PersistenceHelper.isNew(any); result = false;
            }
        };
    }

    @Test
    public void testAggregation() {
        createEntities();

        // Master editor
        createMasterDsContext();

        masterDs.setItem(master);

        assertEquals(detail1, detailsDs.getItem(detail1.getId()));

        // Select Detail to edit
        detailsDs.setItem(detail1);

        // Detail editor
        createDetailDsContext();
        setupParentDs(detailsDs, detailDs, detail1.getId());
        assertEquals(masterPartsDs, ((DatasourceImplementation) partsDs).getParent());
        assertNull(((DatasourceImplementation) embeddableDs).getParent());

        assertEquals(embeddable1.getId(), embeddableDs.getItem().getId());

        // Edit embeddable
        embeddableDs.getItem().setName("embeddable1_1");

        // Edit item in CollectionPropertyDatasource
        partsDs.getItem(part1.getId()).setPartName("part1_1");

        // Commit Detail editor
        dataService.commitValidator = null;
        detailDsContext.commit();
        assertEquals("Commit Detail DsContext", 0, dataService.commitCount);

        // Commit Master editor
        dataService.commitValidator = new TestDataSupplier.CommitValidator() {
            @Override
            public void validate(CommitContext context) {
                assertTrue(containsEntityInstance(context.getCommitInstances(), detail1.getId()));
                assertTrue(containsEntityInstance(context.getCommitInstances(), part1.getId()));
                for (Entity entity : context.getCommitInstances()) {
                    if (entity.getId().equals(detail1.getId()))
                        assertEquals("embeddable1_1", ((TestDetailEntity) entity).getEmbeddable().getName());
                    if (entity.getId().equals(part1.getId()))
                        assertEquals("part1_1", ((TestPartEntity) entity).getPartName());
                }
            }
        };
        masterDsContext.commit();
        assertEquals("Commit Master DsContext", 1, dataService.commitCount);
    }

    @Test
    public void testAggregationNewEntity() {
        // Master editor
        createMasterDsContext();

        final TestMasterEntity master = new TestMasterEntity();
        master.setMasterName("new_master");
        masterDs.setItem(master);

        final TestDetailEntity detail = new TestDetailEntity();
        detail.setMaster(master);

        createDetailDsContext();
        setupParentDs(detailsDs, detailDs, detail);

        detailDs.getItem().setDetailName("new_detail");

        dataService.commitValidator = null;
        detailDsContext.commit();
        assertEquals("Commit Detail DsContext", 0, dataService.commitCount);

        // Commit Master editor
        dataService.commitValidator = new TestDataSupplier.CommitValidator() {
            @Override
            public void validate(CommitContext context) {
                assertTrue(containsEntityInstance(context.getCommitInstances(), detail.getId()));
                assertTrue(containsEntityInstance(context.getCommitInstances(), master.getId()));
                for (Entity entity : context.getCommitInstances()) {
                    if (entity.getId().equals(detail.getId()))
                        assertEquals("new_detail", ((TestDetailEntity) entity).getDetailName());
                }
            }
        };
        masterDsContext.commit();
        assertEquals("Commit Master DsContext", 1, dataService.commitCount);
    }

    @Test
    public void testNestedAggregationNewEntity() {
        new NonStrictExpectations() {
            @Mocked PersistenceHelper persistenceHelper;
            {
                PersistenceHelper.isNew(any); result = true;
            }
        };

        // Master editor
        createMasterDsContext();

        final TestMasterEntity master = new TestMasterEntity();
        master.setMasterName("new_master");
        masterDs.setItem(master);

        // Detail editor
        final TestDetailEntity detail = new TestDetailEntity();
        detail.setMaster(master);

        createDetailDsContext();
        setupParentDs(detailsDs, detailDs, detail);

        detailDs.getItem().setDetailName("new_detail");

        // Part editor
        final TestPartEntity part = new TestPartEntity();
        part.setDetail(detail);

        createPartDsContext();
        setupParentDs(partsDs, partDs, part);

        // Edit item in Datasource
        partDs.getItem().setPartName("new_part");

        // Commit Part editor
        dataService.commitValidator = null;
        partDsContext.commit();
        assertEquals("Commit Part", 0, dataService.commitCount);

        dataService.commitValidator = null;
        detailDsContext.commit();
        assertEquals("Commit Detail DsContext", 0, dataService.commitCount);

        // Commit Master editor
        dataService.commitValidator = new TestDataSupplier.CommitValidator() {
            @Override
            public void validate(CommitContext context) {
                assertTrue(containsEntityInstance(context.getCommitInstances(), detail.getId()));
                assertTrue(containsEntityInstance(context.getCommitInstances(), master.getId()));
                assertTrue(containsEntityInstance(context.getCommitInstances(), part.getId()));
                for (Entity entity : context.getCommitInstances()) {
                    if (entity.getId().equals(detail.getId()))
                        assertEquals("new_detail", ((TestDetailEntity) entity).getDetailName());
                    if (entity.getId().equals(part.getId()))
                        assertEquals("new_part", ((TestPartEntity) entity).getPartName());
                }
            }
        };
        masterDsContext.commit();
        assertEquals("Commit Master DsContext", 1, dataService.commitCount);
    }

    @Test
    public void testAggregationRepeatEdit() {
        createEntities();

        // Master editor
        createMasterDsContext();

        masterDs.setItem(master);

        assertEquals(detail1, detailsDs.getItem(detail1.getId()));

        // Select Detail to edit
        detailsDs.setItem(detail1);

        // Detail editor
        createDetailDsContext();
        setupParentDs(detailsDs, detailDs, detail1.getId());
        assertEquals(masterPartsDs, ((DatasourceImplementation) partsDs).getParent());
        assertNull(((DatasourceImplementation) embeddableDs).getParent());

        assertEquals(embeddable1.getId(), embeddableDs.getItem().getId());

        // Edit embeddable
        embeddableDs.getItem().setName("embeddable1_1");

        // Edit item in CollectionPropertyDatasource
        partsDs.getItem(part1.getId()).setPartName("part1_1");

        // Commit Detail editor
        dataService.commitValidator = null;
        detailDsContext.commit();
        assertEquals("Commit Detail DsContext", 0, dataService.commitCount);

        // Select Detail to edit
        detailsDs.setItem(detail2);
        detailsDs.setItem(detail1);

        // Detail editor 2nd time
        createDetailDsContext();
        setupParentDs(detailsDs, detailDs, detail1.getId());

        assertEquals("embeddable1_1", embeddableDs.getItem().getName());
        assertEquals("part1_1", partsDs.getItem(part1.getId()).getPartName());

        // Edit item in CollectionPropertyDatasource 2nd time
        partsDs.getItem(part1.getId()).setPartName("part1_2");

        // Commit Detail editor 2nd time
        dataService.commitValidator = null;
        detailDsContext.commit();
        assertEquals("Commit Detail DsContext", 0, dataService.commitCount);

        // Commit Master editor
        dataService.commitValidator = new TestDataSupplier.CommitValidator() {
            @Override
            public void validate(CommitContext context) {
                assertTrue(containsEntityInstance(context.getCommitInstances(), detail1.getId()));
                assertTrue(containsEntityInstance(context.getCommitInstances(), part1.getId()));
                for (Entity entity : context.getCommitInstances()) {
                    if (entity.getId().equals(detail1.getId()))
                        assertEquals("embeddable1_1", ((TestDetailEntity) entity).getEmbeddable().getName());
                    if (entity.getId().equals(part1.getId()))
                        assertEquals("part1_2", ((TestPartEntity) entity).getPartName());
                }
            }
        };
        masterDsContext.commit();
        assertEquals("Commit Master DsContext", 1, dataService.commitCount);
    }

    @Test
    public void testNestedAggregation() {
        createEntities();

        // Master editor
        createMasterDsContext();

        masterDs.setItem(master);

        assertEquals(detail1, detailsDs.getItem(detail1.getId()));

        // Select Detail to edit
        detailsDs.setItem(detail1);

        // Detail editor
        createDetailDsContext();
        setupParentDs(detailsDs, detailDs, detail1.getId());

        assertEquals(embeddable1.getId(), embeddableDs.getItem().getId());

        // Select Part to edit
        partsDs.setItem(part1);

        // Part editor
        createPartDsContext();
        setupParentDs(partsDs, partDs, part1.getId());

        // Edit item in Datasource
        partDs.getItem().setPartName("part1_1");

        // Commit Part editor
        dataService.commitValidator = null;
        partDsContext.commit();
        assertEquals("Commit Part", 0, dataService.commitCount);

        // Commit Detail editor
        dataService.commitValidator = null;
        detailDsContext.commit();
        assertEquals("Commit Detail", 0, dataService.commitCount);

        // Commit Master editor
        dataService.commitValidator = new TestDataSupplier.CommitValidator() {
            @Override
            public void validate(CommitContext context) {
                assertTrue(containsEntityInstance(context.getCommitInstances(), part1.getId()));
                for (Entity entity : context.getCommitInstances()) {
                    if (entity.getId().equals(part1.getId()))
                        assertEquals("part1_1", ((TestPartEntity) entity).getPartName());
                }
            }
        };
        masterDsContext.commit();
        assertEquals("Commits Master", 1, dataService.commitCount);
    }

    @Test
    public void testNestedAggregationRepeatEdit() {
        createEntities();

        // Master editor
        createMasterDsContext();

        masterDs.setItem(master);

        assertEquals(detail1, detailsDs.getItem(detail1.getId()));

        // Select Detail to edit
        detailsDs.setItem(detail1);

        // Detail editor
        createDetailDsContext();
        setupParentDs(detailsDs, detailDs, detail1.getId());

        assertEquals(embeddable1.getId(), embeddableDs.getItem().getId());

        // Select Part to edit
        partsDs.setItem(part1);

        // Part editor
        createPartDsContext();
        setupParentDs(partsDs, partDs, part1.getId());

        // Edit item in Datasource
        partDs.getItem().setPartName("part1_1");

        // Commit Part editor
        dataService.commitValidator = null;
        partDsContext.commit();
        assertEquals("Commit Part", 0, dataService.commitCount);

        // Select Part to edit
        partsDs.setItem(null);
        partsDs.setItem(part1);

        // Part editor 2nd time
        createPartDsContext();
        setupParentDs(partsDs, partDs, part1.getId());

        assertEquals("part1_1", partDs.getItem().getPartName());

        // Edit item in Datasource 2nd time
        partDs.getItem().setPartName("part1_2");

        // Commit Part editor 2nd time
        dataService.commitValidator = null;
        partDsContext.commit();
        assertEquals("Commit Part", 0, dataService.commitCount);

        // Commit Detail editor
        dataService.commitValidator = null;
        detailDsContext.commit();
        assertEquals("Commit Detail", 0, dataService.commitCount);

        // Select Detail to edit
        detailsDs.setItem(detail2);
        detailsDs.setItem(detail1);

        // Detail editor 2nd time
        createDetailDsContext();
        setupParentDs(detailsDs, detailDs, detail1.getId());

        // Select Part to edit
        partsDs.setItem(part1);

        // Part editor 3rd time
        createPartDsContext();
        setupParentDs(partsDs, partDs, part1.getId());

        assertEquals("part1_2", partDs.getItem().getPartName());

        // Edit item in Datasource 3rd time
        partDs.getItem().setPartName("part1_3");

        // Commit Part editor 3rd time
        dataService.commitValidator = null;
        partDsContext.commit();
        assertEquals("Commit Part", 0, dataService.commitCount);

        // Add new part
        final TestPartEntity newPart = new TestPartEntity();
        newPart.setDetail(detail1);
        newPart.setPartName("new_part");

        createPartDsContext();
        setupParentDs(partsDs, partDs, newPart);

        // Edit item in Datasource
        partDs.getItem().setPartName("new_part");

        // Commit Part editor 4th time
        dataService.commitValidator = null;
        partDsContext.commit();
        assertEquals("Commit Part", 0, dataService.commitCount);

        // Commit Detail editor 2nd
        dataService.commitValidator = null;
        detailDsContext.commit();
        assertEquals("Commit Detail", 0, dataService.commitCount);

        // Commit Master editor
        dataService.commitValidator = new TestDataSupplier.CommitValidator() {
            @Override
            public void validate(CommitContext context) {
                assertTrue(containsEntityInstance(context.getCommitInstances(), part1.getId()));
                for (Entity entity : context.getCommitInstances()) {
                    if (entity.getId().equals(part1.getId())) {
                        assertEquals("part1_3", ((TestPartEntity) entity).getPartName());
                        assertTrue(((TestPartEntity) entity).getDetail() == detail1);
                    }
                    if (entity.getId().equals(newPart.getId())) {
                        assertEquals("new_part", ((TestPartEntity) entity).getPartName());
                        assertTrue(((TestPartEntity) entity).getDetail() == detail1);
                    }
                }
            }
        };
        masterDsContext.commit();
        assertEquals("Commits Master", 1, dataService.commitCount);
    }

    private void setupParentDs(CollectionDatasource parent, Datasource child, Entity<UUID> item) {
        ((DatasourceImplementation) child).setParent(parent);

        Entity itemCopy = metadata.getTools().copy(item);
        child.setItem(itemCopy);
        ((DatasourceImplementation) child).setModified(false);
    }

    private void setupParentDs(CollectionDatasource parent, Datasource child, UUID itemId) {
        // Equal to EditorWindowDelegate.setParentDs(), EditorWindowDelegate.setItem()
//        ((DatasourceImplementation) child).setCommitMode(Datasource.CommitMode.PARENT);
        ((DatasourceImplementation) child).setParent(parent);

//        // Iterate through all datasources in the same DsContext
//        for (Datasource sibling : child.getDsContext().getAll()) {
//            // If the datasource is a property datasource of the Child
//            if (sibling instanceof NestedDatasource && ((NestedDatasource) sibling).getMaster().equals(child)) {
//                // Look for corresponding property datasource in the Parent's DsContext
//                for (Datasource siblingOfParent : parent.getDsContext().getAll()) {
//                    if (siblingOfParent instanceof NestedDatasource &&
//                            ((NestedDatasource) siblingOfParent).getProperty().equals(((NestedDatasource) sibling).getProperty())) {
//                        // If such corresponding datasource found, set it as a parent for our property datasource
//                        ((DatasourceImplementation) sibling).setParent(siblingOfParent);
//                    }
//                }
//            }
//        }

        Entity item = metadata.getTools().copy(parent.getItem(itemId));
        child.setItem(item);
        ((DatasourceImplementation) child).setModified(false);
    }

    private void createEntities() {
        master = new TestMasterEntity();
        master.setMasterName("master");
        master.setDetails(new HashSet<TestDetailEntity>());

        detail1 = new TestDetailEntity();
        detail1.setDetailName("detail1");
        detail1.setMaster(master);
        detail1.setParts(new HashSet<TestPartEntity>());

        embeddable1 = new TestEmbeddableEntity();
        embeddable1.setName("embeddable1");
        detail1.setEmbeddable(embeddable1);
        detail1.setMaster(master);

        part1 = new TestPartEntity();
        part1.setPartName("part1");
        part1.setDetail(detail1);
        detail1.getParts().add(part1);

        master.getDetails().add(detail1);

        detail2 = new TestDetailEntity();
        detail2.setDetailName("detail2");
        detail2.setMaster(master);
        master.getDetails().add(detail2);
    }

    private void createMasterDsContext() {
        masterDsContext = new DsContextImpl(dataService);

        DsBuilder masterDsBuilder = new DsBuilder(masterDsContext);

        masterDsBuilder.reset().setId("masterDs")
                .setMetaClass(metadataSession.getClass(TestMasterEntity.class))
                .setViewName("withDetails");
        masterDs = masterDsBuilder.buildDatasource();

        masterDsBuilder.reset().setId("detailsDs")
                .setMetaClass(metadataSession.getClass(TestDetailEntity.class))
                .setMaster(masterDs)
                .setProperty("details");
        detailsDs = masterDsBuilder.buildCollectionDatasource();

        masterDsBuilder.reset().setId("masterPartsDs")
                .setMetaClass(metadataSession.getClass(TestDetailEntity.class))
                .setMaster(detailsDs)
                .setProperty("parts");
        masterPartsDs = masterDsBuilder.buildCollectionDatasource();

        for (Datasource ds : masterDsContext.getAll()) {
            ((DatasourceImplementation) ds).initialized();
        }
    }

    private void createDetailDsContext() {
        detailDsContext = new DsContextImpl(dataService);

        DsBuilder detailDsBuilder = new DsBuilder(detailDsContext);

        detailDsBuilder.reset().setId("detailDs")
                .setMetaClass(metadataSession.getClass(TestDetailEntity.class))
                .setViewName("_local");
        detailDs = detailDsBuilder.buildDatasource();

        detailDsBuilder.reset().setId("embeddedDs")
                .setMetaClass(metadataSession.getClass(TestEmbeddableEntity.class))
                .setMaster(detailDs)
                .setProperty("embeddable");
        embeddableDs = detailDsBuilder.buildDatasource();

        detailDsBuilder.reset().setId("partsDs")
                .setMetaClass(metadataSession.getClass(TestPartEntity.class))
                .setMaster(detailDs)
                .setProperty("parts");
        partsDs = detailDsBuilder.buildCollectionDatasource();

        for (Datasource ds : detailDsContext.getAll()) {
            ((DatasourceImplementation) ds).initialized();
        }
    }

    private void createPartDsContext() {
        partDsContext = new DsContextImpl(dataService);

        DsBuilder partDsBuilder = new DsBuilder(partDsContext);

        partDsBuilder.reset().setId("partDs")
                .setMetaClass(metadataSession.getClass(TestPartEntity.class))
                .setViewName("_local");
        partDs = partDsBuilder.buildDatasource();

        for (Datasource ds : partDsContext.getAll()) {
            ((DatasourceImplementation) ds).initialized();
        }
    }

    private boolean containsEntityOfClass(Collection<Entity> collection, Class entityClass) {
        for (Entity entity : collection) {
            if (entity.getClass().equals(entityClass))
                return true;
        }
        return false;
    }

    private boolean containsEntityInstance(Collection<Entity> collection, Object entityId) {
        for (Entity entity : collection) {
            if (entity.getId().equals(entityId))
                return true;
        }
        return false;
    }
}
