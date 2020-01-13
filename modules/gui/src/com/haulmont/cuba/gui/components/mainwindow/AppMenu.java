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

package com.haulmont.cuba.gui.components.mainwindow;

import com.haulmont.cuba.gui.components.Component;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public interface AppMenu extends Component.BelongToFrame, Component.Focusable {

    String NAME = "menu";

    /**
     * Load menu structure from {@link com.haulmont.cuba.gui.config.MenuConfig}
     */
    void loadMenu();

    /**
     * Create new menu item. Does not add item to menu. Id must be unique for whole menu.
     *
     * @param id item id
     * @return menu item instance
     */
    AppMenu.MenuItem createMenuItem(String id);

    /**
     * Create new menu item. Does not add item to menu. Id must be unique for whole menu.
     *
     * @param id item id
     * @param caption item caption
     * @return menu item instance
     */
    AppMenu.MenuItem createMenuItem(String id, String caption);

    /**
     * Create new menu item. Does not add item to menu. Id must be unique for whole menu.
     *
     * @param id item id
     * @param caption item caption
     * @param icon icon
     * @param command command
     * @return menu item instance
     */
    AppMenu.MenuItem createMenuItem(String id, String caption, @Nullable String icon, @Nullable Consumer<AppMenu.MenuItem> command);

    /**
     * Add menu item to the end of root items list.
     *
     * @param menuItem menu item
     */
    void addMenuItem(AppMenu.MenuItem menuItem);

    /**
     * Add menu item to specified position in the root items list.
     *
     * @param menuItem menu item
     * @param index target index
     */
    void addMenuItem(AppMenu.MenuItem menuItem, int index);

    /**
     * Remove menu item from the root items list.
     *
     * @param menuItem menu item
     */
    void removeMenuItem(AppMenu.MenuItem menuItem);

    /**
     * Remove menu item from the root items list by index.
     *
     * @param index index
     */
    void removeMenuItem(int index);

    /**
     * @param id item id
     * @return item from the menu tree by its id
     */
    @Nullable
    AppMenu.MenuItem getMenuItem(String id);

    /**
     * @param id item id
     * @return item from the menu tree by its id
     * @throws IllegalArgumentException if not found
     */
    AppMenu.MenuItem getMenuItemNN(String id);

    /**
     * @return root menu items
     */
    List<AppMenu.MenuItem> getMenuItems();

    /**
     * @return true if the menu has items
     */
    boolean hasMenuItems();

    /**
     * Creates menu separator
     */
    MenuItem createSeparator();

    /**
     * Sets caption to MenuItem with value of shortcut key combination.
     *
     * @param menuItem MenuItem instance
     * @param shortcut shortcut key combination string representation
     */
    void setMenuItemShortcutCaption(MenuItem menuItem, String shortcut);

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
        AppMenu getMenu();

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
        void setIcon(@Nullable String icon);

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
         * @return item command
         */
        Consumer<AppMenu.MenuItem> getCommand();
        /**
         * Set item command
         *
         * @param command item command
         */
        void setCommand(@Nullable Consumer<AppMenu.MenuItem> command);

        /**
         * Add menu item to the end of children list.
         *
         * @param menuItem menu item
         */
        void addChildItem(AppMenu.MenuItem menuItem);

        /**
         * Add menu item to specified position in the children list.
         *
         * @param menuItem menu item
         * @param index target index
         */
        void addChildItem(AppMenu.MenuItem menuItem, int index);

        /**
         * Remove menu item from the children list.
         *
         * @param menuItem menu item
         */
        void removeChildItem(AppMenu.MenuItem menuItem);

        /**
         * Remove menu item from the children list by index.
         *
         * @param index index
         */
        void removeChildItem(int index);

        /**
         * @return child items
         */
        List<AppMenu.MenuItem> getChildren();

        /**
         * @return true if the menu item has child items
         */
        boolean hasChildren();

        /**
         * @return true if item is separator
         */
        boolean isSeparator();
    }
}