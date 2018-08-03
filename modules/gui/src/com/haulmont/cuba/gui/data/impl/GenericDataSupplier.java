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
 *
 */
package com.haulmont.cuba.gui.data.impl;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.KeyValueEntity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.data.DataSupplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class GenericDataSupplier implements DataSupplier {

    @SuppressWarnings("unchecked")
    @Override
    public <E extends Entity> E newInstance(MetaClass metaClass) {
        return (E) getMetadata().create(metaClass);
    }

    @Override
    public <E extends Entity> E reload(E entity, String viewName) {
        return getDataManager().reload(entity, viewName);
    }

    @Override
    public <E extends Entity> E reload(E entity, View view) {
        return getDataManager().reload(entity, view);
    }

    @Override
    public <E extends Entity> E reload(E entity, View view, @Nullable MetaClass metaClass) {
        return getDataManager().reload(entity, view, metaClass);
    }

    @Override
    public <E extends Entity> E reload(E entity, View view, @Nullable MetaClass metaClass, boolean loadDynamicAttributes) {
        return getDataManager().reload(entity, view, metaClass, loadDynamicAttributes);
    }

    @Override
    public <E extends Entity> E commit(E instance, @Nullable View view) {
        return getDataManager().commit(instance, view);
    }

    @Override
    public <E extends Entity> E commit(E entity, @Nullable String viewName) {
        return getDataManager().commit(entity, viewName);
    }

    @Override
    public <E extends Entity> E commit(E instance) {
        return getDataManager().commit(instance);
    }

    @Override
    public EntitySet commit(Entity... entities) {
        return getDataManager().commit(entities);
    }

    @Override
    public void remove(Entity entity) {
        getDataManager().remove(entity);
    }

    @Override
    public List<KeyValueEntity> loadValues(ValueLoadContext context) {
        return getDataManager().loadValues(context);
    }

    @Override
    public DataManager secure() {
        return getDataManager();
    }

    @Override
    public EntitySet commit(CommitContext context) {
        return getDataManager().commit(context);
    }

    @Override
    @Nullable
    public <E extends Entity> E load(LoadContext<E> context) {
        return getDataManager().load(context);
    }

    @Override
    @Nonnull
    public <E extends Entity> List<E> loadList(LoadContext<E> context) {
        return getDataManager().loadList(context);
    }

    @Override
    public long getCount(LoadContext<? extends Entity> context) {
        return getDataManager().getCount(context);
    }

    protected Metadata getMetadata() {
        return AppBeans.get(Metadata.NAME);
    }

    protected DataManager getDataManager() {
        return AppBeans.get(DataManager.NAME);
    }
}