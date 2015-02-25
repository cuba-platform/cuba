/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.portal.restapi;

import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.datatypes.impl.*;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DatatypeFormatter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.ParseException;
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
    protected List<String> paramValuesString = new ArrayList<>();
    protected List<Class> paramTypes = new ArrayList<>();
    protected Convertor convertor;
    protected DatatypeFormatter datatypeFormatter;
    protected Class methodReturnType;

    public ServiceRequest(String serviceName, String methodName, Convertor convertor) {
        this.serviceName = serviceName;
        this.methodName = methodName;
        this.convertor = convertor;
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

    protected Object toObject(Class clazz, String value, Convertor convertor) throws ParseException {
        if (String.class == clazz) return value;
        if (Integer.class == clazz || Integer.TYPE == clazz
                || Byte.class == clazz || Byte.TYPE == clazz
                || Short.class == clazz || Short.TYPE == clazz) return Datatypes.get(IntegerDatatype.NAME).parse(value);
        if (Date.class == clazz) {
            try {
                return Datatypes.get(DateTimeDatatype.NAME).parse(value);
            } catch (ParseException e) {
                try {
                    return Datatypes.get(DateDatatype.NAME).parse(value);
                } catch (ParseException e1) {
                    return Datatypes.get(TimeDatatype.NAME).parse(value);

                }
            }
        }
        if (BigDecimal.class == clazz) return Datatypes.get(BigDecimalDatatype.NAME).parse(value);
        if (Boolean.class == clazz || Boolean.TYPE == clazz) return Datatypes.get(BooleanDatatype.NAME).parse(value);
        if (Long.class == clazz || Long.TYPE == clazz) return Datatypes.get(LongDatatype.NAME).parse(value);
        if (Double.class == clazz || Double.TYPE == clazz
                || Float.class == clazz || Float.TYPE == clazz) return Datatypes.get(DoubleDatatype.NAME).parse(value);
        if (UUID.class == clazz) return UUID.fromString(value);
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

    public Class getMethodReturnType() {
        return methodReturnType;
    }
}
