/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.bali.util.Dom4j;
import com.haulmont.cuba.gui.components.Table;
import org.dom4j.Element;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.table.TableColumnExt;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class SwingXTableSettings implements TableSettings {

    private JXTable table;
    private List<Table.Column> columns;

    public SwingXTableSettings(JXTable table, List<Table.Column> columns) {
        this.table = table;
        this.columns = columns;
    }

    @Override
    public boolean saveSettings(Element element) {
        Element columnsElem = element.element("columns");
        if (columnsElem != null)
            element.remove(columnsElem);
        columnsElem = element.addElement("columns");

        List<TableColumn> columns = table.getColumns(true);
        Collections.sort(
                columns,
                new Comparator<TableColumn>() {
                    @Override
                    public int compare(TableColumn col1, TableColumn col2) {
                        if (col1 instanceof TableColumnExt && !((TableColumnExt) col1).isVisible())
                            return 1;
                        if (col2 instanceof TableColumnExt && !((TableColumnExt) col2).isVisible())
                            return -1;
                        int i1 = table.getColumnModel().getColumnIndex(col1.getIdentifier());
                        int i2 = table.getColumnModel().getColumnIndex(col2.getIdentifier());
                        return i1 - i2;
                    }
                }
        );

        for (TableColumn column : columns) {
            Element colElem = columnsElem.addElement("column");
            colElem.addAttribute("id", column.getIdentifier().toString());

            int width = column.getWidth();
            colElem.addAttribute("width", String.valueOf(width));

            if (column instanceof TableColumnExt) {
                Boolean visible = ((TableColumnExt) column).isVisible();
                colElem.addAttribute("visible", visible.toString());
            }
        }

        if (table.getRowSorter() != null) {
            List<? extends RowSorter.SortKey> sortKeys = table.getRowSorter().getSortKeys();
            if (!sortKeys.isEmpty()) {
                RowSorter.SortKey sortKey = sortKeys.get(0);
                columnsElem.addAttribute("sortColumn", String.valueOf(sortKey.getColumn()));
                columnsElem.addAttribute("sortOrder", sortKey.getSortOrder().toString());
            }
        }

        return true;
    }

    @Override
    public void apply(Element element, boolean sortable) {
        final Element columnsElem = element.element("columns");
        if (columnsElem == null)
            return;

        List<Object> sequence = new ArrayList<Object>();
        List<TableColumnExt> invisible = new ArrayList<TableColumnExt>();
        for (Element colElem : Dom4j.elements(columnsElem, "column")) {
            String id = colElem.attributeValue("id");
            Table.Column column = getColumn(id);
            if (column != null) {
                sequence.add(column);

                TableColumnExt tableColumn = table.getColumnExt(column);

                String width = colElem.attributeValue("width");
                if ((width != null) && (tableColumn != null))
                    tableColumn.setPreferredWidth(Integer.valueOf(width));

                String visible = colElem.attributeValue("visible");
                if (visible != null && !Boolean.valueOf(visible))
                    invisible.add(tableColumn);
            }
        }
        table.setColumnSequence(sequence.toArray(new Object[sequence.size()]));

        for (TableColumnExt invisibleTableColumn : invisible) {
            invisibleTableColumn.setVisible(false);
        }

        if (sortable && table.getRowSorter() != null) {
            String sortColumn = columnsElem.attributeValue("sortColumn");
            if (sortColumn != null) {
                SortOrder sortOrder = SortOrder.valueOf(columnsElem.attributeValue("sortOrder"));
                table.getRowSorter().setSortKeys(Collections.singletonList(new RowSorter.SortKey(Integer.valueOf(sortColumn), sortOrder)));
            }
        }
    }

    private Table.Column getColumn(String id) {
        for (Table.Column column : columns) {
            if (column.getId().toString().equals(id))
                return column;
        }
        return null;
    }
}
