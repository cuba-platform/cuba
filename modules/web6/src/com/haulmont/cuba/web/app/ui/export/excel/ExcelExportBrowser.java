/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.app.ui.export.excel;

import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.export.ExcelExporter;
import com.haulmont.cuba.gui.export.ExportDisplay;

import javax.inject.Inject;
import java.util.*;

/**
 * @author tulupov
 * @version $Id$
 */
public class ExcelExportBrowser extends AbstractWindow {

    protected Table table;

    protected ExportDisplay exportDisplay;

    @Inject
    protected CheckBox exportExpandedCheckBox;

    @Inject
    protected OptionsGroup exportOptions;

    @Inject
    protected Button commitBtn;

    @Inject
    protected Button closeBtn;

    @Inject
    protected Button upBtn;

    @Inject
    protected Button downBtn;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        table = (Table) params.get("param$table");
        exportDisplay = (ExportDisplay) params.get("param$exportDisplay");

        initValues();
        initCheckBox();

        commitBtn.setAction(new ExcelExportAction("actions.Ok"));

        closeBtn.setAction(new AbstractAction("actions.Cancel") {
            @Override
            public void actionPerform(Component component) {
                ExcelExportBrowser.this.close(null);
            }
        });

        upBtn.setAction(new AbstractAction("") {
            {
                setIcon("icons/up.png");
            }

            @Override
            public void actionPerform(Component component) {
                moveColumns(true);
            }
        });

        downBtn.setAction(new AbstractAction("") {
            {
                setIcon("icons/down.png");
            }

            @Override
            public void actionPerform(Component component) {
                moveColumns(false);
            }
        });
    }

    protected void initValues() {
        exportOptions.setOptionsList(new ArrayList(table.getColumns()));
    }

    protected void initCheckBox() {
        if (table instanceof TreeTable) {
            exportExpandedCheckBox.setEnabled(true);
            exportExpandedCheckBox.setValue(Boolean.TRUE);
        } else {
            exportExpandedCheckBox.setEnabled(false);
            exportExpandedCheckBox.setValue(Boolean.FALSE);
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
        final Set<Set> optionsList = exportOptions.getValue();

        java.util.List<Table.Column> columns = new ArrayList<>();
        final List list = new ArrayList(exportOptions.getOptionsList());
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
                            for (ListIterator it = list.listIterator(list.size()); it.hasPrevious(); ) {
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
        final List<Table.Column> optionsList = exportOptions.getOptionsList();

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
        exportOptions.setOptionsList(optionsList);
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

        @Override
        public void actionPerform(Component component) {
            ExcelExporter ee = new ExcelExporter();
            ee.exportTable(table, getSelectedColumns(true), (Boolean) exportExpandedCheckBox.getValue(), exportDisplay);
            ExcelExportBrowser.this.close(null);
        }
    }
}