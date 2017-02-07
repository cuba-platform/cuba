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
import com.haulmont.cuba.core.app.serialization.EntitySerializationOption;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.restapi.common.RestControllerUtils;
import com.haulmont.restapi.common.RestParseUtils;
import com.haulmont.restapi.config.RestServicesConfiguration;
import com.haulmont.restapi.exception.RestAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.validation.ValidationException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Class that executes business logic required by the {@link com.haulmont.restapi.controllers.ServicesController}. It
 * performs middleware services invocations.
 */
@Component("cuba_ServicesControllerManager")
public class ServicesControllerManager {

    @Inject
    protected RestServicesConfiguration restServicesConfiguration;

    @Inject
    protected EntitySerializationAPI entitySerializationAPI;

    @Inject
    protected RestParseUtils restParseUtils;

    @Inject
    protected RestControllerUtils restControllerUtils;

    protected Logger log = LoggerFactory.getLogger(ServicesControllerManager.class);

    @Nullable
    public ServiceCallResult invokeServiceMethodGet(String serviceName, String methodName, Map<String, String> paramsMap) {
        List<String> paramNames = new ArrayList<>(paramsMap.keySet());
        List<String> paramValuesStr = new ArrayList<>(paramsMap.values());
        return _invokeServiceMethod(serviceName, methodName, paramNames, paramValuesStr);
    }

    @Nullable
    public ServiceCallResult invokeServiceMethodPost(String serviceName, String methodName, String paramsJson) {
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
            if (paramValue.isJsonNull()) {
                result.put(paramName, null);
            } else if (paramValue.isJsonPrimitive()) {
                result.put(paramName, paramValue.getAsString());
            } else {
                result.put(paramName, paramValue.toString());
            }
        }

        return result;
    }

    @Nullable
    protected ServiceCallResult _invokeServiceMethod(String serviceName, String methodName, List<String> paramNames, List<String> paramValuesStr) {
        Object service = AppBeans.get(serviceName);
        Method serviceMethod = restServicesConfiguration.getServiceMethod(serviceName, methodName, paramNames);
        if (serviceMethod == null) {
            throw new RestAPIException("Service method not found",
                    serviceName + "." + methodName + "(" + paramNames.stream().collect(Collectors.joining(",")) + ")",
                    HttpStatus.NOT_FOUND);
        }
        List<Object> paramValues = new ArrayList<>();
        Type[] types = serviceMethod.getGenericParameterTypes();
        for (int i = 0; i < types.length; i++) {
            try {
                paramValues.add(restParseUtils.toObject(types[i], paramValuesStr.get(i)));
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
        } catch (InvocationTargetException | IllegalAccessException ex) {
            if (ex.getCause() instanceof ValidationException) {
                throw (ValidationException) ex.getCause();
            } else {
                log.error("Error on service method invoke", ex.getCause());
                throw new RestAPIException("Error on service method invoke", "", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        if (methodResult == null) {
            return null;
        }

        Class<?> methodReturnType = serviceMethod.getReturnType();
        if (Entity.class.isAssignableFrom(methodReturnType)) {
            Entity entity = (Entity) methodResult;
            restControllerUtils.applyAttributesSecurity(entity);
            String entityJson = entitySerializationAPI.toJson(entity,
                    null,
                    EntitySerializationOption.SERIALIZE_INSTANCE_NAME);
            return new ServiceCallResult(entityJson, true);
        } else if (Collection.class.isAssignableFrom(methodReturnType)) {
            if (isEntitiesCollection((Collection) methodResult)) {
                Collection<? extends Entity> entities = (Collection<? extends Entity>) methodResult;
                entities.forEach(entity -> restControllerUtils.applyAttributesSecurity(entity));
                String entitiesJson = entitySerializationAPI.toJson(entities,
                        null,
                        EntitySerializationOption.SERIALIZE_INSTANCE_NAME);
                return new ServiceCallResult(entitiesJson, true);
            } else {
                return new ServiceCallResult(restParseUtils.serialize(methodResult), true);
            }
        } else {
            Datatype<?> datatype = Datatypes.get(methodReturnType);
            if (datatype != null) {
                return new ServiceCallResult(datatype.format(methodResult), false);
            } else {
                return new ServiceCallResult(restParseUtils.serializePOJO(methodResult, methodReturnType), true);
            }
        }
    }

    protected boolean isEntitiesCollection(Collection collection) {
        for (Object item : collection) {
            if (!(item instanceof Entity)) {
                return false;
            }
        }
        return true;
    }

    public static class ServiceCallResult {
        protected String stringValue;
        protected boolean validJson;

        public ServiceCallResult(String stringValue, boolean validJson) {
            this.stringValue = stringValue;
            this.validJson = validJson;
        }

        public boolean isValidJson() {
            return validJson;
        }

        public String getStringValue() {
            return stringValue;
        }
    }
}
