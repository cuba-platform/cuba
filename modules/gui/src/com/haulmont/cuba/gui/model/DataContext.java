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

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.EventObject;

/**
 *
 */
public interface DataContext {

    @Nullable
    <T extends Entity<K>, K> T find(Class<T> entityClass, K entityId);

    boolean contains(@Nullable Entity entity);

    <T extends Entity> T merge(T entity);

    <T extends Entity> T merge(T entity, boolean deep);

    void remove(Entity entity);

    void evict(Entity entity);

    boolean hasChanges();

    void commit();

    DataContext getParent();
    void setParent(DataContext parentContext);

    class PreCommitEvent extends EventObject {

        private final Collection<Entity> modifiedInstances;
        private final Collection<Entity> removedInstances;
        private boolean commitPrevented;

        public PreCommitEvent(DataContext dataContext, Collection<Entity> modified, Collection<Entity> removed) {
            super(dataContext);
            this.modifiedInstances = modified;
            this.removedInstances = removed;
        }

        @Override
        public DataContext getSource() {
            return (DataContext) super.getSource();
        }

        public Collection<Entity> getModifiedInstances() {
            return modifiedInstances;
        }

        public Collection<Entity> getRemovedInstances() {
            return removedInstances;
        }

        public void preventCommit() {
            commitPrevented = true;
        }

        public boolean isCommitPrevented() {
            return commitPrevented;
        }
    }

    @FunctionalInterface
    interface PreCommitListener {
        void preCommit(PreCommitEvent e);
    }

    void addPreCommitListener(PreCommitListener listener);
    void removePreCommitListener(PreCommitListener listener);

    class PostCommitEvent extends EventObject {

        private final Collection<Entity> committedInstances;

        public PostCommitEvent(DataContext dataContext, Collection<Entity> committedInstances) {
            super(dataContext);
            this.committedInstances = committedInstances;
        }

        @Override
        public DataContext getSource() {
            return (DataContext) super.getSource();
        }

        public Collection<Entity> getCommittedInstances() {
            return committedInstances;
        }
    }

    @FunctionalInterface
    interface PostCommitListener {
        void postCommit(PostCommitEvent e);
    }

    void addPostCommitListener(PostCommitListener listener);
    void removePostCommitListener(PostCommitListener listener);
}
