/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.data.impl;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.CommitContext;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.core.global.filter.ParameterInfo;

import java.util.Set;

/**
 * @author krivopustov
 * @version $Id$
 */
public interface DsContextImplementation extends DsContext {

    void setParent(DsContext parentDsContext);

    void register(Datasource datasource);
    void unregister(Datasource datasource);
    void registerListener(ParameterInfo item, Datasource datasource);

    interface LazyTask {
        void execute(DsContext context);
    }

    void addLazyTask(LazyTask lazyTask);
    void executeLazyTasks();

    void resumeSuspended();

    void fireBeforeCommit(CommitContext context);
    void fireAfterCommit(CommitContext context, Set<Entity> committedEntities);
}