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

package com.haulmont.cuba.gui.model;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.Security;
import com.haulmont.cuba.gui.model.impl.*;
import com.haulmont.cuba.security.entity.EntityOp;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Collection;

/**
 * Factory bean for data API components.
 */
@Component("cuba_DataComponents")
public class DataComponents implements ApplicationContextAware {

    @Inject
    protected Metadata metadata;

    @Inject
    protected Security security;

    @Inject
    protected SorterFactory sorterFactory;

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(@Nonnull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * Creates {@code DataContext}.
     */
    public DataContext createDataContext() {
        return new DataContextImpl(applicationContext);
    }

    /**
     * Creates {@code InstanceContainer}.
     */
    public <E extends Entity> InstanceContainer<E> createInstanceContainer(Class<E> entityClass) {
        return new InstanceContainerImpl<>(metadata.getClassNN(entityClass));
    }

    /**
     * Creates {@code InstancePropertyContainer}.
     */
    @SuppressWarnings("unchecked")
    public <E extends Entity> InstancePropertyContainer<E> createInstanceContainer(Class<E> entityClass,
                                                                                   InstanceContainer<? extends Entity> masterContainer,
                                                                                   String property) {
        InstancePropertyContainerImpl<E> container = new InstancePropertyContainerImpl<>(
                metadata.getClassNN(entityClass), masterContainer, property);

        if (security.isEntityAttrReadPermitted(masterContainer.getEntityMetaClass(), property)
                && security.isEntityOpPermitted(entityClass, EntityOp.READ)) {
            masterContainer.addItemChangeListener(e -> {
                Entity item = masterContainer.getItemOrNull();
                container.setItem(item != null ? item.getValue(property) : null);
            });

            masterContainer.addItemPropertyChangeListener(e -> {
                if (e.getProperty().equals(property)) {
                    container.setItem((E) e.getValue());
                }
            });
        }

        return container;
    }

    /**
     * Creates {@code CollectionContainer}.
     */
    public <E extends Entity> CollectionContainer<E> createCollectionContainer(Class<E> entityClass) {
        return new CollectionContainerImpl<>(metadata.getClassNN(entityClass));
    }

    /**
     * Creates {@code CollectionPropertyContainer}.
     */
    @SuppressWarnings("unchecked")
    public <E extends Entity> CollectionPropertyContainer<E> createCollectionContainer(Class<E> entityClass,
                                                                                       InstanceContainer<? extends Entity> masterContainer,
                                                                                       String property) {
        CollectionPropertyContainerImpl<E> container = new CollectionPropertyContainerImpl<>(
                metadata.getClassNN(entityClass), masterContainer, property);
        container.setSorter(sorterFactory.createCollectionPropertyContainerSorter(container));

        if (security.isEntityAttrReadPermitted(masterContainer.getEntityMetaClass(), property)
                && security.isEntityOpPermitted(entityClass, EntityOp.READ)) {
            masterContainer.addItemChangeListener(e -> {
                Entity item = masterContainer.getItemOrNull();
                container.setItems(item != null ? item.getValue(property) : null);
            });

            masterContainer.addItemPropertyChangeListener(e -> {
                if (e.getProperty().equals(property)) {
                    container.setDisconnectedItems((Collection<E>) e.getValue());
                }
            });
        }

        return container;
    }

    /**
     * Creates {@code KeyValueContainer}.
     */
    public KeyValueContainer createKeyValueContainer() {
        return new KeyValueContainerImpl();
    }

    /**
     * Creates {@code KeyValueCollectionContainer}.
     */
    public KeyValueCollectionContainer createKeyValueCollectionContainer() {
        return new KeyValueCollectionContainerImpl();
    }

    /**
     * Creates {@code InstanceLoader}.
     */
    public <E extends Entity> InstanceLoader<E> createInstanceLoader() {
        return new InstanceLoaderImpl<>(applicationContext);
    }

    /**
     * Creates {@code CollectionLoader}.
     */
    public <E extends Entity> CollectionLoader<E> createCollectionLoader() {
        return new CollectionLoaderImpl<>(applicationContext);
    }

    /**
     * Creates {@code KeyValueCollectionLoader}.
     */
    public KeyValueCollectionLoader createKeyValueCollectionLoader() {
        return new KeyValueCollectionLoaderImpl(applicationContext);
    }

    /**
     * Creates {@code KeyValueInstanceLoader}.
     */
    public KeyValueInstanceLoader createKeyValueInstanceLoader() {
        return new KeyValueInstanceLoaderImpl(applicationContext);
    }
}