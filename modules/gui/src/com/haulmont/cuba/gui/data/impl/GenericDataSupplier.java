/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.data.impl;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.data.DataSupplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * @author abramov
 * @version $Id$
 */
public class GenericDataSupplier implements DataSupplier {

    protected Metadata metadata = AppBeans.get(Metadata.NAME, Metadata.class);

    protected DataService dataService = AppBeans.get(DataService.NAME, DataService.class);

    @SuppressWarnings("unchecked")
    @Override
    public <A extends Entity> A newInstance(MetaClass metaClass) {
        return (A) metadata.create(metaClass);
    }

    @Override
    public <A extends Entity> A reload(A entity, String viewName) {
        Objects.requireNonNull(viewName, "viewName is null");

        return reload(entity, metadata.getViewRepository().getView(entity.getClass(), viewName));
    }

    @Override
    public <A extends Entity> A reload(A entity, View view) {
        return reload(entity, view, null);
    }

    @Override
    public <A extends Entity> A reload(A entity, View view, @Nullable MetaClass metaClass) {
        return reload(entity, view, metaClass, true);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <A extends Entity> A reload(A entity, View view, @Nullable MetaClass metaClass, boolean useSecurityConstraints) {
        if (metaClass == null) {
            metaClass = metadata.getSession().getClass(entity.getClass());
        }
        final LoadContext context = new LoadContext(metaClass);
        context.setUseSecurityConstraints(useSecurityConstraints);
        context.setId(entity.getId());
        context.setView(view);

        return (A) load(context);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <A extends Entity> A commit(A instance, @Nullable View view) {
        final CommitContext context = new CommitContext(
                        Collections.singleton((Entity) instance),
                        Collections.<Entity>emptyList());
        if (view != null)
            context.getViews().put(instance, view);

        final Set<Entity> res = commit(context);

        for (Entity entity : res) {
            if (entity.equals(instance))
                return (A) entity;
        }
        return null;
    }

    @Override
    public <A extends Entity> A commit(A instance) {
        return commit(instance, null);
    }

    @Override
    public void remove(Entity entity) {
        final CommitContext context = new CommitContext(
                        Collections.<Entity>emptyList(),
                        Collections.singleton(entity));
        commit(context);
    }

    @Override
    public DbDialect getDbDialect() {
        return dataService.getDbDialect();
    }

    @Override
    public Set<Entity> commit(CommitContext context) {
        return dataService.commit(context);
    }

    @Override
    public Map<Entity, Entity> commitNotDetached(NotDetachedCommitContext context) {
        return dataService.commitNotDetached(context);
    }

    @Override
    @Nullable
    public <A extends Entity> A load(LoadContext context) {
        return dataService.load(context);
    }

    @Override
    @Nonnull
    public <A extends Entity> List<A> loadList(LoadContext context) {
        return dataService.loadList(context);
    }
}