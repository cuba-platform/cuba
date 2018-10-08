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

package com.haulmont.restapi.config;

import com.google.common.base.Strings;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Table;
import com.haulmont.bali.util.Dom4j;
import com.haulmont.cuba.core.global.Resources;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.restapi.transform.EntityJsonTransformer;
import com.haulmont.restapi.transform.JsonTransformationDirection;
import com.haulmont.restapi.transform.StandardEntityJsonTransformer;
import org.apache.commons.io.IOUtils;
import org.apache.commons.text.StringTokenizer;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Class is used for loading and storing JSON transformers. These transformers are used when a request to the REST API
 * is made by the client that works with a data model of the previous version. In this case transformer transforms a
 * JSON from the old data model version to the current or vice versa. JSON transformers are loaded from configuration
 * files defined by the {@code cuba.rest.jsonTransformationConfig} application property.
 */
@Component("cuba_RestJsonTransformations")
public class RestJsonTransformations {

    protected final String CUBA_REST_JSON_TRANSFORMATION_CONFIG_PROP_NAME = "cuba.rest.jsonTransformationConfig";

    private static final Logger log = LoggerFactory.getLogger(RestJsonTransformations.class);

    protected volatile boolean initialized;

    protected ReadWriteLock lock = new ReentrantReadWriteLock();

    protected Table<String, String, EntityJsonTransformer> toVersionTransformers = HashBasedTable.create();

    protected Table<String, String, EntityJsonTransformer> fromVersionTransformers = HashBasedTable.create();

    @Inject
    protected Resources resources;

    //inject the context here because we can't use the AppContext for getting beans. REST API has its own spring context
    @Inject
    protected WebApplicationContext restApiContext;

    /**
     * Tries to find the transformer for the given entity, model version and transformation direction. If such
     * transformer is defined in the transformers configuration file then it will be returned. If there is no
     * transformer defined, a dummy transformer that doesn't do any transformation will be returned.
     *
     * @param entityName           entity name
     * @param modelVersion         domain model version
     * @param transformerDirection transformation direction (to version or from version)
     * @return a JSON transformer instance
     */
    public EntityJsonTransformer getTransformer(String entityName, String modelVersion, JsonTransformationDirection transformerDirection) {
        lock.readLock().lock();
        try {
            checkInitialized();
            EntityJsonTransformer transformer;
            switch (transformerDirection) {
                case TO_VERSION:
                    transformer = toVersionTransformers.get(entityName, modelVersion);
                    break;
                case FROM_VERSION:
                    transformer = fromVersionTransformers.get(entityName, modelVersion);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown transformerType: " + transformerDirection);
            }

            if (transformer == null) {
                //create a transformer that doesn't do transformation to current entity attributes, but processes nested entities
                transformer = restApiContext.getBean(StandardEntityJsonTransformer.class,
                        entityName,
                        entityName,
                        modelVersion,
                        transformerDirection);
            }

            return transformer;
        } finally {
            lock.readLock().unlock();
        }
    }

    protected void checkInitialized() {
        if (!initialized) {
            lock.readLock().unlock();
            lock.writeLock().lock();
            try {
                if (!initialized) {
                    init();
                    initialized = true;
                }
            } finally {
                lock.readLock().lock();
                lock.writeLock().unlock();
            }
        }
    }

    protected void init() {
        String configName = AppContext.getProperty(CUBA_REST_JSON_TRANSFORMATION_CONFIG_PROP_NAME);
        StringTokenizer tokenizer = new StringTokenizer(configName);
        for (String location : tokenizer.getTokenArray()) {
            Resource resource = resources.getResource(location);
            if (resource.exists()) {
                InputStream stream = null;
                try {
                    stream = resource.getInputStream();
                    loadConfig(Dom4j.readDocument(stream).getRootElement());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } finally {
                    IOUtils.closeQuietly(stream);
                }
            } else {
                log.warn("Resource " + location + " not found, ignore it");
            }
        }
    }

    protected void loadConfig(Element rootElem) {
        for (Element transformationElem : Dom4j.elements(rootElem, "transformation")) {
            String modelVersion = transformationElem.attributeValue("modelVersion");
            if (Strings.isNullOrEmpty(modelVersion)) {
                log.error("modelVersion attribute is not defined");
                continue;
            }
            String currentEntityName = transformationElem.attributeValue("currentEntityName");
            if (Strings.isNullOrEmpty(currentEntityName)) {
                log.error("currentEntityName attribute is not defined");
                continue;
            }

            String oldEntityName = transformationElem.attributeValue("oldEntityName");
            if (Strings.isNullOrEmpty(oldEntityName)) {
                oldEntityName = currentEntityName;
            }

            Element customElem = transformationElem.element("custom");

            if (customElem != null) {
                parseCustomTransformers(currentEntityName, oldEntityName, modelVersion, customElem);
            } else {
                parseStandardTransformers(currentEntityName, oldEntityName, modelVersion, transformationElem);
            }
        }
    }

