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
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.data.DataSupplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Override
    public DataManager secure() {
        return this;
    }
}