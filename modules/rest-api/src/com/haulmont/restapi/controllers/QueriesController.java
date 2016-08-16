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

import com.haulmont.restapi.query.RestQueriesManager;
import com.haulmont.restapi.service.QueriesControllerManager;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.text.ParseException;
import java.util.List;
import java.util.Map;


/**
 * Controller that is used for operations with predefined JPQL queries
 */
@RestController
@RequestMapping("/api/queries")
public class QueriesController {

    @Inject
    protected QueriesControllerManager queriesControllerManager;

    @GetMapping("/{entityName}/{queryName}")
    public String executeQuery(@PathVariable String entityName,
                               @PathVariable String queryName,
                               @RequestParam(required = false) Integer limit,
                               @RequestParam(required = false) Integer offset,
                               @RequestParam Map<String, String> params) throws ClassNotFoundException, ParseException {
        return queriesControllerManager.executeQuery(entityName, queryName, limit, offset, params);
    }

    @GetMapping("/{entityName}/{queryName}/count")
    public String getCount(@PathVariable String entityName,
                           @PathVariable String queryName,
                           @RequestParam Map<String, String> params) throws ClassNotFoundException, ParseException {
        return queriesControllerManager.getCount(entityName, queryName, params);
    }

    @GetMapping("/{entityName}")
    public List<RestQueriesManager.QueryInfo> loadQueriesList(@PathVariable String entityName) {
        return queriesControllerManager.loadQueriesList(entityName);
    }
}