    protected void parseStandardTransformers(String currentEntityName, String oldEntityName, String version, Element transformationElem) {
        BiMap<String, String> renamedAttributesMap = HashBiMap.create();

        for (Element renameAttributeElem : Dom4j.elements(transformationElem, "renameAttribute")) {
            String oldAttributeName = renameAttributeElem.attributeValue("oldName");
            if (Strings.isNullOrEmpty(oldAttributeName)) {
                log.error("oldName attribute for renamed attribute is not defined");
                continue;
            }

            String currentAttributeName = renameAttributeElem.attributeValue("currentName");
            if (Strings.isNullOrEmpty(currentAttributeName)) {
                log.error("currentName attribute for renamed attribute is not defined");
                continue;
            }
            renamedAttributesMap.put(oldAttributeName, currentAttributeName);
        }

        StandardEntityJsonTransformer fromVersionTransformer = restApiContext.getBean(StandardEntityJsonTransformer.class,
                oldEntityName,
                currentEntityName,
                version,
                JsonTransformationDirection.FROM_VERSION);
        fromVersionTransformer.setAttributesToRename(renamedAttributesMap);

        StandardEntityJsonTransformer toVersionTransformer = restApiContext.getBean(StandardEntityJsonTransformer.class,
                currentEntityName,
                oldEntityName,
                version,
                JsonTransformationDirection.TO_VERSION);
        toVersionTransformer.setAttributesToRename(renamedAttributesMap.inverse());

        Element fromVersionElem = transformationElem.element("fromVersion");
        if (fromVersionElem != null) {
            processOneDirectionConfig(fromVersionElem, fromVersionTransformer);
        }

        Element toVersionElem = transformationElem.element("toVersion");
        if (toVersionElem != null) {
            processOneDirectionConfig(toVersionElem, toVersionTransformer);
        }

        toVersionTransformers.put(currentEntityName, version, toVersionTransformer);
        fromVersionTransformers.put(oldEntityName, version, fromVersionTransformer);
    }

    protected void parseCustomTransformers(String currentEntityName, String oldEntityName, String version, Element customElem) {
        Element fromVersionElem = customElem.element("fromVersion");
        if (fromVersionElem != null) {
            String transformerBeanName = fromVersionElem.attributeValue("transformerBeanRef");
            try {
                Object transformer = restApiContext.getBean(transformerBeanName);
                if (transformer instanceof EntityJsonTransformer) {
                    fromVersionTransformers.put(oldEntityName, version, (EntityJsonTransformer) transformer);
                } else {
                    log.error("Custom transformer {} doesn't implement EntityJsonTransformer", transformerBeanName);
                }
            } catch (NoSuchBeanDefinitionException e) {
                log.error("Bean {} for custom transformer not found", transformerBeanName);
            }
        }

        Element toVersionElem = customElem.element("toVersion");
        if (toVersionElem != null) {
            String transformerBeanName = toVersionElem.attributeValue("transformerBeanRef");
            try {
                Object transformer = restApiContext.getBean(transformerBeanName);
                if (transformer instanceof EntityJsonTransformer) {
                    toVersionTransformers.put(currentEntityName, version, (EntityJsonTransformer) transformer);
                } else {
                    log.error("Custom transformer {} doesn't implement EntityJsonTransformer", transformerBeanName);
                }
            } catch (NoSuchBeanDefinitionException e) {
                log.error("Bean {} for custom transformer not found", transformerBeanName);
            }
        }
    }

    protected void processOneDirectionConfig(Element element, StandardEntityJsonTransformer transformer) {
        Set<String> removeAttributes = new HashSet<>();
        for (Element removeAttributeElem : Dom4j.elements(element, "removeAttribute")) {
            String name = removeAttributeElem.attributeValue("name");
            if (Strings.isNullOrEmpty(name)) {
                log.error("name for removeAttribute element is not specified");
                continue;
            }
            removeAttributes.add(name);
        }
        transformer.setAttributesToRemove(removeAttributes);
    }
}
