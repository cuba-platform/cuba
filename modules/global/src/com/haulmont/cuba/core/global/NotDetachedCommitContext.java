/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.global;

import com.haulmont.cuba.core.entity.Entity;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class NotDetachedCommitContext extends CommitContext {

    private static final long serialVersionUID = -1449219610134606045L;

    protected Set<String> newInstanceIds = new HashSet<>();

    public NotDetachedCommitContext(Entity... commitInstances) {
        super(commitInstances);
    }

    public NotDetachedCommitContext(Collection commitInstances) {
        super(commitInstances);
    }

    public NotDetachedCommitContext(Collection commitInstances, Collection removeInstances) {
        super(commitInstances, removeInstances);
    }

    public Set<String> getNewInstanceIds() {
        return newInstanceIds;
    }

    public void setNewInstanceIds(Set<String> newInstanceIds) {
        this.newInstanceIds = newInstanceIds;
    }
}
