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
import java.util.List;

import com.vaadin.server.Resource;

public interface Menu extends Serializable {

    /**
     * This interface contains the layer for menu commands of the
     * {@link com.vaadin.ui.MenuBar} class. It's method will fire when the user
     * clicks on the containing {@link MenuItem}. The selected item is given as
     * an argument.
     */
    public interface Command extends Serializable {
        public void menuSelected(MenuItem selectedItem);
    }

    /**
     * Add a new item to the menu bar. Command can be null, but a caption must
     * be given.
     * 
     * @param caption
     *            the text for the menu item
     * @param command
     *            the command for the menu item
     * @throws IllegalArgumentException
     */
    public abstract MenuItem addItem(String caption, Command command);

    /**
     * Add a new item to the menu bar. Icon and command can be null, but a
     * caption must be given.
     * 
     * @param caption
     *            the text for the menu item
     * @param icon
     *            the icon for the menu item
     * @param command
     *            the command for the menu item
     * @throws IllegalArgumentException
     */
    public abstract MenuItem addItem(String caption, Resource icon,
                                     Command command);

    /**
     * Add an item before some item. If the given item does not exist the item
     * is added at the end of the menu. Icon and command can be null, but a
     * caption must be given.
     * 
     * @param caption
     *            the text for the menu item
     * @param icon
     *            the icon for the menu item
     * @param command
     *            the command for the menu item
     * @param itemToAddBefore
     *            the item that will be after the new item
     * @throws IllegalArgumentException
     */
    public abstract MenuItem addItemBefore(String caption, Resource icon,
                                           Command command, MenuItem itemToAddBefore);

    /**
     * Returns a list with all the MenuItem objects in the menu bar
     * 
     * @return a list containing the MenuItem objects in the menu bar
     */
    public abstract List<MenuItem> getItems();

    /**
     * Remove first occurrence the specified item from the main menu
     * 
     * @param item
     *            The item to be removed
     */
    public abstract void removeItem(MenuItem item);

    /**
     * Empty the menu bar
     */
    public abstract void removeItems();

    /**
     * Returns the size of the menu.
     * 
     * @return The size of the menu
     */
    public abstract int getSize();

    /**
     * Sets whether html is allowed in the item captions. If set to true, the
     * captions are passed to the browser as html and the developer is
     * responsible for ensuring no harmful html is used. If set to false, the
     * content is passed to the browser as plain text.
     * 
     * @param htmlContentAllowed
     *            true if the captions are used as html, false if used as plain
     *            text
     */
    public abstract void setHtmlContentAllowed(boolean htmlContentAllowed);

    /**
     * Checks whether item captions are interpreted as html or plain text.
     * 
     * @return true if the captions are used as html, false if used as plain
     *         text
     * @see #setHtmlContentAllowed(boolean)
     */
    public abstract boolean isHtmlContentAllowed();

}