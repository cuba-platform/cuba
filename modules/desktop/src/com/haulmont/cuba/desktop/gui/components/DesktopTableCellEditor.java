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

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.desktop.sys.vcl.DatePicker.DatePicker;
import com.haulmont.cuba.desktop.sys.vcl.Flushable;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Label;
import org.jdesktop.swingx.JXHyperlink;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.Component;
import java.util.*;

import static com.haulmont.cuba.gui.components.Component.BelongToFrame;

public class DesktopTableCellEditor extends AbstractCellEditor implements TableCellEditor, TableCellRenderer {

    // Client property key for cell editor components, value of property contains table for this editor
    public static final String CELL_EDITOR_TABLE = "CELL_EDITOR_TABLE";
    public static final String CELL_COMPONENT = "CELL_COMPONENT";

    private static final long serialVersionUID = 5217563286634642347L;

    private Table.ColumnGenerator columnGenerator;
    private Component activeComponent;
    private Map<Integer, Component> cache = new HashMap<>();

    // Used for properly removing column from table
    private Table.Column associatedRuntimeColumn;

    /*
     * true, if cells of this column hold editable content.
     * Swing treats keyboard, mouse, focus events for editable and not-editable cells differently.
     */
    private boolean editable;
    private DesktopAbstractTable desktopAbstractTable;
    private Border border;
    private Class<? extends com.haulmont.cuba.gui.components.Component> componentClass;

    private static final Set<Class> readOnlyComponentClasses = new HashSet<Class>(Arrays.asList(
            Label.class, Checkbox.class
    ));

    private static final Set<Class> inlineComponentClasses = new HashSet<Class>(Arrays.asList(
            Label.class, Checkbox.class
    ));

    public DesktopTableCellEditor(DesktopAbstractTable desktopAbstractTable, Table.ColumnGenerator columnGenerator,
                                  Class<? extends com.haulmont.cuba.gui.components.Component> componentClass) {
        this.desktopAbstractTable = desktopAbstractTable;
        this.columnGenerator = columnGenerator;
        this.componentClass = componentClass;
        this.editable = isEditableComponent(componentClass);
    }

    /*
     * If component is editable, it should gain focus from table.
     * Mouse events like mouse dragging are treated differently for editable columns.
     */
    protected boolean isEditableComponent(Class<? extends com.haulmont.cuba.gui.components.Component> componentClass) {
        if (componentClass == null) {
            return true;
        }
        for (Class readOnlyClass : readOnlyComponentClasses) {
            if (componentClass.isAssignableFrom(readOnlyClass)) {
                return false;
            }
        }
        return true;
    }

