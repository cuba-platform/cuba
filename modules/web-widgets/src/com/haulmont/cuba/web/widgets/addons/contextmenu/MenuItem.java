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

import com.haulmont.cuba.web.widgets.addons.contextmenu.Menu.Command;
import com.vaadin.server.Resource;

public interface MenuItem extends Serializable {

    /**
     * Checks if the item has children (if it is a sub-menu).
     * 
     * @return True if this item has children
     */
    boolean hasChildren();

    /**
     * Adds a separator to this menu. A separator is a way to visually group
     * items in a menu, to make it easier for users to find what they are
     * looking for in the menu.
     * 
     * @author Jouni Koivuviita / Vaadin Ltd.
     * @since 6.2.0
     */
    MenuItem addSeparator();

    MenuItem addSeparatorBefore(MenuItem itemToAddBefore);

    /**
     * Add a new item inside this item, thus creating a sub-menu. Command can be
     * null, but a caption must be given.
     * 
     * @param caption
     *            the text for the menu item
     * @param command
     *            the command for the menu item
     */
    MenuItem addItem(String caption, Command command);

    /**
     * Add a new item inside this item, thus creating a sub-menu. Icon and
     * command can be null, but a caption must be given.
     * 
     * @param caption
     *            the text for the menu item
     * @param icon
     *            the icon for the menu item
     * @param command
     *            the command for the menu item
     * @throws IllegalStateException
     *             If the item is checkable and thus cannot have children.
     */
    MenuItem addItem(String caption, Resource icon, Command command)
            throws IllegalStateException;

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
     * @throws IllegalStateException
     *             If the item is checkable and thus cannot have children.
     */
    MenuItem addItemBefore(String caption, Resource icon, Command command,
                           MenuItem itemToAddBefore) throws IllegalStateException;

    /**
     * For the associated command.
     * 
     * @return The associated command, or null if there is none
     */
    Command getCommand();

    /**
     * Gets the objects icon.
     * 
     * @return The icon of the item, null if the item doesn't have an icon
     */
    Resource getIcon();

    /**
     * For the containing item. This will return null if the item is in the
     * top-level menu bar.
     * 
     * @return The containing {@link MenuItem} , or null if there is none
     */
    MenuItem getParent();

    /**
     * This will return the children of this item or null if there are none.
     * 
     * @return List of children items, or null if there are none
     */
    List<MenuItem> getChildren();

    /**
     * Gets the objects text
     * 
     * @return The text
     */
    String getText();

    /**
     * Returns the number of children.
     * 
     * @return The number of child items
     */
    int getSize();

    /**
     * Get the unique identifier for this item.
     * 
     * @return The id of this item
     */
    int getId();

    /**
     * Set the command for this item. Set null to remove.
     * 
     * @param command
     *            The MenuCommand of this item
     */
    void setCommand(Command command);

    /**
     * Sets the icon. Set null to remove.
     * 
     * @param icon
     *            The icon for this item
     */
    void setIcon(Resource icon);

    /**
     * Set the text of this object.
     * 
     * @param text
     *            Text for this object
     */
    void setText(String text);

    /**
     * Remove the first occurrence of the item.
     * 
     * @param item
     *            The item to be removed
     */
    void removeChild(MenuItem item);

    /**
     * Empty the list of children items.
     */
    void removeChildren();

    void setEnabled(boolean enabled);

    boolean isEnabled();

    void setVisible(boolean visible);

    boolean isVisible();

    boolean isSeparator();

    void setStyleName(String styleName);

    String getStyleName();

    /**
     * Sets the items's description. See {@link #getDescription()} for more
     * information on what the description is. This method will trigger a menu
     * item repaint.
     * 
     * @param description
     *            the new description string for the component.
     */
    void setDescription(String description);

    /**
     * <p>
     * Gets the items's description. The description can be used to briefly
     * describe the state of the item to the user. The description string may
     * contain certain XML tags:
     * </p>
     * 
     * <p>
     * <table border=1>
     * <tr>
     * <td width=120><b>Tag</b></td>
     * <td width=120><b>Description</b></td>
     * <td width=120><b>Example</b></td>
     * </tr>
     * <tr>
     * <td>&lt;b></td>
     * <td>bold</td>
     * <td><b>bold text</b></td>
     * </tr>
     * <tr>
     * <td>&lt;i></td>
     * <td>italic</td>
     * <td><i>italic text</i></td>
     * </tr>
     * <tr>
     * <td>&lt;u></td>
     * <td>underlined</td>
     * <td><u>underlined text</u></td>
     * </tr>
     * <tr>
     * <td>&lt;br></td>
     * <td>linebreak</td>
     * <td>N/A</td>
     * </tr>
     * <tr>
     * <td>&lt;ul><br>
     * &lt;li>item1<br>
     * &lt;li>item1<br>
     * &lt;/ul></td>
     * <td>item list</td>
     * <td>
     * <ul>
     * <li>item1
     * <li>item2
     * </ul>
     * </td>
     * </tr>
     * </table>
     * </p>
     * 
     * <p>
     * These tags may be nested.
     * </p>
     * 
     * @return item's description <code>String</code>
     */
    String getDescription();

    /**
     * Gets the checkable state of the item - whether the item has checked and
     * unchecked states. If an item is checkable its checked state (as returned
     * by {@link #isChecked()}) is indicated in the UI.
     * 
     * <p>
     * An item is not checkable by default.
     * </p>
     * 
     * @return true if the item is checkable, false otherwise
     * @since 6.6.2
     */
    boolean isCheckable();

    /**
     * Sets the checkable state of the item. If an item is checkable its checked
     * state (as returned by {@link #isChecked()}) is indicated in the UI.
     * 
     * <p>
     * An item is not checkable by default.
     * </p>
     * 
     * <p>
     * Items with sub items cannot be checkable.
     * </p>
     * 
     * @param checkable
     *            true if the item should be checkable, false otherwise
     * @throws IllegalStateException
     *             If the item has children
     * @since 6.6.2
     */
    void setCheckable(boolean checkable) throws IllegalStateException;

    /**
     * Gets the checked state of the item (checked or unchecked). Only used if
     * the item is checkable (as indicated by {@link #isCheckable()}). The
     * checked state is indicated in the UI with the item, if the item is
     * checkable.
     * 
     * <p>
     * An item is not checked by default.
     * </p>
     * 
     * <p>
     * The CSS style corresponding to the checked state is "-checked".
     * </p>
     * 
     * @return true if the item is checked, false otherwise
     * @since 6.6.2
     */
    boolean isChecked();

    /**
     * Sets the checked state of the item. Only used if the item is checkable
     * (indicated by {@link #isCheckable()}). The checked state is indicated in
     * the UI with the item, if the item is checkable.
     * 
     * <p>
     * An item is not checked by default.
     * </p>
     * 
     * <p>
     * The CSS style corresponding to the checked state is "-checked".
     * </p>
     * 
     * @return true if the item is checked, false otherwise
     * @since 6.6.2
     */
    void setChecked(boolean checked);

}