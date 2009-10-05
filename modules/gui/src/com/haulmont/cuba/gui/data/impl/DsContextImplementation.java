/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 19.03.2009 18:38:32
 * $Id$
 */
package com.haulmont.cuba.gui.data.impl;

import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.xml.ParameterInfo;

public interface DsContextImplementation extends DsContext{

    void setParent(DsContext parentDsContext);

    void register(Datasource datasource);
    void registerListener(ParameterInfo item, Datasource datasource);

    public interface LazyTask {
        void execute(DsContext context);
    }

    void addLazyTask(LazyTask lazyTask);
    void executeLazyTasks();
}
