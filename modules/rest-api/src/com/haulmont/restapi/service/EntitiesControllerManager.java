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

import com.google.common.base.Strings;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.app.importexport.EntityImportExportService;
import com.haulmont.cuba.core.app.importexport.EntityImportView;
import com.haulmont.cuba.core.app.importexport.EntityImportViewBuilderAPI;
import com.haulmont.cuba.core.app.serialization.EntitySerializationAPI;
import com.haulmont.cuba.core.app.serialization.EntitySerializationOption;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.Security;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.restapi.common.RestControllerUtils;
import com.haulmont.restapi.data.CreatedEntityInfo;
import com.haulmont.restapi.exception.RestAPIException;
import org.apache.commons.lang.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.*;

/**
 * Class that executes business logic required by the {@link com.haulmont.restapi.controllers.EntitiesController}. It
 * performs CRUD operations with entities
 */
public class EntitiesControllerManager {

    @Inject
    protected DataManager dataManager;

    @Inject
    protected Metadata metadata;

    @Inject
    protected EntitySerializationAPI entitySerializationAPI;

    @Inject
    protected EntityImportViewBuilderAPI entityImportViewBuilderAPI;

    @Inject
    protected EntityImportExportService entityImportExportService;

    @Inject
    protected Security security;

    @Inject
    protected RestControllerUtils restControllerUtils;

    protected Logger log = LoggerFactory.getLogger(EntitiesControllerManager.class);

    public String loadEntity(String entityName,
                             String entityId,
                             @Nullable String view,
                             @Nullable Boolean returnNulls) {
        MetaClass metaClass = restControllerUtils.getMetaClass(entityName);

        checkCanReadEntity(metaClass);

        LoadContext<Entity> ctx = new LoadContext<>(metaClass);
        Object id = getIdFromString(entityId, metaClass);
        ctx.setId(id);

        if (!Strings.isNullOrEmpty(view)) {
            ctx.setView(view);
        }

        Entity entity = dataManager.load(ctx);
        checkEntityIsNotNull(entityName, entityId, entity);

        return BooleanUtils.isTrue(returnNulls) ?
                entitySerializationAPI.toJson(entity, null, EntitySerializationOption.SERIALIZE_NULLS) :
                entitySerializationAPI.toJson(entity);
    }

    public String loadEntitiesList(String entityName,
                                   @Nullable String view,
                                   @Nullable Integer limit,
                                   @Nullable Integer offset,
                                   @Nullable String sort,
                                   @Nullable Boolean returnNulls) {
        MetaClass metaClass = restControllerUtils.getMetaClass(entityName);
        checkCanReadEntity(metaClass);

        LoadContext<Entity> ctx = new LoadContext<>(metaClass);
        String queryString = "select e from " + entityName + " e";
        if (!Strings.isNullOrEmpty(sort)) {
            boolean descSortOrder = false;
            if (sort.startsWith("-")) {
                descSortOrder = true;
                sort = sort.substring(1);
            } else if (sort.startsWith("+")) {
                sort = sort.substring(1);
            }
            queryString += " order by e." + sort + (descSortOrder ? " desc" : "");
        }
        LoadContext.Query query = new LoadContext.Query(queryString);
        if (limit != null) {
            query.setMaxResults(limit);
        }
        if (offset != null) {
            query.setFirstResult(offset);
        }
        ctx.setQuery(query);

        if (!Strings.isNullOrEmpty(view)) {
            ctx.setView(view);
        }

        List<Entity> entities = dataManager.loadList(ctx);

        return BooleanUtils.isTrue(returnNulls) ?
                entitySerializationAPI.toJson(entities, null, EntitySerializationOption.SERIALIZE_NULLS) :
                entitySerializationAPI.toJson(entities);
    }

    public CreatedEntityInfo createEntity(String entityJson, String entityName) {
        MetaClass metaClass = restControllerUtils.getMetaClass(entityName);
        checkCanCreateEntity(metaClass);
        //todo MG catch invalid json
        Entity entity = entitySerializationAPI.entityFromJson(entityJson, metaClass);
        EntityImportView entityImportView = entityImportViewBuilderAPI.buildFromJson(entityJson, metaClass);

        Collection<Entity> importedEntities = entityImportExportService.importEntities(Collections.singletonList(entity), entityImportView);

        //if multiple entities was created (because of @Composition references) we must find the main entity
        return getMainEntityInfo(importedEntities, metaClass);
    }

