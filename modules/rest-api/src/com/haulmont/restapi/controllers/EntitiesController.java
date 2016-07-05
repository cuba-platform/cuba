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

import com.google.common.base.Strings;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.app.importexport.EntityImportExportService;
import com.haulmont.cuba.core.app.importexport.EntityImportView;
import com.haulmont.cuba.core.app.serialization.EntitySerializationAPI;
import com.haulmont.cuba.core.app.importexport.EntityImportViewBuilderAPI;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.Security;
import com.haulmont.restapi.exception.RestAPIException;
import com.haulmont.cuba.security.entity.EntityOp;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Controller that performs CRUD entity operations
 */
@RestController
@RequestMapping(value = "/api/entities", produces = "application/json; charset=UTF-8")
public class EntitiesController {

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

    @RequestMapping(method = RequestMethod.GET, path = "/{entityName}/{entityId}")
    public String loadEntity(@PathVariable String entityName,
                             @PathVariable String entityId,
                             @RequestParam(required = false) String view) {
        MetaClass metaClass = getMetaClass(entityName);

        checkCanReadEntity(metaClass);

        LoadContext<Entity> ctx = new LoadContext<>(metaClass);
        Object id = getIdFromString(entityId, metaClass);
        ctx.setId(id);

        if (!Strings.isNullOrEmpty(view)) {
            ctx.setView(view);
        }

        Entity entity = dataManager.load(ctx);
        checkEntityIsNotNull(entityName, entityId, entity);

        return entitySerializationAPI.toJson(entity);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{entityName}")
    public String loadEntitiesList(@PathVariable String entityName,
                                   @RequestParam(required = false) String view,
                                   @RequestParam(required = false) Integer limit,
                                   @RequestParam(required = false) Integer offset,
                                   @RequestParam(required = false) String sort) {
        MetaClass metaClass = getMetaClass(entityName);
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
        return entitySerializationAPI.toJson(entities, null);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/{entityName}")
    public ResponseEntity<String> createEntity(@RequestBody String entityJson,
                                       @PathVariable String entityName,
                                       UriComponentsBuilder uriComponentsBuilder) {
        MetaClass metaClass = getMetaClass(entityName);
        checkCanCreateEntity(metaClass);
        //todo MG catch invalid json
        Entity entity = entitySerializationAPI.entityFromJson(entityJson, metaClass);
        EntityImportView entityImportView = entityImportViewBuilderAPI.buildFromJson(entityJson, metaClass);
        entityImportExportService.importEntities(Collections.singletonList(entity), entityImportView);

        UriComponents uriComponents = uriComponentsBuilder
                .path("/{id}")
                .buildAndExpand(entity.getId().toString());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriComponents.toUri());
        //todo MG return response body?
        return new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/{entityName}/{entityId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateEntity(@RequestBody String entityJson,
                             @PathVariable String entityName,
                             @PathVariable String entityId) {
        MetaClass metaClass = getMetaClass(entityName);
        checkCanUpdateEntity(metaClass);
        Object id = getIdFromString(entityId, metaClass);
        Entity existingEntity = dataManager.load(new LoadContext(metaClass).setId(id));
        checkEntityIsNotNull(entityName, entityId, existingEntity);
        Entity entity = entitySerializationAPI.entityFromJson(entityJson, metaClass);
        EntityImportView entityImportView = entityImportViewBuilderAPI.buildFromJson(entityJson, metaClass);
        entityImportExportService.importEntities(Collections.singletonList(entity), entityImportView);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/{entityName}/{entityId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteEntity(@PathVariable String entityName,
                             @PathVariable String entityId) {
        MetaClass metaClass = metadata.getClass(entityName);
        checkCanDeleteEntity(metaClass);
        Object id = getIdFromString(entityId, metaClass);
        Entity entity = dataManager.load(new LoadContext<>(metaClass).setId(id));
        checkEntityIsNotNull(entityName, entityId, entity);
        dataManager.remove(entity);
    }

    private Object getIdFromString(@PathVariable String entityId, MetaClass metaClass) {
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

    /**
     * Finds metaClass by entityName. Throws a RestAPIException if metaClass not found
     */
    protected MetaClass getMetaClass(String entityName) {
        MetaClass metaClass = metadata.getClass(entityName);
        if (metaClass == null) {
            throw new RestAPIException("MetaClass not found",
                    String.format("MetaClass %s not found", entityName),
                    HttpStatus.NOT_FOUND);
        }

        return metaClass;
    }

    protected void checkEntityIsNotNull(@PathVariable String entityName, @PathVariable String entityId, Entity entity) {
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
}
