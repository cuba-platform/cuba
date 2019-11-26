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
package com.haulmont.chile.core.model.utils;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.invoke.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class MethodsCache {

    private final Map<String, Function> getters = new HashMap<>();
    private final Map<String, BiConsumer> setters = new HashMap<>();
    private String className;

    private static final Map<Class, Class> primitivesToObjects = new ImmutableMap.Builder<Class, Class>()
            .put(byte.class, Byte.class)
            .put(char.class, Character.class)
            .put(short.class, Short.class)
            .put(int.class, Integer.class)
            .put(long.class, Long.class)
            .put(float.class, Float.class)
            .put(double.class, Double.class)
            .put(boolean.class, Boolean.class)
            .build();


    public MethodsCache(Class clazz) {
        final Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            String name = method.getName();
            if (name.startsWith("get") && method.getParameterTypes().length == 0) {
                Function getter = createGetter(clazz, method);
                name = StringUtils.uncapitalize(name.substring(3));
                getters.put(name, getter);
            } else if (name.startsWith("is") && method.getParameterTypes().length == 0) {
                Function getter = createGetter(clazz, method);
                Field isField = ReflectionUtils.findField(clazz, name);
                if (isField != null) {
                    // for Kotlin entity with a property which name starts with "is*" the getter name will be the same as property name,
                    // e.g "isApproved"
                    getters.put(name, getter);
                } else {
                    name = StringUtils.uncapitalize(name.substring(2));
                    getters.put(name, getter);
                }
            } else if (name.startsWith("set") && method.getParameterTypes().length == 1) {
                BiConsumer setter = createSetter(clazz, method);
                Field isField = ReflectionUtils.findField(clazz, "is" + name.substring(3));
                if (isField != null) {
                    name = "is" + name.substring(3);
                } else {
                    name = StringUtils.uncapitalize(name.substring(3));
                }
                setters.put(name, setter);
            }
        }
        className = clazz.toString();
    }

    protected Function createGetter(Class clazz, Method method) {
        Function getter;
        try {
            MethodHandles.Lookup caller = MethodHandles.lookup();
            CallSite site = LambdaMetafactory.metafactory(caller,
                    "apply",
                    MethodType.methodType(Function.class),
                    MethodType.methodType(Object.class, Object.class),
                    caller.findVirtual(clazz, method.getName(), MethodType.methodType(method.getReturnType())),
                    MethodType.methodType(method.getReturnType(), clazz));
            MethodHandle factory = site.getTarget();
            getter = (Function) factory.invoke();
        } catch (Throwable t) {
            throw new RuntimeException("Can not create getter", t);
        }

        return getter;
    }

    protected BiConsumer createSetter(Class clazz, Method method) {
        Class valueType = method.getParameterTypes()[0];
        BiConsumer setter;
        try {
            MethodHandles.Lookup caller = MethodHandles.lookup();
            CallSite site = LambdaMetafactory.metafactory(caller,
                    "accept",
                    MethodType.methodType(BiConsumer.class),
                    MethodType.methodType(void.class, Object.class, Object.class),
                    caller.findVirtual(clazz, method.getName(), MethodType.methodType(method.getReturnType(), method.getParameterTypes()[0])),
                    MethodType.methodType(void.class, clazz, valueType.isPrimitive() ? primitivesToObjects.get(valueType) : valueType));
            MethodHandle factory = site.getTarget();
            setter = (BiConsumer) factory.invoke();
        } catch (Throwable t) {
            throw new RuntimeException("Can not create setter", t);
        }

        return setter;
    }

    @SuppressWarnings("unchecked")
    public Object invokeGetter(Object object, String property) {
        return getGetterNN(property).apply(object);
    }

    @SuppressWarnings("unchecked")
    public void invokeSetter(Object object, String property, Object value) {
        getSetterNN(property).accept(object, value);
    }

    /**
     * @param property name of property associated with getter
     * @return lambda {@link Function} which represents getter
     * @throws IllegalArgumentException if getter for property not found
     */
    public Function getGetterNN(String property) {
        Function getter = getters.get(property);
        if (getter == null) {
            throw new IllegalArgumentException(
                    String.format("Can't find getter for property '%s' at %s", property, className));
        }
        return getter;
    }

    /**
     * @param property name of property associated with setter
     * @return lambda {@link BiConsumer} which represents setter
     * @throws IllegalArgumentException if setter for property not found
     */
    public BiConsumer getSetterNN(String property) {
        BiConsumer setter = setters.get(property);
        if (setter == null) {
            throw new IllegalArgumentException(
                    String.format("Can't find setter for property '%s' at %s", property, className));
        }
        return setter;
    }
}