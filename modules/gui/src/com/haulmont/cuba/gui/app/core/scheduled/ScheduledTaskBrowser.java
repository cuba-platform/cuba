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
import com.haulmont.cuba.core.global.RunTaskOnceException;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.EditAction;
import com.haulmont.cuba.gui.components.actions.ItemTrackingAction;
import com.haulmont.cuba.gui.components.actions.RemoveAction;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class ScheduledTaskBrowser extends AbstractWindow {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTaskBrowser.class);

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
                Set<ScheduledTask> tasks = tasksTable.getSelected();
                service.setActive(tasks, !BooleanUtils.isTrue(tasks.iterator().next().getActive()));
                tasksDs.refresh();
            }
        });

        activateBtn.setEnabled(false);

        ShowExecutionsAction showExecutionsAction = new ShowExecutionsAction();
        tasksTable.addAction(showExecutionsAction);

        ExecuteOnceAction executeOnceAction = new ExecuteOnceAction();
        tasksTable.addAction(executeOnceAction);

        tasksDs.addItemChangeListener(e -> {
            ScheduledTask singleSelected = tasksTable.getSingleSelected();
            Set<ScheduledTask> selected = tasksTable.getSelected();
            boolean isSingleSelected = selected.size() == 1;
            boolean enableEdit = isSingleSelected && !BooleanUtils.isTrue(singleSelected.getActive());

            editAction.setEnabled(enableEdit);
            removeAction.setEnabled(checkAllTasksIsNotActive(selected));
            activateBtn.setEnabled(checkAllTasksHaveSameStatus(selected));

            if (singleSelected == null) {
                activateBtn.setCaption(getMessage("activate"));
            } else {
                activateBtn.setCaption(BooleanUtils.isTrue(singleSelected.getActive()) ?
                        getMessage("deactivate") : getMessage("activate"));
            }

            showExecutionsAction.setEnabled(isSingleSelected);
            executeOnceAction.setEnabled(isSingleSelected && enableEdit);
        });
    }

    protected boolean checkAllTasksHaveSameStatus(Set<ScheduledTask> tasks) {
        if (!tasks.isEmpty()) {
            boolean firstItemState = BooleanUtils.isTrue(tasks.iterator().next().getActive());

            for (ScheduledTask task : tasks) {
                if (BooleanUtils.isTrue(task.getActive()) != firstItemState) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    protected boolean checkAllTasksIsNotActive(Set<ScheduledTask> tasks) {
        if (!tasks.isEmpty()) {
            for (ScheduledTask task : tasks) {
                if (BooleanUtils.isTrue(task.getActive())) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    protected class ShowExecutionsAction extends ItemTrackingAction {
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
        public boolean isApplicable() {
            return tasksTable.getSelected().size() == 1;
        }
    }

    protected class ExecuteOnceAction extends ItemTrackingAction {
        public ExecuteOnceAction() {
            super("executeOnce");
        }

        @Override
        public void actionPerform(Component component) {
            ScheduledTask task = tasksTable.getSingleSelected();
            try {
                if (task != null) {
                    service.runOnce(task);
                }
            } catch (RunTaskOnceException e) {
                log.error("Can't execute {}: not in permitted hosts or not a master.", e.getMessage());
                showNotification(getMessage("errorNotification.caption"),
                                 getMessage("errorNotification.message"),
                                 NotificationType.ERROR);
            }
        }

        @Override
        public boolean isApplicable() {
            return tasksTable.getSelected().size() == 1
                    && !BooleanUtils.isTrue(tasksTable.getSingleSelected().getActive());
        }
    }
}