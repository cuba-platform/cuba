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

package com.haulmont.cuba.web.gui.components.mainwindow;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.TestIdManager;
import com.haulmont.cuba.gui.components.mainwindow.AppMenu;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.gui.components.WebAbstractComponent;
import com.haulmont.cuba.web.gui.icons.IconResolver;
import com.haulmont.cuba.web.sys.MenuBuilder;
import com.haulmont.cuba.web.widgets.CubaMenuBar;
import com.vaadin.server.ClientConnector;
import com.vaadin.server.Resource;
import com.vaadin.ui.MenuBar;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

public class WebAppMenu extends WebAbstractComponent<CubaMenuBar> implements AppMenu {

    protected Map<String, MenuItem> allItemsIds = new HashMap<>();
    protected Map<MenuBar.MenuItem, MenuItem> viewModelMap = new HashMap<>();

    public static final String MENU_STYLENAME = "c-main-menu";

    public WebAppMenu() {
        component = new CubaMenuBar();
        component.addStyleName(MENU_STYLENAME);

        component.addAttachListener(this::handleAttach);
    }

    protected void handleAttach(ClientConnector.AttachEvent event) {
        AppUI appUi = (AppUI) component.getUI();
        if (appUi == null || !appUi.isTestMode()) {
            return;
        }

        for (Map.Entry<String, MenuItem> entry : allItemsIds.entrySet()) {
            assignTestIds(entry.getValue(), entry.getKey());
        }
    }

    @Override
    public void setStyleName(String name) {
        super.setStyleName(name);

        component.addStyleName(MENU_STYLENAME);
    }

    @Override
    public String getStyleName() {
        return StringUtils.normalizeSpace(super.getStyleName().replace(MENU_STYLENAME, ""));
    }

    @Override
    public void loadMenu() {
        MenuBuilder menuBuilder = AppBeans.getPrototype(MenuBuilder.NAME);
        menuBuilder.build(this);
    }

    @Override
    public MenuItem createMenuItem(String id) {
        return createMenuItem(id, null, null, null);
    }

    @Override
    public MenuItem createMenuItem(String id, String caption) {
        return createMenuItem(id, caption, null, null);
    }

    @Override
    public MenuItem createMenuItem(String id, String caption,
                                   @Nullable String icon, @Nullable Consumer<MenuItem> command) {
        checkNotNullArgument(id);
        checkItemIdDuplicate(id);

        MenuItem menuItem = new MenuItemImpl(this, id);

        Resource iconResource = null;
        if (icon != null) {
            iconResource = AppBeans.get(IconResolver.class).getIconResource(icon);
        }

        MenuBar.MenuItem delegateItem = component.createMenuItem(caption, iconResource, null);
        if (command != null) {
            delegateItem.setCommand(selectedItem ->
                    command.accept(menuItem));
        }
        ((MenuItemImpl) menuItem).setDelegateItem(delegateItem);

        menuItem.setCaption(caption);
        menuItem.setIcon(icon);
        menuItem.setCommand(command);

        return menuItem;
    }

    protected void assignTestIds(MenuItem menuItem, String id) {
        AppUI ui = (AppUI) component.getUI();
        if (ui == null || !ui.isTestMode())
            return;

        MenuBar.MenuItem delegateItem = ((MenuItemImpl) menuItem).getDelegateItem();
        component.setCubaId(delegateItem, id);

        TestIdManager testIdManager = ui.getTestIdManager();
        String testId = component.getId() + "_" + id;

        component.setTestId(delegateItem, testIdManager.reserveId(testId));
    }

    @Override
    public void addMenuItem(MenuItem menuItem) {
        checkNotNullArgument(menuItem);
        checkItemIdDuplicate(menuItem.getId());
        checkItemOwner(menuItem);

        component.addMenuItem(((MenuItemImpl) menuItem).getDelegateItem());
        registerMenuItem(menuItem);

        assignTestIds(menuItem, menuItem.getId());
    }

    @Override
    public void addMenuItem(MenuItem menuItem, int index) {
        checkNotNullArgument(menuItem);
        checkItemIdDuplicate(menuItem.getId());
        checkItemOwner(menuItem);

        component.addMenuItem(((MenuItemImpl) menuItem).getDelegateItem(), index);
        registerMenuItem(menuItem);

        assignTestIds(menuItem, menuItem.getId());
    }

    protected void registerMenuItem(MenuItem menuItem) {
        allItemsIds.put(menuItem.getId(), menuItem);
        viewModelMap.put(((MenuItemImpl) menuItem).getDelegateItem(), menuItem);
    }

    @Override
    public void removeMenuItem(MenuItem menuItem) {
        checkNotNullArgument(menuItem);
        checkItemOwner(menuItem);

        component.removeMenuItem(((MenuItemImpl) menuItem).getDelegateItem());
        unregisterItem(menuItem);
    }

    @Override
    public void removeMenuItem(int index) {
        MenuBar.MenuItem delegateItem = component.getMenuItems().get(index);
        component.removeMenuItem(delegateItem);
        unregisterItem(viewModelMap.get(delegateItem));
    }

    protected void unregisterItem(MenuItem menuItem) {
        allItemsIds.remove(menuItem.getId());
        viewModelMap.remove(((MenuItemImpl) menuItem).getDelegateItem());
    }

    @Nullable
    @Override
    public MenuItem getMenuItem(String id) {
        return allItemsIds.get(id);
    }

    @Override
    public MenuItem getMenuItemNN(String id) {
        MenuItem menuItem = allItemsIds.get(id);
        if (menuItem == null) {
            throw new IllegalArgumentException("Unable to find menu item with id: " + id);
        }
        return menuItem;
    }

