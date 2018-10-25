/*
 * Copyright (c) 2008-2018 Haulmont.
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

package com.haulmont.cuba.gui.model.impl;

import com.haulmont.bali.events.Subscription;
import com.haulmont.bali.events.sys.VoidSubscription;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.gui.model.DataContext;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Dummy implementation of {@link DataContext} used for read-only screens like entity browsers.
 */
public class NoopDataContext implements DataContext {

    @Nullable
    @Override
    public <T extends Entity<K>, K> T find(Class<T> entityClass, K entityId) {
        return null;
    }

    @Override
    public boolean contains(Entity entity) {
        return false;
    }

    @Override
    public <T extends Entity> T merge(T entity) {
        return entity;
    }

    @Override
    public <T extends Entity> T merge(T entity, boolean deep) {
        return entity;
    }

    @Override
    public void remove(Entity entity) {
    }

    @Override
    public void evict(Entity entity) {
    }

    @Override
    public boolean hasChanges() {
        return false;
    }

    @Override
    public boolean isModified(Entity entity) {
        return false;
    }

    @Override
    public boolean isRemoved(Entity entity) {
        return false;
    }

    @Override
    public void commit() {
    }

    @Override
    public DataContext getParent() {
        return null;
    }

    @Override
    public void setParent(DataContext parentContext) {
    }

    @Override
    public Subscription addChangeListener(Consumer<ChangeEvent> listener) {
        return VoidSubscription.INSTANCE;
    }

    @Override
    public Subscription addPreCommitListener(Consumer<PreCommitEvent> listener) {
        return VoidSubscription.INSTANCE;
    }

    @Override
    public Subscription addPostCommitListener(Consumer<PostCommitEvent> listener) {
        return VoidSubscription.INSTANCE;
    }

    @Override
    public Function<CommitContext, Set<Entity>> getCommitDelegate() {
        return null;
    }

    @Override
    public void setCommitDelegate(Function<CommitContext, Set<Entity>> delegate) {
    }
}
