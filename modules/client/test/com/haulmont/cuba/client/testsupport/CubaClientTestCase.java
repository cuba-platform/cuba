/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.client.testsupport;

import com.haulmont.cuba.core.app.PersistenceManagerService;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.AppContext;
import mockit.Mocked;
import mockit.NonStrictExpectations;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for building client-side integration tests.
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class CubaClientTestCase {

    private List<String> entityPackages = new ArrayList<String>();

    private String viewConfig;

    @Mocked
    protected AppContext appContext;

    @Mocked
    protected AppBeans appBeans;

    @Mocked
    protected Configuration configuration;

    @Mocked
    protected PersistenceManagerService persistenceManager;

    protected TestMetadataClient metadata;

    protected TestViewRepositoryClient viewRepository;

    protected TestUserSessionSource userSessionSource;

    protected TestUuidSource uuidSource;

    /**
     * Add entities package to build metadata from. Should be invoked by concrete test classes in their @Before method.
     * @param pack  package FQN, e.g. <code>com.haulmont.cuba.core.entity</code>
     */
    protected void addEntityPackage(String pack) {
        entityPackages.add(pack);
    }

    /**
     * Set path to Views configuration file to build ViewRepository from it. Should be invoked by concrete test classes
     * once in their @Before method.
     * @param viewConfigPath    configuration resource FQN, e.g. <code>/com/haulmont/cuba/gui/data/impl/testmodel1/test-views.xml</code>
     */
    protected void setViewConfig(String viewConfigPath) {
        viewConfig = viewConfigPath;
    }

    /**
     * Set up {@link Metadata} and other infrastructure objects before running test. Should be invoked by concrete test classes
     * once in their @Before method.
     */
    protected void setupInfrastructure() {
        new NonStrictExpectations() {
            {
                AppContext.getProperty("cuba.confDir"); result = System.getProperty("user.dir");
            }
        };
        viewRepository = new TestViewRepositoryClient(viewConfig);
        metadata = new TestMetadataClient(entityPackages, viewRepository);
        userSessionSource = new TestUserSessionSource();
        uuidSource = new TestUuidSource();

        new NonStrictExpectations() {
            {
                AppBeans.get(Metadata.NAME); result = metadata;
                AppBeans.get(Metadata.class); result = metadata;
                AppBeans.get(Metadata.NAME, Metadata.class); result = metadata;

                AppBeans.get(ViewRepository.NAME); result = viewRepository;
                AppBeans.get(ViewRepository.class); result = viewRepository;
                AppBeans.get(ViewRepository.NAME, ViewRepository.class); result = viewRepository;

                AppBeans.get(MetadataTools.NAME); result = metadata.getTools();
                AppBeans.get(MetadataTools.class); result = metadata.getTools();
                AppBeans.get(MetadataTools.NAME, MetadataTools.class); result = metadata.getTools();

                AppBeans.get(Configuration.NAME); result = configuration;
                AppBeans.get(Configuration.class); result = configuration;
                AppBeans.get(Configuration.NAME, Configuration.class); result = configuration;

                AppBeans.get(PersistenceManagerService.NAME); result = persistenceManager;
                AppBeans.get(PersistenceManagerService.class); result = persistenceManager;
                AppBeans.get(PersistenceManagerService.NAME, PersistenceManagerService.class); result = persistenceManager;

                AppBeans.get(UserSessionSource.NAME); result = userSessionSource;
                AppBeans.get(UserSessionSource.class); result = userSessionSource;
                AppBeans.get(UserSessionSource.NAME, UserSessionSource.class); result = userSessionSource;

                AppBeans.get(UuidSource.NAME); result = uuidSource;
                AppBeans.get(UuidSource.class); result = uuidSource;
                AppBeans.get(UuidSource.NAME, UuidSource.class); result = uuidSource;
            }
        };
    }
}
