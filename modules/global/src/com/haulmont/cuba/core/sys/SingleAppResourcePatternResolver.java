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

package com.haulmont.cuba.core.sys;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SingleAppResourcePatternResolver extends PathMatchingResourcePatternResolver {

    public static final Pattern JAR_NAME_PATTERN = Pattern.compile(".*/(.+?\\.jar).*");

    private String libFolder;

    public SingleAppResourcePatternResolver(ResourceLoader resourceLoader, String libFolder) {
        super(resourceLoader);
        this.libFolder = libFolder;
    }

    @Override
    public Resource[] getResources(String locationPattern) throws IOException {
        Resource[] resources = super.getResources(locationPattern);
        return Arrays.stream(resources)
                .filter(this::foundInDependencies)
                .toArray(Resource[]::new);
    }

    private boolean foundInDependencies(Resource resource) {
        try {
            String url = resource.getURL().toString();
            if (url.contains("META-INF/resources/webjars/")) {
                // WebJAR resources could be loaded from shared dependencies
                return true;
            }

            Matcher matcher = JAR_NAME_PATTERN.matcher(url);
            if (matcher.find()) {
                return url.contains(libFolder);
            }
            return true;
        } catch (IOException e) {
            throw new RuntimeException("An error occurred while looking for resources", e);
        }
    }
}
