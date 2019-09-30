/*
 * Copyright (c) 2008-2018 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.haulmont.cuba.gui.model.impl;

import com.google.common.base.Strings;
import com.haulmont.bali.util.Preconditions;
import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.global.queryconditions.Condition;
import com.haulmont.cuba.core.global.queryconditions.ConditionXmlLoader;
import com.haulmont.cuba.core.sys.ViewLoader;
import com.haulmont.cuba.gui.model.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.dom4j.Element;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.text.ParseException;

@Component(ScreenDataXmlLoader.NAME)
public class ScreenDataXmlLoader {

    public static final String NAME = "cuba_ScreenDataXmlLoader";

    @Inject
    protected ViewRepository viewRepository;

    @Inject
    protected ViewLoader viewLoader;

    @Inject
    protected Metadata metadata;

    @Inject
    protected MetadataTools metadataTools;

    @Inject
    protected DataComponents factory;

    @Inject
    protected ConditionXmlLoader conditionXmlLoader;

    public void load(ScreenData screenData, Element element, @Nullable ScreenData hostScreenData) {
        Preconditions.checkNotNullArgument(screenData, "screenData is null");
        Preconditions.checkNotNullArgument(element, "element is null");

        DataContext hostDataContext = null;
        if (hostScreenData != null) {
            hostDataContext = hostScreenData.getDataContext();
        }
        if (hostDataContext != null) {
            screenData.setDataContext(hostDataContext);
        } else {
            boolean readOnly = Boolean.valueOf(element.attributeValue("readOnly"));
            DataContext dataContext = readOnly ? new NoopDataContext() : factory.createDataContext();
            screenData.setDataContext(dataContext);
        }

        for (Element el : element.elements()) {
            switch (el.getName()) {
                case "collection":
                    loadCollectionContainer(screenData, el, hostScreenData);
                    break;
                case "instance":
                    loadInstanceContainer(screenData, el, hostScreenData);
                    break;
                case "keyValueCollection":
                    loadKeyValueCollectionContainer(screenData, el, hostScreenData);
                    break;
                case "keyValueInstance":
                    loadKeyValueInstanceContainer(screenData, el, hostScreenData);
                    break;
                default:
                    // no action
                    break;
            }
        }
    }

    protected void loadInstanceContainer(ScreenData screenData, Element element, @Nullable ScreenData hostScreenData) {
        String containerId = getRequiredAttr(element, "id");

        InstanceContainer<Entity> container;

        if (checkProvided(element, hostScreenData)) {
            //noinspection ConstantConditions
            container = hostScreenData.getContainer(containerId);
        } else {
            container = factory.createInstanceContainer(getEntityClass(element));
            loadView(element, getEntityClass(element), container);
        }

        screenData.registerContainer(containerId, container);

        Element loaderEl = element.element("loader");
        if (loaderEl != null) {
            loadInstanceLoader(screenData, loaderEl, container, hostScreenData);
        }

        for (Element collectionEl : element.elements()) {
            loadNestedContainer(screenData, collectionEl, container, hostScreenData);
        }
    }

    protected void loadCollectionContainer(ScreenData screenData, Element element, @Nullable ScreenData hostScreenData) {
        String containerId = getRequiredAttr(element, "id");

        CollectionContainer<Entity> container;

        if (checkProvided(element, hostScreenData)) {
            //noinspection ConstantConditions
            container = hostScreenData.getContainer(containerId);
        } else {
            container = factory.createCollectionContainer(getEntityClass(element));
            loadView(element, getEntityClass(element), container);
        }

        screenData.registerContainer(containerId, container);

        Element loaderEl = element.element("loader");
        if (loaderEl != null) {
            loadCollectionLoader(screenData, loaderEl, container, hostScreenData);
        }

        for (Element collectionEl : element.elements()) {
            loadNestedContainer(screenData, collectionEl, container, hostScreenData);
        }
    }

    protected void loadKeyValueCollectionContainer(ScreenData screenData, Element element, @Nullable ScreenData hostScreenData) {
        String containerId = getRequiredAttr(element, "id");

        KeyValueCollectionContainer container;

        if (checkProvided(element, hostScreenData)) {
            //noinspection ConstantConditions
            container = hostScreenData.getContainer(containerId);
        } else {
            container = factory.createKeyValueCollectionContainer();

            loadProperties(element, container);

            String idName = element.attributeValue("idName");
            if (!Strings.isNullOrEmpty(idName))
                container.setIdName(idName);
        }

        screenData.registerContainer(containerId, container);

        Element loaderEl = element.element("loader");
        if (loaderEl != null) {
            loadKeyValueCollectionLoader(screenData, loaderEl, container, hostScreenData);
        }
    }

    protected void loadKeyValueInstanceContainer(ScreenData screenData, Element element, ScreenData hostScreenData) {
        String containerId = getRequiredAttr(element, "id");

        KeyValueContainer container;

        if (checkProvided(element, hostScreenData)) {
            container = hostScreenData.getContainer(containerId);
        } else {
            container = factory.createKeyValueContainer();

            loadProperties(element, container);

            String idName = element.attributeValue("idName");
            if (!Strings.isNullOrEmpty(idName))
                container.setIdName(idName);
        }

        screenData.registerContainer(containerId, container);

        Element loaderEl = element.element("loader");
        if (loaderEl != null) {
            loadKeyValueInstanceLoader(screenData, loaderEl, container, hostScreenData);
        }
    }

    private void loadProperties(Element element, KeyValueContainer container) {
        Element propsEl = element.element("properties");
        if (propsEl != null) {
            for (Element propEl : propsEl.elements()) {
                String name = propEl.attributeValue("name");
                String className = propEl.attributeValue("class");
                if (className != null) {
                    container.addProperty(name, ReflectionHelper.getClass(className));
                } else {
                    String typeName = propEl.attributeValue("datatype");
                    Datatype datatype = typeName == null ? Datatypes.getNN(String.class) : Datatypes.get(typeName);
                    container.addProperty(name, datatype);
                }
            }
            String idProperty = propsEl.attributeValue("idProperty");
            if (idProperty != null) {
                if (container.getEntityMetaClass().getProperty(idProperty) == null)
                    throw new DevelopmentException(String.format("Property '%s' is not defined", idProperty));
                container.setIdName(idProperty);
            }
        }
    }

    @SuppressWarnings("unchecked")
    protected void loadNestedContainer(ScreenData screenData, Element element, InstanceContainer<Entity> masterContainer,
                                       @Nullable ScreenData hostScreenData) {
        if (!element.getName().equals("collection") && !element.getName().equals("instance"))
            return;

        String containerId = getRequiredAttr(element, "id");

        String property = getRequiredAttr(element, "property");
        MetaProperty metaProperty = masterContainer.getEntityMetaClass().getPropertyNN(property);

        InstanceContainer nestedContainer = null;

        if (checkProvided(element, hostScreenData)) {
            //noinspection ConstantConditions
            nestedContainer = hostScreenData.getContainer(containerId);
        } else {
            if (element.getName().equals("collection")) {
                if (!metaProperty.getRange().isClass() || !metaProperty.getRange().getCardinality().isMany()) {
                    throw new IllegalStateException(String.format(
                            "Cannot bind collection container '%s' to a non-collection property '%s'", containerId, property));
                }
                nestedContainer = factory.createCollectionContainer(
                        metaProperty.getRange().asClass().getJavaClass(), masterContainer, property);

            } else if (element.getName().equals("instance")) {
                if (!metaProperty.getRange().isClass() || metaProperty.getRange().getCardinality().isMany()) {
                    throw new IllegalStateException(String.format(
                            "Cannot bind instance container '%s' to a non-reference property '%s'", containerId, property));
                }
                nestedContainer = factory.createInstanceContainer(
                        metaProperty.getRange().asClass().getJavaClass(), masterContainer, property);
            }
        }

        if (nestedContainer != null) {
            screenData.registerContainer(containerId, nestedContainer);

            for (Element collectionEl : element.elements()) {
                loadNestedContainer(screenData, collectionEl, nestedContainer, hostScreenData);
            }
        }
    }

    protected void loadInstanceLoader(ScreenData screenData, Element element, InstanceContainer<Entity> container,
                                      @Nullable ScreenData hostScreenData) {
        InstanceLoader<Entity> loader;

        String loaderId = element.attributeValue("id");
        if (loaderId == null) {
            loaderId = generateId();
        }

        if (checkProvided(element, hostScreenData)) {
            //noinspection ConstantConditions
            loader = hostScreenData.getLoader(loaderId);
        } else {
            loader = factory.createInstanceLoader();
            loader.setDataContext(screenData.getDataContext());
            loader.setContainer(container);

            loadSoftDeletion(element, loader);
            loadDynamicAttributes(element, loader);
            loadQuery(element, loader);
            loadEntityId(element, loader);
        }

        screenData.registerLoader(loaderId, loader);
    }

    protected void loadCollectionLoader(ScreenData screenData, Element element, CollectionContainer<Entity> container,
                                        @Nullable ScreenData hostScreenData) {
        CollectionLoader<Entity> loader;

        String loaderId = element.attributeValue("id");
        if (loaderId == null) {
            loaderId = generateId();
        }

        if (checkProvided(element, hostScreenData)) {
            //noinspection ConstantConditions
            loader = hostScreenData.getLoader(loaderId);
        } else {
            loader = factory.createCollectionLoader();
            loader.setDataContext(screenData.getDataContext());
            loader.setContainer(container);

            loadQuery(element, loader);
            loadSoftDeletion(element, loader);
            loadDynamicAttributes(element, loader);
            loadFirstResult(element, loader);
            loadMaxResults(element, loader);
            loadCacheable(element, loader);
        }

        screenData.registerLoader(loaderId, loader);
    }

    protected void loadKeyValueCollectionLoader(ScreenData screenData, Element element, KeyValueCollectionContainer container,
                                                @Nullable ScreenData hostScreenData) {
        KeyValueCollectionLoader loader;

        String loaderId = element.attributeValue("id");
        if (loaderId == null) {
            loaderId = generateId();
        }

        if (checkProvided(element, hostScreenData)) {
            //noinspection ConstantConditions
            loader = hostScreenData.getLoader(loaderId);
        } else {
            loader = factory.createKeyValueCollectionLoader();
            loader.setContainer(container);

            loadQuery(element, loader);
            loadSoftDeletion(element, loader);
            loadFirstResult(element, loader);
            loadMaxResults(element, loader);

            String storeName = element.attributeValue("store");
            if (!Strings.isNullOrEmpty(storeName))
                loader.setStoreName(storeName);
        }

        screenData.registerLoader(loaderId, loader);
    }

    protected void loadKeyValueInstanceLoader(ScreenData screenData, Element element, KeyValueContainer container, ScreenData hostScreenData) {
        KeyValueInstanceLoader loader;

        String loaderId = element.attributeValue("id");
        if (loaderId == null) {
            loaderId = generateId();
        }

        if (checkProvided(element, hostScreenData)) {
            loader = hostScreenData.getLoader(loaderId);
        } else {
            loader = factory.createKeyValueInstanceLoader();
            loader.setContainer(container);

            loadQuery(element, loader);
            loadSoftDeletion(element, loader);

            String storeName = element.attributeValue("store");
            if (!Strings.isNullOrEmpty(storeName))
                loader.setStoreName(storeName);
        }

        screenData.registerLoader(loaderId, loader);
    }

    protected Class<Entity> getEntityClass(Element element) {
        String entityClassName = getRequiredAttr(element, "class");
        return ReflectionHelper.getClass(entityClassName);
    }

    protected void loadView(Element element, Class<Entity> entityClass, InstanceContainer<Entity> container) {
        Element viewElement = element.element("view");
        if (viewElement != null) {
            container.setView(loadAdHocView(viewElement, entityClass));
            return;
        }

        String viewName = element.attributeValue("view");
        if (viewName != null) {
            container.setView(viewRepository.getView(entityClass, viewName));
        }
    }

    protected View loadAdHocView(Element viewElem, Class<Entity> entityClass) {
        ViewLoader.ViewInfo viewInfo = viewLoader.getViewInfo(viewElem, metadata.getClassNN(entityClass));
        View.ViewParams viewParams = viewLoader.getViewParams(viewInfo, a -> viewRepository.getView(viewInfo.getMetaClass(), a));
        View view = new View(viewParams);
        viewLoader.loadViewProperties(viewElem, view, viewInfo.isSystemProperties(), (metaClass, viewName) -> viewRepository.getView(metaClass, viewName));
        return view;
    }

    protected void loadQuery(Element element, DataLoader loader) {
        Element queryEl = element.element("query");
        if (queryEl != null) {
            loader.setQuery(loadQueryText(queryEl));
            Element conditionEl = queryEl.element("condition");
            if (conditionEl != null) {
                if (!conditionEl.elements().isEmpty()) {
                    if (conditionEl.elements().size() == 1) {
                        Condition condition = conditionXmlLoader.fromXml(conditionEl.elements().get(0));
                        loader.setCondition(condition);
                    } else {
                        throw new IllegalStateException("'condition' element must have exactly one nested element");
                    }
                }
            }
        }
    }

    protected String loadQueryText(Element queryEl) {
        return queryEl.getText().trim();
    }

    protected void loadSoftDeletion(Element element, DataLoader loader) {
        String softDeletionVal = element.attributeValue("softDeletion");
        if (!Strings.isNullOrEmpty(softDeletionVal))
            loader.setSoftDeletion(Boolean.valueOf(softDeletionVal));
    }

    protected void loadDynamicAttributes(Element element, DataLoader loader) {
        String dynamicAttributes = element.attributeValue("dynamicAttributes");
        if (!Strings.isNullOrEmpty(dynamicAttributes)) {
            if (loader instanceof InstanceLoader) {
                ((InstanceLoader) loader).setLoadDynamicAttributes(Boolean.valueOf(dynamicAttributes));
            } else if (loader instanceof CollectionLoader) {
                ((CollectionLoader) loader).setLoadDynamicAttributes(Boolean.valueOf(dynamicAttributes));
            }
        }
    }

    protected void loadEntityId(Element element, InstanceLoader<Entity> loader) {
        String entityIdStr = element.attributeValue("entityId");
        if (Strings.isNullOrEmpty(entityIdStr)) {
            return;
        }
        MetaProperty property = metadataTools.getPrimaryKeyProperty(loader.getContainer().getEntityMetaClass());
        if (property == null) {
            throw new IllegalStateException("Cannot determine id property for " + loader.getContainer().getEntityMetaClass());
        }
        if (property.getRange().isDatatype()) {
            try {
                Object value = property.getRange().asDatatype().parse(entityIdStr);
                loader.setEntityId(value);
            } catch (ParseException e) {
                throw new RuntimeException("Error parsing entityId for " + loader, e);
            }
        } else {
            throw new IllegalStateException("Cannot assign id to " + loader + " because the entity has a composite PK");
        }
    }

    protected void loadFirstResult(Element element, BaseCollectionLoader loader) {
        String firstResultStr = element.attributeValue("firstResult");
        if (Strings.isNullOrEmpty(firstResultStr))
            return;

        loader.setFirstResult(Integer.parseInt(firstResultStr));
    }

    protected void loadMaxResults(Element element, BaseCollectionLoader loader) {
        String maxResultsStr = element.attributeValue("maxResults");
        if (Strings.isNullOrEmpty(maxResultsStr))
            return;

        loader.setMaxResults(Integer.parseInt(maxResultsStr));
    }

    protected void loadCacheable(Element element, CollectionLoader<Entity> loader) {
        String cacheableVal = element.attributeValue("cacheable");
        if (!Strings.isNullOrEmpty(cacheableVal))
            loader.setCacheable(Boolean.valueOf(cacheableVal));
    }

    protected String getRequiredAttr(Element element, String attributeName) {
        String id = element.attributeValue(attributeName);
        if (id == null)
            throw new IllegalStateException("Required attribute '" + attributeName + "' not found in " + element);
        return id.trim();
    }

    protected boolean checkProvided(Element element, ScreenData hostScreenData) {
        boolean provided = Boolean.parseBoolean(element.attributeValue("provided"));
        if (provided && hostScreenData == null) {
            throw new IllegalStateException("Host ScreenData is null");
        }
        return provided;
    }

    protected String generateId() {
        return RandomStringUtils.randomAlphanumeric(8);
    }
}
