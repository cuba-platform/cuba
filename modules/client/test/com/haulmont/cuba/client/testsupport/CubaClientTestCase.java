/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.client.testsupport;

import com.haulmont.cuba.core.app.PersistenceManagerService;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.Metadata;
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
    protected Configuration configuration;

    @Mocked
    protected PersistenceManagerService persistenceManager;

    protected TestMetadataClient metadata;

    protected void addEntityPackage(String pack) {
        entityPackages.add(pack);
    }

    protected void setViewConfig(String viewConfigPath) {
        viewConfig = viewConfigPath;
    }

    protected void setupInfrastructure() {
        metadata = new TestMetadataClient(entityPackages, viewConfig);

        new NonStrictExpectations() {
            {
                AppContext.getProperty("cuba.confDir"); result = System.getProperty("user.dir");

                AppContext.getBean(Metadata.NAME); result = metadata;
                AppContext.getBean(Metadata.NAME, Metadata.class); result = metadata;

                AppContext.getBean(Configuration.NAME); result = configuration;
                AppContext.getBean(Configuration.NAME, Configuration.class); result = configuration;

                AppContext.getBean(PersistenceManagerService.NAME); result = persistenceManager;
                AppContext.getBean(PersistenceManagerService.NAME, PersistenceManagerService.class); result = persistenceManager;
            }
        };

    }
}
