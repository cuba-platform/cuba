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
package com.haulmont.cuba.core.sys;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.Properties;

/**
 */
public class CubaPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

    @Override
    protected String resolvePlaceholder(String placeholder, Properties props, int systemPropertiesMode) {
        String key = placeholder;
        String defValue = null;
        String[] parts = placeholder.split("\\?:");
        if (parts.length == 2) {
            key = parts[0];
            defValue = parts[1];
        }

        String value = null;
        if (systemPropertiesMode == SYSTEM_PROPERTIES_MODE_OVERRIDE)
            value = super.resolvePlaceholder(key, props, systemPropertiesMode);

        if (value == null)
            value = AppContext.getProperty(key);

        if (value == null && systemPropertiesMode == SYSTEM_PROPERTIES_MODE_FALLBACK)
            value = super.resolvePlaceholder(key, props, systemPropertiesMode);

        if (value == null && defValue != null) {
            value = defValue;
        }
        return value;
    }
}
