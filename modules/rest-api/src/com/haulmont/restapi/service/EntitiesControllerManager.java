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
import com.haulmont.cuba.core.app.importexport.EntityImportException;
import com.haulmont.cuba.core.app.importexport.EntityImportExportService;
import com.haulmont.cuba.core.app.importexport.EntityImportView;
import com.haulmont.cuba.core.app.importexport.EntityImportViewBuilderAPI;
import com.haulmont.cuba.core.app.serialization.EntitySerializationAPI;
import com.haulmont.cuba.core.app.serialization.EntitySerializationOption;
import com.haulmont.cuba.core.entity.BaseGenericIdEntity;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.global.validation.groups.RestApiChecks;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.restapi.common.RestControllerUtils;
import com.haulmont.restapi.data.CreatedEntityInfo;
import com.haulmont.restapi.exception.RestAPIException;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.*;

/**
 * Class that executes business logic required by the {@link com.haulmont.restapi.controllers.EntitiesController}. It
 * performs CRUD operations with entities
 */
@Component("cuba_EntitiesControllerManager")
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
    protected BeanValidation beanValidation;

    @Inject
    protected RestControllerUtils restControllerUtils;

    public String loadEntity(String entityName,
                             String entityId,
                             @Nullable String view,
                             @Nullable Boolean returnNulls,
                             @Nullable Boolean dynamicAttributes) {
        MetaClass metaClass = restControllerUtils.getMetaClass(entityName);

        checkCanReadEntity(metaClass);

        LoadContext<Entity> ctx = new LoadContext<>(metaClass);
        Object id = getIdFromString(entityId, metaClass);
        ctx.setId(id);

        if (!Strings.isNullOrEmpty(view)) {
            ctx.setView(view);
        }

        ctx.setLoadDynamicAttributes(BooleanUtils.isTrue(dynamicAttributes));

        Entity entity = dataManager.load(ctx);
        checkEntityIsNotNull(entityName, entityId, entity);

        List<EntitySerializationOption> serializationOptions = new ArrayList<>();
        serializationOptions.add(EntitySerializationOption.SERIALIZE_INSTANCE_NAME);
        if (BooleanUtils.isTrue(returnNulls)) serializationOptions.add(EntitySerializationOption.SERIALIZE_NULLS);

        restControllerUtils.applyAttributesSecurity(entity);

        return entitySerializationAPI.toJson(entity, null, serializationOptions.toArray(new EntitySerializationOption[0]));

    }

    public String loadEntitiesList(String entityName,
                                   @Nullable String viewName,
                                   @Nullable Integer limit,
                                   @Nullable Integer offset,
                                   @Nullable String sort,
                                   @Nullable Boolean returnNulls,
                                   @Nullable Boolean dynamicAttributes) {
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


        View view = null;
        if (!Strings.isNullOrEmpty(viewName)) {
            view = metadata.getViewRepository().getView(metaClass, viewName);
            ctx.setView(view);
        }

        ctx.setLoadDynamicAttributes(BooleanUtils.isTrue(dynamicAttributes));

        List<Entity> entities = dataManager.loadList(ctx);
        entities.forEach(entity -> restControllerUtils.applyAttributesSecurity(entity));

        List<EntitySerializationOption> serializationOptions = new ArrayList<>();
        serializationOptions.add(EntitySerializationOption.SERIALIZE_INSTANCE_NAME);
        if (BooleanUtils.isTrue(returnNulls)) serializationOptions.add(EntitySerializationOption.SERIALIZE_NULLS);

        return entitySerializationAPI.toJson(entities, view, serializationOptions.toArray(new EntitySerializationOption[0]));
    }

    public CreatedEntityInfo createEntity(String entityJson, String entityName) {
        MetaClass metaClass = restControllerUtils.getMetaClass(entityName);
        checkCanCreateEntity(metaClass);
        //todo MG catch invalid json
        Entity entity = entitySerializationAPI.entityFromJson(entityJson, metaClass);

        Validator validator = beanValidation.getValidator();
        Set<ConstraintViolation<Entity>> violations = validator.validate(entity, Default.class, RestApiChecks.class);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(
                    "Validation failed on entity creation through REST-API for " + entityName, violations);
        }

        EntityImportView entityImportView = entityImportViewBuilderAPI.buildFromJson(entityJson, metaClass);

        Collection<Entity> importedEntities;
        try {
            importedEntities = entityImportExportService.importEntities(Collections.singletonList(entity), entityImportView);
        } catch (EntityImportException e) {
            throw new RestAPIException("Entity creation failed", e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        //if many entities were created (because of @Composition references) we must find the main entity
        return getMainEntityInfo(importedEntities, metaClass);
    }

    public CreatedEntityInfo updateEntity(String entityJson,
                                          String entityName,
                                          String entityId) {
        MetaClass metaClass = restControllerUtils.getMetaClass(entityName);
        checkCanUpdateEntity(metaClass);
        Object id = getIdFromString(entityId, metaClass);

        LoadContext loadContext = new LoadContext(metaClass).setId(id);
        @SuppressWarnings("unchecked")
        Entity existingEntity = dataManager.load(loadContext);

        checkEntityIsNotNull(entityName, entityId, existingEntity);
        Entity entity = entitySerializationAPI.entityFromJson(entityJson, metaClass);
        if (entity instanceof BaseGenericIdEntity) {
            //noinspection unchecked
            ((BaseGenericIdEntity) entity).setId(id);
        }

        Validator validator = beanValidation.getValidator();
        Set<ConstraintViolation<Entity>> violations = validator.validate(entity, Default.class, RestApiChecks.class);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(
                    "Validation failed on entity creation through REST-API for " + entityName, violations);
        }

        EntityImportView entityImportView = entityImportViewBuilderAPI.buildFromJson(entityJson, metaClass);
        Collection<Entity> importedEntities;
        try {
            importedEntities = entityImportExportService.importEntities(Collections.singletonList(entity), entityImportView);
        } catch (EntityImportException e) {
            throw new RestAPIException("Entity update failed", e.getMessage(), HttpStatus.BAD_REQUEST);
        }
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