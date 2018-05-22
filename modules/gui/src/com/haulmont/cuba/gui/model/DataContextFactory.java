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

import javax.inject.Inject;

/**
 *
 */
@Component("cuba_DataContextFactory")
public class DataContextFactory implements ApplicationContextAware {

    @Inject
    protected Metadata metadata;

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public DataContext createDataContext() {
        return new StandardDataContext(applicationContext);
    }

    public <T extends Entity> InstanceContainer<T> createInstanceContainer(Class<T> entityClass) {
        return new InstanceContainerImpl<>(metadata.getClassNN(entityClass));
    }

    public <T extends Entity> CollectionContainer<T> createCollectionContainer(Class<T> entityClass) {
        return new CollectionContainerImpl<>(metadata.getClassNN(entityClass));
    }

    public <T extends Entity<K>, K> InstanceLoader<T, K> createInstanceLoader() {
        return new StandardInstanceLoader<>(applicationContext);
    }

    public <T extends Entity> CollectionLoader<T> createCollectionLoader() {
        return new StandardCollectionLoader<>(applicationContext);
    }
}
