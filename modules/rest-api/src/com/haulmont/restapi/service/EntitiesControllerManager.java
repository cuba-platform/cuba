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
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.client.sys.PersistenceManagerClient;
import com.haulmont.cuba.core.app.importexport.EntityImportException;
import com.haulmont.cuba.core.app.importexport.EntityImportExportService;
import com.haulmont.cuba.core.app.importexport.EntityImportView;
import com.haulmont.cuba.core.app.importexport.EntityImportViewBuilderAPI;
import com.haulmont.cuba.core.app.serialization.EntitySerializationAPI;
import com.haulmont.cuba.core.app.serialization.EntitySerializationOption;
import com.haulmont.cuba.core.entity.*;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.restapi.common.RestControllerUtils;
import com.haulmont.restapi.data.CreatedEntityInfo;
import com.haulmont.restapi.data.EntitiesSearchResult;
import com.haulmont.restapi.exception.RestAPIException;
import com.haulmont.restapi.service.filter.RestFilterParseException;
import com.haulmont.restapi.service.filter.RestFilterParseResult;
import com.haulmont.restapi.service.filter.RestFilterParser;
import com.haulmont.restapi.transform.JsonTransformationDirection;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.lang.reflect.Method;
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

    @Inject
    protected PersistenceManagerClient persistenceManagerClient;

    @Inject
    protected RestFilterParser restFilterParser;

    public String loadEntity(String entityName,
                             String entityId,
                             @Nullable String viewName,
                             @Nullable Boolean returnNulls,
                             @Nullable Boolean dynamicAttributes,
                             @Nullable String modelVersion) {

        entityName = restControllerUtils.transformEntityNameIfRequired(entityName, modelVersion, JsonTransformationDirection.FROM_VERSION);
        MetaClass metaClass = restControllerUtils.getMetaClass(entityName);
        checkCanReadEntity(metaClass);

        LoadContext<Entity> ctx = new LoadContext<>(metaClass);
        Object id = getIdFromString(entityId, metaClass);
        ctx.setId(id);

        if (!Strings.isNullOrEmpty(viewName)) {
            View view = restControllerUtils.getView(metaClass, viewName);
            ctx.setView(view);
        }

        ctx.setLoadDynamicAttributes(BooleanUtils.isTrue(dynamicAttributes));

        Entity entity = dataManager.load(ctx);
        checkEntityIsNotNull(entityName, entityId, entity);

        List<EntitySerializationOption> serializationOptions = new ArrayList<>();
        serializationOptions.add(EntitySerializationOption.SERIALIZE_INSTANCE_NAME);
        if (BooleanUtils.isTrue(returnNulls)) serializationOptions.add(EntitySerializationOption.SERIALIZE_NULLS);

        restControllerUtils.applyAttributesSecurity(entity);

        String json = entitySerializationAPI.toJson(entity, ctx.getView(), serializationOptions.toArray(new EntitySerializationOption[0]));
        json = restControllerUtils.transformJsonIfRequired(entityName, modelVersion, JsonTransformationDirection.TO_VERSION, json);
        return json;
    }

    public EntitiesSearchResult loadEntitiesList(String entityName,
                                                 @Nullable String viewName,
                                                 @Nullable Integer limit,
                                                 @Nullable Integer offset,
                                                 @Nullable String sort,
                                                 @Nullable Boolean returnNulls,
                                                 @Nullable Boolean returnCount,
                                                 @Nullable Boolean dynamicAttributes,
                                                 @Nullable String modelVersion) {
        entityName = restControllerUtils.transformEntityNameIfRequired(entityName, modelVersion, JsonTransformationDirection.FROM_VERSION);
        MetaClass metaClass = restControllerUtils.getMetaClass(entityName);
        checkCanReadEntity(metaClass);

        String queryString = "select e from " + entityName + " e";
        String json = _loadEntitiesList(queryString, viewName, limit, offset, sort, returnNulls, dynamicAttributes, modelVersion,
                metaClass, new HashMap<>());

        json = restControllerUtils.transformJsonIfRequired(entityName, modelVersion, JsonTransformationDirection.TO_VERSION, json);

        Long count = null;
        if (BooleanUtils.isTrue(returnCount)) {
            LoadContext ctx = LoadContext.create(metaClass.getJavaClass())
                    .setQuery(LoadContext.createQuery(queryString));
            count = dataManager.getCount(ctx);
        }
        return new EntitiesSearchResult(json, count);

    }

    public EntitiesSearchResult searchEntities(String entityName,
                                               String filterJson,
                                               @Nullable String viewName,
                                               @Nullable Integer limit,
                                               @Nullable Integer offset,
                                               @Nullable String sort,
                                               @Nullable Boolean returnNulls,
                                               @Nullable Boolean returnCount,
                                               @Nullable Boolean dynamicAttributes,
                                               @Nullable String modelVersion) {
        if (filterJson == null) {
            throw new RestAPIException("Cannot parse entities filter", "Entities filter cannot be null", HttpStatus.BAD_REQUEST);
        }

        entityName = restControllerUtils.transformEntityNameIfRequired(entityName, modelVersion, JsonTransformationDirection.FROM_VERSION);
        MetaClass metaClass = restControllerUtils.getMetaClass(entityName);
        checkCanReadEntity(metaClass);

        RestFilterParseResult filterParseResult;
        try {
            filterParseResult = restFilterParser.parse(filterJson, metaClass);
        } catch (RestFilterParseException e) {
            throw new RestAPIException("Cannot parse entities filter", e.getMessage(), HttpStatus.BAD_REQUEST, e);
        }

        String jpqlWhere = filterParseResult.getJpqlWhere().replace("{E}", "e");
        Map<String, Object> queryParameters = filterParseResult.getQueryParameters();

        String queryString = "select e from " + entityName + " e where " + jpqlWhere;
        String json = _loadEntitiesList(queryString, viewName, limit, offset, sort, returnNulls,
                dynamicAttributes, modelVersion, metaClass, queryParameters);
        Long count = null;
        if (BooleanUtils.isTrue(returnCount)) {
            LoadContext ctx = LoadContext.create(metaClass.getJavaClass())
                    .setQuery(LoadContext.createQuery(queryString).setParameters(queryParameters));
            count = dataManager.getCount(ctx);
        }

        return new EntitiesSearchResult(json, count);
    }

    public EntitiesSearchResult searchEntities(String entityName, String searchRequestBody) {
        SearchEntitiesRequestDTO searchEntitiesRequest = new Gson()
                .fromJson(searchRequestBody, SearchEntitiesRequestDTO.class);

        if (searchEntitiesRequest.getFilter() == null) {
            throw new RestAPIException("Cannot parse entities filter", "Entities filter cannot be null", HttpStatus.BAD_REQUEST);
        }

        //for backward compatibility we should support both 'view' and 'viewName' properties. In future the
        //'viewName' parameter will be removed
        String view = !Strings.isNullOrEmpty(searchEntitiesRequest.getView()) ?
                searchEntitiesRequest.getView() :
                searchEntitiesRequest.getViewName();

        return searchEntities(entityName,
                searchEntitiesRequest.getFilter().toString(),
                view,
                searchEntitiesRequest.getLimit(),
                searchEntitiesRequest.getOffset(),
                searchEntitiesRequest.getSort(),
                searchEntitiesRequest.getReturnNulls(),
                searchEntitiesRequest.getReturnCount(),
                searchEntitiesRequest.getDynamicAttributes(),
                searchEntitiesRequest.getModelVersion()
        );
    }

    protected String _loadEntitiesList(String queryString,
                                       @Nullable String viewName,
                                       @Nullable Integer limit,
                                       @Nullable Integer offset,
                                       @Nullable String sort,
                                       @Nullable Boolean returnNulls,
                                       @Nullable Boolean dynamicAttributes,
                                       @Nullable String modelVersion,
                                       MetaClass metaClass,
                                       Map<String, Object> queryParameters) {
        LoadContext<Entity> ctx = new LoadContext<>(metaClass);
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
        } else {
            query.setMaxResults(persistenceManagerClient.getMaxFetchUI(metaClass.getName()));
        }
        if (offset != null) {
            query.setFirstResult(offset);
        }
        if (queryParameters != null) {
            query.setParameters(queryParameters);
        }
        ctx.setQuery(query);

        View view = null;
        if (!Strings.isNullOrEmpty(viewName)) {
            view = restControllerUtils.getView(metaClass, viewName);
            ctx.setView(view);
        }

        ctx.setLoadDynamicAttributes(BooleanUtils.isTrue(dynamicAttributes));

        List<Entity> entities = dataManager.loadList(ctx);
        entities.forEach(entity -> restControllerUtils.applyAttributesSecurity(entity));

        List<EntitySerializationOption> serializationOptions = new ArrayList<>();
        serializationOptions.add(EntitySerializationOption.SERIALIZE_INSTANCE_NAME);
        if (BooleanUtils.isTrue(returnNulls)) serializationOptions.add(EntitySerializationOption.SERIALIZE_NULLS);

        String json = entitySerializationAPI.toJson(entities, view, serializationOptions.toArray(new EntitySerializationOption[0]));
        json = restControllerUtils.transformJsonIfRequired(metaClass.getName(), modelVersion, JsonTransformationDirection.TO_VERSION, json);
        return json;
    }

    public CreatedEntityInfo createEntity(String entityJson, String entityName, String modelVersion) {
        String transformedEntityName = restControllerUtils.transformEntityNameIfRequired(entityName, modelVersion, JsonTransformationDirection.FROM_VERSION);
        MetaClass metaClass = restControllerUtils.getMetaClass(transformedEntityName);
        checkCanCreateEntity(metaClass);

        entityJson = restControllerUtils.transformJsonIfRequired(entityName, modelVersion, JsonTransformationDirection.FROM_VERSION, entityJson);

        Entity entity;
        try {
            entity = entitySerializationAPI.entityFromJson(entityJson, metaClass);
        } catch (Exception e) {
            throw new RestAPIException("Cannot deserialize an entity from JSON", "", HttpStatus.BAD_REQUEST, e);
        }

        EntityImportView entityImportView = entityImportViewBuilderAPI.buildFromJson(entityJson, metaClass);

        Collection<Entity> importedEntities;
        try {
            importedEntities = entityImportExportService.importEntities(Collections.singletonList(entity), entityImportView, true);
        } catch (EntityImportException e) {
            throw new RestAPIException("Entity creation failed", e.getMessage(), HttpStatus.BAD_REQUEST, e);
        }

        //if many entities were created (because of @Composition references) we must find the main entity
        return getMainEntityInfo(importedEntities, metaClass, modelVersion);
    }

    public CreatedEntityInfo updateEntity(String entityJson,
                                          String entityName,
                                          String entityId,
                                          String modelVersion) {
        String transformedEntityName = restControllerUtils.transformEntityNameIfRequired(entityName, modelVersion, JsonTransformationDirection.FROM_VERSION);
        MetaClass metaClass = restControllerUtils.getMetaClass(transformedEntityName);
        checkCanUpdateEntity(metaClass);
        Object id = getIdFromString(entityId, metaClass);

        LoadContext loadContext = new LoadContext(metaClass).setId(id);
        @SuppressWarnings("unchecked")
        Entity existingEntity = dataManager.load(loadContext);

        checkEntityIsNotNull(transformedEntityName, entityId, existingEntity);
        entityJson = restControllerUtils.transformJsonIfRequired(entityName, modelVersion, JsonTransformationDirection.FROM_VERSION, entityJson);

        Entity entity;
        try {
            entity = entitySerializationAPI.entityFromJson(entityJson, metaClass);
        } catch (Exception e) {
            throw new RestAPIException("Cannot deserialize an entity from JSON", "", HttpStatus.BAD_REQUEST, e);
        }

        if (entity instanceof BaseGenericIdEntity) {
            //noinspection unchecked
            ((BaseGenericIdEntity) entity).setId(id);
        }

        EntityImportView entityImportView = entityImportViewBuilderAPI.buildFromJson(entityJson, metaClass);
        Collection<Entity> importedEntities;
        try {
            importedEntities = entityImportExportService.importEntities(Collections.singletonList(entity), entityImportView, true);
            importedEntities.forEach(it-> restControllerUtils.applyAttributesSecurity(it));
        } catch (EntityImportException e) {
            throw new RestAPIException("Entity update failed", e.getMessage(), HttpStatus.BAD_REQUEST, e);
        }
        //there may be multiple entities in importedEntities (because of @Composition references), so we must find
        // the main entity that will be returned
        return getMainEntityInfo(importedEntities, metaClass, modelVersion);
    }

    public void deleteEntity(String entityName,
                             String entityId,
                             String modelVersion) {
        entityName = restControllerUtils.transformEntityNameIfRequired(entityName, modelVersion, JsonTransformationDirection.FROM_VERSION);
        MetaClass metaClass = restControllerUtils.getMetaClass(entityName);
        checkCanDeleteEntity(metaClass);
        Object id = getIdFromString(entityId, metaClass);
        Entity entity = dataManager.load(new LoadContext<>(metaClass).setId(id));
        checkEntityIsNotNull(entityName, entityId, entity);
        dataManager.remove(entity);
    }

    private Object getIdFromString(String entityId, MetaClass metaClass) {
        try {
            if (BaseDbGeneratedIdEntity.class.isAssignableFrom(metaClass.getJavaClass())) {
                if (BaseIdentityIdEntity.class.isAssignableFrom(metaClass.getJavaClass())) {
                    return IdProxy.of(Long.valueOf(entityId));
                } else if (BaseIntIdentityIdEntity.class.isAssignableFrom(metaClass.getJavaClass())) {
                    return IdProxy.of(Integer.valueOf(entityId));
                } else {
                    Class<?> clazz = metaClass.getJavaClass();
                    while (clazz != null) {
                        Method[] methods = clazz.getDeclaredMethods();
                        for (Method method : methods) {
                            if (method.getName().equals("getDbGeneratedId")) {
                                Class<?> idClass = method.getReturnType();
                                if (Long.class.isAssignableFrom(idClass)) {
                                    return Long.valueOf(entityId);
                                } else if (Integer.class.isAssignableFrom(idClass)) {
                                    return Integer.valueOf(entityId);
                                } else if (Short.class.isAssignableFrom(idClass)) {
                                    return Long.valueOf(entityId);
                                } else if (UUID.class.isAssignableFrom(idClass)) {
                                    return UUID.fromString(entityId);
                                }
                            }
                        }
                        clazz = clazz.getSuperclass();
                    }
                }
                throw new UnsupportedOperationException("Unsupported ID type in entity " + metaClass.getName());
            } else {
                //noinspection unchecked
                Method getIdMethod = metaClass.getJavaClass().getMethod("getId");
                Class<?> idClass = getIdMethod.getReturnType();
                if (UUID.class.isAssignableFrom(idClass)) {
                    return UUID.fromString(entityId);
                } else if (Integer.class.isAssignableFrom(idClass)) {
                    return Integer.valueOf(entityId);
                } else if (Long.class.isAssignableFrom(idClass)) {
                    return Long.valueOf(entityId);
                } else {
                    return entityId;
                }
            }
        } catch (Exception e) {
            throw new RestAPIException("Invalid entity ID",
                    String.format("Cannot convert %s into valid entity ID", entityId),
                    HttpStatus.BAD_REQUEST,
                    e);
        }
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
    protected CreatedEntityInfo getMainEntityInfo(Collection<Entity> importedEntities, MetaClass metaClass, String version) {
        Entity mainEntity = null;
        if (importedEntities.size() > 1) {
            Optional<Entity> first = importedEntities.stream().filter(e -> e.getMetaClass().equals(metaClass)).findFirst();
            if (first.isPresent()) mainEntity = first.get();
        } else {
            mainEntity = importedEntities.iterator().next();
        }

        if (mainEntity != null) {
            String json = entitySerializationAPI.toJson(mainEntity);
            json = restControllerUtils.transformJsonIfRequired(metaClass.getName(), version, JsonTransformationDirection.TO_VERSION, json);
            return new CreatedEntityInfo(mainEntity.getId(), json);
        }
        return null;
    }

    protected class SearchEntitiesRequestDTO {
        protected JsonObject filter;
        protected String view;
        @Deprecated //the viewName property has been left for a backward compatibility. It will removed in future releases
        protected String viewName;
        protected Integer limit;
        protected Integer offset;
        protected String sort;
        protected Boolean returnNulls;
        protected Boolean returnCount;
        protected Boolean dynamicAttributes;
        protected String modelVersion;

        public SearchEntitiesRequestDTO() {
        }

        public JsonObject getFilter() {
            return filter;
        }

        public String getView() {
            return view;
        }

        @Deprecated
        public String getViewName() {
            return viewName;
        }

        public Integer getLimit() {
            return limit;
        }

        public Integer getOffset() {
            return offset;
        }

        public String getSort() {
            return sort;
        }

        public Boolean getReturnNulls() {
            return returnNulls;
        }

        public Boolean getReturnCount() {
            return returnCount;
        }

        public Boolean getDynamicAttributes() {
            return dynamicAttributes;
        }

        public String getModelVersion() {
            return modelVersion;
        }

        public void setFilter(JsonObject filter) {
            this.filter = filter;
        }

        public void setView(String view) {
            this.view = view;
        }

        @Deprecated
        public void setViewName(String viewName) {
            this.viewName = viewName;
        }

        public void setLimit(Integer limit) {
            this.limit = limit;
        }

        public void setOffset(Integer offset) {
            this.offset = offset;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public void setReturnNulls(Boolean returnNulls) {
            this.returnNulls = returnNulls;
        }

        public void setReturnCount(Boolean returnCount) {
            this.returnCount = returnCount;
        }

        public void setDynamicAttributes(Boolean dynamicAttributes) {
            this.dynamicAttributes = dynamicAttributes;
        }

        public void setModelVersion(String modelVersion) {
            this.modelVersion = modelVersion;
        }
    }
}