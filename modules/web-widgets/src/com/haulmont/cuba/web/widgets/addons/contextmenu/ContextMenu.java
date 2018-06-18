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

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.EventObject;
import java.util.List;

import com.haulmont.cuba.web.widgets.client.addons.contextmenu.ContextMenuClientRpc;
import com.haulmont.cuba.web.widgets.client.addons.contextmenu.ContextMenuServerRpc;
import com.haulmont.cuba.web.widgets.client.addons.contextmenu.MenuSharedState;
import com.haulmont.cuba.web.widgets.client.addons.contextmenu.MenuSharedState.MenuItemState;
import com.vaadin.event.ContextClickEvent;
import com.vaadin.event.ContextClickEvent.ContextClickListener;
import com.vaadin.event.ContextClickEvent.ContextClickNotifier;
import com.vaadin.server.AbstractExtension;
import com.vaadin.server.Resource;
import com.vaadin.server.ResourceReference;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;
import com.vaadin.util.ReflectTools;

@SuppressWarnings("serial")
public class ContextMenu extends AbstractExtension implements Menu {

    private AbstractMenu menu = new AbstractMenu();

    private ContextClickListener contextClickListener = new ContextClickListener() {
        @Override
        public void contextClick(ContextClickEvent event) {
            fireEvent(new ContextMenuOpenListener.ContextMenuOpenEvent(ContextMenu.this, event));

            open(event.getClientX(), event.getClientY());
        }
    };

    /**
     * @param parentComponent
     *            The component to whose lifecycle the context menu is tied to.
     * @param setAsMenuForParentComponent
     *            Determines if this menu will be shown for the parent
     *            component.
     */
    public ContextMenu(AbstractComponent parentComponent,
            boolean setAsMenuForParentComponent) {
        extend(parentComponent);

        registerRpc(new ContextMenuServerRpc() {
            @Override
            public void itemClicked(int itemId, boolean menuClosed) {
                menu.itemClicked(itemId);
            }
        });

        if (setAsMenuForParentComponent) {
            setAsContextMenuOf(parentComponent);
        }
    }

    /**
     * Sets this as a context menu of the component. You can set one menu to as
     * many components as you wish.
     *
     * @param component
     *            the component to set the context menu to
     */
    public void setAsContextMenuOf(ContextClickNotifier component) {
        component.addContextClickListener(contextClickListener);
    }

    public void addContextMenuOpenListener(
            ContextMenuOpenListener contextMenuComponentListener) {
        addListener(ContextMenuOpenListener.ContextMenuOpenEvent.class, contextMenuComponentListener,
                ContextMenuOpenListener.MENU_OPENED);
    }

    @Override
    public void beforeClientResponse(boolean initial) {
        super.beforeClientResponse(initial);

        // FIXME: think about where this is supposed to be

        /*
         * This should also be used by MenuBar, upgrading it from Vaadin 6 to
         * Vaadin 7 communication mechanism. Thus to be moved e.g. to the
         * AbstractMenu.
         */
        MenuSharedState menuSharedState = getState();
        menuSharedState.htmlContentAllowed = isHtmlContentAllowed();
        menuSharedState.menuItems = convertItemsToState(getItems());
    }

    public void open(int x, int y) {
        getRpcProxy(ContextMenuClientRpc.class).showContextMenu(x, y);
    }

    private List<MenuItemState> convertItemsToState(List<MenuItem> items) {
        if (items == null || items.size() == 0) {
            return null;
        }

        List<MenuItemState> state = new ArrayList<>();

        for (MenuItem item : items) {
            MenuItemState menuItemState = new MenuItemState();

            if (!item.isVisible()) {
                continue;
            }

            menuItemState.id = item.getId();
            menuItemState.text = item.getText();
            menuItemState.checkable = item.isCheckable();
            menuItemState.checked = item.isChecked();
            menuItemState.description = item.getDescription();
            menuItemState.enabled = item.isEnabled();
            menuItemState.separator = item.isSeparator();
            menuItemState.icon = ResourceReference.create(item.getIcon(), this,
                    "");
            menuItemState.styleName = item.getStyleName();

            menuItemState.childItems = convertItemsToState(item.getChildren());

            state.add(menuItemState);
        }

        return state;
    }

    @Override
    protected MenuSharedState getState() {
        return (MenuSharedState) super.getState();
    }

    protected ContextClickListener getContextClickListener() {
        return contextClickListener;
    }

    // Should these also be in MenuInterface and then throw exception for
    // MenuBar?
    public MenuItem addSeparator() {
        // FIXME: this is a wrong way
        MenuItemImpl item = (MenuItemImpl) addItem("", null);
        item.setSeparator(true);
        return item;
    }

    public MenuItem addSeparatorBefore(MenuItem itemToAddBefore) {
        // FIXME: this is a wrong way
        MenuItemImpl item = (MenuItemImpl) addItemBefore("", null, null,
                itemToAddBefore);
        item.setSeparator(true);
        return item;
    }

    /**** Delegates to AbstractMenu ****/

    @Override
    public MenuItem addItem(String caption, Command command) {
        return menu.addItem(caption, command);
    }

    @Override
    public MenuItem addItem(String caption, Resource icon, Command command) {
        return menu.addItem(caption, icon, command);
    }

    @Override
    public MenuItem addItemBefore(String caption, Resource icon,
            Command command, MenuItem itemToAddBefore) {
        return menu.addItemBefore(caption, icon, command, itemToAddBefore);
    }

    @Override
    public List<MenuItem> getItems() {
        return menu.getItems();
    }

    @Override
    public void removeItem(MenuItem item) {
        menu.removeItem(item);
    }

    @Override
    public void removeItems() {
        menu.removeItems();
    }

    @Override
    public int getSize() {
        return menu.getSize();
    }

    @Override
    public void setHtmlContentAllowed(boolean htmlContentAllowed) {
        menu.setHtmlContentAllowed(htmlContentAllowed);
    }

    @Override
    public boolean isHtmlContentAllowed() {
        return menu.isHtmlContentAllowed();
    }

    /**** End of delegates to AbstractMenu ****/

    public interface ContextMenuOpenListener
            extends EventListener, Serializable {

        public static final Method MENU_OPENED = ReflectTools.findMethod(
                ContextMenuOpenListener.class, "onContextMenuOpen",
                ContextMenuOpenEvent.class);

        public void onContextMenuOpen(ContextMenuOpenEvent event);

        public static class ContextMenuOpenEvent extends EventObject {
            private final ContextMenu contextMenu;

            private final int x;
            private final int y;

            private ContextClickEvent contextClickEvent;

            public ContextMenuOpenEvent(ContextMenu contextMenu,
                    ContextClickEvent contextClickEvent) {
                super(contextClickEvent.getComponent());

                this.contextMenu = contextMenu;
                this.contextClickEvent = contextClickEvent;
                x = contextClickEvent.getClientX();
                y = contextClickEvent.getClientY();
            }

            /**
             * @return ContextMenu that was opened.
             */
            public ContextMenu getContextMenu() {
                return contextMenu;
            }

            /**
             * @return Component which initiated the context menu open request.
             */
            public Component getSourceComponent() {
                return (Component) getSource();
            }

            /**
             * @return x-coordinate of open position.
             */
            public int getX() {
                return x;
            }

            /**
             * @return y-coordinate of open position.
             */
            public int getY() {
                return y;
            }

            public ContextClickEvent getContextClickEvent() {
                return contextClickEvent;
            }
        }
    }
}