    /*
     * Inline components always fit in standard row height,
     * so there is no need to pack rows for desktop table.
     */
    public boolean isInline() {
        if (componentClass == null) {
            return false;
        }
        for (Class inlineClass : inlineComponentClasses) {
            if (componentClass.isAssignableFrom(inlineClass)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isCellEditable(EventObject e) {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public Table.ColumnGenerator getColumnGenerator() {
        return columnGenerator;
    }

    protected Component getCellComponent(int row) {
        Entity item = desktopAbstractTable.getTableModel().getItem(row);

        StopWatch sw = new Log4JStopWatch("TableColumnGenerator." + desktopAbstractTable.getId());
        @SuppressWarnings("unchecked")
        com.haulmont.cuba.gui.components.Component component = columnGenerator.generateCell(item);
        sw.stop();

        Component comp;
        if (component == null) {
            comp = new JLabel("");
        } else if (component instanceof Table.PlainTextCell) {
            comp = new JLabel(((Table.PlainTextCell) component).getText());
        } else {
            if (component instanceof BelongToFrame) {
                BelongToFrame belongToFrame = (BelongToFrame) component;
                if (belongToFrame.getFrame() == null) {
                    belongToFrame.setFrame(desktopAbstractTable.getFrame());
                }
            }
            component.setParent(desktopAbstractTable);

            JComponent jComposition = DesktopComponentsHelper.getComposition(component);
            jComposition.putClientProperty(CELL_EDITOR_TABLE, desktopAbstractTable.getComponent());
            jComposition.putClientProperty(CELL_COMPONENT, component);

            comp = jComposition;
        }

        cache.put(row, comp);
        return comp;
    }

    public Table.Column getAssociatedRuntimeColumn() {
        return associatedRuntimeColumn;
    }

    public void setAssociatedRuntimeColumn(Table.Column associatedRuntimeColumn) {
        this.associatedRuntimeColumn = associatedRuntimeColumn;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        Component component = getCellComponent(row);
        applyStyle(component, table, true, true, row);

        String stylename = desktopAbstractTable.getStylename(table, row, column);
        desktopAbstractTable.applyStylename(isSelected, true, activeComponent, stylename);
        return component;
    }

    @Override
    public Object getCellEditorValue() {
        if (activeComponent != null) {
            flush(activeComponent);

            Component focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();

            if (focusOwner == activeComponent ||
                    (activeComponent instanceof Container && ((Container) activeComponent).isAncestorOf(focusOwner))) {
                desktopAbstractTable.impl.requestFocus();
            }
        }
        return "";
    }

    private void flush(Component component) {
        if (component instanceof Flushable) {
            ((Flushable) component).flushValue();
        } else if (component instanceof Container) {
            for(Component child : ((Container) component).getComponents()){
                flush(child);
            }
        }
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int column) {
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
            if (isTextField(jcomponent)) {
                // another JTextField dirty workaround. If use selectionBackground, then it's all blue
                jcomponent.setBackground(table.getBackground());
                jcomponent.setForeground(table.getForeground());
            } else {
                jcomponent.setBackground(table.getSelectionBackground());
                jcomponent.setForeground(table.getSelectionForeground());
            }
        } else {
            jcomponent.setForeground(table.getForeground());
            Color background = UIManager.getDefaults().getColor("Table:\"Table.cellRenderer\".background");
            if (row % 2 == 1) {
                Color alternateColor = UIManager.getDefaults().getColor("Table.alternateRowColor");
                if (alternateColor != null) {
                    background = alternateColor;
                }
            }
            jcomponent.setBackground(background);
        }

        com.haulmont.cuba.gui.components.Component cellComponent =
                (com.haulmont.cuba.gui.components.Component) jcomponent.getClientProperty(CELL_COMPONENT);
        if (cellComponent instanceof DesktopAbstractField) {
            ((DesktopAbstractField) cellComponent).updateMissingValueState();
        }

        jcomponent.setFont(table.getFont());

        assignBorder(table, isSelected, hasFocus, jcomponent);
    }

    private boolean isTextField(JComponent jcomponent) {
        if (jcomponent instanceof JTextField) {
            return true;
        }
        if (jcomponent instanceof JPanel) {
            Component[] panelChildren = jcomponent.getComponents();
            if ((panelChildren.length == 1 && panelChildren[0] instanceof JTextField)) {
                return true;
            }
        }
        return false;
    }

    private boolean isLookupField(JComponent jcomponent) {
        if (jcomponent instanceof JComboBox) {
            return true;
        }
        if (jcomponent instanceof JPanel) {
            Component[] panelChildren = jcomponent.getComponents();
            if ((panelChildren.length == 1 && panelChildren[0] instanceof JComboBox)) {
                return true;
            }
        }
        return false;
    }

    private boolean isDateField(JComponent jcomponent) {
        if (jcomponent instanceof JComboBox) {
            return true;
        }
        if (jcomponent instanceof JPanel) {
            Component[] panelChildren = jcomponent.getComponents();
            if ((panelChildren.length == 2 && panelChildren[0] instanceof DatePicker)) {
                return true;
            }
        }
        return false;
    }

    private void assignBorder(JTable table, boolean isSelected, boolean hasFocus, JComponent jcomponent) {
        if (isTextField(jcomponent)) {
            // looks like simple label when with empty border
        } else if (border != null) {
            jcomponent.setBorder(border);
        } else if (isLookupField(jcomponent) || isDateField(jcomponent) || jcomponent instanceof JXHyperlink) {
            // empty borders for fields except text fields in tables
            jcomponent.setBorder(new EmptyBorder(0, 0, 0, 0));
        } else {
            if (hasFocus) {
                Border border = null;
                if (isSelected) {
                    border = UIManager.getDefaults().getBorder("Table.focusSelectedCellHighlightBorder");
                }
                if (border == null) {
                    border = UIManager.getDefaults().getBorder("Table.focusCellHighlightBorder");
                }
                jcomponent.setBorder(border);
            } else {
                jcomponent.setBorder(getNoFocusBorder(jcomponent, table));
            }
        }
    }

    private Border getNoFocusBorder(JComponent jcomponent, JTable table) {
        return UIManager.getDefaults().getBorder("Table.cellNoFocusBorder");
    }
}