/*
 * Copyright (c) 2008-2018 Haulmont.
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

package com.haulmont.cuba.gui.sys;

import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Stream;

public abstract class AbstractScanConfiguration {
    public static final String DEFAULT_CLASS_RESOURCE_PATTERN = "**/*.class";

    protected abstract MetadataReaderFactory getMetadataReaderFactory();

    protected abstract ResourceLoader getResourceLoader();

    protected abstract Environment getEnvironment();

    protected Stream<MetadataReader> scanPackage(String packageName) {
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                resolveBasePackage(packageName) + '/' + DEFAULT_CLASS_RESOURCE_PATTERN;
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver(getResourceLoader());
        Resource[] resources;
        try {
            resources = resourcePatternResolver.getResources(packageSearchPath);
        } catch (IOException e) {
            throw new RuntimeException("Unable to scan package " + packageName, e);
        }

        return Arrays.stream(resources)
                .filter(Resource::isReadable)
                .map(resource -> {
                    try {
                        return getMetadataReaderFactory().getMetadataReader(resource);
                    } catch (IOException e) {
                        throw new RuntimeException("Unable to read resource " + resource, e);
                    }
                });
    }

    protected String resolveBasePackage(String basePackage) {
        Environment environment = getEnvironment();
        return ClassUtils.convertClassNameToResourcePath(environment.resolveRequiredPlaceholders(basePackage));
    }
}