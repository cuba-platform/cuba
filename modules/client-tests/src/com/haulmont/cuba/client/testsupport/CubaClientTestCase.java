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

import com.haulmont.chile.core.datatypes.DatatypeRegistry;
import com.haulmont.chile.core.datatypes.FormatStringsRegistry;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.app.PersistenceManagerService;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.FormatStringsRegistryImpl;
import mockit.Mocked;
import mockit.Expectations;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;

import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Base class for building client-side integration tests.
 *
 */
public class CubaClientTestCase {

    private Map<String, List<String>> entityPackages = new LinkedHashMap<>();

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

    protected FormatStringsRegistry formatStringsRegistry;

    protected TestMessages messages;

    protected TestMessageTools messageTools;

    protected TestBeanValidation beanValidation;

    protected ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
    protected MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(this.resourcePatternResolver);

    public CubaClientTestCase() {
        String property = System.getProperty("logback.configurationFile");
        if (StringUtils.isBlank(property)) {
            System.setProperty("logback.configurationFile", "test-logback.xml");
        }
    }

    /**
     * Add entities package to build metadata from. Should be invoked by concrete test classes in their @Before method.
     * @param packageName  package FQN, e.g. <code>com.haulmont.cuba.core.entity</code>
     */
    protected void addEntityPackage(String packageName) {
        String packagePrefix = packageName.replace(".", "/") + "/**/*.class";
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + packagePrefix;
        Resource[] resources;
        try {
            resources = resourcePatternResolver.getResources(packageSearchPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        entityPackages.put(packageName, getClasses(resources));
    }

    protected List<String> getClasses(Resource[] resources) {
        List<String> classNames = new ArrayList<>();

        for (Resource resource : resources) {
            if (resource.isReadable()) {
                MetadataReader metadataReader;
                try {
                    metadataReader = metadataReaderFactory.getMetadataReader(resource);
                } catch (IOException e) {
                    throw new RuntimeException("Unable to read metadata resource", e);
                }

                AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
                if (annotationMetadata.isAnnotated(com.haulmont.chile.core.annotations.MetaClass.class.getName())
                        || annotationMetadata.isAnnotated(MappedSuperclass.class.getName())
                        || annotationMetadata.isAnnotated(Entity.class.getName())) {
                    ClassMetadata classMetadata = metadataReader.getClassMetadata();
                    classNames.add(classMetadata.getClassName());
                }
            }
        }
        return classNames;
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
        new Expectations() {
            {
                AppContext.getProperty("cuba.confDir"); result = System.getProperty("user.dir"); minTimes = 0;
            }
        };
        viewRepository = new TestViewRepositoryClient(viewConfig);
        metadata = new TestMetadataClient(entityPackages, viewRepository, globalConfig);
        userSessionSource = new TestUserSessionSource();
        uuidSource = new TestUuidSource();

        extendedEntities = new TestExtendedEntities(metadata);
        security = new TestSecurity(userSessionSource, metadata, extendedEntities);
        formatStringsRegistry = new FormatStringsRegistryImpl();

        new Expectations() {
            {
                configuration.getConfig(GlobalConfig.class); result = globalConfig; minTimes = 0;
                configuration.getConfig(ClientConfig.class); result = clientConfig; minTimes = 0;
                globalConfig.getConfDir(); result = System.getProperty("user.dir"); minTimes = 0;
                clientConfig.getRemoteMessagesSearchEnabled(); result = false; minTimes = 0;
            }
        };

        messages = new TestMessages(userSessionSource, configuration, metadata, extendedEntities, formatStringsRegistry);
        messageTools = (TestMessageTools) messages.getTools();

        beanValidation = new TestBeanValidation();

        ((TestMetadataTools) metadata.getTools()).setMessages(messages);
        ((TestMetadataTools) metadata.getTools()).setUserSessionSource(userSessionSource);

        messages.setConfiguration(configuration);

        new Expectations() {
            {
                AppBeans.get(Metadata.NAME); result = metadata; minTimes = 0;
                AppBeans.get(Metadata.class); result = metadata; minTimes = 0;
                AppBeans.get(Metadata.NAME, Metadata.class); result = metadata; minTimes = 0;

                AppBeans.get(ViewRepository.NAME); result = viewRepository; minTimes = 0;
                AppBeans.get(ViewRepository.class); result = viewRepository; minTimes = 0;
                AppBeans.get(ViewRepository.NAME, ViewRepository.class); result = viewRepository; minTimes = 0;

                AppBeans.get(MetadataTools.NAME); result = metadata.getTools(); minTimes = 0;
                AppBeans.get(MetadataTools.class); result = metadata.getTools(); minTimes = 0;
                AppBeans.get(MetadataTools.NAME, MetadataTools.class); result = metadata.getTools(); minTimes = 0;

                AppBeans.get(DatatypeRegistry.NAME); result = metadata.getDatatypes(); minTimes = 0;
                AppBeans.get(DatatypeRegistry.class); result = metadata.getDatatypes(); minTimes = 0;
                AppBeans.get(DatatypeRegistry.NAME, DatatypeRegistry.class); result = metadata.getDatatypes(); minTimes = 0;

                AppBeans.get(FormatStringsRegistry.NAME); result = formatStringsRegistry; minTimes = 0;
                AppBeans.get(FormatStringsRegistry.class); result = formatStringsRegistry; minTimes = 0;
                AppBeans.get(FormatStringsRegistry.NAME, FormatStringsRegistry.class); result = formatStringsRegistry; minTimes = 0;

                AppBeans.get(Configuration.NAME); result = configuration; minTimes = 0;
                AppBeans.get(Configuration.class); result = configuration; minTimes = 0;
                AppBeans.get(Configuration.NAME, Configuration.class); result = configuration; minTimes = 0;

                AppBeans.get(PersistenceManagerService.NAME); result = persistenceManager; minTimes = 0;
                AppBeans.get(PersistenceManagerService.class); result = persistenceManager; minTimes = 0;
                AppBeans.get(PersistenceManagerService.NAME, PersistenceManagerService.class); result = persistenceManager; minTimes = 0;

                AppBeans.get(UserSessionSource.NAME); result = userSessionSource; minTimes = 0;
                AppBeans.get(UserSessionSource.class); result = userSessionSource; minTimes = 0;
                AppBeans.get(UserSessionSource.NAME, UserSessionSource.class); result = userSessionSource; minTimes = 0;

                AppBeans.get(UuidSource.NAME); result = uuidSource; minTimes = 0;
                AppBeans.get(UuidSource.class); result = uuidSource; minTimes = 0;
                AppBeans.get(UuidSource.NAME, UuidSource.class); result = uuidSource; minTimes = 0;

                AppBeans.get(Security.NAME); result = security; minTimes = 0;
                AppBeans.get(Security.class); result= security; minTimes = 0;
                AppBeans.get(Security.NAME, Security.class); result = security; minTimes = 0;

                AppBeans.get(ExtendedEntities.NAME); result = extendedEntities; minTimes = 0;
                AppBeans.get(ExtendedEntities.class); result = extendedEntities; minTimes = 0;
                AppBeans.get(ExtendedEntities.NAME, ExtendedEntities.class); result = extendedEntities; minTimes = 0;

                AppBeans.get(Messages.NAME); result = messages; minTimes = 0;
                AppBeans.get(Messages.class); result = messages; minTimes = 0;
                AppBeans.get(Messages.NAME, Messages.class); result = messages; minTimes = 0;

                AppBeans.get(MessageTools.NAME); result = messageTools; minTimes = 0;
                AppBeans.get(MessageTools.class); result = messageTools; minTimes = 0;
                AppBeans.get(MessageTools.NAME, MessageTools.class); result = messageTools; minTimes = 0;

                AppBeans.get(BeanValidation.NAME); result = beanValidation; minTimes = 0;
                AppBeans.get(BeanValidation.class); result = beanValidation; minTimes = 0;
                AppBeans.get(BeanValidation.NAME, BeanValidation.class); result = beanValidation; minTimes = 0;
            }
        };

        metadata.initMetadata();
    }
}