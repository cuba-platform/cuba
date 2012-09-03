/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 12.02.2009 12:06:15
 * $Id$
 */
package com.haulmont.cuba.gui.data.impl;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.gui.data.DataService;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.*;

public class GenericDataService implements DataService, Serializable {

    private static final long serialVersionUID = -2688273748125419411L;

    private Metadata metadata = AppBeans.get(Metadata.NAME, Metadata.class);

    private com.haulmont.cuba.core.app.DataService dataService = AppBeans.get(
            com.haulmont.cuba.core.app.DataService.NAME, com.haulmont.cuba.core.app.DataService.class);

    public <A extends Entity> A newInstance(MetaClass metaClass) {
        return (A) metadata.create(metaClass);
    }

    public <A extends Entity> A reload(A entity, String viewName) {
        Objects.requireNonNull(viewName, "viewName is null");

        return reload(entity, metadata.getViewRepository().getView(entity.getClass(), viewName));
    }

    public <A extends Entity> A reload(A entity, View view) {
        return reload(entity, view, null);
    }

    public <A extends Entity> A reload(A entity, View view, @Nullable MetaClass metaClass) {
        return reload(entity, view, metaClass, true);
    }

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

    public void remove(Entity entity) {
        final CommitContext context = new CommitContext(
                        Collections.<Entity>emptyList(),
                        Collections.singleton(entity));
        commit(context);
    }

    public DbDialect getDbDialect() {
        return dataService.getDbDialect();
    }

    public Set<Entity> commit(CommitContext context) {
        return dataService.commit(context);
    }

    public Map<Entity, Entity> commitNotDetached(NotDetachedCommitContext context) {
        return dataService.commitNotDetached(context);
    }

    @Nullable
    public <A extends Entity> A load(LoadContext context) {
        return dataService.<A>load(context);
    }

    public <A extends Entity> List<A> loadList(LoadContext context) {
        return dataService.<A>loadList(context);
    }
}
