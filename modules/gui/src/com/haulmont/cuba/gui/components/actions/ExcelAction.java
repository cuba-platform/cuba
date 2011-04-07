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

public class ExcelAction extends AbstractAction {

    private static final long serialVersionUID = -1811807609363983998L;

    public static final String ACTION_ID = "excel";

    protected final Table table;
    protected final ExportDisplay display;
    private boolean parameterized;

    public ExcelAction(Table table) {
        this(table, AppConfig.createExportDisplay(), false, ACTION_ID);
    }

    public ExcelAction(Table table, ExportDisplay display) {
        this(table, display, false, ACTION_ID);
    }

    public ExcelAction(Table table, ExportDisplay display, boolean parameterized) {
        this(table, display, parameterized, ACTION_ID);
    }

    public ExcelAction(Table table, ExportDisplay display, boolean parameterized, String id) {
        super(id);
        this.table = table;
        this.display = display;
        this.parameterized = parameterized;
    }

    public String getCaption() {
        final String messagesPackage = AppConfig.getInstance().getMessagesPack();
        return MessageProvider.getMessage(messagesPackage, "actions.Excel");
    }

    public void actionPerform(Component component) {
        if (parameterized)
            parameterizedExport();
        else
            export();
    }

    protected void export() {
        ExcelExporter exporter = new ExcelExporter();
        exporter.exportTable(table, table.getNotCollapsedColumns(), display);
    }

    protected void parameterizedExport() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("table", table);
        params.put("exportDisplay", display);
        table.getFrame().openWindow("cuba$ExcelExport", WindowManager.OpenType.DIALOG, params);
    }
}
