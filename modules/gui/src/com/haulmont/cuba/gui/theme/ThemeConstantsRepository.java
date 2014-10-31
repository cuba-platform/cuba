/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.theme;

import com.haulmont.cuba.core.global.DevelopmentException;
import com.haulmont.cuba.core.global.Resources;
import com.haulmont.cuba.core.sys.AppContext;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author artamonov
 * @version $Id$
 */
@ManagedBean(ThemeConstantsRepository.NAME)
public class ThemeConstantsRepository {

    public static final String NAME = "cuba_ThemeConstantsRepository";

    @Inject
    protected Resources resources;

    protected Log log = LogFactory.getLog(getClass());

    private volatile boolean initialized;

    protected Map<String, ThemeConstants> themeConstantsMap;

    protected void checkInitialized() {
        if (!initialized) {
            synchronized (this) {
                if (!initialized) {
                    log.info("Loading theme constants");
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

            Map<String, ThemeConstants> themes = new HashMap<>();

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
}