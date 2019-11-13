/*
 * Copyright (c) 2008-2017 Haulmont.
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
 */

package com.haulmont.cuba.gui.components.mainwindow;

import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.Component;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

/**
 * Side menu component that shows items as collapsible multi level list.
 */
public interface SideMenu extends Component.BelongToFrame, Component.Focusable {

    String NAME = "sideMenu";

    /**
     * Load menu structure from {@link com.haulmont.cuba.gui.config.MenuConfig}
     */
    void loadMenuConfig();

    /**
     * Bind show/hide side panel action to button.
     *
     * @param button button that should trigger show/hide of side panel
     */
    void setSidePanelToggleButton(Button button);
    /**
     * @return side panel toggle button
     */
    Button getSidePanelToggleButton();

    /**
     * Bind side panel for show/hide action.
     *
     * @param sidePanel side panel
     */
    void setSidePanel(Component sidePanel);
    /**
     * @return side panel
     */
    Component getSidePanel();

    /**
     * @return true if an item becomes selected by click
     */
    boolean isSelectOnClick();
    /**
     * Enable or disable automatic selected styling by click.
     *
     * @param selectOnClick enable/disable option
     */
    void setSelectOnClick(boolean selectOnClick);

    /**
     * @return currently selected item
     */
    @Nullable
    MenuItem getSelectedItem();
    /**
     * Set selected item.
     *
     * @param selectedItem new selected item
     */
    void setSelectedItem(MenuItem selectedItem);

    /**
     * Create new menu item. Does not add item to menu. Id must be unique for whole menu.
     *
     * @param id item id
     * @return menu item instance
     */
    MenuItem createMenuItem(String id);

    /**
     * Create new menu item. Does not add item to menu. Id must be unique for whole menu.
     *
     * @param id item id
     * @param caption item caption
     * @return menu item instance
     */
    MenuItem createMenuItem(String id, String caption);

    /**
     * Create new menu item. Does not add item to menu. Id must be unique for whole menu.
     *
     * @param id item id
     * @param caption item caption
     * @param icon icon
     * @param command command
     * @return menu item instance
     */
    MenuItem createMenuItem(String id, String caption, @Nullable String icon, @Nullable Consumer<MenuItem> command);

    /**
     * Add menu item to the end of root items list.
     *
     * @param menuItem menu item
     */
    void addMenuItem(MenuItem menuItem);

    /**
     * Add menu item to specified position in the root items list.
     *
     * @param menuItem menu item
     * @param index target index
     */
    void addMenuItem(MenuItem menuItem, int index);

    /**
     * Remove menu item from the root items list.
     *
     * @param menuItem menu item
     */
    void removeMenuItem(MenuItem menuItem);

    /**
     * Remove menu item from the root items list by index.
     *
     * @param index index
     */
    void removeMenuItem(int index);

    /**
     * Remove all the root menu items.
     */
    void removeAllMenuItems();

    /**
     * @param id item id
     * @return item from the menu tree by its id
     */
    @Nullable
    MenuItem getMenuItem(String id);

    /**
     * @param id item id
     * @return item from the menu tree by its id
     * @throws IllegalArgumentException if not found
     */
    MenuItem getMenuItemNN(String id);

    /**
     * @return root menu items
     */
    List<MenuItem> getMenuItems();

    /**
     * @return true if the menu has items
     */
    boolean hasMenuItems();

    /**
     * Set true for collapsing a submenu when another parent menu item is clicked
     *
     * @param singleExpandedMenu
     */
    void setShowSingleExpandedMenu(boolean singleExpandedMenu);

    /**
     * @return true if a submenu is collapsing when another parent menu item is clicked
     */
    boolean isShowSingleExpandedMenu();

    /**
     * Menu item
     */
    interface MenuItem {
        /**
         * @return id
         */
        String getId();

        /**
         * @return owner
         */
        SideMenu getMenu();

        /**
         * @return caption
         */
        String getCaption();
        /**
         * Set item caption.
         *
         * @param caption caption
         */
        void setCaption(String caption);

        /**
         * @return description
         */
        String getDescription();
        /**
         * Set description.
         *
         * @param description description
         */
        void setDescription(String description);

        /**
         * @return icon name
         */
        String getIcon();
        /**
         * Set icon.
         *
         * @param icon icon name
         */
        void setIcon(String icon);

        /**
         * @return true if caption is inserted to DOM as HTML
         */
        boolean isCaptionAsHtml();
        /**
         * Enable or disable HTML mode for caption.
         *
         * @param captionAsHtml pass true to enable HTML mode for caption.
         */
        void setCaptionAsHtml(boolean captionAsHtml);

        /**
         * @return true if item will be sent to the client side
         */
        boolean isVisible();
        /**
         * Show or hide item.
         *
         * @param visible pass false to hide menu item
         */
        void setVisible(boolean visible);

        /**
         * @return true if sub menu with children will be initially expanded
         */
        boolean isExpanded();
        /**
         * Expand or collapse sub menu with children by default.
         *
         * @param expanded pass true to set sub menu expanded by default.
         */
        void setExpanded(boolean expanded);

        /**
         * @return all user-defined CSS style names of a component. If the item has multiple style names defined,
         * the return string is a space-separated list of style names.
         */
        String getStyleName();
        /**
         * Sets one or more user-defined style names of the component, replacing any previous user-defined styles.
         * Multiple styles can be specified as a space-separated list of style names. The style names must be valid CSS
         * class names.
         *
         * @param styleName style name string
         */
        void setStyleName(String styleName);

        /**
         * Adds one or more style names to this component. Multiple styles can be specified as a space-separated list
         * of style names. The style name will be rendered as a HTML class name, which can be used in a CSS definition.
         *
         * @param styleName style name string
         */
        void addStyleName(String styleName);

        /**
         * Removes one or more style names from component. Multiple styles can be specified as a space-separated
         * list of style names.
         *
         * @param styleName style name string
         */
        void removeStyleName(String styleName);

        /**
         * @return badge text
         */
        String getBadgeText();
        /**
         * Set badge text for item. Badges are shown as small widget on the right side of menu items.
         *
         * @param badgeText badge text
         */
        void setBadgeText(String badgeText);

        /**
         * @return item command
         */
        Consumer<MenuItem> getCommand();
        /**
         * Set item command
         *
         * @param command item command
         */
        void setCommand(Consumer<MenuItem> command);

        /**
         * Add menu item to the end of children list.
         *
         * @param menuItem menu item
         */
        void addChildItem(MenuItem menuItem);

        /**
         * Add menu item to specified position in the children list.
         *
         * @param menuItem menu item
         * @param index target index
         */
        void addChildItem(MenuItem menuItem, int index);

        /**
         * Remove menu item from the children list.
         *
         * @param menuItem menu item
         */
        void removeChildItem(MenuItem menuItem);

        /**
         * Remove menu item from the children list by index.
         *
         * @param index index
         */
        void removeChildItem(int index);

        /**
         * Remove all child items from the children list.
         */
        void removeAllChildItems();

        /**
         * @return child items
         */
        List<MenuItem> getChildren();

        /**
         * @return true if the menu item has child items
         */
        boolean hasChildren();

        /**
         * @return parent menu item if it's nested item, null otherwise
         */
        @Nullable
        MenuItem getParent();

        /**
         * @return parent menu item if it's nested item, null otherwise
         * @throws IllegalArgumentException if not found
         */
        MenuItem getParentNN();
    }
}