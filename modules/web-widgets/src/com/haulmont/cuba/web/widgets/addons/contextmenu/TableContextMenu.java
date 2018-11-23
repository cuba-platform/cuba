/*
 * Copyright 2000-2018 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.haulmont.cuba.web.widgets.addons.contextmenu;

import com.vaadin.event.ContextClickEvent;
import com.vaadin.event.ContextClickEvent.ContextClickNotifier;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.shared.MouseEventDetails.MouseButton;
import com.vaadin.v7.event.ItemClickEvent;
import com.vaadin.v7.event.ItemClickEvent.ItemClickListener;
import com.vaadin.v7.ui.Table;
import com.vaadin.v7.ui.TreeTable;

/**
 * Compatibility version of ContextMenu to use with v7.Table and v7.TreeTable in
 * the Framework 8.0 compatibility package.
 * <p>
 * This compatibility version exists to fix #29 using context menu in table &
 * tree table with item click listener.
 *
 * @deprecated To use only for compatibility v7.Table
 */
@Deprecated
public class TableContextMenu extends ContextMenu {

    /**
     * Constructs a context menu and sets it for the given table.
     *
     * @param table
     *            the table to set the context menu to
     */
    public TableContextMenu(Table table) {
        super(table, true);
    }

    /**
     * Constructs a context menu and sets it for the given tree table.
     *
     * @param treeTable
     *            the tree table to set the context menu to
     */
    public TableContextMenu(TreeTable treeTable) {
        super(treeTable, true);
    }

    /**
     * Sets this as a context menu of the component. You can set one menu to as
     * many components as you wish.
     *
     * @param component
     *            the component to set the context menu to
     */
    @Override
    public void setAsContextMenuOf(ContextClickNotifier component) {
        /*
         * Workaround for VScrollTable click handling, which prevents context
         * clicks from rows when ItemClickListener has been added. (#29)
         */
        if (component instanceof Table) {
            useTableSpecificContextClickListener((Table) component);
            // For context clicks outside rows (header, footer, body) we still
            // need the context click listener.
        }
        super.setAsContextMenuOf(component);
    }

    private void useTableSpecificContextClickListener(final Table table) {
        table.addItemClickListener(new ItemClickListener() {

            @Override
            public void itemClick(ItemClickEvent event) {
                if (event.getButton() == MouseButton.RIGHT) {
                    MouseEventDetails mouseEventDetails = new MouseEventDetails();
                    mouseEventDetails.setAltKey(event.isAltKey());
                    mouseEventDetails.setButton(event.getButton());
                    mouseEventDetails.setClientX(event.getClientX());
                    mouseEventDetails.setClientY(event.getClientY());
                    mouseEventDetails.setCtrlKey(event.isCtrlKey());
                    mouseEventDetails.setMetaKey(event.isMetaKey());
                    mouseEventDetails.setRelativeX(event.getRelativeX());
                    mouseEventDetails.setRelativeY(event.getRelativeY());
                    mouseEventDetails.setShiftKey(event.isShiftKey());
                    if (event.isDoubleClick()) {
                        mouseEventDetails.setType(0x00002);
                    } else {
                        mouseEventDetails.setType(0x00001);
                    }

                    getContextClickListener().contextClick(
                            new ContextClickEvent(table, mouseEventDetails));
                }
            }
        });
    }

}
