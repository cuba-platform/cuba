/*
 * Copyright (c) 2008-2018 Haulmont.
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

package com.haulmont.cuba.gui.sys;

import com.haulmont.bali.util.Preconditions;
import com.haulmont.cuba.gui.screen.UiController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Configuration that performs ClassPath scanning of {@link UiController}s and provides {@link UiControllerDefinition}.
 */
public class UiControllersConfiguration extends AbstractScanConfiguration{

    private static final Logger log = LoggerFactory.getLogger(UiControllersConfiguration.class);

    protected ApplicationContext applicationContext;
    protected MetadataReaderFactory metadataReaderFactory;

    protected List<String> packages = Collections.emptyList();
    protected List<String> classNames = Collections.emptyList();

    public UiControllersConfiguration() {
    }

    @Inject
    protected void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Inject
    public void setMetadataReaderFactory(AnnotationScanMetadataReaderFactory metadataReaderFactory) {
        this.metadataReaderFactory = metadataReaderFactory;
    }

    public List<String> getPackages() {
        return packages;
    }

    public void setPackages(List<String> packages) {
        Preconditions.checkNotNullArgument(packages);

        this.packages = packages;
    }

    public List<String> getClassNames() {
        return classNames;
    }

    public void setClassNames(List<String> classNames) {
        this.classNames = classNames;
    }

    public List<UiControllerDefinition> getUiControllers() {
        log.trace("Scanning packages {}", packages);

        Stream<UiControllerDefinition> scannedControllersStream = packages.stream()
                .flatMap(this::scanPackage)
                .filter(this::isCandidateUiController)
                .map(this::extractControllerDefinition);

        Stream<UiControllerDefinition> explicitControllersStream = classNames.stream()
                .map(this::loadClassMetadata)
                .map(this::extractControllerDefinition);

        return Stream.concat(scannedControllersStream, explicitControllersStream)
                .collect(Collectors.toList());
    }

    protected UiControllerDefinition extractControllerDefinition(MetadataReader metadataReader) {
        Map<String, Object> uiControllerAnn =
                metadataReader.getAnnotationMetadata().getAnnotationAttributes(UiController.class.getName());

        String idAttr = null;
        String valueAttr = null;
        if (uiControllerAnn != null) {
            idAttr = (String) uiControllerAnn.get(UiController.ID_ATTRIBUTE);
            valueAttr = (String) uiControllerAnn.get(UiController.VALUE_ATTRIBUTE);
        }

        String className = metadataReader.getClassMetadata().getClassName();
        String controllerId = UiDescriptorUtils.getInferredScreenId(idAttr, valueAttr, className);

        return new UiControllerDefinition(controllerId, className);
    }

    protected boolean isCandidateUiController(MetadataReader metadataReader) {
        return metadataReader.getClassMetadata().isConcrete()
                && metadataReader.getAnnotationMetadata().hasAnnotation(UiController.class.getName());
    }

    @Override
    protected MetadataReaderFactory getMetadataReaderFactory() {
        return metadataReaderFactory;
    }

    @Override
    protected ResourceLoader getResourceLoader() {
        return applicationContext;
    }

    @Override
    protected Environment getEnvironment() {
        return applicationContext.getEnvironment();
    }
}