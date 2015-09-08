/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.data.impl.compatibility;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.gui.data.DsContext;

import java.util.Set;

/**
 * @author artamonov
 * @version $Id$
 */
@Deprecated
public class DsContextCommitListenerWrapper implements DsContext.BeforeCommitListener, DsContext.AfterCommitListener {

    private final DsContext.CommitListener commitListener;

    public DsContextCommitListenerWrapper(DsContext.CommitListener commitListener) {
        this.commitListener = commitListener;
    }

    @Override
    public void afterCommit(CommitContext context, Set<Entity> result) {
        commitListener.afterCommit(context, result);
    }

    @Override
    public void beforeCommit(CommitContext context) {
        commitListener.beforeCommit(context);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        DsContextCommitListenerWrapper that = (DsContextCommitListenerWrapper) obj;

        return this.commitListener.equals(that.commitListener);
    }

    @Override
    public int hashCode() {
        return commitListener.hashCode();
    }
}