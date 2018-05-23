/*
 * Copyright (c) 2008-2018 Haulmont.
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

package com.haulmont.cuba.core.config;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Invokes default method of configuration interfaces.
 */
public class ConfigDefaultMethod extends ConfigMethod {

    private final Class<?> configInterface;
    private final Method configMethod;

    public ConfigDefaultMethod(Class<?> configInterface, Method configMethod) {
        this.configInterface = configInterface;
        this.configMethod = configMethod;
    }

    @Override
    public Object invoke(ConfigHandler handler, Object[] args, Object proxy) {
        try {
            // hack to invoke default method of an interface reflectively
            Constructor<MethodHandles.Lookup> lookupConstructor =
                    MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, Integer.TYPE);
            if (!lookupConstructor.isAccessible()) {
                lookupConstructor.setAccessible(true);
            }
            //TODO: CUBA 7 Config default methods should work for JAVA 8 and 10
            //TODO: https://github.com/cuba-platform/cuba/issues/895
//            return MethodHandles.lookup()
//                    .findSpecial(configInterface, configMethod.getName(), MethodType.methodType(configMethod.getReturnType(),
//                            configMethod.getParameterTypes()), configInterface)
//                    .bindTo(proxy)
//                    .invokeWithArguments(args);
            return lookupConstructor.newInstance(configInterface, MethodHandles.Lookup.PRIVATE)
                    .unreflectSpecial(configMethod, configInterface)
                    .bindTo(proxy)
                    .invokeWithArguments(args);
        } catch (Throwable throwable) {
            throw new RuntimeException("Error invoking default method of config interface", throwable);
        }
    }

    /**
     * The ConfigDefaultMethod factory.
     */
    public static final Factory FACTORY = new Factory() {

        /**
         * The method is default and has a non-void return type.
         */
        @Override
        public boolean canHandle(Method method) {
            return method.isDefault() && !Void.TYPE.equals(method.getReturnType());
        }

        @Override
        public ConfigMethod newInstance(Class<?> configInterface, Method configMethod) {
            return new ConfigDefaultMethod(configInterface, configMethod);
        }
    };

}
