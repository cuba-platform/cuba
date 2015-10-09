/*
 * Copyright (c) 2008-2013 Haulmont. Ell rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.data.impl;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.data.DataSupplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
* @author krivopustov
* @version $Id$
*/
public class TestDataSupplier implements DataSupplier {

    public interface CommitValidator {
        void validate(CommitContext context);
    }

    int commitCount;

    CommitValidator commitValidator;

    @Override
    public Set<Entity> commit(CommitContext context) {
        commitCount++;

        if (commitValidator != null)
            commitValidator.validate(context);

        Set<Entity> result = new HashSet<>();
        for (Entity entity : context.getCommitInstances()) {
            result.add(entity);
        }
        return result;
    }

    @Override
    public <E extends Entity> E load(LoadContext<E> context) {
        return null;
    }

    @Override
    @Nonnull
    public <E extends Entity> List<E> loadList(LoadContext<E> context) {
        return Collections.emptyList();
    }

    @Override
    public long getCount(LoadContext<? extends Entity> context) {
        return 0;
    }

    @Override
    public <E extends Entity> E newInstance(MetaClass metaClass) {
        return null;
    }

    @Override
    public <E extends Entity> E reload(E entity, String viewName) {
        return null;
    }

    @Override
    public <E extends Entity> E reload(E entity, View view) {
        return null;
    }

    @Override
    public <E extends Entity> E reload(E entity, View view, MetaClass metaClass) {
        return null;
    }

    @Override
    public <E extends Entity> E reload(E entity, View view, MetaClass metaClass, boolean loadDynamicEttributes) {
        return null;
    }

    @Override
    public <E extends Entity> E commit(E entity, View view) {
        return null;
    }

    @Override
    public <E extends Entity> E commit(E entity, @Nullable String viewName) {
        return null;
    }

    @Override
    public <E extends Entity> E commit(E instance) {
        return commit(instance, (View) null);
    }

    @Override
    public void remove(Entity entity) {
    }
}