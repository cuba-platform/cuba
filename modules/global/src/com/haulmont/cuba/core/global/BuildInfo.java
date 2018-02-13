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

package com.haulmont.cuba.core.global;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Provides build information stored in global artifact by CubaBuildInfo Gradle task.
 */
@Component(BuildInfo.NAME)
public class BuildInfo {

    public static final String NAME = "cuba_BuildInfo";

    /**
     * Build information stored in global artifact by CubaBuildInfo Gradle task.
     */
    public static class Content {

        public final Map<String, String> properties;

        public Content(Map<String, String> properties) {
            this.properties = properties;
        }

        public Content() {
            properties = Collections.emptyMap();
        }

        /**
         * @return all properties stored in build info.
         */
        public Map<String, String> getProperties() {
            return Collections.unmodifiableMap(properties);
        }

        /**
         * @return build date
         */
        public Date getBuildDate() {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                return dateFormat.parse(properties.get("buildDate"));
            } catch (ParseException e) {
                log.error("Error parsing date", e);
                return new Date(0);
            }
        }

        /**
         * @return appName property which is a project name by default
         */
        public String getAppName() {
            return Strings.nullToEmpty(properties.get("appName"));
        }

        /**
         * @return artifactGroup property which is cuba.artifact.group by default
         */
        public String getArtifactGroup() {
            return Strings.nullToEmpty(properties.get("artifactGroup"));
        }

        /**
         * @return version property which is cuba.artifact.version by default
         */
        public String getVersion() {
            return Strings.nullToEmpty(properties.get("version"));
        }

        /**
         * @return list of application components and their versions used at build time
         */
        public List<String> getAppComponents() {
            String appComponentsStr = properties.get("appComponents");
            if (appComponentsStr == null)
                return Collections.emptyList();
            return Splitter.on(',').omitEmptyStrings().splitToList(appComponentsStr);
        }
    }

    private static final Logger log = LoggerFactory.getLogger(BuildInfo.class);

    @Inject
    protected Metadata metadata;

    /**
     * @return build information
     */
    public Content getContent() {
        List<String> rootPackages = metadata.getRootPackages();
        if (rootPackages.isEmpty()) {
            log.debug("Empty rootPackages");
            return new Content();
        }
        String rootPackage = rootPackages.get(rootPackages.size() - 1);
        String resourcePath = "/" + rootPackage.replace('.', '/') + "/build-info.properties";
        InputStream inputStream = getClass().getResourceAsStream(resourcePath);
        if (inputStream == null) {
            log.debug("Resource {} not found", resourcePath);
            return new Content();
        }
        try {
            try (Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
                Map<String, String> properties = new LinkedHashMap<>();
                try (BufferedReader bufferedReader = new BufferedReader(reader)) {
                    bufferedReader.lines().forEach(line -> {
                        List<String> strings = Splitter.on('=').omitEmptyStrings().trimResults().splitToList(line);
                        if (strings.size() == 2) {
                            properties.put(strings.get(0), strings.get(1));
                        } else {
                            log.debug("Cannot parse line {}, ignoring it", line);
                        }
                    });
                }
                return new Content(properties);
            } catch (IOException e) {
                log.error("Error reading resource {}", resourcePath, e);
                return new Content();
            }
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }
}
