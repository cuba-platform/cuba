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

package com.haulmont.restapi.service;

import com.google.common.base.Strings;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.cuba.core.app.serialization.EntitySerializationAPI;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.restapi.common.RestParseUtils;
import com.haulmont.restapi.config.RestServicesConfiguration;
import com.haulmont.restapi.exception.RestAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.*;


/**
 * Class that executes business logic required by the {@link com.haulmont.restapi.controllers.ServicesController}. It
 * performs middleware services invocations.
 */
public class ServicesControllerManager {

    @Inject
    protected RestServicesConfiguration restServicesConfiguration;

    @Inject
    protected EntitySerializationAPI entitySerializationAPI;

    @Inject
    protected RestParseUtils restParseUtils;

    protected Logger log = LoggerFactory.getLogger(ServicesControllerManager.class);

    @Nullable
    public String invokeServiceMethodGet(String serviceName, String methodName, Map<String, String> paramsMap) {
        List<String> paramNames = new ArrayList<>(paramsMap.keySet());
        List<String> paramValuesStr = new ArrayList<>(paramsMap.values());
        return _invokeServiceMethod(serviceName, methodName, paramNames, paramValuesStr);
    }

    @Nullable
    public String invokeServiceMethodPost(String serviceName, String methodName, String paramsJson) {
        Map<String, String> paramsMap = parseParamsJson(paramsJson);
        List<String> paramNames = new ArrayList<>(paramsMap.keySet());
        List<String> paramValuesStr = new ArrayList<>(paramsMap.values());
        return _invokeServiceMethod(serviceName, methodName, paramNames, paramValuesStr);
    }

    public Collection<RestServicesConfiguration.RestServiceInfo> getServiceInfos() {
        return restServicesConfiguration.getServiceInfos();
    }

    public RestServicesConfiguration.RestServiceInfo getServiceInfo(String serviceName) {
        RestServicesConfiguration.RestServiceInfo serviceInfo = restServicesConfiguration.getServiceInfo(serviceName);
        if (serviceInfo == null) {
            throw new RestAPIException("Service not found",
                    String.format("Service %s not found", serviceName),
                    HttpStatus.NOT_FOUND);
        }
        return serviceInfo;
    }

    private Map<String, String> parseParamsJson(String paramsJson) {
        Map<String, String> result = new LinkedHashMap<>();
        if (Strings.isNullOrEmpty(paramsJson)) return result;

        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(paramsJson).getAsJsonObject();

        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            String paramName = entry.getKey();
            JsonElement paramValue = entry.getValue();
            if (paramValue.isJsonPrimitive()) {
                result.put(paramName, paramValue.getAsString());
            } else {
                result.put(paramName, paramValue.toString());
            }
        }

        return result;
    }

    @Nullable
    protected String _invokeServiceMethod(String serviceName, String methodName, List<String> paramNames, List<String> paramValuesStr) {
        Object service = AppBeans.get(serviceName);
        Method serviceMethod = restServicesConfiguration.getServiceMethod(serviceName, methodName, paramNames);
        if (serviceMethod == null) {
            throw new RestAPIException("Service method not found",
                    "Service method not found",
                    HttpStatus.NOT_FOUND);
        }
        List<Object> paramValues = new ArrayList<>();
        Class<?>[] types = serviceMethod.getParameterTypes();
        for (int i = 0; i < types.length; i++) {
            Class<?> aClass = types[i];
            try {
                paramValues.add(restParseUtils.toObject(aClass, paramValuesStr.get(i)));
            } catch (ParseException e) {
                log.error("Error on parsing service param value", e);
                throw new RestAPIException("Invalid parameter value",
                        "",
                        HttpStatus.BAD_REQUEST);
            }
        }

        Object methodResult;
        try {
            methodResult = serviceMethod.invoke(service, paramValues.toArray());
        } catch (Exception e) {
            log.error("Error on service method invoke", e);
            throw new RestAPIException("Error on service method invoke", "", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (methodResult == null) return null;

        Class<?> methodReturnType = serviceMethod.getReturnType();
        if (Entity.class.isAssignableFrom(methodReturnType)) {
            return entitySerializationAPI.toJson((Entity) methodResult);
        } else if (Collection.class.isAssignableFrom(methodReturnType)) {
            return entitySerializationAPI.toJson((Collection<? extends Entity>) methodResult);
        } else {
            Datatype<?> datatype = Datatypes.get(methodReturnType);
            if (datatype != null) {
                return datatype.format(methodResult);
            } else {
                return restParseUtils.serializePOJO(methodResult, methodReturnType);
            }
        }
    }
}
