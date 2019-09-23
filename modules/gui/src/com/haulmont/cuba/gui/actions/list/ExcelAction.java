/*
 * Copyright (c) 2008-2018 Haulmont.
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
 */

package com.haulmont.cuba.gui.actions.list;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.BeanLocator;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.Notifications.NotificationType;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.ListAction;
import com.haulmont.cuba.gui.components.data.meta.ContainerDataUnit;
import com.haulmont.cuba.gui.export.ExcelExporter;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.icons.CubaIcon;
import com.haulmont.cuba.gui.icons.Icons;
import com.haulmont.cuba.gui.model.CollectionContainer;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Standard action for exporting a list of entities to XLS file.
 * <p>
 * Should be defined for a list component ({@code Table}, {@code DataGrid}, etc.) in a screen XML descriptor.
 */
@ActionType(ExcelAction.ID)
public class ExcelAction extends ListAction {

    public static final String ID = "excel";

    protected Messages messages;
    protected BeanLocator beanLocator;

    protected String fileName = null;

    /**
     * If true and table is aggregatable will export aggregation row to excel document.
     */
    protected boolean exportAggregation = true;

    public ExcelAction() {
        super(ID);
    }

    public ExcelAction(String id) {
        super(id);
    }

    @Inject
    protected void setIcons(Icons icons) {
        this.icon = icons.get(CubaIcon.EXCEL_ACTION);
    }

    @Inject
    protected void setMessages(Messages messages) {
        this.messages = messages;
        this.caption = messages.getMainMessage("actions.Excel");
    }

    @Inject
    protected void setBeanLocator(BeanLocator beanLocator) {
        this.beanLocator = beanLocator;
    }

    @Override
    public void actionPerform(Component component) {
        if (!hasSubscriptions(ActionPerformedEvent.class)) {
            execute();
        } else {
            super.actionPerform(component);
        }
    }

    /**
     * Executes the action.
     */
    public void execute() {
        if (target == null) {
            throw new IllegalStateException("ExcelAction target is not set");
        }

        if (needExportAll()) {
            export(ExcelExporter.ExportMode.ALL_ROWS);
        } else {
            AbstractAction exportSelectedAction = new AbstractAction("actions.export.SELECTED_ROWS", Status.PRIMARY) {
                @Override
                public void actionPerform(Component component) {
                    export(ExcelExporter.ExportMode.SELECTED_ROWS);
                }
            };
            exportSelectedAction.setCaption(messages.getMainMessage(exportSelectedAction.getId()));

            AbstractAction exportAllAction = new AbstractAction("actions.export.ALL_ROWS") {
                @Override
                public void actionPerform(Component component) {
                    export(ExcelExporter.ExportMode.ALL_ROWS);
                }
            };
            exportAllAction.setCaption(messages.getMainMessage(exportAllAction.getId()));

            Action[] actions = new Action[]{
                    exportSelectedAction,
                    exportAllAction,
                    new DialogAction(DialogAction.Type.CANCEL)
            };

            Dialogs dialogs = ComponentsHelper.getScreenContext(target).getDialogs();

            dialogs.createOptionDialog()
                    .withCaption(messages.getMainMessage("actions.exportSelectedTitle"))
                    .withMessage(messages.getMainMessage("actions.exportSelectedCaption"))
                    .withType(Dialogs.MessageType.CONFIRMATION)
                    .withActions(actions)
                    .show();
        }
    }

    protected boolean needExportAll() {
        if (target.getSelected().isEmpty()
                || !(target.getItems() instanceof ContainerDataUnit)) {
            return true;
        }
        CollectionContainer container = ((ContainerDataUnit) target.getItems()).getContainer();
        return container != null && container.getItems().size() <= 1;
    }

    public boolean isExportAggregation() {
        return exportAggregation;
    }

    public void setExportAggregation(boolean exportAggregation) {
        this.exportAggregation = exportAggregation;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Export via {@link ExcelExporter}.
     */
    protected void export(ExcelExporter.ExportMode exportMode) {
        ExcelExporter exporter = new ExcelExporter();
        exporter.setExportAggregation(exportAggregation);

        Window window = ComponentsHelper.getWindowNN(target);

        ExportDisplay display = beanLocator.get(ExportDisplay.NAME);
        display.setFrame(window);

        if (target instanceof Table) {
            @SuppressWarnings("unchecked")
            Table<Entity> table = (Table<Entity>) target;
            exporter.exportTable(table, table.getNotCollapsedColumns(), false, display, null, fileName, exportMode);
        }

        if (target instanceof DataGrid) {
            @SuppressWarnings("unchecked")
            DataGrid<Entity> dataGrid = (DataGrid<Entity>) target;
            List<DataGrid.Column> columns = dataGrid.getVisibleColumns().stream()
                    .filter(col -> !col.isCollapsed())
                    .collect(Collectors.toList());
            exporter.exportDataGrid(dataGrid, columns, display, null, fileName, exportMode);
        }

        if (exporter.isXlsMaxRowNumberExceeded()) {
            Notifications notifications = ComponentsHelper.getScreenContext(target).getNotifications();

            notifications.create(NotificationType.WARNING)
                    .withCaption(messages.getMainMessage("actions.warningExport.title"))
                    .withDescription(messages.getMainMessage("actions.warningExport.message"))
                    .show();
        }
    }
}