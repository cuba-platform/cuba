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

package com.haulmont.restapi.service;

import com.google.common.base.Joiner;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.app.serialization.ViewSerializationAPI;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.core.global.ViewRepository;
import com.haulmont.restapi.common.RestControllerUtils;
import com.haulmont.restapi.data.MetaClassInfo;
import com.haulmont.restapi.exception.RestAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Class is used by the {@link com.haulmont.restapi.controllers.EntitiesMetadataController}. Class is sed for getting
 * entities metadata. User permissions for entities access aren't taken into account at the moment.
 */
public class EntitiesMetadataControllerManager {

    protected Logger log = LoggerFactory.getLogger(EntitiesMetadataControllerManager.class);

    @Inject
    protected Metadata metadata;

    @Inject
    protected RestControllerUtils restControllersUtils;

    @Inject
    protected ViewSerializationAPI viewSerializationAPI;

    @Inject
    protected ViewRepository viewRepository;

    @Inject
    protected Messages messages;

    public MetaClassInfo getMetaClassInfo(String entityName) {
        MetaClass metaClass = restControllersUtils.getMetaClass(entityName);
        return new MetaClassInfo(metaClass);
    }

    public Collection<MetaClassInfo> getAllMetaClassesInfo() {
        Set<MetaClass> metaClasses = new HashSet<>(metadata.getTools().getAllPersistentMetaClasses());
        metaClasses.addAll(metadata.getTools().getAllEmbeddableMetaClasses());

        return metaClasses.stream()
                .filter(metaClass -> metadata.getExtendedEntities().getExtendedClass(metaClass) == null)
                .map(MetaClassInfo::new)
                .collect(Collectors.toList());
    }

    public String getView(String entityName, String viewName) {
        MetaClass metaClass = restControllersUtils.getMetaClass(entityName);
        View view = viewRepository.findView(metaClass, viewName);
        if (view == null) {
            throw new RestAPIException("View not found",
                    String.format("View %s for metaClass %s not found", viewName, entityName),
                    HttpStatus.NOT_FOUND);
        }
        return viewSerializationAPI.toJson(view);
    }

    public String getAllViewsForMetaClass(String entityName) {
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
}
