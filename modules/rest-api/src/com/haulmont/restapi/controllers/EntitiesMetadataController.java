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

package com.haulmont.restapi.controllers;


import com.google.common.base.Joiner;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.Range;
import com.haulmont.cuba.core.app.serialization.ViewSerializationAPI;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.global.ViewRepository;
import com.haulmont.restapi.common.RestControllerUtils;
import com.haulmont.restapi.exception.RestAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controller that is used for getting entities metadata. User permissions for entities access aren't taken into account
 * at the moment.
 */
@RestController
@RequestMapping(value = "/api/metadata", produces = "application/json; charset=UTF-8")
public class EntitiesMetadataController {

    protected Logger log = LoggerFactory.getLogger(EntitiesMetadataController.class);

    @Inject
    protected Metadata metadata;

    @Inject
    protected RestControllerUtils restControllersUtils;

    @Inject
    protected ViewSerializationAPI viewSerializationAPI;

    @Inject
    protected ViewRepository viewRepository;

    @RequestMapping(path = "/entities/{entityName}", method = RequestMethod.GET)
    public MetaClassInfo getMetaClassInfo(@PathVariable String entityName) {
        MetaClass metaClass = restControllersUtils.getMetaClass(entityName);
        return new MetaClassInfo(metaClass);
    }

    @RequestMapping(path = "/entities", method = RequestMethod.GET)
    public Collection<MetaClassInfo> getAllMetaClassesInfo() {
        Set<MetaClass> metaClasses = new HashSet<>(metadata.getTools().getAllPersistentMetaClasses());
        metaClasses.addAll(metadata.getTools().getAllEmbeddableMetaClasses());

        return metaClasses.stream()
                .filter(metaClass -> metadata.getExtendedEntities().getExtendedClass(metaClass) == null)
                .map(MetaClassInfo::new)
                .collect(Collectors.toList());
    }

    @RequestMapping(path = "/entities/{entityName}/views/{viewName}", method = RequestMethod.GET)
    public String getView(@PathVariable String entityName,
                          @PathVariable String viewName) {
        MetaClass metaClass = restControllersUtils.getMetaClass(entityName);
        View view = viewRepository.findView(metaClass, viewName);
        if (view == null) {
            throw new RestAPIException("View not found",
                    String.format("View %s for metaClass %s not found", viewName, entityName),
                    HttpStatus.NOT_FOUND);
        }
        return viewSerializationAPI.toJson(view);
    }

    @RequestMapping(path = "/entities/{entityName}/views", method = RequestMethod.GET)
    public String getAllViewsForMetaClass(@PathVariable String entityName) {
        MetaClass metaClass = restControllersUtils.getMetaClass(entityName);
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        List<String> jsonViews = new ArrayList<>();
        for (String viewName : viewRepository.getViewNames(metaClass)) {
            View view = viewRepository.getView(metaClass, viewName);
            jsonViews.add(viewSerializationAPI.toJson(view));
        }
        sb.append(Joiner.on(",").join(jsonViews));
        sb.append("]");
        return sb.toString();
    }

    protected class MetaClassInfo {
        public String entityName;
        public List<MetaPropertyInfo> properties = new ArrayList<>();

        public MetaClassInfo(MetaClass metaClass) {
            this.entityName = metaClass.getName();

            properties.addAll(metaClass.getProperties().stream()
                    .map(MetaPropertyInfo::new)
                    .collect(Collectors.toList()));
        }
    }

    protected class MetaPropertyInfo {
        public String name;
        public MetaProperty.Type attributeType;
        public String type;
        public Range.Cardinality cardinality;
        public boolean mandatory;
        public boolean readOnly;
        boolean isTransient;

        public MetaPropertyInfo(MetaProperty metaProperty) {
            this.name = metaProperty.getName();
            this.attributeType = metaProperty.getType();
            switch (attributeType) {
                case DATATYPE:
                    this.type = metaProperty.getRange().asDatatype().getName();
                    break;
                case ASSOCIATION:
                case COMPOSITION:
                    this.type = metaProperty.getRange().asClass().getName();
                    break;
                case ENUM:
                    this.type = metaProperty.getRange().asEnumeration().getJavaClass().getName();
                    break;
            }
            this.cardinality = metaProperty.getRange().getCardinality();
            this.readOnly = metaProperty.isReadOnly();
            this.mandatory = metaProperty.isMandatory();
            this.isTransient = metadata.getTools().isTransient(metaProperty);
        }

        public boolean getTransient() {
            return isTransient;
        }
    }
}
