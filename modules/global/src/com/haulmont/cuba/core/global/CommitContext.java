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
package com.haulmont.cuba.core.global;

import com.haulmont.cuba.core.entity.Entity;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.*;

/**
 * DTO that contains information about currently committed entities.
 * <br> Used by {@link com.haulmont.cuba.core.app.DataService}
 */
@SuppressWarnings("unchecked")
public class CommitContext implements Serializable {

    private static final long serialVersionUID = 2510011302544968537L;

    protected Collection<Entity> commitInstances = new LinkedHashSet<>();
    protected Collection<Entity> removeInstances = new LinkedHashSet<>();

    protected Map<Object, View> views = new HashMap<>();

    protected boolean softDeletion = true;
    protected boolean discardCommitted;
    protected boolean authorizationRequired;
    protected boolean joinTransaction;
    protected Map<String, Object> dbHints = new HashMap<>();

    /**
     * @param commitInstances changed entities to be committed to the database
     */
    public CommitContext(Entity... commitInstances) {
        this.commitInstances.addAll(Arrays.asList(commitInstances));
    }

    /**
     * @param commitInstances collection of changed entities to be committed to the database
     */
    public CommitContext(Collection commitInstances) {
        this.commitInstances.addAll(commitInstances);
    }

    /**
     * @param commitInstances collection of changed entities to be committed to the database
     * @param removeInstances collection of entities to be removed from the database
     */
    public CommitContext(Collection commitInstances, Collection removeInstances) {
        this.commitInstances.addAll(commitInstances);
        this.removeInstances.addAll(removeInstances);
    }

    /**
     * Adds an entity to be committed to the database.
     *
     * @param entity entity instance
     * @return this instance for chaining
     */
    public CommitContext addInstanceToCommit(Entity entity) {
        commitInstances.add(entity);
        return this;
    }

    /**
     * Adds an entity to be committed to the database.
     *
     * @param entity entity instance
     * @param view   view which is used in merge operation to ensure all required attributes are loaded in the returned instance
     * @return this instance for chaining
     */
    public CommitContext addInstanceToCommit(Entity entity, @Nullable View view) {
        commitInstances.add(entity);
        if (view != null)
            views.put(entity, view);
        return this;
    }

    /**
     * Adds an entity to be committed to the database.
     *
     * @param entity   entity instance
     * @param viewName view which is used in merge operation to ensure all required attributes are loaded in the returned instance
     * @return this instance for chaining
     */
    public CommitContext addInstanceToCommit(Entity entity, @Nullable String viewName) {
        commitInstances.add(entity);
        if (viewName != null) {
            views.put(entity, getViewFromRepository(entity, viewName));
        }
        return this;
    }

    /**
     * Adds an entity to be removed from the database.
     *
     * @param entity entity instance
     * @return this instance for chaining
     */
    public CommitContext addInstanceToRemove(Entity entity) {
        removeInstances.add(entity);
        return this;
    }

    /**
     * @return direct reference to collection of changed entities that will be committed to the database.
     * The collection is modifiable.
     */
    public Collection<Entity> getCommitInstances() {
        return commitInstances;
    }

    /**
     * @param commitInstances collection of changed entities that will be committed to the database
     */
    public void setCommitInstances(Collection commitInstances) {
        this.commitInstances = commitInstances;
    }

    /**
     * @return direct reference to collection of entities that will be removed from the database.
     * The collection is modifiable.
     */
    public Collection<Entity> getRemoveInstances() {
        return removeInstances;
    }

    /**
     * @param removeInstances collection of entities to be removed from the database
     */
    public void setRemoveInstances(Collection removeInstances) {
        this.removeInstances = removeInstances;
    }

    /**
     * Enables defining a view for each committed entity. These views are used in merge operation to ensure all
     * required attributes are loaded in returned instances.
     *
     * @return editable map of entities to their views
     */
    public Map<Object, View> getViews() {
        return views;
    }

    /**
     * @return custom hints which can be used later during query construction
     */
    public Map<String, Object> getDbHints() {
        return dbHints;
    }

    /**
     * @return whether to use soft deletion for this commit
     */
    public boolean isSoftDeletion() {
        return softDeletion;
    }

    /**
     * @param softDeletion  whether to use soft deletion for this commit
     */
    public void setSoftDeletion(boolean softDeletion) {
        this.softDeletion = softDeletion;
    }

    /**
     * @return true if calling code does not need committed instances, which allows for performance optimization
     */
    public boolean isDiscardCommitted() {
        return discardCommitted;
    }

    /**
     * Set to true if calling code does not need committed instances, which allows for performance optimization.
     */
    public void setDiscardCommitted(boolean discardCommitted) {
        this.discardCommitted = discardCommitted;
    }

    public boolean isAuthorizationRequired() {
        return authorizationRequired;
    }

    public CommitContext setAuthorizationRequired(boolean authorizationRequired) {
        this.authorizationRequired = authorizationRequired;
        return this;
    }

    public boolean isJoinTransaction() {
        return joinTransaction;
    }

    public CommitContext setJoinTransaction(boolean joinTransaction) {
        this.joinTransaction = joinTransaction;
        return this;
    }

    private View getViewFromRepository(Entity entity, String viewName) {
        Metadata metadata = AppBeans.get(Metadata.NAME);
        return metadata.getViewRepository().getView(metadata.getClass(entity.getClass()), viewName);
    }
}