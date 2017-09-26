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

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.haulmont.restapi.data.CreatedEntityInfo;
import com.haulmont.restapi.data.EntitiesSearchResult;
import com.haulmont.restapi.service.EntitiesControllerManager;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * Controller that performs CRUD entity operations
 */
@RestController
@RequestMapping(value = "/v2/entities", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class EntitiesController {

    @Inject
    protected EntitiesControllerManager entitiesControllerManager;

    @GetMapping("/{entityName}/{entityId}")
    public String loadEntity(@PathVariable String entityName,
                             @PathVariable String entityId,
                             @RequestParam(required = false) String view,
                             @RequestParam(required = false) Boolean returnNulls,
                             @RequestParam(required = false) Boolean dynamicAttributes,
                             @RequestParam(required = false) String modelVersion) {
        return entitiesControllerManager.loadEntity(entityName, entityId, view, returnNulls, dynamicAttributes, modelVersion);
    }

    @GetMapping("/{entityName}")
    public ResponseEntity<String> loadEntitiesList(@PathVariable String entityName,
                                                   @RequestParam(required = false) String view,
                                                   @RequestParam(required = false) Integer limit,
                                                   @RequestParam(required = false) Integer offset,
                                                   @RequestParam(required = false) String sort,
                                                   @RequestParam(required = false) Boolean returnNulls,
                                                   @RequestParam(required = false) Boolean returnCount,
                                                   @RequestParam(required = false) Boolean dynamicAttributes,
                                                   @RequestParam(required = false) String modelVersion) {
        EntitiesSearchResult entitiesSearchResult = entitiesControllerManager.loadEntitiesList(entityName, view, limit,
                offset, sort, returnNulls, returnCount, dynamicAttributes, modelVersion);
        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(HttpStatus.OK);
        if (BooleanUtils.isTrue(returnCount)) {
            responseBuilder.header("X-Total-Count", entitiesSearchResult.getCount().toString());
        }
        return responseBuilder.body(entitiesSearchResult.getJson());
    }

    @GetMapping("/{entityName}/search")
    public ResponseEntity<String> searchEntitiesListGet(@PathVariable String entityName,
                                                        @RequestParam String filter,
                                                        @RequestParam(required = false) String view,
                                                        @RequestParam(required = false) Integer limit,
                                                        @RequestParam(required = false) Integer offset,
                                                        @RequestParam(required = false) String sort,
                                                        @RequestParam(required = false) Boolean returnNulls,
                                                        @RequestParam(required = false) Boolean returnCount,
                                                        @RequestParam(required = false) Boolean dynamicAttributes,
                                                        @RequestParam(required = false) String modelVersion) {
        EntitiesSearchResult entitiesSearchResult = entitiesControllerManager.searchEntities(entityName, filter,
                view, limit, offset, sort, returnNulls, returnCount, dynamicAttributes, modelVersion);
        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(HttpStatus.OK);
        if (BooleanUtils.isTrue(returnCount)) {
            responseBuilder.header("X-Total-Count", entitiesSearchResult.getCount().toString());
        }
        return responseBuilder.body(entitiesSearchResult.getJson());
    }

    @PostMapping("/{entityName}/search")
    public ResponseEntity<String> searchEntitiesListPost(@PathVariable String entityName,
                                                         @RequestBody String requestBodyJson) {
        EntitiesSearchResult entitiesSearchResult = entitiesControllerManager.searchEntities(entityName, requestBodyJson);
        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(HttpStatus.OK);
        JsonObject requestJsonObject = new JsonParser().parse(requestBodyJson).getAsJsonObject();
        JsonPrimitive returnCount = requestJsonObject.getAsJsonPrimitive("returnCount");
        if (returnCount != null && returnCount.getAsBoolean()) {
            responseBuilder.header("X-Total-Count", entitiesSearchResult.getCount().toString());
        }
        return responseBuilder.body(entitiesSearchResult.getJson());
    }

    @PostMapping("/{entityName}")
    public ResponseEntity<String> createEntity(@RequestBody String entityJson,
                                               @PathVariable String entityName,
                                               @RequestParam(required = false) String modelVersion,
                                               HttpServletRequest request) {
        CreatedEntityInfo entityInfo = entitiesControllerManager.createEntity(entityJson, entityName, modelVersion);

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
                               @PathVariable String entityId,
                               @RequestParam(required = false) String modelVersion) {
        CreatedEntityInfo entityInfo = entitiesControllerManager.updateEntity(entityJson, entityName, entityId, modelVersion);
        return entityInfo.getJson();
    }

    @DeleteMapping(path = "/{entityName}/{entityId}")
    public void deleteEntity(@PathVariable String entityName,
                             @PathVariable String entityId,
                             @RequestParam(required = false) String modelVersion) {
        entitiesControllerManager.deleteEntity(entityName, entityId, modelVersion);
    }
}