/*
 * Copyright (c) 2008-2017 Haulmont.
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

package com.haulmont.cuba.core.entity;

import com.google.common.base.Joiner;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.UserSessionSource;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.*;
import java.util.regex.Pattern;

public final class LocaleHelper {

    private LocaleHelper() {
    }

    public static String getLocalizedName(String localeBundle) {
        Locale locale = AppBeans.get(UserSessionSource.class).getLocale();
        String localeName = null;
        if (StringUtils.isNotEmpty(localeBundle)) {
            Properties localeProperties = loadProperties(localeBundle);
            if (localeProperties != null) {
                String key = locale.getLanguage();
                if (StringUtils.isNotEmpty(locale.getCountry()))
                    key += "_" + locale.getCountry();
                if (localeProperties.containsKey(key))
                    localeName = (String) localeProperties.get(key);
            }
        }
        return localeName;
    }

    public static Map<String, String> getLocalizedValuesMap(String localeBundle) {
        if (StringUtils.isNotEmpty(localeBundle)) {
            Properties localeProperties = loadProperties(localeBundle);
            if (localeProperties != null) {
                Map<String, String> map = new HashMap<>();
                for (Map.Entry<Object, Object> entry : localeProperties.entrySet()) {
                    map.put((String) entry.getKey(), (String) entry.getValue());
                }
                return map;
            }
        }
        return Collections.emptyMap();
    }

    public static String getLocalizedEnumeration(String localeBundle) {
        String result = null;
        if (StringUtils.isNotEmpty(localeBundle)) {
            Properties localeProperties = loadProperties(localeBundle);
            if (localeProperties != null) {

                Locale locale = AppBeans.get(UserSessionSource.class).getLocale();
                String key = locale.getLanguage();
                if (StringUtils.isNotEmpty(locale.getCountry())) {
                    key += "_" + locale.getCountry();
                }

                List<String> enumValues = new ArrayList<>();

                for (Map.Entry<Object, Object> entry : localeProperties.entrySet()) {
                    String enumValue = (String) entry.getKey();
                    String localizedEnumValues = ((String) entry.getValue()).replaceAll("\\\\r\\\\n", "\r\n");
                    Map<String, String> localizedEnumValuesMap = LocaleHelper.getLocalizedValuesMap(localizedEnumValues);

                    enumValues.add(localizedEnumValuesMap.getOrDefault(key, enumValue));
                }
                result = Joiner.on(",").join(enumValues);
            }
        }
        return result;
    }

    public static String getEnumLocalizedValue(String enumValue, String localeBundle) {
        if (enumValue == null) {
            return null;
        }

        if (localeBundle == null) {
            return enumValue;
        }

        Map<String, String> map = getLocalizedValuesMap(localeBundle);
        String locales = map.getOrDefault(enumValue, "");
        locales = locales.replaceAll("\\\\r\\\\n", "\r\n");
        String result = getLocalizedName(locales);

        return result == null ? enumValue : result;
    }

    public static String convertPropertiesToString(Properties properties) {
        StringWriter writer = new StringWriter();
        String result = null;
        boolean written = false;
        try {
            properties.store(writer, "");
            written = true;
        } catch (IOException ignored) {
        }

        if (written) {
            StringBuffer buffer = writer.getBuffer();
            Pattern pattern = Pattern.compile("(?m)^#.*\\s\\s");
            result = pattern.matcher(buffer).replaceAll("");
        }
        return result;
    }

    protected static Properties loadProperties(String localeBundle) {
        StringReader reader = new StringReader(localeBundle);
        Properties localeProperties = null;
        try {
            localeProperties = new Properties();
            localeProperties.load(reader);
        } catch (IOException ignored) {
        }
        return localeProperties;
    }
}