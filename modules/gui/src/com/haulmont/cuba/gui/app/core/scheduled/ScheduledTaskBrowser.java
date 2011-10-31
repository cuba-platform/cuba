/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.gui.app.core.scheduled;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.ScheduledExecution;
import com.haulmont.cuba.core.entity.ScheduledTask;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.AbstractAction;
import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.impl.CollectionDsListenerAdapter;

import javax.inject.Inject;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class ScheduledTaskBrowser extends AbstractWindow {

    @Inject
    protected Table tasksTable;

    protected ShowExecutionsAction showExecutionsAction;

    @Override
    public void init(Map<String, Object> params) {
        ComponentsHelper.createActions(tasksTable);

        showExecutionsAction = new ShowExecutionsAction();
        showExecutionsAction.setEnabled(false);
        tasksTable.addAction(showExecutionsAction);

        tasksTable.getDatasource().addListener(new CollectionDsListenerAdapter() {
            @Override
            public void itemChanged(Datasource ds, Entity prevItem, Entity item) {
                showExecutionsAction.setEnabled(tasksTable.getSingleSelected() != null);
            }
        });
    }

    protected class ShowExecutionsAction extends AbstractAction {
        public ShowExecutionsAction() {
            super("executions");
        }

        @Override
        public void actionPerform(Component component) {
            ScheduledTask task = tasksTable.getSingleSelected();
            if (task != null) {
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("task", task);
                openWindow("core$ScheduledExecution.browse", WindowManager.OpenType.THIS_TAB, params);
            }
        }
    }
}
