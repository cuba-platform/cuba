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
    public <A extends Entity> A newInstance(MetaClass metaClass) {
        return (A) metadata.create(metaClass);
    }

    @Override
    public <A extends Entity> A reload(A entity, String viewName) {
        return dataManager.reload(entity, viewName);
    }

    @Override
    public <A extends Entity> A reload(A entity, View view) {
        return dataManager.reload(entity, view);
    }

    @Override
    public <A extends Entity> A reload(A entity, View view, @Nullable MetaClass metaClass) {
        return dataManager.reload(entity, view, metaClass);
    }

    @Override
    public <A extends Entity> A reload(A entity, View view, @Nullable MetaClass metaClass, boolean useSecurityConstraints) {
        return dataManager.reload(entity, view, metaClass, useSecurityConstraints);
    }

    @Override
    public <A extends Entity> A commit(A instance, @Nullable View view) {
        return dataManager.commit(instance, view);
    }

    @Override
    public <A extends Entity> A commit(A instance) {
        return dataManager.commit(instance);
    }

    @Override
    public void remove(Entity entity) {
        dataManager.remove(entity);
    }

    @Override
    public Set<Entity> commit(CommitContext context) {
        return dataManager.commit(context);
    }

    @Override
    @Nullable
    public <A extends Entity> A load(LoadContext context) {
        return dataManager.load(context);
    }

    @Override
    @Nonnull
    public <A extends Entity> List<A> loadList(LoadContext context) {
        return dataManager.loadList(context);
    }
}