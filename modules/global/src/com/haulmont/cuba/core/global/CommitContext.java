/*
 * Copyright (c) 2009 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 22.09.2009 11:20:38
 *
 * $Id$
 */
package com.haulmont.cuba.core.global;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

public class CommitContext<Entity> implements Serializable {

    private static final long serialVersionUID = 2510011302544968537L;

    protected Collection<Entity> commitInstances = new HashSet<Entity>();
    protected Collection<Entity> removeInstances = new HashSet<Entity>();

    protected Map<Entity, View> views = new HashMap<Entity, View>();

    protected boolean softDeletion = true;

    public CommitContext() {
    }

    public CommitContext(Collection<Entity> commitInstances) {
        this.commitInstances.addAll(commitInstances);
    }

    public CommitContext(Collection<Entity> commitInstances, Collection<Entity> removeInstances) {
        this.commitInstances.addAll(commitInstances);
        this.removeInstances.addAll(removeInstances);
    }

    public Collection<Entity> getCommitInstances() {
        return commitInstances;
    }

    public void setCommitInstances(Collection<Entity> commitInstances) {
        this.commitInstances = commitInstances;
    }

    public Collection<Entity> getRemoveInstances() {
        return removeInstances;
    }

    public void setRemoveInstances(Collection<Entity> removeInstances) {
        this.removeInstances = removeInstances;
    }

    public Map<Entity, View> getViews() {
        return views;
    }

    public boolean isSoftDeletion() {
        return softDeletion;
    }

    public void setSoftDeletion(boolean softDeletion) {
        this.softDeletion = softDeletion;
    }
}
