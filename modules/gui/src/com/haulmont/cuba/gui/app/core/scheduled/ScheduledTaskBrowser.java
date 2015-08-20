/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.app.core.scheduled;

import com.haulmont.cuba.core.app.SchedulingService;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.ScheduledTask;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.EditAction;
import com.haulmont.cuba.gui.components.actions.RemoveAction;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.impl.CollectionDsListenerAdapter;
import org.apache.commons.lang.BooleanUtils;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * @author krivopustov
 * @version $Id$
 */
public class ScheduledTaskBrowser extends AbstractWindow {

    @Inject
    protected CollectionDatasource tasksDs;

    @Inject
    protected Table<ScheduledTask> tasksTable;

    @Inject
    protected Button activateBtn;

    @Inject
    protected SchedulingService service;

    @Override
    public void init(Map<String, Object> params) {
        ComponentsHelper.createActions(tasksTable);

        final Action editAction = tasksTable.getAction(EditAction.ACTION_ID);
        editAction.setEnabled(false);

        final Action removeAction = tasksTable.getAction(RemoveAction.ACTION_ID);
        removeAction.setEnabled(false);

        activateBtn.setAction(new AbstractAction("activate") {
            @Override
            public void actionPerform(Component component) {
                ScheduledTask task = tasksTable.getSingleSelected();
                if (task != null) {
                    service.setActive(task, !BooleanUtils.isTrue(task.getActive()));
                    tasksDs.refresh();
                }
            }
        });
        activateBtn.setEnabled(false);

        final ShowExecutionsAction showExecutionsAction = new ShowExecutionsAction();
        showExecutionsAction.setEnabled(false);
        tasksTable.addAction(showExecutionsAction);

        tasksDs.addListener(new CollectionDsListenerAdapter() {
            @Override
            public void itemChanged(Datasource ds, Entity prevItem, Entity item) {
                ScheduledTask selected = tasksTable.getSingleSelected();

                boolean enableEdit = selected != null && !BooleanUtils.isTrue(selected.getActive());
                editAction.setEnabled(enableEdit);
                removeAction.setEnabled(enableEdit);

                activateBtn.setEnabled(selected != null);
                if (selected == null)
                    activateBtn.setCaption(getMessage("activate"));
                else
                    activateBtn.setCaption(BooleanUtils.isTrue(selected.getActive()) ?
                            getMessage("deactivate") : getMessage("activate"));

                showExecutionsAction.setEnabled(selected != null);
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
                openWindow("sys$ScheduledExecution.browse", WindowManager.OpenType.THIS_TAB, params);
            }
        }

        @Override
        public boolean isEnabled() {
            return tasksTable.getSingleSelected() != null;
        }
    }
}
