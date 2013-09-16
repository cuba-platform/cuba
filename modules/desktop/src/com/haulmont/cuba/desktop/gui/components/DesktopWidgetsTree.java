/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.desktop.gui.data.TreeModelAdapter;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.WidgetsTree;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeCellRenderer;
import java.util.EventObject;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class DesktopWidgetsTree
    extends DesktopTree
    implements WidgetsTree
{
    @Override
    public void setWidgetBuilder(final WidgetBuilder widgetBuilder) {
        if (widgetBuilder == null)
            return;

        impl.setEditable(true);
        impl.setCellRenderer(new CellRenderer(widgetBuilder));
        impl.setCellEditor(new CellEditor(widgetBuilder));
    }

    private class CellEditor implements TreeCellEditor {

        private WidgetBuilder widgetBuilder;

        private CellEditor(WidgetBuilder widgetBuilder) {
            this.widgetBuilder = widgetBuilder;
        }

        @Override
        public java.awt.Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {
            Component component = widgetBuilder.build(
                    datasource,
                    ((TreeModelAdapter.Node) value).getEntity().getId(),
                    leaf
            );
            return DesktopComponentsHelper.getComposition(component);
        }

        @Override
        public Object getCellEditorValue() {
            return null;
        }

        @Override
        public boolean isCellEditable(EventObject anEvent) {
            return true;
        }

        @Override
        public boolean shouldSelectCell(EventObject anEvent) {
            return true;
        }

        @Override
        public boolean stopCellEditing() {
            return true;
        }

        @Override
        public void cancelCellEditing() {
        }

        @Override
        public void addCellEditorListener(CellEditorListener l) {
        }

        @Override
        public void removeCellEditorListener(CellEditorListener l) {
        }
    }

    private class CellRenderer implements TreeCellRenderer {

        private WidgetBuilder widgetBuilder;

        private CellRenderer(WidgetBuilder widgetBuilder) {
            this.widgetBuilder = widgetBuilder;
        }

        @Override
        public java.awt.Component getTreeCellRendererComponent(JTree tree, Object value,
                                                               boolean selected, boolean expanded,
                                                               boolean leaf, int row, boolean hasFocus)
        {
            Component component = widgetBuilder.build(
                    datasource,
                    ((TreeModelAdapter.Node) value).getEntity().getId(),
                    leaf
            );
            return DesktopComponentsHelper.getComposition(component);
        }
    }
}
