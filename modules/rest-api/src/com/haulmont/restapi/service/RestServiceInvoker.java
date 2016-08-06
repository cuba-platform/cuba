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
import com.haulmont.restapi.exception.RestAPIException;
import org.springframework.http.HttpStatus;
import org.springframework.util.ClassUtils;

import javax.inject.Inject;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Class is used for invoking middleware services from REST API service controller
 */
public class RestServiceInvoker {

    @Inject
    protected RestParseUtils restParseUtils;

    @Inject
    protected EntitySerializationAPI entitySerializationAPI;

    public String invokeServiceMethod(String serviceName, String methodName, String paramsJson) {
        List<String> paramValuesStr = new ArrayList<>();
        List<Class> paramTypes = new ArrayList<>();

        parseParamsJson(paramsJson, paramValuesStr, paramTypes);

        return _invokeServiceMethod(serviceName, methodName, paramValuesStr, paramTypes);
    }

    public String invokeServiceMethod(String serviceName, String methodName, Map<String, String> paramsMap) {
        List<String> paramValuesStr = new ArrayList<>();
        List<Class> paramTypes = new ArrayList<>();

        parseParamsJson(paramsMap, paramValuesStr, paramTypes);

        return _invokeServiceMethod(serviceName, methodName, paramValuesStr, paramTypes);
    }

    protected String _invokeServiceMethod(String serviceName, String methodName, List<String> paramValuesStr, List<Class> paramTypes) {
        if (!paramTypes.isEmpty() && paramValuesStr.size() != paramTypes.size()) {
            throw new RestAPIException(
                    "Wrong request parameters",
                    "The number of parameters that define method argument values should be equal to the number of " +
                            "parameters that define method argument types",
                    HttpStatus.BAD_REQUEST
            );
        }

        Object service = AppBeans.get(serviceName);
        Method serviceMethod = findMethod(service, methodName, paramValuesStr, paramTypes);
        List<Object> paramValues = new ArrayList<>();
        Class<?>[] types = serviceMethod.getParameterTypes();
        for (int i = 0; i < types.length; i++) {
            Class<?> aClass = types[i];
            try {
                paramValues.add(restParseUtils.toObject(aClass, paramValuesStr.get(i)));
            } catch (ParseException e) {
                throw new RestAPIException("Invalid parameter value",
                        e.getMessage(),
                        HttpStatus.BAD_REQUEST,
                        e);
            }
        }

        Object methodResult;
        try {
            methodResult = serviceMethod.invoke(service, paramValues.toArray());
        } catch (Exception e) {
            throw new RestAPIException("Error on service method invoke", e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, e);
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

    private void parseParamsJson(String paramsJson, List<String> paramValuesStr, List<Class> paramTypes) {
        if (Strings.isNullOrEmpty(paramsJson)) return;
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(paramsJson).getAsJsonObject();
        int idx = 0;
        while (true) {
            JsonElement nthParam = jsonObject.get("param" + idx);
            if (nthParam == null) break;
            if (nthParam.isJsonPrimitive()) {
                paramValuesStr.add(nthParam.getAsString());
            } else {
                paramValuesStr.add(nthParam.toString());
            }

            JsonElement nthParamType = jsonObject.get("param" + idx + "_type");
            if (nthParamType != null) {
                try {
                    paramTypes.add(ClassUtils.forName(nthParamType.getAsString(), null));
                } catch (ClassNotFoundException e) {
                    throw new RestAPIException("Error on evaluating parameter type", e.getMessage(), HttpStatus.BAD_REQUEST, e);
                }
            }
            idx++;
        }
    }

    private void parseParamsJson(Map<String, String> paramsMap, List<String> paramValuesStr, List<Class> paramTypes) {
        int idx = 0;
        while (true) {
            String paramValueStr = paramsMap.get("param" + idx);
            if (paramValueStr == null) break;
            paramValuesStr.add(paramValueStr);

            String paramType = paramsMap.get("param" + idx + "_type");
            if (paramType != null) {
                try {
                    paramTypes.add(ClassUtils.forName(paramType, null));
                } catch (ClassNotFoundException e) {
                    throw new RestAPIException("Error on evaluating parameter type", e.getMessage(), HttpStatus.BAD_REQUEST, e);
                }
            }
            idx++;
        }
    }

    protected Method findMethod(Object service, String methodName, List<String> paramValues, List<Class> paramTypes) throws RestAPIException {
        Method serviceMethod;
        if (paramTypes.isEmpty()) {
            //trying to guess which method to invoke
            Method[] methods = service.getClass().getMethods();
            List<Method> appropriateMethods = new ArrayList<>();
            for (Method method : methods) {
                if (methodName.equals(method.getName()) && method.getParameterTypes().length == paramValues.size()) {
                    appropriateMethods.add(method);
                }
            }
            if (appropriateMethods.size() == 1) {
                serviceMethod = appropriateMethods.get(0);
            } else if (appropriateMethods.size() > 1) {
                throw new RestAPIException("Multiple methods found",
                        "There are multiple methods with given argument numbers. Please define parameter types in request",
                        HttpStatus.BAD_REQUEST);
            } else {
                throw new RestAPIException("Method not found",
                        "",
                        HttpStatus.NOT_FOUND);
            }
        } else {
            try {
                serviceMethod = service.getClass().getMethod(methodName, paramTypes.toArray(new Class[paramTypes.size()]));
            } catch (NoSuchMethodException e) {
                throw new RestAPIException("Method not found",
                        "",
                        HttpStatus.NOT_FOUND);
            }
        }
        return serviceMethod;
    }

}
