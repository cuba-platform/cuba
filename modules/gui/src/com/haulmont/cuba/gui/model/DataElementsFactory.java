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
import com.haulmont.cuba.gui.model.impl.*;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.inject.Inject;

/**
 * Factory bean for data API elements.
 */
@Component("cuba_DataElementsFactory")
public class DataElementsFactory implements ApplicationContextAware {

    @Inject
    protected Metadata metadata;

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(@Nonnull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * Creates {@code DataContext}.
     */
    public DataContext createDataContext() {
        return new StandardDataContext(applicationContext);
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
    public <E extends Entity> InstancePropertyContainer<E> createInstanceContainer(Class<E> entityClass,
                                                                                   InstanceContainer parent, String property) {
        return new InstancePropertyContainerImpl<>(metadata.getClassNN(entityClass), parent, property);
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
    public <E extends Entity> CollectionPropertyContainer<E> createCollectionContainer(Class<E> entityClass,
                                                                                       InstanceContainer parent, String property) {
        return new CollectionPropertyContainerImpl<>(metadata.getClassNN(entityClass), parent, property);
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
        return new StandardInstanceLoader<>(applicationContext);
    }

    /**
     * Creates {@code CollectionLoader}.
     */
    public <E extends Entity> CollectionLoader<E> createCollectionLoader() {
        return new StandardCollectionLoader<>(applicationContext);
    }

    /**
     * Creates {@code KeyValueCollectionLoader}.
     */
    public KeyValueCollectionLoader createKeyValueCollectionLoader() {
        return new StandardKeyValueCollectionLoader(applicationContext);
    }
}