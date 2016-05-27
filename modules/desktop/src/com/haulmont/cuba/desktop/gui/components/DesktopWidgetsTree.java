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
import com.haulmont.cuba.desktop.gui.data.TreeModelAdapter;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.WidgetsTree;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeCellRenderer;
import java.util.EventObject;

public class DesktopWidgetsTree<E extends Entity>
        extends DesktopTree<E>
        implements WidgetsTree<E> {

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
        public java.awt.Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected,
                                                             boolean expanded, boolean leaf, int row) {
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