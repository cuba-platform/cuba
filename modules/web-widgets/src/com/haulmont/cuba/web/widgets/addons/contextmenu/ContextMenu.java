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

import com.haulmont.cuba.web.widgets.client.addons.contextmenu.ContextMenuClientRpc;
import com.haulmont.cuba.web.widgets.client.addons.contextmenu.ContextMenuItemState;
import com.haulmont.cuba.web.widgets.client.addons.contextmenu.ContextMenuServerRpc;
import com.haulmont.cuba.web.widgets.client.addons.contextmenu.ContextMenuState;
import com.vaadin.event.ContextClickEvent;
import com.vaadin.event.ContextClickEvent.ContextClickListener;
import com.vaadin.event.ContextClickEvent.ContextClickNotifier;
import com.vaadin.server.AbstractExtension;
import com.vaadin.server.Extension;
import com.vaadin.server.Resource;
import com.vaadin.server.ResourceReference;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.UI;
import com.vaadin.util.ReflectTools;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class ContextMenu extends AbstractExtension {
    private MenuBar innerMenuBar = new MenuBar() {
        @Override
        protected void addExtension(Extension extension) {
            ContextMenu.this.addExtension(extension);
        }
    };
    private MenuItem rootItem = innerMenuBar.addItem("");

    private ContextClickListener contextClickListener = new ContextClickListener() {
        @Override
        public void contextClick(ContextClickEvent event) {
            fireEvent(new ContextMenuOpenListener.ContextMenuOpenEvent(ContextMenu.this, event));

            open(event.getClientX(), event.getClientY());
        }
    };
    private Map<Integer, MenuItem> itemById = Collections.emptyMap();

    @Override
    protected ContextMenuState getState(boolean markAsDirty) {
        return (ContextMenuState) super.getState(markAsDirty);
    }

    @Override
    protected ContextMenuState getState() {
        return (ContextMenuState) super.getState();
    }

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
            public void itemClicked(int itemId) {
                MenuItem clickedItem = itemById.get(itemId);
                if (clickedItem != null) {
                    if (clickedItem.isCheckable())
                        clickedItem.setChecked(!clickedItem.isChecked());

                    if (clickedItem.getCommand() != null)
                        clickedItem.getCommand().menuSelected(clickedItem);
                }
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
        UI uI = getUI();
        if (uI != null && uI.getConnectorTracker().isDirty(this)) {

            /*
             * This should also be used by MenuBar, upgrading it from Vaadin 6
             * to Vaadin 7 communication mechanism. Thus to be moved e.g. to the
             * AbstractMenu.
             */
            ContextMenuState menuSharedState = getState();
            itemById = new HashMap<>();
            menuSharedState.menuItems = convertItemsToState(getItems(),
                    itemById);
        }
    }

    public void open(int x, int y) {
        if (rootItem.hasChildren()) {
            getRpcProxy(ContextMenuClientRpc.class).showContextMenu(x, y);
        }
    }

    private List<ContextMenuItemState> convertItemsToState(List<MenuItem> items,
                                                           Map<Integer, MenuItem> itemRegistry) {
        if (items == null || items.size() == 0) {
            return null;
        }

        List<ContextMenuItemState> stateItems = new ArrayList<>(items.size());

        for (MenuItem item : items) {
            ContextMenuItemState menuItemState = new ContextMenuItemState();

            if (!item.isVisible()) {
                continue;
            }

            menuItemState.id = item.getId();
            menuItemState.text = item.getText();
            menuItemState.checkable = item.isCheckable();
            menuItemState.command = item.getCommand() != null;
            menuItemState.checked = item.isChecked();
            menuItemState.description = item.getDescription();
            menuItemState.descriptionContentMode = item
                    .getDescriptionContentMode();
            menuItemState.enabled = item.isEnabled();
            menuItemState.separator = item.isSeparator();
            menuItemState.icon = ResourceReference.create(item.getIcon(), this,
                    "");
            menuItemState.styleName = item.getStyleName();

            menuItemState.childItems = convertItemsToState(item.getChildren(),
                    itemRegistry);

            stateItems.add(menuItemState);
            itemRegistry.put(item.getId(), item);
        }

        return stateItems;
    }

    protected ContextClickListener getContextClickListener() {
        return contextClickListener;
    }

    public MenuItem addSeparator() {
        return rootItem.addSeparator();
    }

    public MenuItem addSeparatorBefore(MenuItem itemToAddBefore) {
        return rootItem.addSeparatorBefore(itemToAddBefore);
    }

    public MenuItem addItem(String caption) {
        return rootItem.addItem(caption);
    }

    public MenuItem addItem(String caption, Command command) {
        return rootItem.addItem(caption, command);
    }

    public MenuItem addItem(String caption, Resource icon, Command command) {
        return rootItem.addItem(caption, icon, command);
    }

    public MenuItem addItemBefore(String caption, Resource icon,
                                  Command command, MenuItem itemToAddBefore) {
        return rootItem.addItemBefore(caption, icon, command, itemToAddBefore);
    }

    public List<MenuItem> getItems() {
        return rootItem.getChildren();
    }

    public void removeItem(MenuItem item) {
        rootItem.removeChild(item);
    }

    public void removeItems() {
        rootItem.removeChildren();
    }

    public int getSize() {
        return rootItem.getSize();
    }

    public void setHtmlContentAllowed(boolean htmlContentAllowed) {
        getState().htmlContentAllowed = htmlContentAllowed;
        innerMenuBar.setHtmlContentAllowed(htmlContentAllowed);
    }

    public boolean isHtmlContentAllowed() {
        return getState(false).htmlContentAllowed;
    }

    public interface ContextMenuOpenListener extends java.util.EventListener, java.io.Serializable {

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
