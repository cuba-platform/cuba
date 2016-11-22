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

package com.haulmont.cuba.client.testsupport;

import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.app.PersistenceManagerService;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.AppContext;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for building client-side integration tests.
 *
 */
public class CubaClientTestCase {

    private List<String> entityPackages = new ArrayList<>();

    private String viewConfig;

    @Mocked
    protected AppContext appContext;

    @Mocked
    protected AppBeans appBeans;

    @Mocked
    protected Configuration configuration;

    @Mocked
    protected PersistenceManagerService persistenceManager;

    @Mocked
    protected GlobalConfig globalConfig;

    @Mocked
    protected ClientConfig clientConfig;

    protected TestMetadataClient metadata;

    protected TestViewRepositoryClient viewRepository;

    protected TestUserSessionSource userSessionSource;

    protected TestUuidSource uuidSource;

    protected TestSecurity security;

    protected TestExtendedEntities extendedEntities;

    protected TestMessages messages;

    protected TestMessageTools messageTools;

    public CubaClientTestCase() {
        String property = System.getProperty("logback.configurationFile");
        if (StringUtils.isBlank(property)) {
            System.setProperty("logback.configurationFile", "test-logback.xml");
        }
    }

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

        extendedEntities = new TestExtendedEntities(metadata);
        security = new TestSecurity(userSessionSource, metadata, extendedEntities);

        new NonStrictExpectations() {
            {
                configuration.getConfig(GlobalConfig.class); result = globalConfig;
                configuration.getConfig(ClientConfig.class); result = clientConfig;
                globalConfig.getConfDir(); result = System.getProperty("user.dir");
                clientConfig.getRemoteMessagesSearchEnabled(); result = false;
            }
        };

        messages = new TestMessages(userSessionSource, configuration, metadata, extendedEntities);
        messageTools = (TestMessageTools) messages.getTools();

        ((TestMetadataTools) metadata.getTools()).setMessages(messages);
        ((TestMetadataTools) metadata.getTools()).setUserSessionSource(userSessionSource);

        messages.setConfiguration(configuration);

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

                AppBeans.get(Security.NAME); result = security;
                AppBeans.get(Security.class); result= security;
                AppBeans.get(Security.NAME, Security.class); result = security;

                AppBeans.get(ExtendedEntities.NAME); result = extendedEntities;
                AppBeans.get(ExtendedEntities.class); result = extendedEntities;
                AppBeans.get(ExtendedEntities.NAME, ExtendedEntities.class); result = extendedEntities;

                AppBeans.get(Messages.NAME); result = messages;
                AppBeans.get(Messages.class); result = messages;
                AppBeans.get(Messages.NAME, Messages.class); result = messages;

                AppBeans.get(MessageTools.NAME); result = messageTools;
                AppBeans.get(MessageTools.class); result = messageTools;
                AppBeans.get(MessageTools.NAME, MessageTools.class); result = messageTools;
            }
        };
    }
}