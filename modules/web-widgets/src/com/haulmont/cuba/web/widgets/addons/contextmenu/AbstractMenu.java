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

import java.util.ArrayList;
import java.util.List;

import com.vaadin.server.ClientConnector;
import com.vaadin.server.Resource;

public class AbstractMenu implements Menu {

    private final List<MenuItem> menuItems = new ArrayList<MenuItem>();
    private boolean htmlContentAllowed;
    private ClientConnector connector;

    private void markAsDirty() {
        // FIXME check if it can be removed at all, or find a way to call it
        // when needed
        if (connector != null)
            connector.markAsDirty();
    }

    public AbstractMenu() {
    }

    public AbstractMenu(ClientConnector connector) {
        this.connector = connector;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.example.contextmenu.Menu#addItem(java.lang.String,
     * com.example.contextmenu.AbstractMenu.Command)
     */
    @Override
    public MenuItem addItem(String caption, Command command) {
        return addItem(caption, null, command);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.example.contextmenu.Menu#addItem(java.lang.String,
     * com.vaadin.server.Resource, com.example.contextmenu.AbstractMenu.Command)
     */
    @Override
    public MenuItem addItem(String caption, Resource icon, Command command) {
        if (caption == null) {
            throw new IllegalArgumentException("caption cannot be null");
        }
        MenuItem newItem = new MenuItemImpl(caption, icon, command);
        menuItems.add(newItem);
        markAsDirty();

        return newItem;

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.example.contextmenu.Menu#addItemBefore(java.lang.String,
     * com.vaadin.server.Resource, com.example.contextmenu.AbstractMenu.Command,
     * com.example.contextmenu.AbstractMenu.MenuItem)
     */
    @Override
    public MenuItem addItemBefore(String caption, Resource icon,
            Command command, MenuItem itemToAddBefore) {
        if (caption == null) {
            throw new IllegalArgumentException("caption cannot be null");
        }

        MenuItem newItem = new MenuItemImpl(caption, icon, command);
        if (menuItems.contains(itemToAddBefore)) {
            int index = menuItems.indexOf(itemToAddBefore);
            menuItems.add(index, newItem);

        } else {
            menuItems.add(newItem);
        }

        markAsDirty();

        return newItem;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.example.contextmenu.Menu#getItems()
     */
    @Override
    public List<MenuItem> getItems() {
        return menuItems;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.example.contextmenu.Menu#removeItem(com.example.contextmenu.
     * AbstractMenu .MenuItem)
     */
    @Override
    public void removeItem(MenuItem item) {
        if (item != null) {
            menuItems.remove(item);
        }
        markAsDirty();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.example.contextmenu.Menu#removeItems()
     */
    @Override
    public void removeItems() {
        menuItems.clear();
        markAsDirty();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.example.contextmenu.Menu#getSize()
     */
    @Override
    public int getSize() {
        return menuItems.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.example.contextmenu.Menu#setHtmlContentAllowed(boolean)
     */
    @Override
    public void setHtmlContentAllowed(boolean htmlContentAllowed) {
        this.htmlContentAllowed = htmlContentAllowed;
        markAsDirty();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.example.contextmenu.Menu#isHtmlContentAllowed()
     */
    @Override
    public boolean isHtmlContentAllowed() {
        return htmlContentAllowed;
    }

    void itemClicked(int itemId) {
        MenuItem clickedItem = findItemById(itemId);
        if (clickedItem != null) {
            if (clickedItem.isCheckable())
                clickedItem.setChecked(!clickedItem.isChecked());

            if (clickedItem.getCommand() != null)
                clickedItem.getCommand().menuSelected(clickedItem);
        }
    }

    private MenuItem findItemById(int id) {
        // TODO: create a map to avoid that?
        return findItemById(getItems(), id);
    }

    private MenuItem findItemById(List<MenuItem> items, int id) {
        if (items == null)
            return null;

        for (MenuItem item : items) {
            if (item.getId() == id)
                return item;
            else {
                MenuItem subItem = findItemById(item.getChildren(), id);
                if (subItem != null)
                    return subItem;
            }
        }

        return null;
    }
}
