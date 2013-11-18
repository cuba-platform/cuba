/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.global;

import com.haulmont.cuba.core.entity.Entity;

import java.io.Serializable;
import java.util.*;

/**
 * DTO that contains information about currently committed entities.
 * <p/> Used by {@link com.haulmont.cuba.core.app.DataService}
 *
 * @author krivopustov
 * @version $Id$
 */
public class CommitContext implements Serializable {

    private static final long serialVersionUID = 2510011302544968537L;

    protected Collection commitInstances = new HashSet();
    protected Collection removeInstances = new HashSet();

    protected Map<Object, View> views = new HashMap<>();

    protected boolean softDeletion = true;

    public CommitContext() {
    }

    @SafeVarargs
    public <T extends Entity> CommitContext(T... commitInstances) {
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
     * @return direct reference to collection of changed entities that will be committed to the database.
     * The collection is modifiable.
     */
    public <E extends Entity> Collection<E> getCommitInstances() {
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
    public <E extends Entity> Collection<E> getRemoveInstances() {
        return removeInstances;
    }

    /**
     * @param removeInstances collection of entities to be removed from the database
     */
    public void setRemoveInstances(Collection removeInstances) {
        this.removeInstances = removeInstances;
    }

    /**
     * Allows to define a view for each committed entity. These views are used after merging changes to fetch merged
     * entities before returning them to the caller.
     * @return editable map of entities to their views
     */
    public Map<Object, View> getViews() {
        return views;
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
}
