/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.portal.restapi;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Class is used for invoking middleware services with REST API
 *
 * @author gorbunkov
 * @version $Id$
 */
public class ServiceRequest {
    protected String serviceName;
    protected String methodName;
    protected String viewName;
    protected List<String> paramValuesString = new ArrayList<>();
    protected List<Class> paramTypes = new ArrayList<>();
    protected Convertor convertor;

    public ServiceRequest(String serviceName, String methodName, Convertor convertor) {
        this.serviceName = serviceName;
        this.methodName = methodName;
        this.convertor = convertor;
    }

    public Object invokeMethod() throws RestServiceException, InvocationTargetException, IllegalAccessException {
        validate();
        Object service = AppBeans.get(serviceName);
        Method serviceMethod = findMethod(service);
        List<Object> paramValues = new ArrayList<>();
        Class<?>[] types = serviceMethod.getParameterTypes();
        for (int i = 0; i < types.length; i++) {
            Class<?> aClass = types[i];
            paramValues.add(toObject(aClass, paramValuesString.get(i), convertor));
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

    protected Object toObject(Class clazz, String value, Convertor convertor) {
        if (Boolean.class == clazz || Boolean.TYPE == clazz) return Boolean.parseBoolean(value);
        if (Byte.class == clazz || Byte.TYPE == clazz) return Byte.parseByte(value);
        if (Short.class == clazz || Short.TYPE == clazz) return Short.parseShort(value);
        if (Integer.class == clazz || Integer.TYPE == clazz) return Integer.parseInt(value);
        if (Long.class == clazz || Long.TYPE == clazz) return Long.parseLong(value);
        if (Float.class == clazz || Float.TYPE == clazz) return Float.parseFloat(value);
        if (Double.class == clazz || Double.TYPE == clazz) return Double.parseDouble(value);
        if (UUID.class == clazz) return UUID.fromString(value);
        if (String.class == clazz) return value;
        if (Entity.class.isAssignableFrom(clazz)) return convertor.parseEntity(value);
        if (Collection.class.isAssignableFrom(clazz)) return convertor.parseEntitiesCollection(value, clazz);
        throw new IllegalArgumentException("Parameters of type " + clazz.getName() + " are not supported");
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

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }
}
