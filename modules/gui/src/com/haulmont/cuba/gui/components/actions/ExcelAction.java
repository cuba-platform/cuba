/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 23.03.11 11:25
 *
 * $Id$
 */
package com.haulmont.cuba.gui.components.actions;

import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.AbstractAction;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.export.ExcelExporter;
import com.haulmont.cuba.gui.export.ExportDisplay;

import java.util.HashMap;
import java.util.Map;

/**
 * Standard table action to export the list of entities to XLS.
 * <p>
 * Action's behaviour can be customized by providing arguments to constructor or setting properties.
 * </p>
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class ExcelAction extends AbstractAction {

    public static final String ACTION_ID = ListActionType.EXCEL.getId();

    protected final Table table;
    protected final ExportDisplay display;
    private boolean parameterized;

    /**
     * The simplest constructor. The action uses default name and other parameters.
     * @param table     table containing this action
     */
    public ExcelAction(Table table) {
        this(table, AppConfig.createExportDisplay(), false, ACTION_ID);
    }

    /**
     * Constructor that allows to specify the ExportDisplay implementation. The action uses default name
     * and other parameters.
     * @param table     table containing this action
     * @param display   ExportDisplay implementation
     */
    public ExcelAction(Table table, ExportDisplay display) {
        this(table, display, false, ACTION_ID);
    }

    /**
     * Constructor that allows to specify the ExportDisplay implementation and parameterized flag.
     * The action uses default name.
     * @param table         table containing this action
     * @param display       ExportDisplay implementation
     * @param parameterized if true, the special window "cuba$ExcelExport" will be opened instead of direct export via
     * {@link ExcelExporter}
     */
    public ExcelAction(Table table, ExportDisplay display, boolean parameterized) {
        this(table, display, parameterized, ACTION_ID);
    }

    /**
     * Constructor that allows to specify all parameters.
     * @param table         table containing this action
     * @param display       ExportDisplay implementation
     * @param parameterized if true, the special window "cuba$ExcelExport" will be opened instead of direct export via
     * {@link ExcelExporter}
     * @param id            action's name
     */
    public ExcelAction(Table table, ExportDisplay display, boolean parameterized, String id) {
        super(id);
        this.table = table;
        this.display = display;
        this.parameterized = parameterized;
        this.caption = MessageProvider.getMessage(AppConfig.getMessagesPack(), "actions.Excel");
        this.icon = "icons/excel.png";
    }

    /**
     * This method is invoked by action owner component.
     * @param component component invoking action
     */
    public void actionPerform(Component component) {
        if (parameterized)
            parameterizedExport();
        else
            export();
    }

    /**
     * Export via {@link ExcelExporter}.
     */
    protected void export() {
        ExcelExporter exporter = new ExcelExporter();
        exporter.exportTable(table, table.getNotCollapsedColumns(), display);
    }

    /**
     * Export via screen "cuba$ExcelExport".
     */
    protected void parameterizedExport() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("table", table);
        params.put("exportDisplay", display);
        table.getFrame().openWindow("cuba$ExcelExport", WindowManager.OpenType.DIALOG, params);
    }
}
