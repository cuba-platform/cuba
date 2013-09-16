/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.global;

import java.util.*;

public class NotDetachedCommitContext extends CommitContext {

    private static final long serialVersionUID = -1449219610134606045L;

    protected Collection newInstanceIds = new ArrayList();

    public NotDetachedCommitContext() {
    }

    public NotDetachedCommitContext(Collection commitInstances) {
        this.commitInstances.addAll(commitInstances);
    }

    public NotDetachedCommitContext(Collection commitInstances, Collection removeInstances) {
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
