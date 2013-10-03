/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.tree;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.vaadin.client.ui.ShortcutActionHandler;
import com.vaadin.client.ui.VTree;

/**
 * @author devyatkin
 * @version $Id$
 */
public class CubaTreeWidget extends VTree implements ShortcutActionHandler.ShortcutActionHandlerOwner {

    protected ShortcutActionHandler shortcutHandler;

    protected boolean contextMenuHandling = false;

    @Override
    public ShortcutActionHandler getShortcutActionHandler() {
        return shortcutHandler;
    }

    public void setShortcutActionHandler(ShortcutActionHandler handler) {
        shortcutHandler = handler;
    }

    @Override
    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);

        final int type = DOM.eventGetType(event);
        if (type == Event.ONKEYDOWN && shortcutHandler != null) {
            shortcutHandler.handleKeyboardEvent(event);
        }
    }

    public class CubaTreeNode extends TreeNode {
        @Override
        public void showContextMenu(Event event) {
            if (!readonly && !disabled && actionKeys != null) {
                selectNodeForContextMenu();
                super.showContextMenu(event);
            }
        }

        protected void selectNodeForContextMenu() {
            client.updateVariable(getPaintableId(), "popupSelection", true, false);
            contextMenuHandling = true;
            handleClickSelection(false, false);
        }
    }

    public void setContextMenuHandling(boolean contextMenuHandling) {
        this.contextMenuHandling = contextMenuHandling;
    }

    public boolean isSelected(TreeNode treeNode) {
        return selectedIds.contains(treeNode.key) && !contextMenuHandling;
    }
}
