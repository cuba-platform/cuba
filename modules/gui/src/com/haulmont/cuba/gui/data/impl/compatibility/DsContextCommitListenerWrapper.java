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

package com.haulmont.cuba.gui.data.impl.compatibility;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.gui.data.DsContext;

import java.util.Set;

/**
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