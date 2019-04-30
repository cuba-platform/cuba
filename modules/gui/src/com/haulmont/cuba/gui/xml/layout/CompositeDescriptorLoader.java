/*
 * Copyright (c) 2008-2019 Haulmont.
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

package com.haulmont.cuba.gui.xml.layout;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.haulmont.bali.util.Dom4j;
import com.haulmont.cuba.core.global.DevelopmentException;
import com.haulmont.cuba.core.global.Resources;
import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.Element;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@org.springframework.stereotype.Component(CompositeDescriptorLoader.NAME)
public class CompositeDescriptorLoader {

    public static final String NAME = "cuba_CompositeDescriptorLoader";

    protected static final int CACHE_DESCRIPTORS_COUNT = 20;

    protected Cache<String, Document> cache;

    @Inject
    protected Resources resources;

    public Element load(String path) {
        String descriptor = loadDescriptor(path);
        Document document = getDocument(descriptor);
        return document.getRootElement();
    }

    protected String loadDescriptor(String resourcePath) {
        try (InputStream stream = resources.getResourceAsStream(resourcePath)) {
            if (stream == null) {
                throw new DevelopmentException("Composite component descriptor not found " + resourcePath, "Path", resourcePath);
            }
            return IOUtils.toString(stream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Unable to read composite component descriptor");
        }
    }

    protected Document getDocument(String descriptor) {
        if (cache == null) {
            cache = CacheBuilder.newBuilder()
                    .maximumSize(CACHE_DESCRIPTORS_COUNT)
                    .build();
        }

        Document document = cache.getIfPresent(descriptor);
        if (document == null) {
            document = createDocument(descriptor);
            cache.put(descriptor, document);
        }

        return document;
    }

    protected Document createDocument(String descriptor) {
        return Dom4j.readDocument(descriptor);
    }
}
