/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.data.impl;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.data.DataSupplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

/**
 * @author abramov
 * @version $Id$
 */
public class GenericDataSupplier implements DataSupplier {

    protected Metadata metadata = AppBeans.get(Metadata.NAME, Metadata.class);

    protected DataManager dataManager = AppBeans.get(DataManager.NAME);

    @SuppressWarnings("unchecked")
    @Override
    public <E extends Entity> E newInstance(MetaClass metaClass) {
        return (E) metadata.create(metaClass);
    }

    @Override
    public <E extends Entity> E reload(E entity, String viewName) {
        return dataManager.reload(entity, viewName);
    }

    @Override
    public <E extends Entity> E reload(E entity, View view) {
        return dataManager.reload(entity, view);
    }

    @Override
    public <E extends Entity> E reload(E entity, View view, @Nullable MetaClass metaClass) {
        return dataManager.reload(entity, view, metaClass);
    }

    @Override
    public <E extends Entity> E reload(E entity, View view, @Nullable MetaClass metaClass, boolean loadDynamicAttributes) {
        return dataManager.reload(entity, view, metaClass, loadDynamicAttributes);
    }

    @Override
    public <E extends Entity> E commit(E instance, @Nullable View view) {
        return dataManager.commit(instance, view);
    }

    @Override
    public <E extends Entity> E commit(E entity, @Nullable String viewName) {
        return dataManager.commit(entity, viewName);
    }

    @Override
    public <E extends Entity> E commit(E instance) {
        return dataManager.commit(instance);
    }

    @Override
    public void remove(Entity entity) {
        dataManager.remove(entity);
    }

    @Override
    public DataManager secure() {
        return dataManager;
    }

    @Override
    public Set<Entity> commit(CommitContext context) {
        return dataManager.commit(context);
    }

    @Override
    @Nullable
    public <E extends Entity> E load(LoadContext<E> context) {
        return dataManager.load(context);
    }

    @Override
    @Nonnull
    public <E extends Entity> List<E> loadList(LoadContext<E> context) {
        return dataManager.loadList(context);
    }

    @Override
    public long getCount(LoadContext<? extends Entity> context) {
        return dataManager.getCount(context);
    }
}