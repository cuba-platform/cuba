/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.data.impl;

import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.xml.ParameterInfo;

public interface DsContextImplementation extends DsContext {

    void setParent(DsContext parentDsContext);

    void register(Datasource datasource);
    void registerListener(ParameterInfo item, Datasource datasource);

    public interface LazyTask {
        void execute(DsContext context);
    }

    void addLazyTask(LazyTask lazyTask);
    void executeLazyTasks();

    void resumeSuspended();
}
