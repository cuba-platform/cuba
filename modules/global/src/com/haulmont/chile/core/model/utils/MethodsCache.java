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

import org.apache.commons.lang3.StringUtils;

import java.lang.invoke.*;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class MethodsCache {

    private final transient Map<String, Function> getters = new HashMap<>();
    private final transient Map<String, BiConsumer> setters = new HashMap<>();

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
                name = StringUtils.uncapitalize(name.substring(2));
                getters.put(name, getter);
            } else if (name.startsWith("set") && method.getParameterTypes().length == 1) {
                BiConsumer setter = createSetter(clazz, method);
                name = StringUtils.uncapitalize(name.substring(3));
                setters.put(name, setter);
            }
        }
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
            t.printStackTrace();
            return null;
        }

        return getter;
    }

    protected BiConsumer createSetter(Class clazz, Method method) {
        BiConsumer setter;
        try {
            MethodHandles.Lookup caller = MethodHandles.lookup();
            CallSite site = LambdaMetafactory.metafactory(caller,
                    "accept",
                    MethodType.methodType(BiConsumer.class),
                    MethodType.methodType(void.class, Object.class, Object.class),
                    caller.findVirtual(clazz, method.getName(), MethodType.methodType(method.getReturnType(), method.getParameterTypes()[0])),
                    MethodType.methodType(void.class, clazz, method.getParameterTypes()[0]));
            MethodHandle factory = site.getTarget();
            setter = (BiConsumer) factory.invoke();
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }

        return setter;
    }

    public Object invokeGetter(Object object, String property) {
        Function getter = getters.get(property);
        if (getter == null) {
            throw new IllegalArgumentException(
                    String.format("Can't find getter for property '%s' at %s", property, object.getClass()));
        }
        return getter.apply(object);
    }

    public void invokeSetter(Object object, String property, Object value) {
        BiConsumer setter = setters.get(property);
        if (setter == null) {
            throw new IllegalArgumentException(
                    String.format("Can't find setter for property '%s' at %s", property, object.getClass()));
        }
        setter.accept(object, value);
    }
}