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
import org.springframework.http.MediaType;
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
    public String executeQuery(@PathVariable String entityName,
                               @PathVariable String queryName,
                               @RequestParam(required = false) Integer limit,
                               @RequestParam(required = false) Integer offset,
                               @RequestParam Map<String, String> params,
                               @RequestParam(required = false) boolean dynamicAttributes) throws ClassNotFoundException, ParseException {
        return queriesControllerManager.executeQuery(entityName, queryName, limit, offset, params, dynamicAttributes);
    }

    @GetMapping(value = "/{entityName}/{queryName}/count", produces = "text/plain;charset=UTF-8")
    public String getCount(@PathVariable String entityName,
                           @PathVariable String queryName,
                           @RequestParam Map<String, String> params) throws ClassNotFoundException, ParseException {
        return queriesControllerManager.getCount(entityName, queryName, params);
    }

    @GetMapping("/{entityName}")
    public List<RestQueriesConfiguration.QueryInfo> loadQueriesList(@PathVariable String entityName) {
        return queriesControllerManager.loadQueriesList(entityName);
    }
}