/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.Table;
import org.jdesktop.swingx.JXHyperlink;
import sun.swing.DefaultLookup;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>$Id$</p>
 *
 * @author Alexander Budarov
 */
public class DesktopTableCellEditor extends AbstractCellEditor implements TableCellEditor, TableCellRenderer {

    private static final long serialVersionUID = 5217563286634642347L;

    private Table.ColumnGenerator columnGenerator;
    private Component activeComponent;
    private Map<Integer, Component> cache = new HashMap<Integer, Component>();

    /*
     * true, if cells of this column hold editable content.
     * Swing treats keyboard, mouse, focus events for editable and not-editable cells differently.
     */
    private boolean editable = true;
    private DesktopAbstractTable desktopAbstractTable;
    private Border border;

    public DesktopTableCellEditor(DesktopAbstractTable desktopAbstractTable, Table.ColumnGenerator columnGenerator) {
        this.desktopAbstractTable = desktopAbstractTable;
        this.columnGenerator = columnGenerator;
    }

    @Override
    public boolean isCellEditable(EventObject e) {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    protected Component getCellComponent(int row) {
        Entity item = desktopAbstractTable.getTableModel().getItem(row);
        com.haulmont.cuba.gui.components.Component component = columnGenerator.generateCell(desktopAbstractTable, item.getId());
        Component comp;
        if (component == null)
            comp = new JLabel("");
        else
            comp = DesktopComponentsHelper.getComposition(component);
        cache.put(row, comp);
        return comp;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        Component component = getCellComponent(row);
        applyStyle(component, table, true, false, row);

        String stylename = desktopAbstractTable.getStylename(table, row, column);
        desktopAbstractTable.applyStylename(isSelected, true, activeComponent, stylename);
        return component;
    }

    @Override
    public Object getCellEditorValue() {
        if (activeComponent != null) {
            // normally handle focus lost
            activeComponent.dispatchEvent(new FocusEvent(activeComponent, FocusEvent.FOCUS_LOST));
        }
        return "";
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        activeComponent = cache.get(row);
        if (activeComponent == null) {
            activeComponent = getCellComponent(row);
            cache.put(row, activeComponent);
        }

        applyStyle(activeComponent, table, isSelected, hasFocus, row);

        String stylename = desktopAbstractTable.getStylename(table, row, column);
        desktopAbstractTable.applyStylename(isSelected, hasFocus, activeComponent, stylename);

        return activeComponent;
    }

    public void clearCache() {
        cache.clear();
    }

    public Border getBorder() {
        return border;
    }

    public void setBorder(Border border) {
        this.border = border;
    }

    public void applyStyle(java.awt.Component component, JTable table, boolean isSelected, boolean hasFocus, int row) {
        if (!(component instanceof JComponent)) {
            return;
        }
        JComponent jcomponent = (JComponent) component;
        jcomponent.setOpaque(true);

        if (isSelected) {
            if (jcomponent instanceof JTextField) {
                // another JTextField dirty workaround. If use selectionBackground, then it's all blue
                jcomponent.setBackground(Color.WHITE);
                jcomponent.setForeground(table.getForeground());
            } else {
                jcomponent.setBackground(table.getSelectionBackground());
                jcomponent.setForeground(table.getSelectionForeground());
            }
        } else {
            jcomponent.setForeground(table.getForeground());
            Color background = DefaultLookup.getColor(jcomponent, table.getUI(), "Table:\"Table.cellRenderer\".background");
            jcomponent.setBackground(background);
        }

        jcomponent.setFont(table.getFont());

        assignBorder(table, isSelected, hasFocus, jcomponent);
    }

    private void assignBorder(JTable table, boolean isSelected, boolean hasFocus, JComponent jcomponent) {
        if (jcomponent instanceof JTextField) {
            // looks bad with empty border
        } else if (border != null) {
            jcomponent.setBorder(border);
        } else if (jcomponent instanceof JComboBox || jcomponent instanceof JXHyperlink) {
            // empty borders for fields except text fields in tables
            jcomponent.setBorder(new EmptyBorder(0, 0, 0, 0));
        } else {
            if (hasFocus) {
                Border border = null;
                if (isSelected) {
                    border = DefaultLookup.getBorder(jcomponent, table.getUI(), "Table.focusSelectedCellHighlightBorder");
                }
                if (border == null) {
                    border = DefaultLookup.getBorder(jcomponent, table.getUI(), "Table.focusCellHighlightBorder");
                }
                jcomponent.setBorder(border);
            } else {
                jcomponent.setBorder(getNoFocusBorder(jcomponent, table));
            }
        }
    }

    private Border getNoFocusBorder(JComponent jcomponent, JTable table) {
        Border border = DefaultLookup.getBorder(jcomponent, table.getUI(), "Table.cellNoFocusBorder");
        return border;
    }

}
