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

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.app.importexport.EntityImportExportService;
import com.haulmont.cuba.core.app.importexport.EntityImportView;
import com.haulmont.cuba.core.app.importexport.EntityImportViewBuilderAPI;
import com.haulmont.cuba.core.app.serialization.EntitySerializationAPI;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.Security;
import com.haulmont.cuba.security.entity.EntityOp;
import com.haulmont.restapi.common.RestControllerUtils;
import com.haulmont.restapi.data.CreatedEntityInfo;
import com.haulmont.restapi.exception.RestAPIException;
import com.haulmont.restapi.service.EntitiesControllerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Controller that performs CRUD entity operations
 */
@RestController
@RequestMapping(value = "/api/entities", produces = "application/json; charset=UTF-8")
public class EntitiesController {

    protected Logger log = LoggerFactory.getLogger(EntitiesController.class);

    @Inject
    protected EntitiesControllerManager entitiesControllerManager;

    @GetMapping("/{entityName}/{entityId}")
    public String loadEntity(@PathVariable String entityName,
                             @PathVariable String entityId,
                             @RequestParam(required = false) String view,
                             @RequestParam(required = false) Boolean returnNulls) {
        return entitiesControllerManager.loadEntity(entityName, entityId, view, returnNulls);
    }

    @GetMapping("/{entityName}")
    public String loadEntitiesList(@PathVariable String entityName,
                                   @RequestParam(required = false) String view,
                                   @RequestParam(required = false) Integer limit,
                                   @RequestParam(required = false) Integer offset,
                                   @RequestParam(required = false) String sort,
                                   @RequestParam(required = false) Boolean returnNulls) {
        return entitiesControllerManager.loadEntitiesList(entityName, view, limit, offset, sort, returnNulls);
    }

    @PostMapping("/{entityName}")
    public ResponseEntity<String> createEntity(@RequestBody String entityJson,
                                               @PathVariable String entityName,
                                               HttpServletRequest request) {
        CreatedEntityInfo entityInfo = entitiesControllerManager.createEntity(entityJson, entityName);

        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString())
                .path("/{id}")
                .buildAndExpand(entityInfo.getId().toString());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uriComponents.toUri());
        return new ResponseEntity<>(entityInfo.getJson(), httpHeaders, HttpStatus.CREATED);
    }

    @PutMapping("/{entityName}/{entityId}")
    public String updateEntity(@RequestBody String entityJson,
                             @PathVariable String entityName,
                             @PathVariable String entityId) {
        CreatedEntityInfo entityInfo = entitiesControllerManager.updateEntity(entityJson, entityName, entityId);
        return entityInfo.getJson();
    }

    @DeleteMapping(path = "/{entityName}/{entityId}")
    public void deleteEntity(@PathVariable String entityName,
                             @PathVariable String entityId) {
        entitiesControllerManager.deleteEntity(entityName, entityId);
    }
}
