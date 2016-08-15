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

import com.haulmont.cuba.restapi.RestServicePermissions;
import com.haulmont.restapi.service.ServicesControllerManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Map;

/**
 * Controller that is used for service method invocations with the REST API
 */
@RestController
@RequestMapping(value = "/api/services", produces = "application/json; charset=UTF-8")
public class ServicesController {

    @Inject
    protected ServicesControllerManager servicesControllerManager;

    @PostMapping("/{serviceName}/{methodName}")
    public ResponseEntity<String> invokeServiceMethodPost(@PathVariable String serviceName,
                                                          @PathVariable String methodName,
                                                          @RequestBody(required = false) String paramsJson) {
        String result = servicesControllerManager.invokeServiceMethodPost(serviceName, methodName, paramsJson);
        HttpStatus status;
        if (result == null) {
            status = HttpStatus.NO_CONTENT;
            result = "";
        } else {
            status = HttpStatus.OK;
        }
        return new ResponseEntity<>(result, status);
    }

    @GetMapping("/{serviceName}/{methodName}")
    public ResponseEntity<String> invokeServiceMethodGet(@PathVariable String serviceName,
                                                         @PathVariable String methodName,
                                                         @RequestParam Map<String, String> paramsMap) {
        String result = servicesControllerManager.invokeServiceMethodGet(serviceName, methodName, paramsMap);
        HttpStatus status;
        if (result == null) {
            status = HttpStatus.NO_CONTENT;
            result = "";
        } else {
            status = HttpStatus.OK;
        }
        return new ResponseEntity<>(result, status);
    }

    @GetMapping
    public Collection<RestServicePermissions.ServiceInfo> getServiceInfos() {
        return servicesControllerManager.getServiceInfos();
    }

    @GetMapping("/{serviceName}")
    public RestServicePermissions.ServiceInfo getServiceInfo(@PathVariable String serviceName) {
        return servicesControllerManager.getServiceInfo(serviceName);
    }
}
