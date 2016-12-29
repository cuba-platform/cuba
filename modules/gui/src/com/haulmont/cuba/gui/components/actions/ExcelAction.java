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
package com.haulmont.cuba.gui.components.actions;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.DialogAction.Type;
import com.haulmont.cuba.gui.components.Frame.MessageType;
import com.haulmont.cuba.gui.export.ExcelExporter;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.gui.theme.ThemeConstantsManager;
import org.springframework.context.annotation.Scope;

import static com.haulmont.cuba.gui.export.ExcelExporter.ExportMode;

/**
 * Standard table action to export the list of entities to XLS.
 * <p>
 * Action's behaviour can be customized by providing arguments to constructor or setting properties.
 * <p>
 * In order to provide your own implementation globally, create a subclass and register it in {@code web-spring.xml},
 * for example:
 * <pre>
 * &lt;bean id="cuba_ExcelAction" class="com.company.sample.gui.MyExcelAction" scope="prototype"/&gt;
 * </pre>
 * Also, use {@code create()} static methods instead of constructors when creating the action programmatically.
 */
@org.springframework.stereotype.Component("cuba_ExcelAction")
@Scope("prototype")
public class ExcelAction extends BaseAction implements Action.HasBeforeActionPerformedHandler {

    public static final String ACTION_ID = ListActionType.EXCEL.getId();

    protected final Table table;
    protected final ExportDisplay display;

    protected BeforeActionPerformedHandler beforeActionPerformedHandler;

    /**
     * Creates an action with default id.
     * @param target    component containing this action
     */
    public static ExcelAction create(ListComponent target) {
        return AppBeans.getPrototype("cuba_ExcelAction", target);
    }

    /**
     * Creates an action with default id.
     * @param target    component containing this action
     * @param display   ExportDisplay implementation
     */
    public static ExcelAction create(ListComponent target, ExportDisplay display) {
        return AppBeans.getPrototype("cuba_ExcelAction", target, display);
    }

    /**
     * Creates an action with the given id.
     * @param target    component containing this action
     * @param display   ExportDisplay implementation
     * @param id            action's name
     */
    public static ExcelAction create(ListComponent target, ExportDisplay display, String id) {
        return AppBeans.getPrototype("cuba_ExcelAction", target, display, id);
    }

    /**
     * The simplest constructor. The action uses default name and other parameters.
     * @param table     table containing this action
     */
    public ExcelAction(Table table) {
        this(table, AppConfig.createExportDisplay(table.getFrame()), ACTION_ID);
    }

    /**
     * Constructor that allows to specify the ExportDisplay implementation. The action uses default name
     * and other parameters.
     * @param table     table containing this action
     * @param display   ExportDisplay implementation
     */
    public ExcelAction(Table table, ExportDisplay display) {
        this(table, display, ACTION_ID);
    }

    /**
     * Constructor that allows to specify all parameters.
     * @param table         table containing this action
     * @param display       ExportDisplay implementation
     * {@link ExcelExporter}
     * @param id            action's name
     */
    public ExcelAction(Table table, ExportDisplay display, String id) {
        super(id);
        this.table = table;
        this.display = display;
        this.caption = messages.getMainMessage("actions.Excel");

        ThemeConstantsManager thCM = AppBeans.get(ThemeConstantsManager.NAME);
        this.icon = thCM.getThemeValue("actions.Excel.icon");
    }

    /**
     * This method is invoked by action owner component.
     * @param component component invoking action
     */
    @Override
    public void actionPerform(Component component) {
        if (beforeActionPerformedHandler != null) {
            if (!beforeActionPerformedHandler.beforeActionPerformed())
                return;
        }

        if (table.getSelected().isEmpty() || table.getDatasource().size() <= 1) {
            export(ExportMode.ALL_ROWS);
        } else {
            String title = messages.getMainMessage("actions.exportSelectedTitle");
            String caption = messages.getMainMessage("actions.exportSelectedCaption");

            AbstractAction exportSelectedAction = new AbstractAction("actions.export.SELECTED_ROWS", Status.PRIMARY) {
                @Override
                public void actionPerform(Component component) {
                    export(ExportMode.SELECTED_ROWS);
                }
            };
            exportSelectedAction.setCaption(messages.getMainMessage(exportSelectedAction.getId()));

            AbstractAction exportAllAction = new AbstractAction("actions.export.ALL_ROWS") {
                @Override
                public void actionPerform(Component component) {
                    export(ExportMode.ALL_ROWS);
                }
            };
            exportAllAction.setCaption(messages.getMainMessage(exportAllAction.getId()));

            Action[] actions = new Action[]{
                    exportSelectedAction,
                    exportAllAction,
                    new DialogAction(Type.CANCEL)
            };
            Frame frame = table.getFrame();
            frame.showOptionDialog(title, caption, MessageType.CONFIRMATION, actions);
        }
    }

    /**
     * Export via {@link ExcelExporter}.
     */
    protected void export(ExportMode exportMode) {
        ExcelExporter exporter = new ExcelExporter();
        exporter.exportTable(table, table.getNotCollapsedColumns(), display, exportMode);
    }

    @Override
    public BeforeActionPerformedHandler getBeforeActionPerformedHandler() {
        return beforeActionPerformedHandler;
    }

    @Override
    public void setBeforeActionPerformedHandler(BeforeActionPerformedHandler handler) {
        beforeActionPerformedHandler = handler;
    }
}