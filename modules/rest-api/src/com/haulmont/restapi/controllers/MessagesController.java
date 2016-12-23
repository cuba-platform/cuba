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

import com.haulmont.restapi.service.MessagesControllerManager;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.Map;

/**
 * Controller that returns localized messages
 */
@RestController
@RequestMapping(value = "/v2/messages", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class MessagesController {

    @Inject
    protected MessagesControllerManager messagesControllerManager;

    @GetMapping("/entities/{entityName}")
    public Map<String, String> getLocalizationForEntity(@PathVariable String entityName) {
        return messagesControllerManager.getLocalizationForEntity(entityName);
    }

    @GetMapping("/entities")
    public Map<String, String> getLocalizationForAllEntities() {
        return messagesControllerManager.getLocalizationForAllEntities();
    }

    @GetMapping("/enums/{enumClassName:.+}")
    public Map<String, String> getLocalizationForEnum(@PathVariable String enumClassName) {
        return messagesControllerManager.getLocalizationForEnum(enumClassName);
    }

    @GetMapping("/enums")
    public Map<String, String> getLocalizationForAllEnums() {
        return messagesControllerManager.getLocalizationForAllEnums();
    }
}
