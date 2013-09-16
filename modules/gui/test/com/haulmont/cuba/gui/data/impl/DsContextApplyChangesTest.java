/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.data.impl;

import com.haulmont.chile.core.model.Session;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.client.testsupport.CubaClientTestCase;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.PersistenceHelper;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class DsContextApplyChangesTest extends CubaClientTestCase {

    private Session metadataSession;
    private TestDataSupplier dataService;

    private DsContextImplementation masterDsContext;

    private TestMasterEntity master;
    private TestDetailEntity detail1;
    private TestEmbeddableEntity embeddable1;
    private Datasource<TestMasterEntity> masterDs;
    private Datasource<TestDetailEntity> detailDs;
    private Datasource<TestEmbeddableEntity> embeddableDs;

    @Before
    public void setUp() throws Exception {
        addEntityPackage("com.haulmont.cuba");
        setViewConfig("/com/haulmont/cuba/gui/data/impl/testmodel1/test-views.xml");
        setupInfrastructure();

        metadataSession = metadata.getSession();
        dataService = new TestDataSupplier();

        dataService.commitCount = 0;

        new NonStrictExpectations() {
            @Mocked ClientConfig clientConfig;
            @Mocked PersistenceHelper persistenceHelper;
            {
                configuration.getConfig(ClientConfig.class); result = clientConfig;

                clientConfig.getCollectionDatasourceDbSortEnabled(); result = true;

                persistenceManager.getMaxFetchUI(anyString); result = 10000;

                PersistenceHelper.isNew(any); result = false;
            }
        };
    }

    private void createEntities() {
        master = new TestMasterEntity();
        master.setMasterName("master");

        detail1 = new TestDetailEntity();
        detail1.setDetailName("detail1");
        detail1.setMaster(master);
        detail1.setParts(new HashSet<TestPartEntity>());

        embeddable1 = new TestEmbeddableEntity();
        embeddable1.setName("embeddable1");
        detail1.setEmbeddable(embeddable1);

        master.setDetail(detail1);
    }

    private void createMasterDsContext() {
        masterDsContext = new DsContextImpl(dataService);

        DsBuilder masterDsBuilder = new DsBuilder(masterDsContext);

        masterDsBuilder.reset().setId("masterDs")
                .setMetaClass(metadataSession.getClass(TestMasterEntity.class))
                .setViewName("withDetail");
        masterDs = masterDsBuilder.buildDatasource();

        masterDsBuilder.reset().setId("detailDs")
                .setMetaClass(metadataSession.getClass(TestDetailEntity.class))
                .setMaster(masterDs)
                .setProperty("detail");
        detailDs = masterDsBuilder.buildDatasource();

        masterDsBuilder.reset().setId("embeddedDs")
                .setMetaClass(metadataSession.getClass(TestEmbeddableEntity.class))
                .setMaster(detailDs)
                .setProperty("embeddable");
        embeddableDs = masterDsBuilder.buildDatasource();

        for (Datasource ds : masterDsContext.getAll()) {
            ((DatasourceImplementation) ds).initialized();
        }
    }

    @Test
    public void test() {
        createEntities();

        createMasterDsContext();

        masterDs.setItem(master);

        assertEquals(embeddable1.getId(), embeddableDs.getItem().getId());

        embeddableDs.getItem().setName("embeddable1_1");

        dataService.commitValidator = new TestDataSupplier.CommitValidator() {
            @Override
            public void validate(CommitContext context) {
                assertTrue(containsEntityInstance(context.getCommitInstances(), detail1.getId()));
                for (Entity entity : context.getCommitInstances()) {
                    if (entity.getId().equals(detail1.getId()))
                        assertEquals("embeddable1_1", ((TestDetailEntity) entity).getEmbeddable().getName());
                }
            }
        };
        masterDsContext.commit();

        assertEquals("embeddable1_1", embeddableDs.getItem().getName());

        detailDs.getItem().setDetailName("detail1_1");

        dataService.commitValidator = new TestDataSupplier.CommitValidator() {
            @Override
            public void validate(CommitContext context) {
                assertTrue(containsEntityInstance(context.getCommitInstances(), detail1.getId()));
                for (Entity entity : context.getCommitInstances()) {
                    if (entity.getId().equals(detail1.getId())) {
                        assertEquals("detail1_1", ((TestDetailEntity) entity).getDetailName());
                        assertEquals("embeddable1_1", ((TestDetailEntity) entity).getEmbeddable().getName());
                    }
                }
            }
        };
        masterDsContext.commit();

        assertEquals("detail1_1", detailDs.getItem().getDetailName());
    }

    private boolean containsEntityInstance(Collection<Entity> collection, Object entityId) {
        for (Entity entity : collection) {
            if (entity.getId().equals(entityId))
                return true;
        }
        return false;
    }
}
