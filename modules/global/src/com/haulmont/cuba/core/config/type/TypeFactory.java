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

package com.haulmont.cuba.core.config.type;

import com.haulmont.chile.core.datatypes.impl.EnumClass;
import com.haulmont.cuba.core.config.ConfigUtil;
import com.haulmont.cuba.core.config.EnumStore;
import com.haulmont.cuba.core.config.EnumStoreMode;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import org.apache.commons.lang.ClassUtils;

import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * A class that gets a configuration type by retrieving a string value
 * from the configuration and then building the appropriate type from
 * that string.
 *
 */
public abstract class TypeFactory {
    public static final String ENTITY_FACTORY_BEAN_NAME = "cuba_ConfigEntityFactory";

    /**
     * Build an instance of the configuration type from a string.
     *
     * @param string The string value.
     * @return An instance of the type.
     */
    public abstract Object build(String string);

    /**
     * Standard static factory methods to look for.
     */
    private static final String[] FACTORY_METHOD_NAMES = {
            "valueOf",
            "getInstance",
            "parse", // Level
            "forName" // Class
    };

    /**
     * Get a TypeFactory instance appropriate for the return type of the
     * specified configuration interface method.
     *
     * @param configInterface The configuration interface.
     * @param method          The method.
     * @return An appropriate TypeFactory.
     * @throws IllegalArgumentException If the type is not supported.
     */
    public static TypeFactory getInstance(Class<?> configInterface, Method method) {
        Class<?> returnType = method.getReturnType();
        if (returnType.isPrimitive()) {
            returnType = ClassUtils.primitiveToWrapper(returnType);
        }
        Factory factory = ConfigUtil.getAnnotation(configInterface, method, Factory.class, true);
        if (factory != null) {
            try {
                if ("".equals(factory.method())) {
                    return factory.factory().newInstance();
                } else {
                    String methodName = factory.method();
                    Method factoryMethod = returnType.getMethod(methodName, String.class);
                    if (!isAcceptableMethod(returnType, factoryMethod)) {
                        throw new IllegalArgumentException("Invalid factory method: " + factoryMethod);
                    }
                    return new StaticTypeFactory(factoryMethod);
                }
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException("Unable to instantiate an type factory", e);
            }
        } else {
            if (Entity.class.isAssignableFrom(returnType)) {
                return AppBeans.get(ENTITY_FACTORY_BEAN_NAME, TypeFactory.class);
            } else {
                if (EnumClass.class.isAssignableFrom(returnType)) {
                    EnumStore mode = ConfigUtil.getAnnotation(configInterface, method, EnumStore.class, true);
                    if (mode != null && EnumStoreMode.ID == mode.value()) {
                        @SuppressWarnings("unchecked")
                        Class<EnumClass> enumeration = (Class<EnumClass>) returnType;
                        Class<?> idType = ConfigUtil.getEnumIdType(enumeration);
                        TypeFactory idFactory = getInferred(idType);
                        try {
                            Method fromIdMethod = returnType.getMethod("fromId", idType);
                            if (!isAcceptableMethod(returnType, fromIdMethod) || idFactory == null) {
                                throw new IllegalArgumentException("Cannot use method as factory method: " + method);
                            }
                            return new EnumClassFactory(idFactory, fromIdMethod);
                        } catch (NoSuchMethodException e) {
                            throw new IllegalArgumentException("fromId method is not found for " + enumeration.getName());
                        }
                    }
                }
                TypeFactory factoryT = getInferred(returnType);
                if (factoryT == null) {
                    throw new IllegalArgumentException("Unsupported return type for " + method);
                }
                return factoryT;
            }
        }
    }

    @Nullable
    private static TypeFactory getInferred(Class<?> returnType) {
        for (String methodName : FACTORY_METHOD_NAMES) {
            try {
                Method factoryMethod = returnType.getMethod(methodName, String.class);
                if (isAcceptableMethod(returnType, factoryMethod)) {
                    return new StaticTypeFactory(factoryMethod);
                }
            } catch (NoSuchMethodException ex) {
                // Ignore failure.
            }
        }
        try {
            Constructor ctor = returnType.getConstructor(String.class);
            if (Modifier.isPublic(ctor.getModifiers())) {
                return new ConstructorTypeFactory(ctor);
            }
        } catch (NoSuchMethodException e) {
            return null;
        }
        return null;
    }

    private static boolean isAcceptableMethod(Class<?> returnType, Method factoryMethod) {
        int modifiers = factoryMethod.getModifiers();
        return Modifier.isStatic(modifiers) &&
                Modifier.isPublic(modifiers) &&
                returnType.isAssignableFrom(factoryMethod.getReturnType());
    }
}