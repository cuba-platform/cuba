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

import com.haulmont.cuba.core.config.type.TypeFactory;

import java.util.Locale;
import java.util.Map;
import java.util.LinkedHashMap;

public class AvailableLocalesFactory extends TypeFactory {

    @Override
    public Object build(String string) {
        if (string == null)
            return null;

        String[] items = string.split(";");
        Map<String, Locale> map = new LinkedHashMap<>(items.length);
        for (String item : items) {
            String[] parts = item.split("\\|");
            String[] locParts = parts[1].split("_");
            Locale loc;
            if (locParts.length == 1)
                loc = new Locale(parts[1]);
            else
                loc = new Locale(locParts[0], locParts[1]);

            map.put(parts[0], loc);
        }
        return map;
    }
}