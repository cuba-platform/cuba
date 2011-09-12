/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Maksim Tulupov
 * Created: 21.08.2009 13:18:20
 *
 * $Id$
 */
package com.haulmont.cuba.web.app.ui.export.excel;

import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.CheckBox;
import com.haulmont.cuba.gui.export.ExcelExporter;
import com.haulmont.cuba.gui.export.ExportDisplay;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.vaadin.terminal.ThemeResource;

import java.util.*;
import java.util.List;

public class ExcelExportBrowser extends AbstractWindow {

    protected Table table;

    protected ExportDisplay exportDisplay;

    protected OptionsGroup optionsGroup;

    public ExcelExportBrowser(IFrame frame) {
        super(frame);
    }

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
        table = (Table) params.get("param$table");
        exportDisplay = (ExportDisplay) params.get("param$exportDisplay");
        optionsGroup = ((OptionsGroup) getComponent("optionsGroup"));
        initValues();
        initCheckBox();

        Button commitButton = getComponent("commit");
        commitButton.setAction(new ExcelExportAction("actions.Ok"));

        Button closeButton = getComponent("close");
        closeButton.setAction(new AbstractAction("actions.Cancel") {

            public void actionPerform(Component component) {
                ExcelExportBrowser.this.close(null);
            }
        });

        Button upButton = getComponent("up");
        upButton.setAction(new AbstractAction("") {

            public void actionPerform(Component component) {
                moveColumns(true);
            }
        });

        Button downButton = getComponent("down");
        downButton.setAction(new AbstractAction("") {

            public void actionPerform(Component component) {
                moveColumns(false);
            }
        });
        final com.vaadin.ui.Component upButtonIT = WebComponentsHelper.unwrap(upButton);
        upButtonIT.setIcon(new ThemeResource("icons/32/arrow-up.png"));

        final com.vaadin.ui.Component downButtonIT = WebComponentsHelper.unwrap(downButton);
        downButtonIT.setIcon(new ThemeResource("icons/32/arrow-down.png"));
    }

    protected void initValues() {
        optionsGroup.setOptionsList(new ArrayList(table.getColumns()));
    }

    protected void initCheckBox() {
        final CheckBox cb = getComponent("exportExpanded");
        if (table instanceof TreeTable) {
                        cb.setEnabled(true);
            cb.setValue(Boolean.TRUE);

        } else {
                cb.setEnabled(false);
            cb.setValue(Boolean.FALSE);
        }
    }

    protected void moveColumns(boolean up) {
        final List<Table.Column> selectedColumns = getSelectedColumns(up);

        if (selectedColumns != null) {
            for (Table.Column c : selectedColumns) {
                moveColumn(up, c);
            }
        }

    }

    protected List<Table.Column> getSelectedColumns(boolean asc) {
        final Set<Set> optionsList = optionsGroup.getValue();

        java.util.List<Table.Column> columns = new ArrayList<Table.Column>();
        final List list = new ArrayList(optionsGroup.getOptionsList());
        if (optionsList != null) {
            for (Set s : optionsList) {
                for (Object o : s) {
                    if (o instanceof Table.Column) {
                        if (asc) {
                            for (Object o1 : list) {
                                final Object obj = findElement(s, o1);
                                if (obj != null && s.contains(obj)) {
                                    columns.add((Table.Column) obj);
                                    list.remove(o1);
                                    break;
                                }
                            }
                        } else {
                            for (ListIterator it = list.listIterator(list.size()); it.hasPrevious();) {
                                final Object o1 = it.previous();
                                final Object obj = findElement(s, o1);
                                if (obj != null && s.contains(obj)) {
                                    columns.add((Table.Column) obj);
                                    it.remove();
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        return columns;
    }

    protected void moveColumn(boolean up, Table.Column column) {
        final List<Table.Column> optionsList = optionsGroup.getOptionsList();

        if (optionsList != null) {
            final int oldPosition = optionsList.indexOf(column);
            int step = up ? -1 : 1;
            if (oldPosition >= 0) {
                int newPosition = oldPosition + step;
                if (newPosition < 0) {
                    newPosition += optionsList.size();
                } else if (newPosition >= optionsList.size()) {
                    newPosition -= optionsList.size();
                }
                moveElement(optionsList, oldPosition, newPosition);
            }
        }
        optionsGroup.setOptionsList(optionsList);
    }

    protected void moveElement(List list, int sourceIndex, int destIndex) {
        Object o1 = list.get(sourceIndex);
        Object o2 = list.get(destIndex);

        list.set(sourceIndex, o2);
        list.set(destIndex, o1);
    }

    protected Object findElement(Set elements, Object obj) {
        for (Object o : elements) {
            if (o.equals(obj)) {
                return o;
            }
        }
        return null;
    }


    private class ExcelExportAction extends AbstractAction {
        protected ExcelExportAction(String id) {
            super(id);
        }

        public void actionPerform(Component component) {

            ExcelExporter ee = new ExcelExporter();
            ee.exportTable(table, getSelectedColumns(true),
                    (Boolean) ((CheckBox)getComponent("exportExpanded")).getValue(), exportDisplay);
            ExcelExportBrowser.this.close(null);
        }
    }
}
