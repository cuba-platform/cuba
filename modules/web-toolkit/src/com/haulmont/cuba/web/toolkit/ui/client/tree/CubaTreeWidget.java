/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.tree;

import com.google.gwt.core.client.Scheduler;
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

    protected boolean doubleClickHandling = false;

    protected long lastDoubleClickHandled = 0;

    protected boolean doubleClickMode = false;

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

        @Override
        protected void toggleSelection() {
            if (!doubleClickMode) {
                super.toggleSelection();
            } else {
                if (selectable) {
                    CubaTreeWidget.this.setSelected(this, doubleClickHandling || !isSelected());
                }
            }
        }

        @Override
        protected void fireClick(Event evt) {
            doubleClickHandling = doubleClickMode && DOM.eventGetType(evt) == Event.ONDBLCLICK;

            super.fireClick(evt);
        }

        @Override
        protected boolean isNeedToHandleClick() {
            if (!doubleClickMode) {
                return super.isNeedToHandleClick();
            } else {
                return !doubleClickHandling;
            }
        }

        @Override
        protected void executeEventCommand(final Scheduler.ScheduledCommand command) {
            if (!doubleClickMode || doubleClickHandling) {
                super.executeEventCommand(command);
            } else {
                Scheduler.get().scheduleFixedDelay(new Scheduler.RepeatingCommand() {

                    private long scheduledTimestamp = System.currentTimeMillis();

                    @Override
                    public boolean execute() {
                        if (!doubleClickHandling && lastDoubleClickHandled < scheduledTimestamp) {
                            command.execute();
                        }

                        return false;
                    }
                }, 250);
            }
        }

        @Override
        protected void prepareToFireClick(int eventType) {
            if (eventType == Event.ONDBLCLICK && doubleClickHandling) {
                if (selectable) {
                    CubaTreeWidget.this.deselectAll();

                    TreeNode targetNode = getNodeByKey(key);
                    targetNode.setFocused(true);

                    targetNode.setSelected(true);
                    selectedIds.add(targetNode.key);

                    client.updateVariable(paintableId, "selected",
                            selectedIds.toArray(new String[selectedIds.size()]),
                            false);
                    selectionHasChanged = false;
                }
            }
        }

        @Override
        protected boolean isNeedToSendDoubleClick(int eventType, boolean sendClickEventNow) {
            if (eventType != Event.ONDBLCLICK || !doubleClickMode) {
                return super.isNeedToSendDoubleClick(eventType, sendClickEventNow);
            } else {
                return doubleClickHandling;
            }
        }
    }

    public void setDoubleClickHandling(boolean doubleClickHandling) {
        this.doubleClickHandling = doubleClickHandling;

        lastDoubleClickHandled = System.currentTimeMillis();
    }

    public void setContextMenuHandling(boolean contextMenuHandling) {
        this.contextMenuHandling = contextMenuHandling;
    }

    public boolean isSelected(TreeNode treeNode) {
        return selectedIds.contains(treeNode.key) && !contextMenuHandling;
    }
}