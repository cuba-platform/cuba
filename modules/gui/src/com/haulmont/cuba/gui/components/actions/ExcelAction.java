/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components.actions;

import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.DialogAction.Type;
import com.haulmont.cuba.gui.export.ExcelExporter;
import com.haulmont.cuba.gui.export.ExportDisplay;

import static com.haulmont.cuba.gui.export.ExcelExporter.ExportMode;

/**
 * Standard table action to export the list of entities to XLS.
 * <p>
 * Action's behaviour can be customized by providing arguments to constructor or setting properties.
 *
 * @author krivopustov
 * @version $Id$
 */
public class ExcelAction extends BaseAction {

    public static final String ACTION_ID = ListActionType.EXCEL.getId();

    protected final Table table;
    protected final ExportDisplay display;

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
        this.icon = messages.getMainMessage("actions.Excel.icon");
    }

    /**
     * This method is invoked by action owner component.
     * @param component component invoking action
     */
    @Override
    public void actionPerform(Component component) {
        if (table.getSelected().size() > 0) {
            String title = messages.getMainMessage("actions.exportSelectedTitle");
            String caption = messages.getMainMessage("actions.exportSelectedCaption");
            Action[] actions = new Action[] {
                    new AbstractAction("actions.export.SELECTED_ROWS", Status.PRIMARY) {
                        {
                            setCaption(messages.getMainMessage(getId()));
                        }

                        @Override
                        public void actionPerform(Component component) {
                            export(ExportMode.SELECTED_ROWS);
                        }
                    },
                    new AbstractAction("actions.export.ALL_ROWS") {
                        {
                            setCaption(messages.getMainMessage(getId()));
                        }

                        @Override
                        public void actionPerform(Component component) {
                            export(ExportMode.ALL_ROWS);
                        }
                    },
                    new DialogAction(Type.CANCEL)
            };
            Frame frame = table.getFrame();
            frame.showOptionDialog(title, caption, Frame.MessageType.CONFIRMATION, actions);
        } else {
            export(ExportMode.ALL_ROWS);
        }
    }

    /**
     * Export via {@link ExcelExporter}.
     */
    protected void export(ExportMode exportMode) {
        ExcelExporter exporter = new ExcelExporter();
        exporter.exportTable(table, table.getNotCollapsedColumns(), display, exportMode);
    }
}