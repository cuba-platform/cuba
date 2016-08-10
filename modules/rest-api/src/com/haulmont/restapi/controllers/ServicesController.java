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
import com.haulmont.restapi.exception.RestAPIException;
import com.haulmont.restapi.service.RestServiceInvoker;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.*;

/**
 * Controller that is used for service method invocations with the REST API
 */
@RestController
@RequestMapping(value = "/api/services", produces = "application/json; charset=UTF-8")
public class ServicesController {

    @Inject
    protected RestServiceInvoker restServiceInvoker;

    @Inject
    protected RestServicePermissions restServicePermissions;

    @RequestMapping(path = "/{serviceName}/{methodName}", method = RequestMethod.POST)
    public ResponseEntity<String> invokeServiceMethodPost(@PathVariable String serviceName,
                                                          @PathVariable String methodName,
                                                          @RequestBody(required = false) String paramsJson) {
        checkServicePermissions(serviceName, methodName);
        String result = restServiceInvoker.invokeServiceMethod(serviceName, methodName, paramsJson);
        HttpStatus status;
        if (result == null) {
            status = HttpStatus.NO_CONTENT;
            result = "";
        } else {
            status = HttpStatus.OK;
        }
        return new ResponseEntity<>(result, status);
    }

    @RequestMapping(path = "/{serviceName}/{methodName}", method = RequestMethod.GET)
    public ResponseEntity<String> invokeServiceMethodGet(@PathVariable String serviceName,
                                         @PathVariable String methodName,
                                         @RequestParam Map<String, String> paramsMap) {
        checkServicePermissions(serviceName, methodName);
        String result  = restServiceInvoker.invokeServiceMethod(serviceName, methodName, paramsMap);
        HttpStatus status;
        if (result == null) {
            status = HttpStatus.NO_CONTENT;
            result = "";
        } else {
            status = HttpStatus.OK;
        }
        return new ResponseEntity<>(result, status);
    }

    @RequestMapping(method = RequestMethod.GET)
    public Collection<RestServicePermissions.ServiceInfo> getServiceInfos() {
        return restServicePermissions.getServiceInfos();
    }

    @RequestMapping(path = "/{serviceName}", method = RequestMethod.GET)
    public RestServicePermissions.ServiceInfo getServiceInfo(@PathVariable String serviceName) {
        RestServicePermissions.ServiceInfo serviceInfo = restServicePermissions.getServiceInfo(serviceName);
        if (serviceInfo == null) {
            throw new RestAPIException("Service not allowed",
                    "The service with the given name not allowed for using with REST API",
                    HttpStatus.FORBIDDEN);
        }
        return serviceInfo;
    }

    protected void checkServicePermissions(String serviceName, String methodName) {
        if (!restServicePermissions.isPermitted(serviceName, methodName)) {
            throw new RestAPIException("Method not available",
                    String.format("Method %s of service %s is not available", methodName, serviceName),
                    HttpStatus.FORBIDDEN);
        }
    }
}
