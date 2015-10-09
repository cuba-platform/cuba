/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.client.sys;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.app.DataService;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;

import org.springframework.stereotype.Component;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author krivopustov
 * @version $Id$
 */
@Component(DataManager.NAME)
public class DataManagerClientImpl implements DataManager {

    @Inject
    protected DataService dataService;

    @Inject
    protected Metadata metadata;

    @Nullable
    @Override
    public <E extends Entity> E load(LoadContext<E> context) {
        return dataService.load(context);
    }

    @Nonnull
    @Override
    public <E extends Entity> List<E> loadList(LoadContext<E> context) {
        return dataService.loadList(context);
    }

    @Override
    public long getCount(LoadContext<? extends Entity> context) {
        return dataService.getCount(context);
    }

    @Override
    public <E extends Entity> E reload(E entity, String viewName) {
        Objects.requireNonNull(viewName, "viewName is null");
        return reload(entity, metadata.getViewRepository().getView(entity.getClass(), viewName));
    }

    @Override
    public <E extends Entity> E reload(E entity, View view) {
        return reload(entity, view, null);
    }

    @Override
    public <E extends Entity> E reload(E entity, View view, @Nullable MetaClass metaClass) {
        return reload(entity, view, metaClass, true);
    }

    @Override
    public <E extends Entity> E reload(E entity, View view, @Nullable MetaClass metaClass, boolean loadDynamicAttributes) {
        if (metaClass == null) {
            metaClass = metadata.getSession().getClass(entity.getClass());
        }
        LoadContext<E> context = new LoadContext<>(metaClass);
        context.setId(entity.getId());
        context.setView(view);
        context.setLoadDynamicAttributes(loadDynamicAttributes);

        E reloaded = load(context);
        if (reloaded == null)
            throw new EntityAccessException();

        return reloaded;
    }

    @Override
    public Set<Entity> commit(CommitContext context) {
        return dataService.commit(context);
    }

    @Override
    public <E extends Entity> E commit(E entity, @Nullable View view) {
        Set<Entity> res = commit(new CommitContext().addInstanceToCommit(entity, view));

        for (Entity e : res) {
            if (e.equals(entity)) {
                //noinspection unchecked
                return (E) e;
            }
        }
        return null;
    }

    @Override
    public <E extends Entity> E commit(E entity, @Nullable String viewName) {
        if (viewName != null) {
            View view = metadata.getViewRepository().getView(metadata.getClassNN(entity.getClass()), viewName);
            return commit(entity, view);
        } else {
            return commit(entity, (View) null);
        }
    }

    @Override
    public <E extends Entity> E commit(E entity) {
        return commit(entity, (View) null);
    }

    @Override
    public void remove(Entity entity) {
        CommitContext context = new CommitContext(
                        Collections.<Entity>emptyList(),
                        Collections.singleton(entity));
        commit(context);
    }
}
