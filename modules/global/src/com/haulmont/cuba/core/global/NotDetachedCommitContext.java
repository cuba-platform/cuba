/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.core.global;

import java.util.*;

public class NotDetachedCommitContext<Entity> extends CommitContext<Entity> {
    protected Collection newInstanceIds = new ArrayList();

    public NotDetachedCommitContext() {
    }

    public NotDetachedCommitContext(Collection<Entity> commitInstances) {
        this.commitInstances.addAll(commitInstances);
    }

    public NotDetachedCommitContext(Collection<Entity> commitInstances, Collection<Entity> removeInstances) {
        this.commitInstances.addAll(commitInstances);
        this.removeInstances.addAll(removeInstances);
    }

    public Collection getNewInstanceIds() {
        return newInstanceIds;
    }

    public void setNewInstanceIds(Collection newInstanceIds) {
        this.newInstanceIds = newInstanceIds;
    }
}
