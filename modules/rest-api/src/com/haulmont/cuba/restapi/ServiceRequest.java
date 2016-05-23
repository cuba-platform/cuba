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
 *
 */

package com.haulmont.cuba.restapi;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DatatypeFormatter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class is used for invoking middleware services with REST API
 *
 */
public class ServiceRequest {
    protected String serviceName;
    protected String methodName;
    protected List<String> paramValuesString = new ArrayList<>();
    protected List<Class> paramTypes = new ArrayList<>();
    protected Converter converter;
    protected DatatypeFormatter datatypeFormatter;
    protected Class methodReturnType;

    public ServiceRequest(String serviceName, String methodName, Converter converter) {
        this.serviceName = serviceName;
        this.methodName = methodName;
        this.converter = converter;
        this.datatypeFormatter = AppBeans.get(DatatypeFormatter.class);
    }

    public Object invokeMethod() throws RestServiceException, InvocationTargetException, IllegalAccessException, ParseException {
        validate();
        Object service = AppBeans.get(serviceName);
        Method serviceMethod = findMethod(service);
        this.methodReturnType = serviceMethod.getReturnType();
        List<Object> paramValues = new ArrayList<>();
        Class<?>[] types = serviceMethod.getParameterTypes();
        for (int i = 0; i < types.length; i++) {
            Class<?> aClass = types[i];
            paramValues.add(ParseUtils.toObject(aClass, paramValuesString.get(i), converter));
        }

        return serviceMethod.invoke(service, paramValues.toArray());
    }

    protected Method findMethod(Object service) throws RestServiceException {
        Method serviceMethod;
        if (paramTypes.isEmpty()) {
            //trying to guess which method to invoke
            Method[] methods = service.getClass().getMethods();
            List<Method> appropriateMethods = new ArrayList<>();
            for (Method method : methods) {
                if (methodName.equals(method.getName()) && method.getParameterTypes().length == paramValuesString.size()) {
                    appropriateMethods.add(method);
                }
            }
            if (appropriateMethods.size() == 1) {
                serviceMethod = appropriateMethods.get(0);
            } else if (appropriateMethods.size() > 1) {
                throw new RestServiceException("There are multiple methods with given argument numbers. Please define parameter types in request");
            } else {
                throw new RestServiceException("Method not found");
            }
        } else {
            try {
                serviceMethod = service.getClass().getMethod(methodName, paramTypes.toArray(new Class[paramTypes.size()]));
            } catch (NoSuchMethodException e) {
                throw new RestServiceException("Method not found");
            }
        }
        return serviceMethod;
    }

    protected boolean validate() throws RestServiceException{
        return true;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public List<String> getParamValuesString() {
        return paramValuesString;
    }

    public void setParamValuesString(List<String> paramValuesString) {
        this.paramValuesString = paramValuesString;
    }

    public List<Class> getParamTypes() {
        return paramTypes;
    }

    public void setParamTypes(List<Class> paramTypes) {
        this.paramTypes = paramTypes;
    }

    public Class getMethodReturnType() {
        return methodReturnType;
    }
}
