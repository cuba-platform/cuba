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

import com.haulmont.bali.events.Subscription;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.gui.screen.InstallSubject;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.EventObject;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Interface for tracking changes in entities loaded to the client tier.
 * <p>
 * Within {@code DataContext}, an entity with the given identifier is represented by a single object instance, no matter
 * where and how many times it is used in object graphs.
 */
@InstallSubject("commitDelegate")
public interface DataContext {

    /**
     * Returns an entity instance by its class and id.
     * @return entity instance or null if there is no such entity in the context
     */
    @Nullable
    <T extends Entity<K>, K> T find(Class<T> entityClass, K entityId);

    /**
     * Returns true if the context contains the given entity (distinguished by its class and id).
     */
    boolean contains(Entity entity);

    /**
     * Merge the given entity into the context. The whole object graph with all references will be merged.
     * <p>
     * If an entity with the same identifier already exists in the context, the passed entity state is copied into
     * it and the existing instance is returned. Otherwise, the passed instance is registered in the context and returned.
     * <p>
     * It's very important to continue work with the returned value because it can be a different object instance.
     *
     * @return the instance which is tracked by the context
     */
    <T extends Entity> T merge(T entity);

    /**
     * Merge the given entity into the context.
     * <p>
     * If an entity with the same identifier already exists in the context, the passed entity state is copied into
     * it and the existing instance is returned. Otherwise, the passed instance is registered in the context and returned.
     * <p>
     * It's very important to continue work with the returned value because it can be a different object instance.
     *
     * @param deep if true, the whole object graph with all references will be merged. Otherwise, only the passed entity
     *             is merged.
     * @return the instance which is tracked by the context
     */
    <T extends Entity> T merge(T entity, boolean deep);

    /**
     * Removes the entity from the context and registers it as deleted. The entity will be removed from the data store
     * upon subsequent call to {@link #commit()}.
     * <p>
     * If the given entity is not in the context, nothing happens.
     */
    void remove(Entity entity);

    /**
     * Removes the entity from the context so the context stops tracking it.
     * <p>
     * If the given entity is not in the context, nothing happens.
     */
    void evict(Entity entity);

    /**
     * Returns true if the context has detected changes in the tracked entities.
     */
    boolean hasChanges();

    /**
     * Returns true if the context has detected changes in the given entity.
     */
    boolean isModified(Entity entity);

    /**
     * Returns true if the context has registered removal of the given entity.
     */
    boolean isRemoved(Entity entity);

    /**
     * Commits changed and removed instances to the middleware. After successful commit, the context contains
     * updated instances returned from the middleware.
     *
     * @see #setParent(DataContext)
     */
    void commit();

    /**
     * Returns a parent context, if any. If the parent context is set, {@link #commit()} method merges the changed instances
     * to it instead of sending to the middleware.
     */
    @Nullable
    DataContext getParent();

    /**
     * Sets the parent context. If the parent context is set, {@link #commit()} method merges the changed instances
     * to it instead of sending to the middleware.
     */
    void setParent(DataContext parentContext);

    /**
     * Event sent when the context detects changes in an entity, a new instance is merged or an entity is removed.
     */
    class ChangeEvent extends EventObject {

        private final Entity entity;

        public ChangeEvent(DataContext dataContext, Entity entity) {
            super(dataContext);
            this.entity = entity;
        }

        /**
         * The data context which sent the event.
         */
        @Override
        public DataContext getSource() {
            return (DataContext) super.getSource();
        }

        /**
         * Returns the changed entity.
         */
        public Entity getEntity() {
            return entity;
        }
    }

    /**
     * Adds a listener to {@link ChangeEvent}.
     */
    Subscription addChangeListener(Consumer<ChangeEvent> listener);

    /**
     * Event sent before committing changes.
     */
    class PreCommitEvent extends EventObject {

        private final Collection<Entity> modifiedInstances;
        private final Collection<Entity> removedInstances;
        private boolean commitPrevented;

        public PreCommitEvent(DataContext dataContext, Collection<Entity> modified, Collection<Entity> removed) {
            super(dataContext);
            this.modifiedInstances = modified;
            this.removedInstances = removed;
        }

        /**
         * The data context which sent the event.
         */
        @Override
        public DataContext getSource() {
            return (DataContext) super.getSource();
        }

        /**
         * Returns the collection of modified instances.
         */
        public Collection<Entity> getModifiedInstances() {
            return modifiedInstances;
        }

        /**
         * Returns the collection of removed instances.
         */
        public Collection<Entity> getRemovedInstances() {
            return removedInstances;
        }

        /**
         * Invoke this method if you want to abort the commit.
         */
        public void preventCommit() {
            commitPrevented = true;
        }

        /**
         * Returns true if {@link #preventCommit()} method was called and commit will be aborted.
         */
        public boolean isCommitPrevented() {
            return commitPrevented;
        }
    }

    /**
     * Adds a listener to {@link PreCommitEvent}.
     */
    Subscription addPreCommitListener(Consumer<PreCommitEvent> listener);

    /**
     * Event sent after committing changes.
     */
    class PostCommitEvent extends EventObject {

        private final Collection<Entity> committedInstances;

        public PostCommitEvent(DataContext dataContext, Collection<Entity> committedInstances) {
            super(dataContext);
            this.committedInstances = committedInstances;
        }

        /**
         * The data context which sent the event.
         */
        @Override
        public DataContext getSource() {
            return (DataContext) super.getSource();
        }

        /**
         * Returns the collection of committed entities.
         */
        public Collection<Entity> getCommittedInstances() {
            return committedInstances;
        }
    }

    /**
     * Adds a listener to {@link PostCommitEvent}.
     */
    Subscription addPostCommitListener(Consumer<PostCommitEvent> listener);

    /**
     * Returns a function which will be used to commit data instead of standard implementation.
     */
    Function<CommitContext, Set<Entity>> getCommitDelegate();

    /**
     * Sets a function which will be used to commit data instead of standard implementation.
     */
    void setCommitDelegate(Function<CommitContext, Set<Entity>> delegate);

}
