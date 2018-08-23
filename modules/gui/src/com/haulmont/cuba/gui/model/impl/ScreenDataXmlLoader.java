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
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.core.global.ViewRepository;
import com.haulmont.cuba.core.global.queryconditions.Condition;
import com.haulmont.cuba.core.global.queryconditions.ConditionXmlLoader;
import com.haulmont.cuba.gui.model.*;
import org.dom4j.Element;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.text.ParseException;

@Component(ScreenDataXmlLoader.NAME)
public class ScreenDataXmlLoader {

    public static final String NAME = "cuba_ScreenDataXmlLoader";

    @Inject
    protected ViewRepository viewRepository;

    @Inject
    protected MetadataTools metadataTools;

    @Inject
    protected DataContextFactory factory;

    @Inject
    protected ConditionXmlLoader conditionXmlLoader;

    public void load(ScreenData screenData, Element element) {
        Preconditions.checkNotNullArgument(screenData, "screenData is null");
        Preconditions.checkNotNullArgument(element, "element is null");

        for (Element el : element.elements()) {
            if (el.getName().equals("collection")) {
                loadNestedContainer(screenData, el);
            } else if (el.getName().equals("instance")) {
                loadInstanceContainer(screenData, el);
            }
        }
    }

    protected void loadInstanceContainer(ScreenData screenData, Element element) {
        String containerId = getRequiredAttr(element, "id");

        InstanceContainer<Entity> container = factory.createInstanceContainer(getEntityClass(element));
        loadView(element, getEntityClass(element), container);

        Element loaderEl = element.element("loader");
        if (loaderEl != null) {
            loadInstanceLoader(screenData, loaderEl, container);
        }

        screenData.registerContainer(containerId, container);

        for (Element collectionEl : element.elements()) {
            loadNestedContainer(screenData, collectionEl, container);
        }
    }

    protected void loadNestedContainer(ScreenData screenData, Element element) {
        String containerId = getRequiredAttr(element, "id");

        CollectionContainer<Entity> container = factory.createCollectionContainer(getEntityClass(element));
        loadView(element, getEntityClass(element), container);

        Element loaderEl = element.element("loader");
        if (loaderEl != null) {
            loadCollectionLoader(screenData, loaderEl, container);
        }

        screenData.registerContainer(containerId, container);

        for (Element collectionEl : element.elements()) {
            loadNestedContainer(screenData, collectionEl, container);
        }
    }

    @SuppressWarnings("unchecked")
    protected void loadNestedContainer(ScreenData screenData, Element element, InstanceContainer<Entity> parentContainer) {
        if (!element.getName().equals("collection") && !element.getName().equals("instance"))
            return;

        String containerId = getRequiredAttr(element, "id");

        String property = getRequiredAttr(element, "property");
        MetaProperty metaProperty = parentContainer.getEntityMetaClass().getPropertyNN(property);

        InstanceContainer nestedContainer = null;

        if (element.getName().equals("collection")) {
            if (!metaProperty.getRange().isClass() || !metaProperty.getRange().getCardinality().isMany()) {
                throw new IllegalStateException(String.format(
                        "Cannot bind collection container '%s' to a non-collection property '%s'", containerId, property));
            }
            CollectionContainer<Entity> container = factory.createCollectionContainer(
                    metaProperty.getRange().asClass().getJavaClass());

            parentContainer.addItemChangeListener(e -> {
                container.setItems(parentContainer.getItem().getValue(property));
            });
            nestedContainer = container;

        } else if (element.getName().equals("instance")) {
            if (!metaProperty.getRange().isClass() || metaProperty.getRange().getCardinality().isMany()) {
                throw new IllegalStateException(String.format(
                        "Cannot bind instance container '%s' to a non-reference property '%s'", containerId, property));
            }
            InstanceContainer<Entity> container = factory.createInstanceContainer(
                    metaProperty.getRange().asClass().getJavaClass());

            parentContainer.addItemChangeListener(e -> {
                container.setItem(parentContainer.getItem().getValue(property));
            });

            nestedContainer = container;
        }

        if (nestedContainer != null) {
            screenData.registerContainer(containerId, nestedContainer);

            for (Element collectionEl : element.elements()) {
                loadNestedContainer(screenData, collectionEl, nestedContainer);
            }
        }
    }

    protected void loadInstanceLoader(ScreenData screenData, Element element, InstanceContainer<Entity> container) {
        InstanceLoader<Entity> loader = factory.createInstanceLoader();
        loader.setDataContext(screenData.getDataContext());
        loader.setContainer(container);

        loadSoftDeletion(element, loader);
        loadQuery(element, loader);
        loadEntityId(element, loader);

        String loaderId = element.attributeValue("id");
        if (loaderId != null) {
            screenData.registerLoader(loaderId, loader);
        }
    }

    protected void loadCollectionLoader(ScreenData screenData, Element element, CollectionContainer<Entity> container) {
        CollectionLoader<Entity> loader = factory.createCollectionLoader();
        loader.setDataContext(screenData.getDataContext());
        loader.setContainer(container);

        loadQuery(element, loader);
        loadSoftDeletion(element, loader);
        loadFirstResult(element, loader);
        loadMaxResults(element, loader);
        loadCacheable(element, loader);

        String loaderId = element.attributeValue("id");
        if (loaderId != null) {
            screenData.registerLoader(loaderId, loader);
        }
    }

    protected Class<Entity> getEntityClass(Element element) {
        String entityClassName = getRequiredAttr(element, "class");
        return ReflectionHelper.getClass(entityClassName);
    }

    protected void loadView(Element element, Class<Entity> entityClass, InstanceContainer<Entity> container) {
        String viewName = element.attributeValue("view");
        if (viewName != null) {
            container.setView(viewRepository.getView(entityClass, viewName));
        }
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

    protected void loadFirstResult(Element element, CollectionLoader<Entity> loader) {
        String firstResultStr = element.attributeValue("firstResult");
        if (Strings.isNullOrEmpty(firstResultStr))
            return;

        loader.setFirstResult(Integer.valueOf(firstResultStr));
    }

    protected void loadMaxResults(Element element, CollectionLoader<Entity> loader) {
        String maxResultsStr = element.attributeValue("maxResults");
        if (Strings.isNullOrEmpty(maxResultsStr))
            return;

        loader.setMaxResults(Integer.valueOf(maxResultsStr));
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
}
