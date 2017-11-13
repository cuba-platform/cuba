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

import com.haulmont.restapi.config.RestQueriesConfiguration;
import com.haulmont.restapi.service.QueriesControllerManager;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * Controller that is used for operations with predefined JPQL queries
 */
@RestController
@RequestMapping(value = "/v2/queries", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class QueriesController {

    @Inject
    protected QueriesControllerManager queriesControllerManager;

    @GetMapping("/{entityName}/{queryName}")
    public ResponseEntity<String> executeQueryGet(@PathVariable String entityName,
                               @PathVariable String queryName,
                               @RequestParam(required = false) Integer limit,
                               @RequestParam(required = false) Integer offset,
                               @RequestParam(required = false) String view,
                               @RequestParam(required = false) Boolean returnNulls,
                               @RequestParam(required = false) Boolean dynamicAttributes,
                               @RequestParam(required = false) Boolean returnCount,
                               @RequestParam(required = false) String modelVersion,
                               @RequestParam Map<String, String> params) {
        String resultJson = queriesControllerManager.executeQueryGet(entityName, queryName, limit, offset, view, returnNulls, dynamicAttributes, modelVersion, params);
        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(HttpStatus.OK);
        if (BooleanUtils.isTrue(returnCount)) {
            String count = queriesControllerManager.getCountGet(entityName, queryName, modelVersion, params);
            responseBuilder.header("X-Total-Count", count);
        }
        return responseBuilder.body(resultJson);
    }

    @PostMapping("/{entityName}/{queryName}")
    public ResponseEntity<String> executeQueryPost(@PathVariable String entityName,
                               @PathVariable String queryName,
                               @RequestParam(required = false) Integer limit,
                               @RequestParam(required = false) Integer offset,
                               @RequestParam(required = false) String view,
                               @RequestParam(required = false) Boolean returnNulls,
                               @RequestParam(required = false) Boolean dynamicAttributes,
                               @RequestParam(required = false) Boolean returnCount,
                               @RequestParam(required = false) String modelVersion,
                               @RequestBody String paramsJson) {

        String resultJson = queriesControllerManager.executeQueryPost(entityName, queryName, limit, offset, view, returnNulls, dynamicAttributes, modelVersion, paramsJson);
        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(HttpStatus.OK);
        if (BooleanUtils.isTrue(returnCount)) {
            String count = queriesControllerManager.getCountPost(entityName, queryName, modelVersion, paramsJson);
            responseBuilder.header("X-Total-Count", count);
        }
        return responseBuilder.body(resultJson);
    }

    @GetMapping(value = "/{entityName}/{queryName}/count")
    public String getCountGet(@PathVariable String entityName,
                              @PathVariable String queryName,
                              @RequestParam(required = false) String modelVersion,
                              @RequestParam Map<String, String> params) throws ClassNotFoundException, ParseException {
        return queriesControllerManager.getCountGet(entityName, queryName, modelVersion, params);
    }

    @GetMapping(value = "/{entityName}/{queryName}/count", produces = "text/plain;charset=UTF-8")
    public String getCountGetText(@PathVariable String entityName,
                              @PathVariable String queryName,
                              @RequestParam(required = false) String modelVersion,
                              @RequestParam Map<String, String> params) throws ClassNotFoundException, ParseException {
        return queriesControllerManager.getCountGet(entityName, queryName, modelVersion, params);
    }

    @PostMapping(value = "/{entityName}/{queryName}/count")
    public String getCountPost(@PathVariable String entityName,
                           @PathVariable String queryName,
                           @RequestParam(required = false) String modelVersion,
                           @RequestBody String paramsJson) throws ClassNotFoundException, ParseException {
        return queriesControllerManager.getCountPost(entityName, queryName, modelVersion, paramsJson);
    }

    @PostMapping(value = "/{entityName}/{queryName}/count", produces = "text/plain;charset=UTF-8")
    public String getCountPostText(@PathVariable String entityName,
                           @PathVariable String queryName,
                           @RequestParam(required = false) String modelVersion,
                           @RequestBody String paramsJson) throws ClassNotFoundException, ParseException {
        return queriesControllerManager.getCountPost(entityName, queryName, modelVersion, paramsJson);
    }

    @GetMapping("/{entityName}")
    public List<RestQueriesConfiguration.QueryInfo> loadQueriesList(@PathVariable String entityName) {
        return queriesControllerManager.loadQueriesList(entityName);
    }
}