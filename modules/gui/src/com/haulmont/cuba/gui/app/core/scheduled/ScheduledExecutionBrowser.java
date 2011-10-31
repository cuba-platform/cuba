/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.app.core.scheduled;

import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.actions.RefreshAction;
import com.haulmont.cuba.gui.data.CollectionDatasource;

import javax.inject.Inject;
import java.util.Map;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class ScheduledExecutionBrowser extends AbstractWindow {

    @Inject
    protected Table executionsTable;

    @Override
    public void init(Map<String, Object> params) {
        executionsTable.addAction(new RefreshAction(executionsTable));
    }
}
