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

import com.google.common.base.Strings;
import com.haulmont.cuba.gui.components.ActionType;
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

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

public class ActionsConfiguration extends AbstractScanConfiguration {

    private static final Logger log = LoggerFactory.getLogger(ActionsConfiguration.class);

    protected ApplicationContext applicationContext;
    protected MetadataReaderFactory metadataReaderFactory;

    protected List<String> basePackages = Collections.emptyList();
    protected List<ActionDefinition> explicitDefinitions = Collections.emptyList();

    public ActionsConfiguration() {
    }

    @Inject
    protected void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Inject
    public void setMetadataReaderFactory(AnnotationScanMetadataReaderFactory metadataReaderFactory) {
        this.metadataReaderFactory = metadataReaderFactory;
    }

    public List<String> getBasePackages() {
        return basePackages;
    }

    public void setBasePackages(List<String> basePackages) {
        checkNotNullArgument(basePackages);

        this.basePackages = basePackages;
    }

    public List<ActionDefinition> getExplicitDefinitions() {
        return explicitDefinitions;
    }

    public void setExplicitDefinitions(List<ActionDefinition> explicitDefinitions) {
        checkNotNullArgument(explicitDefinitions);

        this.explicitDefinitions = explicitDefinitions;
    }

    public List<ActionDefinition> getActions() {
        log.trace("Scanning packages {}", basePackages);

        Stream<ActionDefinition> scannedActionsStream = basePackages.stream()
                .flatMap(this::scanPackage)
                .filter(this::isCandidateUiController)
                .map(this::extractActionDefinition);

        return Stream.concat(scannedActionsStream, explicitDefinitions.stream())
                .collect(Collectors.toList());
    }

    protected ActionDefinition extractActionDefinition(MetadataReader metadataReader) {
        Map<String, Object> actionTypeAnn =
                metadataReader.getAnnotationMetadata().getAnnotationAttributes(ActionType.class.getName());

        String valueAttr = null;
        if (actionTypeAnn != null) {
            valueAttr = (String) actionTypeAnn.get(ActionType.VALUE_ATTRIBUTE);
        }

        String className = metadataReader.getClassMetadata().getClassName();
        String actionTypeId = Strings.isNullOrEmpty(valueAttr) ?  className : valueAttr;

        return new ActionDefinition(actionTypeId, className);
    }

    protected boolean isCandidateUiController(MetadataReader metadataReader) {
        return metadataReader.getClassMetadata().isConcrete()
                && metadataReader.getAnnotationMetadata().hasAnnotation(ActionType.class.getName());
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