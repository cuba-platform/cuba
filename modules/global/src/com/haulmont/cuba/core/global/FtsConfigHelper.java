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

package com.haulmont.cuba.core.global;

import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.cuba.core.config.Config;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Provides reflective access to {@code FtsConfig} interface which is located in the FTS base project.
 */
public class FtsConfigHelper {

    private static Config config;

    private static Method method;

    static {
        try {
            //noinspection unchecked
            Class<? extends Config> ftsConfigClass = (Class<? extends Config>) ReflectionHelper.loadClass("com.haulmont.fts.global.FtsConfig");
            config = AppBeans.get(Configuration.class).getConfig(ftsConfigClass);
            method = config.getClass().getMethod("getEnabled");
        } catch (ClassNotFoundException | NoSuchMethodException ignored) {
        }
    }

    /**
     * Returns the value of the "fts.enabled" application property.
     */
    public static boolean getEnabled() {
        if (method != null) {
            try {
                return (boolean) method.invoke(config);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException("Error invoking FtsConfig.getEnabled()", e);
            }
        }
        return false;
    }
}
