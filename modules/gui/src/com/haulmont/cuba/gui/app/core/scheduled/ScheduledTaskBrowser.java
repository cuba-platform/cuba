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

package com.haulmont.cuba.gui.app.core.scheduled;

import com.haulmont.cuba.core.app.SchedulingService;
import com.haulmont.cuba.core.entity.ScheduledTask;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.EditAction;
import com.haulmont.cuba.gui.components.actions.RemoveAction;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import org.apache.commons.lang.BooleanUtils;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ScheduledTaskBrowser extends AbstractWindow {

    @Inject
    protected CollectionDatasource<ScheduledTask, UUID> tasksDs;

    @Inject
    protected Table<ScheduledTask> tasksTable;

    @Inject
    protected Button activateBtn;

    @Inject
    protected SchedulingService service;

    @Override
    public void init(Map<String, Object> params) {
        ComponentsHelper.createActions(tasksTable);

        final Action editAction = tasksTable.getActionNN(EditAction.ACTION_ID);
        editAction.setEnabled(false);

        final Action removeAction = tasksTable.getActionNN(RemoveAction.ACTION_ID);
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

        tasksDs.addItemChangeListener(e -> {
            ScheduledTask selected = tasksTable.getSingleSelected();

            boolean enableEdit = selected != null && !BooleanUtils.isTrue(selected.getActive());
            editAction.setEnabled(enableEdit);
            removeAction.setEnabled(enableEdit);

            activateBtn.setEnabled(selected != null);
            if (selected == null) {
                activateBtn.setCaption(getMessage("activate"));
            } else {
                activateBtn.setCaption(BooleanUtils.isTrue(selected.getActive()) ?
                        getMessage("deactivate") : getMessage("activate"));
            }

            showExecutionsAction.setEnabled(selected != null);
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
                Map<String, Object> params = new HashMap<>();
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