    @Override
    public List<MenuItem> getMenuItems() {
        return component.getMenuItems().stream()
                .map(viewItem -> viewModelMap.get(viewItem))
                .collect(Collectors.toList());
    }

    @Override
    public boolean hasMenuItems() {
        return component.hasMenuItems();
    }

    protected void checkItemIdDuplicate(String id) {
        if (allItemsIds.containsKey(id)) {
            throw new IllegalArgumentException(String.format("MenuItem with id \"%s\" already exists", id));
        }
    }

    protected void checkItemOwner(MenuItem item) {
        if (item.getMenu() != this) {
            throw new IllegalArgumentException("MenuItem is not created by this menu");
        }
    }

    @Override
    public MenuItem createSeparator() {
        MenuItemImpl menuItem = new MenuItemImpl(this, null);
        menuItem.setSeparator(true);

        MenuBar.MenuItem separator = component.createSeparator();
        menuItem.setDelegateItem(separator);

        return menuItem;
    }

    @Override
    public void setMenuItemShortcutCaption(MenuItem menuItem, String shortcut) {
        component.setShortcut(((MenuItemImpl) menuItem).getDelegateItem(), shortcut);
    }

    @Override
    public int getTabIndex() {
        return component.getTabIndex();
    }

    @Override
    public void setTabIndex(int tabIndex) {
        component.setTabIndex(tabIndex);
    }

    protected static class MenuItemImpl implements MenuItem {
        protected WebAppMenu menu;
        protected String id;
        protected MenuBar.MenuItem delegateItem;
        protected Consumer<MenuItem> command;

        protected String icon;
        protected boolean separator;

        public MenuItemImpl(WebAppMenu menu, String id) {
            this.menu = menu;
            this.id = id;
        }

        @Override
        public String getId() {
            return id;
        }

        @Override
        public AppMenu getMenu() {
            return menu;
        }

        public MenuBar.MenuItem getDelegateItem() {
            return delegateItem;
        }

        public void setDelegateItem(MenuBar.MenuItem delegateItem) {
            this.delegateItem = delegateItem;
        }

        @Override
        public String getCaption() {
            return delegateItem.getText();
        }

        @Override
        public void setCaption(String caption) {
            delegateItem.setText(caption);
        }

        @Override
        public String getDescription() {
            return delegateItem.getDescription();
        }

        @Override
        public void setDescription(String description) {
            delegateItem.setDescription(description);
        }

        @Override
        public String getIcon() {
            return icon;
        }

        @Override
        public void setIcon(String icon) {
            this.icon = icon;

            if (icon != null) {
                Resource iconResource = AppBeans.get(IconResolver.class)
                        .getIconResource(this.icon);
                delegateItem.setIcon(iconResource);
            } else {
                delegateItem.setIcon(null);
            }
        }

        @Override
        public boolean isVisible() {
            return delegateItem.isVisible();
        }

        @Override
        public void setVisible(boolean visible) {
            delegateItem.setVisible(visible);
        }

        @Override
        public String getStyleName() {
            return delegateItem.getStyleName();
        }

        @Override
        public void setStyleName(String styleName) {
            delegateItem.setStyleName(styleName);
        }

        @Override
        public Consumer<MenuItem> getCommand() {
            return command;
        }

        @Override
        public void setCommand(Consumer<MenuItem> command) {
            this.command = command;

            if (command != null) {
                delegateItem.setCommand(event -> this.command.accept(this));
            } else {
                delegateItem.setCommand(null);
            }
        }

        @Override
        public void addChildItem(MenuItem menuItem) {
            MenuBar.MenuItem childItem = ((MenuItemImpl) menuItem).getDelegateItem();
            if (childItem.getText() == null) {
                throw new IllegalArgumentException("Caption cannot be null");
            }

            MenuBar.MenuItem delegateItem = this.getDelegateItem();

            childItem.setParent(delegateItem);

            delegateItem.getChildren().add(childItem);
            menu.registerMenuItem(menuItem);

            menu.getComponent().markAsDirty();
        }

        @Override
        public void addChildItem(MenuItem menuItem, int index) {
            MenuBar.MenuItem childItem = ((MenuItemImpl) menuItem).getDelegateItem();
            if (childItem.getText() == null) {
                throw new IllegalArgumentException("Caption cannot be null");
            }

            MenuBar.MenuItem delegateItem = this.getDelegateItem();

            childItem.setParent(delegateItem);

            delegateItem.getChildren().add(index, childItem);
            menu.registerMenuItem(menuItem);

            menu.getComponent().markAsDirty();
        }

        @Override
        public void removeChildItem(MenuItem menuItem) {
            MenuBar.MenuItem childItem = ((MenuItemImpl) menuItem).getDelegateItem();

            getDelegateItem().getChildren().remove(childItem);
            menu.unregisterItem(menuItem);
        }

        @Override
        public void removeChildItem(int index) {
            MenuItem menuItem = getChildren().get(index);
            removeChildItem(menuItem);
            menu.unregisterItem(menuItem);
        }

        @Override
        public List<MenuItem> getChildren() {
            return delegateItem.getChildren().stream()
                    .map(menuItem -> menu.viewModelMap.get(menuItem))
                    .collect(Collectors.toList());
        }

        @Override
        public boolean hasChildren() {
            return delegateItem.hasChildren();
        }

        @Override
        public boolean isSeparator() {
            return separator;
        }

        protected void setSeparator(boolean separator) {
            this.separator = separator;
        }
    }
}