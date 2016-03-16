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

import com.haulmont.cuba.core.global.DevelopmentException;
import com.haulmont.cuba.core.global.Resources;
import com.haulmont.cuba.core.sys.AppContext;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 */
@Component(ThemeConstantsRepository.NAME)
public class ThemeConstantsRepository {

    public static final String NAME = "cuba_ThemeConstantsRepository";

    @Inject
    protected Resources resources;

    protected Logger log = LoggerFactory.getLogger(getClass());

    private volatile boolean initialized;

    protected Map<String, ThemeConstants> themeConstantsMap;

    protected void checkInitialized() {
        if (!initialized) {
            synchronized (this) {
                if (!initialized) {
                    log.debug("Loading theme constants");
                    init();
                    initialized = true;
                }
            }
        }
    }

    protected void init() {
        String configName = AppContext.getProperty("cuba.themeConfig");
        if (!StringUtils.isBlank(configName)) {
            Map<String, Map<String, String>> themeProperties = new HashMap<>();

            StrTokenizer tokenizer = new StrTokenizer(configName);
            for (String fileName : tokenizer.getTokenArray()) {
                String themeName = parseThemeName(fileName);
                if (StringUtils.isNotBlank(themeName)) {
                    Map<String, String> themeMap = themeProperties.get(themeName);
                    if (themeMap == null) {
                        themeMap = new HashMap<>();
                        themeProperties.put(themeName, themeMap);
                    }

                    loadThemeProperties(fileName, themeMap);
                }
            }

            Map<String, ThemeConstants> themes = new LinkedHashMap<>();

            for (Map.Entry<String, Map<String, String>> entry : themeProperties.entrySet()) {
                themes.put(entry.getKey(), new ThemeConstants(entry.getValue()));
            }

            this.themeConstantsMap = Collections.unmodifiableMap(themes);
        } else {
            this.themeConstantsMap = Collections.emptyMap();
        }
    }

    public void loadThemeProperties(String fileName, Map<String, String> themeMap) {
        InputStream propertiesStream = null;
        try {
            propertiesStream = resources.getResourceAsStream(fileName);
            if (propertiesStream == null) {
                throw new DevelopmentException("Unable to load theme constants for: '" + fileName + "'");
            }

            InputStreamReader propertiesReader = new InputStreamReader(propertiesStream, StandardCharsets.UTF_8);

            Properties properties = new Properties();
            try {
                properties.load(propertiesReader);
            } catch (IOException e) {
                throw new DevelopmentException("Unable to parse theme constants for: '" + fileName + "'");
            }

            Object includeValue = properties.get("@include");
            if (includeValue != null) {
                String[] themeIncludes = StringUtils.split(includeValue.toString(), " ,");

                for (String include : themeIncludes) {
                    loadThemeProperties(include, themeMap);
                }
            }

            for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                Object key = entry.getKey();
                Object value = entry.getValue();

                if (key != null && !"@include".equals(key) && value != null) {
                    themeMap.put(key.toString(), value.toString());
                }
            }
        } finally {
            IOUtils.closeQuietly(propertiesStream);
        }
    }

    public String parseThemeName(String fileName) {
        String name = FilenameUtils.getBaseName(fileName);
        if (name.endsWith("-theme")) {
            int dashIndex = name.lastIndexOf("-theme");
            return name.substring(0, dashIndex);
        } else {
            return name;
        }
    }

    public ThemeConstants getConstants(String themeName) {
        checkInitialized();

        return themeConstantsMap.get(themeName);
    }

    public Set<String> getAvailableThemes() {
        checkInitialized();

        return themeConstantsMap.keySet();
    }
}