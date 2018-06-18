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
package com.haulmont.cuba.web.widgets.client.addons.contextmenu;

import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.haulmont.cuba.web.widgets.addons.contextmenu.ContextMenu;
import com.haulmont.cuba.web.widgets.client.addons.contextmenu.MenuSharedState.MenuItemState;
import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.WidgetUtil;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.extensions.AbstractExtensionConnector;
import com.vaadin.client.ui.Icon;
import com.vaadin.client.ui.VMenuBar;
import com.vaadin.client.ui.VMenuBar.CustomMenuItem;
import com.vaadin.shared.ui.Connect;

@SuppressWarnings("serial")
@Connect(ContextMenu.class)
public class ContextMenuConnector extends AbstractExtensionConnector {
    @SuppressWarnings("unused")
    private static Logger logger = Logger.getLogger("ContextMenuConnector");

    // TODO: make it so that we don't need this dummy root menu bar.
    private MyVMenuBar dummyRootMenuBar;
    private MyVMenuBar contextMenuWidget;

    @Override
    public MenuSharedState getState() {
        return (MenuSharedState) super.getState();
    }

    @Override
    public void onStateChanged(StateChangeEvent stateChangeEvent) {
        super.onStateChanged(stateChangeEvent);
        contextMenuWidget.clearItems();
        addMenuItemsFromState(contextMenuWidget, getState().menuItems);
    }

    @Override
    protected void init() {
        super.init();

        dummyRootMenuBar = GWT.create(MyVMenuBar.class);

        CustomMenuItem item = GWT.create(CustomMenuItem.class);
        dummyRootMenuBar.getItems().add(item);

        contextMenuWidget = new MyVMenuBar(true, dummyRootMenuBar);
        item.setSubMenu(contextMenuWidget);

        // application connection that is used for all our overlays
        MyVOverlay.setApplicationConnection(this.getConnection());

        registerRpc(ContextMenuClientRpc.class, new ContextMenuClientRpc() {
            @Override
            public void showContextMenu(int x, int y) {
                showMenu(x, y);
            }
        });

        Event.addNativePreviewHandler(new NativePreviewHandler() {
            @Override
            public void onPreviewNativeEvent(NativePreviewEvent event) {
                if (event.getTypeInt() == Event.ONKEYDOWN
                        && contextMenuWidget.isPopupShowing()) {
                    boolean handled = contextMenuWidget.handleNavigation(
                            event.getNativeEvent().getKeyCode(),
                            event.getNativeEvent().getCtrlKey(),
                            event.getNativeEvent().getShiftKey());

                    if (handled) {
                        event.cancel();
                    }
                }
            }
        });
    }

    private void addMenuItemsFromState(VMenuBar menuToAddTo,
            List<MenuItemState> menuItems) {
        if (menuItems == null)
            return;

        for (MenuItemState menuItemState : menuItems) {
            CustomMenuItem newItem = addMenuItemToMenu(menuToAddTo,
                    menuItemState);

            if (menuItemState.childItems != null
                    && menuItemState.childItems.size() > 0) {
                VMenuBar subMenu = new MyVMenuBar(true, menuToAddTo);
                addMenuItemsFromState(subMenu, menuItemState.childItems);
                newItem.setSubMenu(subMenu);
            }
        }
    }

    private CustomMenuItem addMenuItemToMenu(VMenuBar menuToAddTo,
            final MenuItemState menuItemState) {
        String itemText = buildItemHTML(menuItemState,
                getState().htmlContentAllowed, getConnection());
        CustomMenuItem item = menuToAddTo.addItem(itemText, new Command() {
            @Override
            public void execute() {
                if (contextMenuWidget.isAttached()) {
                    dummyRootMenuBar.hideChildren();
                    itemSelected(menuItemState.id);
                }
            }
        });

        updateMenuItemFromState(item, menuItemState);

        return item;
    }

    private void updateMenuItemFromState(CustomMenuItem item,
            MenuItemState state) {
        item.setEnabled(state.enabled);
        item.setCheckable(state.checkable);
        item.setChecked(state.checked);
        item.setStyleName(state.styleName);
        if (state.description != null) {
                    item.getElement()
                            .setAttribute("title", state.description);
            }
        if (item instanceof VMenuItem) { // TODO: when these are added, the
                                         // condition must be removed
            ((VMenuItem) item).setSeparator(state.separator);
            ((VMenuItem) item).setDescription(state.description);
        }
    }

    // TODO adapted from VMenuBar.buildItemHTML, must be removed/refactored asap
    private static String buildItemHTML(MenuItemState state,
            boolean htmlContentAllowed, ApplicationConnection connection) {
        // Construct html from the text and the optional icon
        StringBuffer itemHTML = new StringBuffer();
        if (state.separator) {
            itemHTML.append("<span>---</span>");
        } else {
            // Add submenu indicator
            if (state.childItems != null && state.childItems.size() > 0) {
                itemHTML.append(
                        "<span class=\"v-menubar-submenu-indicator\">&#x25BA;</span>");
            }

            itemHTML.append("<span class=\"v-menubar-menuitem-caption\">");

            if (state.icon != null) {
                Icon icon = connection.getIcon(state.icon.getURL());
                if (icon != null) {
                    itemHTML.append(icon.getElement().getString());
                }
            }

            String itemText = state.text;
            if (!htmlContentAllowed) {
                itemText = WidgetUtil.escapeHTML(itemText);
            }
            itemHTML.append(itemText);
            itemHTML.append("</span>");
        }
        return itemHTML.toString();
    }

    protected void itemSelected(int id) {
        getRpcProxy(ContextMenuServerRpc.class).itemClicked(id, true);
    }

    private void showMenu(int eventX, int eventY) {
        CustomMenuItem firstItem = dummyRootMenuBar.getItems().get(0);
        if(contextMenuWidget.getItems().size()==0){
            return;
        }
        dummyRootMenuBar.setSelected(firstItem);
        dummyRootMenuBar.showChildMenuAt(firstItem, eventY, eventX);
    }

    @Override
    protected void extend(ServerConnector target) {
        Logger.getLogger("ContextMenuConnector").info("extend");

        // Widget widget = ((AbstractComponentConnector) target).getWidget();

        // widget.addDomHandler(new ContextMenuHandler() {
        //
        // @Override
        // public void onContextMenu(ContextMenuEvent event) {
        // event.stopPropagation();
        // event.preventDefault();
        //
        // showMenu(event.getNativeEvent().getClientX(), event
        // .getNativeEvent().getClientY());
        // }
        // }, ContextMenuEvent.getType());

        // widget.addDomHandler(new KeyDownHandler() {
        // @Override
        // public void onKeyDown(KeyDownEvent event) {
        // // FIXME: check if menu is shown or handleNavigation will do it?
        //
        // boolean handled = contextMenuWidget.handleNavigation(event
        // .getNativeEvent().getKeyCode(), event.getNativeEvent()
        // .getCtrlKey(), event.getNativeEvent().getShiftKey());
        //
        // if (handled) {
        // event.stopPropagation();
        // event.preventDefault();
        // }
        // }
        // }, KeyDownEvent.getType());
    }
}