    public CreatedEntityInfo updateEntity(String entityJson,
                                          String entityName,
                                          String entityId) {
        MetaClass metaClass = restControllerUtils.getMetaClass(entityName);
        checkCanUpdateEntity(metaClass);
        Object id = getIdFromString(entityId, metaClass);
        Entity existingEntity = dataManager.load(new LoadContext(metaClass).setId(id));
        checkEntityIsNotNull(entityName, entityId, existingEntity);
        Entity entity = entitySerializationAPI.entityFromJson(entityJson, metaClass);
        EntityImportView entityImportView = entityImportViewBuilderAPI.buildFromJson(entityJson, metaClass);
        Collection<Entity> importedEntities = entityImportExportService.importEntities(Collections.singletonList(entity), entityImportView);
        //there may be multiple entities in importedEntities (because of @Composition references), so we must find
        // the main entity that will be returned
        return getMainEntityInfo(importedEntities, metaClass);
    }

    public void deleteEntity(String entityName,
                             String entityId) {
        MetaClass metaClass = metadata.getClass(entityName);
        checkCanDeleteEntity(metaClass);
        Object id = getIdFromString(entityId, metaClass);
        Entity entity = dataManager.load(new LoadContext<>(metaClass).setId(id));
        checkEntityIsNotNull(entityName, entityId, entity);
        dataManager.remove(entity);
    }

    private Object getIdFromString(String entityId, MetaClass metaClass) {
        MetaProperty primaryKeyProperty = metadata.getTools().getPrimaryKeyProperty(metaClass);
        Class<?> declaringClass = primaryKeyProperty.getJavaType();
        Object id;
        if (UUID.class.isAssignableFrom(declaringClass)) {
            id = UUID.fromString(entityId);
        } else if (Integer.class.isAssignableFrom(declaringClass)) {
            id = Integer.valueOf(entityId);
        } else if (Long.class.isAssignableFrom(declaringClass)) {
            id = Long.valueOf(entityId);
        } else {
            id = entityId;
        }
        return id;
    }

    protected void checkEntityIsNotNull(String entityName, String entityId, Entity entity) {
        if (entity == null) {
            throw new RestAPIException("Entity not found",
                    String.format("Entity %s with id %s not found", entityName, entityId),
                    HttpStatus.NOT_FOUND);
        }
    }

    protected void checkCanReadEntity(MetaClass metaClass) {
        if (!security.isEntityOpPermitted(metaClass, EntityOp.READ)) {
            throw new RestAPIException("Reading forbidden",
                    String.format("Reading of the %s is forbidden", metaClass.getName()),
                    HttpStatus.FORBIDDEN);
        }
    }

    protected void checkCanCreateEntity(MetaClass metaClass) {
        if (!security.isEntityOpPermitted(metaClass, EntityOp.CREATE)) {
            throw new RestAPIException("Creation forbidden",
                    String.format("Creation of the %s is forbidden", metaClass.getName()),
                    HttpStatus.FORBIDDEN);
        }
    }

    protected void checkCanDeleteEntity(MetaClass metaClass) {
        if (!security.isEntityOpPermitted(metaClass, EntityOp.DELETE)) {
            throw new RestAPIException("Deletion forbidden",
                    String.format("Deletion of the %s is forbidden", metaClass.getName()),
                    HttpStatus.FORBIDDEN);
        }
    }

    protected void checkCanUpdateEntity(MetaClass metaClass) {
        if (!security.isEntityOpPermitted(metaClass, EntityOp.UPDATE)) {
            throw new RestAPIException("Updating forbidden",
                    String.format("Updating of the %s is forbidden", metaClass.getName()),
                    HttpStatus.FORBIDDEN);
        }
    }

    /**
     * Finds entity with given metaClass and converts it to JSON.
     */
    @Nullable
    protected CreatedEntityInfo getMainEntityInfo(Collection<Entity> importedEntities, MetaClass metaClass) {
        Entity mainEntity = null;
        if (importedEntities.size() > 1) {
            Optional<Entity> first = importedEntities.stream().filter(e -> e.getMetaClass().equals(metaClass)).findFirst();
            if (first.isPresent()) mainEntity = first.get();
        } else {
            mainEntity = importedEntities.iterator().next();
        }

        if (mainEntity != null) {
            return new CreatedEntityInfo(mainEntity.getId(), entitySerializationAPI.toJson(mainEntity));
        }
        return null;
    }

}
