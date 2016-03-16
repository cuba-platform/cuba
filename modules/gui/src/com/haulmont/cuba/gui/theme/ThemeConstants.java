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

package com.haulmont.cuba.gui.theme;

import java.util.Collections;
import java.util.Map;

/**
 * Theme defaults for application UI, components and screens.
 *
 */
public class ThemeConstants {

    public static final String PREFIX = "theme://";

    protected Map<String, String> properties;

    public ThemeConstants(Map<String, String> properties) {
        this.properties = Collections.unmodifiableMap(properties);
    }

    public String get(String key) {
        return properties.get(key);
    }

    public int getInt(String key) {
        String value = properties.get(key);
        if (value != null && value.endsWith("px")) {
            value = value.substring(0, value.length() - 2);
        }

        if (value == null) {
            throw new IllegalArgumentException("Null value for theme key " + key);
        }

        return Integer.parseInt(value);
    }
}