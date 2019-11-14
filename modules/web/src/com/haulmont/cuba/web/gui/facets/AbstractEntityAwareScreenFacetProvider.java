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

package com.haulmont.cuba.web.gui.facets;

import com.haulmont.bali.util.ReflectionHelper;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import org.dom4j.Element;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * Base class for entity aware screen facet providers.
 *
 * @param <T> screen facet type
 *
 * @see EditorScreenFacet
 * @see LookupScreenFacet
 */
public abstract class AbstractEntityAwareScreenFacetProvider<T extends ScreenFacet & EntityAwareScreenFacet>
        extends AbstractScreenFacetProvider<T> {

    protected abstract Metadata getMetadata();

    @Override
    public void loadFromXml(T facet, Element element, ComponentLoader.ComponentContext context) {
        super.loadFromXml(facet, element, context);

        loadEntityClass(facet, element, context);
        loadListComponent(facet, element, context);
        loadField(facet, element, context);
        loadContainer(facet, element, context);
    }

    @SuppressWarnings("unchecked")
    protected void loadEntityClass(T facet, Element element,
                                   ComponentLoader.ComponentContext context) {
        String entityClassFqn = element.attributeValue("entityClass");
        if (isNotEmpty(entityClassFqn)) {
            try {
                Class clazz = ReflectionHelper.loadClass(entityClassFqn);

                MetaClass entityClass = getMetadata().getClass(clazz);
                if (entityClass != null) {
                    facet.setEntityClass(((Class<? extends Entity>) clazz));
                } else {
                    throw new GuiDevelopmentException(
                            String.format("Screen entity class '%s' does not extend Entity class", entityClassFqn),
                            context);
                }
            } catch (ClassNotFoundException e) {
                throw new GuiDevelopmentException(
                        String.format("Unable to load screen entity class: '%s'", entityClassFqn),
                        context);
            }
        }
    }

    @SuppressWarnings("unchecked")
    protected void loadListComponent(T facet, Element element,
                                     ComponentLoader.ComponentContext context) {
        String listComponentId = element.attributeValue("listComponent");
        if (isNotEmpty(listComponentId)) {
            Component component = context.getFrame().getComponent(listComponentId);
            if (component != null) {
                if (component instanceof ListComponent) {
                    facet.setListComponent(((ListComponent) component));
                } else {
                    throw new GuiDevelopmentException(
                            String.format("Screen listComponent '%s' should inherit ListComponent interface",
                                    listComponentId),
                            context);
                }
            } else {
                throw new GuiDevelopmentException(
                        String.format("Screen listComponent with id '%s' not found", listComponentId),
                        context);
            }
        }
    }

    @SuppressWarnings("unchecked")
    protected void loadField(T facet, Element element,
                             ComponentLoader.ComponentContext context) {
        String fieldId = element.attributeValue("field");
        if (isNotEmpty(fieldId)) {
            Component component = context.getFrame().getComponent(fieldId);
            if (component != null) {
                if (component instanceof PickerField) {
                    facet.setPickerField(((PickerField) component));
                } else {
                    throw new GuiDevelopmentException(
                            String.format("Screen field '%s' should be PickerField", fieldId),
                            context);
                }
            } else {
                throw new GuiDevelopmentException(
                        String.format("Screen field with id '%s' not found", fieldId),
                        context);
            }
        }
    }

    @SuppressWarnings("unchecked")
    protected void loadContainer(T facet, Element element,
                                 ComponentLoader.ComponentContext context) {
        String containerId = element.attributeValue("container");
        if (isNotEmpty(containerId)) {
            InstanceContainer container = context.getScreenData().getContainer(containerId);
            if (container != null) {
                if (container instanceof CollectionContainer) {
                    facet.setContainer(((CollectionContainer) container));
                } else {
                    throw new GuiDevelopmentException(
                            String.format("Screen container '%s' should inherit CollectionContainer interface",
                                    containerId),
                            context);
                }
            } else {
                throw new GuiDevelopmentException(
                        String.format("Screen container '%s' not found", containerId),
                        context);
            }
        }
    }
}
