/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.app.core.scheduled;

import com.haulmont.cuba.core.app.PersistenceManagerService;
import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.actions.RefreshAction;
import com.haulmont.cuba.gui.data.CollectionDatasource;

import javax.inject.Inject;
import java.util.Map;

/**
 * @author krivopustov
 * @version $Id$
 */
public class ScheduledExecutionBrowser extends AbstractWindow {

    @Inject
    protected Table executionsTable;

    @Inject
    protected CollectionDatasource executionsDs;

    @Inject
    protected PersistenceManagerService persistenceManager;

    @Override
    public void init(Map<String, Object> params) {
        executionsTable.addAction(new RefreshAction(executionsTable));

        int maxResults = persistenceManager.getFetchUI(executionsDs.getMetaClass().getName());
        executionsDs.setMaxResults(maxResults);
    }
}
