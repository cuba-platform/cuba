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

package com.haulmont.cuba.web.toolkit.ui.client.tree;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.haulmont.cuba.web.toolkit.ui.client.Tools;
import com.vaadin.client.BrowserInfo;
import com.vaadin.client.ConnectorMap;
import com.vaadin.client.ui.ShortcutActionHandler;
import com.vaadin.client.ui.VOverlay;
import com.vaadin.client.ui.VTree;

public class CubaTreeWidget extends VTree implements ShortcutActionHandler.ShortcutActionHandlerOwner {

    protected ShortcutActionHandler shortcutHandler;

    protected boolean doubleClickHandling = false;

    protected long lastDoubleClickHandled = 0;

    protected boolean doubleClickMode = false;

    protected boolean nodeCaptionsAsHtml = false;

    protected VOverlay customContextMenuPopup;
    protected Widget customContextMenu;

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

    protected CubaTreeConnector getConnector() {
        return (CubaTreeConnector) ConnectorMap.get(client).getConnector(this);
    }

    @Override
    protected void applySelectionCommand(final Scheduler.ScheduledCommand command) {
        if (!doubleClickMode || doubleClickHandling) {
        super.applySelectionCommand(command);
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
    protected Class<? extends Widget> getTreeNodeClass() {
        return CubaTreeNode.class;
    }

    public class CubaTreeNode extends TreeNode {

        @Override
        public void showContextMenu(Event event) {
            if (!readonly && !disabled) {
                if (!isSelected()) {
                    getConnector().setContextMenuSelection(true);

                    toggleSelection();
                }
                if (customContextMenu == null) {
                    super.showContextMenu(event);
                } else {
                    int left = event.getClientX();
                    int top = event.getClientY();
                    top += Window.getScrollTop();
                    left += Window.getScrollLeft();

                    showContextMenuPopup(left, top);

                    event.stopPropagation();
                    event.preventDefault();
                }
            }
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

        @Override
        public void setText(String text) {
            if (!nodeCaptionsAsHtml) {
                super.setText(text);
            } else {
                if (nodeCaptionSpan != null) {
                    nodeCaptionSpan.setInnerHTML(text);
                }
            }
        }

        @Override
        public boolean isCaptionElement(Element target) {
            return super.isCaptionElement(target) || nodeCaptionSpan.isOrHasChild(target);
        }
    }

    public void setDoubleClickHandling(boolean doubleClickHandling) {
        this.doubleClickHandling = doubleClickHandling;

        lastDoubleClickHandled = System.currentTimeMillis();
    }

    @Override
    public void onFocus(FocusEvent event) {
        super.onFocus(event);

        addStyleDependentName("focus");
    }

    @Override
    public void onBlur(BlurEvent event) {
        super.onBlur(event);

        removeStyleDependentName("focus");
    }

    @Override
    protected void onDetach() {
        super.onDetach();

        if (customContextMenuPopup != null) {
            customContextMenuPopup.hide();
        }
    }

    @Override
    protected void handleBodyContextMenu(ContextMenuEvent event) {
        if (customContextMenu == null) {
            super.handleBodyContextMenu(event);
        } else if (!selectedIds.isEmpty()) {
            int left = event.getNativeEvent().getClientX();
            int top = event.getNativeEvent().getClientY();
            top += Window.getScrollTop();
            left += Window.getScrollLeft();

            showContextMenuPopup(left, top);

            event.stopPropagation();
            event.preventDefault();
        }
    }

    protected void showContextMenuPopup(int left, int top) {
        if (customContextMenu instanceof HasWidgets) {
            if (!((HasWidgets) customContextMenu).iterator().hasNext()) {
                // there are no actions to show
                return;
            }
        }

        customContextMenuPopup = Tools.createCubaTableContextMenu();
        customContextMenuPopup.setOwner(this);
        customContextMenuPopup.setWidget(customContextMenu);

        customContextMenuPopup.addCloseHandler(new CloseHandler<PopupPanel>() {
            @Override
            public void onClose(CloseEvent<PopupPanel> event) {
                customContextMenuPopup = null;
            }
        });

        Tools.showPopup(customContextMenuPopup, left, top);
    }

    @Override
    protected boolean isAllowSingleSelectToggle() {
        return BrowserInfo.get().isTouchDevice()&& Tools.isUseSimpleMultiselectForTouchDevice();
    }
}