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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.*;
import java.util.regex.Pattern;

public final class LocaleHelper {

    private final static Logger log = LoggerFactory.getLogger(LocaleHelper.class);

    private LocaleHelper() {
    }

    // todo extract to bean
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

    // todo extract to bean
    public static String getLocalizedEnumeration(String enumerationValues, String localeBundle) {
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

                String[] enumerationValuesArray = enumerationValues.split(",");
                Map<String, String> localizedValuesMap = LocaleHelper.getLocalizedValuesMap(localeBundle);
                for (String value : enumerationValuesArray) {
                    String resultValue = localizedValuesMap.getOrDefault(key + "/" + value, value);
                    enumValues.add(resultValue);
                }
                result = Joiner.on(",").join(enumValues);
            }
        }
        return result;
    }

    // todo extract to bean
    public static String convertToSimpleKeyLocales(String localeBundle) {
        Properties result = new Properties();
        Properties properties = loadProperties(localeBundle);
        if (properties != null) {
            for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                String key = (String) entry.getKey();
                key = key.substring(0, key.indexOf("/"));
                result.put(key, entry.getValue());
            }
        }
        return convertPropertiesToString(result);
    }

    // todo extract to bean
    public static String convertFromSimpleKeyLocales(String enumValue, String localeBundle) {
        Properties result = new Properties();
        Properties properties = loadProperties(localeBundle);
        if (properties != null) {
            for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                String key = (String) entry.getKey();
                result.put(key + "/" + enumValue, entry.getValue());
            }
        }
        return convertPropertiesToString(result);
    }

    // todo extract to bean
    public static String getEnumLocalizedValue(String enumValue, String localeBundle) {
        if (enumValue == null) {
            return null;
        }

        if (localeBundle == null) {
            return enumValue;
        }

        Map<String, String> map = getLocalizedValuesMap(localeBundle);

        Locale locale = AppBeans.get(UserSessionSource.class).getLocale();
        String key = locale.getLanguage();
        if (StringUtils.isNotEmpty(locale.getCountry())) {
            key += "_" + locale.getCountry();
        }

        String result = map.getOrDefault(key + "/" + enumValue, "");

        return Objects.equals(result, "") ? enumValue : result;
    }

    // todo extract to bean
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

    // todo extract to bean
    protected static Properties loadProperties(String localeBundle) {
        StringReader reader = new StringReader(localeBundle);
        Properties localeProperties = null;
        try {
            localeProperties = new Properties();
            localeProperties.load(reader);
        } catch (IOException e) {
            log.debug("Unable to load properties: {}", localeBundle, e);
        }
        return localeProperties;
    }

    // todo extract to bean
    public static boolean isLocalizedValueDefined(String localeBundle) {
        return getLocalizedName(localeBundle) != null;
    }
}