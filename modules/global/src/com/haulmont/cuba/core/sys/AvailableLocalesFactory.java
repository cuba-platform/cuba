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

import com.google.common.base.Splitter;
import com.haulmont.cuba.core.config.type.TypeFactory;
import com.haulmont.cuba.core.global.LocaleResolver;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class AvailableLocalesFactory extends TypeFactory {

    @Override
    public Object build(String string) {
        if (string == null)
            return null;

        Map<String, Locale> result = new LinkedHashMap<>();
        for (String item : Splitter.on(';').trimResults().omitEmptyStrings().split(string)) {
            String[] parts = item.split("\\|");
            result.put(parts[0], LocaleResolver.resolve(parts[1]));
        }
        return result;
    }
}