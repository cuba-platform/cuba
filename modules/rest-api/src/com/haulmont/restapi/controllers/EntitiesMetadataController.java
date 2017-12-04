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

import com.haulmont.restapi.data.MetaClassInfo;
import com.haulmont.restapi.service.EntitiesMetadataControllerManager;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.Collection;

/**
 * Controller that is used for getting entities metadata. User permissions for entities access aren't taken into account
 * at the moment.
 */
@RestController("cuba_EntitiesMetadataController")
@RequestMapping(value = "/v2/metadata", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class EntitiesMetadataController {
    @Inject
    protected EntitiesMetadataControllerManager controllerManager;

    @GetMapping("/entities/{entityName}")
    public MetaClassInfo getMetaClassInfo(@PathVariable String entityName) {
        return controllerManager.getMetaClassInfo(entityName);
    }

    @GetMapping("/entities")
    public Collection<MetaClassInfo> getAllMetaClassesInfo() {
        return controllerManager.getAllMetaClassesInfo();
    }

    @GetMapping("/entities/{entityName}/views/{viewName}")
    public String getView(@PathVariable String entityName,
                          @PathVariable String viewName) {
        return controllerManager.getView(entityName, viewName);
    }

    @GetMapping("/entities/{entityName}/views")
    public String getAllViewsForMetaClass(@PathVariable String entityName) {
        return controllerManager.getAllViewsForMetaClass(entityName);
    }
}