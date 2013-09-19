/*
 * A High-Level Framework for Application Configuration
 *
 * Copyright 2007 Merlin Hughes / Learning Objects, Inc.
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

package com.haulmont.cuba.core.config.type;

import com.haulmont.chile.core.datatypes.impl.EnumClass;
import com.haulmont.cuba.core.config.ConfigUtil;
import com.haulmont.cuba.core.entity.Entity;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * A class that sets a configuration type by converting the type to a
 * string and then setting the configuration property.
 *
 * @author Merlin Hughes
 * @version $Id$
 */
public abstract class TypeStringify
{
    /**
     * Convert an instance of the configuration type into a string.
     *
     * @param value The configuration value.
     * @return A string representation.
     */
    public abstract String stringify(Object value);

    /**
     * Get a TypeStringify instance appropriate for the parameter type of the
     * specified configuration interface method.
     *
     * @param configInterface The configuration interface.
     * @param method          The method.
     * @return An appropriate TypeStringify.
     * @throws IllegalArgumentException If the type is not supported.
     */
    public static TypeStringify getInstance(Class<?> configInterface, Method method) {
        Class<?> methodType = ConfigUtil.getMethodType(method);
        try {
            Stringify stringify = ConfigUtil.getAnnotation(configInterface, method, Stringify.class, true);
            if (stringify != null) {
                if ("".equals(stringify.method())) {
                    return stringify.stringify().newInstance();
                } else {
                    String methodName = stringify.method();
                    return new MethodTypeStringify(methodType.getMethod(methodName));
                }
            } else {
                if ((method.getParameterTypes().length > 0)) {
                    if (Entity.class.isAssignableFrom(method.getParameterTypes()[0]))
                        return new EntityStringify();
                }
                if (EnumClass.class.isAssignableFrom(methodType)) {
                    @SuppressWarnings("unchecked")
                    Class<EnumClass> enumeration = (Class<EnumClass>) methodType;
                    TypeStringify idStringify = getInferred(ConfigUtil.getEnumIdType(enumeration));
                    return new EnumClassStringify(idStringify);
                }
                return getInferred(methodType);
            }
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | RuntimeException e) {
            throw new RuntimeException("Type stringify error", e);
        }
    }

    private static TypeStringify getInferred(Class<?> type) {
        String method;
        if (type.isPrimitive()) {
            return new PrimitiveTypeStringify();
        } else if (type.isEnum()) { // enum
            method = "name";
        } else if (Class.class.equals(type)) { // Class
            method = "getName";
        } else { // all else
            method = "toString";
        }
        try {
            Method stringifyMethod = type.getMethod(method);
            if (Modifier.isStatic(stringifyMethod.getModifiers()) ||
                    !Modifier.isPublic(stringifyMethod.getModifiers()) ||
                    Void.TYPE.equals(stringifyMethod.getReturnType()) ||
                    (stringifyMethod.getParameterTypes().length > 0))
            {
                throw new IllegalArgumentException("Invalid stringify method: " + method);
            }
            return new MethodTypeStringify(stringifyMethod);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Stringify method not found: " + method);
        }
    }
}
