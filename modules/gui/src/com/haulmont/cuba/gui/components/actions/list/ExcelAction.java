/*
 * Copyright (c) 2008-2018 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.haulmont.cuba.gui.components.actions.list;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.Dialogs;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.ListAction;
import com.haulmont.cuba.gui.export.ExcelExporter;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.icons.CubaIcon;
import com.haulmont.cuba.gui.icons.Icons;
import com.haulmont.cuba.gui.model.CollectionContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@ActionType(ExcelAction.ID)
public class ExcelAction extends ListAction {

    public static final String ID = "excel";

    private static final Logger log = LoggerFactory.getLogger(ExcelAction.class);

    protected Messages messages;

    protected ExportDisplay display;

    protected String fileName = null;

    @Inject
    protected Notifications notifications;

    @Inject
    protected Dialogs dialogs;

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
    protected void setExportDisplay(ExportDisplay exportDisplay) {
        this.display = exportDisplay;
        if (target != null) {
            display.setFrame(target.getFrame());
        }
    }

    @Override
    public void setTarget(ListComponent target) {
        if (target != null
                && !(target instanceof SupportsEntityBinding)) {
            throw new IllegalStateException("ExcelAction target does not implement SupportsEntityBinding");
        }
        if (display != null && target != null) {
            display.setFrame(target.getFrame());
        }
        super.setTarget(target);
    }

    @Override
    public void actionPerform(Component component) {
        if (!hasSubscriptions(ActionPerformedEvent.class)) {
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

                dialogs.createOptionDialog()
                        .setCaption(messages.getMainMessage("actions.exportSelectedTitle"))
                        .setMessage(messages.getMainMessage("actions.exportSelectedCaption"))
                        .setType(Dialogs.MessageType.CONFIRMATION)
                        .setActions(actions)
                        .show();
            }
        } else {
            super.actionPerform(component);
        }
    }

    protected boolean needExportAll() {
        if (target.getSelected().isEmpty())
            return true;
        CollectionContainer container = ((SupportsContainerBinding) target).getBindingContainer();
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
            notifications.create()
                    .setCaption(messages.getMainMessage("actions.warningExport.title"))
                    .setDescription(messages.getMainMessage("actions.warningExport.message"))
                    .setType(Notifications.NotificationType.WARNING)
                    .show();
        }
    }
}