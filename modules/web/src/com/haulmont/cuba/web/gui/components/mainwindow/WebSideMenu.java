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

package com.haulmont.cuba.web.gui.components.mainwindow;

import com.haulmont.cuba.gui.components.AbstractAction;
import com.haulmont.cuba.gui.components.Button;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.mainwindow.SideMenu;
import com.haulmont.cuba.web.AppUI;
import com.haulmont.cuba.web.gui.components.WebAbstractComponent;
import com.haulmont.cuba.web.gui.icons.IconResolver;
import com.haulmont.cuba.web.sys.SideMenuBuilder;
import com.haulmont.cuba.web.theme.HaloTheme;
import com.haulmont.cuba.web.widgets.CubaSideMenu;
import com.vaadin.server.ClientConnector;
import com.vaadin.server.Resource;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

public class WebSideMenu extends WebAbstractComponent<CubaSideMenu> implements SideMenu {

    protected Map<String, MenuItem> allItemsIds = new HashMap<>();

    protected Button toggleButton;
    protected Component sidePanel;

    public WebSideMenu() {
        component = new CubaSideMenu();
        component.setBeforeMenuItemTriggeredHandler(menuItem -> {
            if (sidePanel != null) {
                sidePanel.removeStyleName(HaloTheme.SIDEMENU_PANEL_OPEN);
            }
        });

        component.addAttachListener(this::handleAttach);
    }

    protected void handleAttach(@SuppressWarnings("unused") ClientConnector.AttachEvent attachEvent) {
        AppUI appUi = (AppUI) component.getUI();
        if (appUi == null || !appUi.isTestMode()) {
            return;
        }

        for (CubaSideMenu.MenuItem vMenuItem : component.getMenuItems()) {
            assignCubaId(((MenuItemWrapper) vMenuItem).getMenuItem());
        }
    }

    @Override
    public void loadMenuConfig() {
        SideMenuBuilder menuBuilder = beanLocator.getPrototype(SideMenuBuilder.NAME);
        menuBuilder.build(this);
    }

    @Override
    public void setSidePanelToggleButton(Button toggleButton) {
        if (this.toggleButton != null) {
            toggleButton.setAction(null);
        }

        if (toggleButton != null) {
            AbstractAction toggleAction = new AbstractAction("toggleSideMenu") {
                @Override
                public void actionPerform(Component component) {
                    toggleSidePanel();
                }
            };

            toggleAction.setCaption(toggleButton.getCaption());
            toggleAction.setIcon(toggleButton.getIcon());
            toggleAction.setDescription(toggleButton.getDescription());
            toggleAction.setEnabled(toggleButton.isEnabled());
            toggleAction.setVisible(toggleButton.isVisible());

            toggleButton.setAction(toggleAction);
        }

        this.toggleButton = toggleButton;
    }

    protected void toggleSidePanel() {
        if (sidePanel != null) {
            if (sidePanel.getStyleName().contains(HaloTheme.SIDEMENU_PANEL_OPEN)) {
                sidePanel.removeStyleName(HaloTheme.SIDEMENU_PANEL_OPEN);
            } else {
                sidePanel.addStyleName(HaloTheme.SIDEMENU_PANEL_OPEN);
            }
        }
    }

    @Override
    public Button getSidePanelToggleButton() {
        return toggleButton;
    }

    @Override
    public void setSidePanel(Component sidePanel) {
        this.sidePanel = sidePanel;
    }

    @Override
    public Component getSidePanel() {
        return sidePanel;
    }

    @Override
    public boolean isSelectOnClick() {
        return component.isSelectOnClick();
    }

    @Override
    public void setSelectOnClick(boolean selectOnClick) {
        component.setSelectOnClick(selectOnClick);
    }

    @Nullable
    @Override
    public MenuItem getSelectedItem() {
        CubaSideMenu.MenuItem selectedItem = component.getSelectedItem();
        return selectedItem != null
                ? ((MenuItemWrapper) selectedItem).getMenuItem()
                : null;
    }

    @Override
    public void setSelectedItem(MenuItem selectedItem) {
        component.setSelectedItem(((MenuItemImpl) selectedItem).getDelegateItem());
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

        MenuItemWrapper delegateItem = new MenuItemWrapper();

        MenuItem menuItem = new MenuItemImpl(this, id, delegateItem);
        menuItem.setCaption(caption);
        menuItem.setIcon(icon);
        menuItem.setCommand(command);

        delegateItem.setMenuItem(menuItem);

        return menuItem;
    }

    protected void assignCubaId(MenuItem menuItem) {
        AppUI ui = (AppUI) component.getUI();
        if (ui == null || !ui.isTestMode())
            return;

        assignCubaIdInternal(menuItem);
    }

    protected void assignCubaIdInternal(MenuItem menuItem) {
        ((MenuItemImpl) menuItem).setCubaId(menuItem.getId());

        if (menuItem.hasChildren()) {
            for (MenuItem item : menuItem.getChildren()) {
                assignCubaIdInternal(item);
            }
        }
    }

    @Override
    public void addMenuItem(MenuItem menuItem) {
        checkNotNullArgument(menuItem);
        checkItemIdDuplicate(menuItem.getId());
        checkItemOwner(menuItem);

        component.addMenuItem(((MenuItemImpl) menuItem).getDelegateItem());
        registerMenuItem(menuItem);

        assignCubaId(menuItem);
    }

    protected void registerMenuItem(MenuItem menuItem) {
        allItemsIds.put(menuItem.getId(), menuItem);
        if (menuItem.hasChildren()) {
            for (MenuItem item : menuItem.getChildren()) {
                registerMenuItem(item);
            }
        }
    }

    protected void unregisterItem(MenuItem menuItem) {
        allItemsIds.remove(menuItem.getId());
        if (menuItem.hasChildren()) {
            for (MenuItem item : menuItem.getChildren()) {
                unregisterItem(item);
            }
        }
    }

    @Override
    public void addMenuItem(MenuItem menuItem, int index) {
        checkNotNullArgument(menuItem);
        checkItemIdDuplicate(menuItem.getId());
        checkItemOwner(menuItem);

        component.addMenuItem(((MenuItemImpl) menuItem).getDelegateItem(), index);
        registerMenuItem(menuItem);

        assignCubaId(menuItem);
    }

    @Override
    public void removeMenuItem(MenuItem menuItem) {
        checkNotNullArgument(menuItem);
        checkItemOwner(menuItem);

        if (getMenuItems().contains(menuItem)) {
            unregisterItem(menuItem);
        }

        component.removeMenuItem(((MenuItemImpl) menuItem).getDelegateItem());
    }

    @Override
    public void removeAllMenuItems() {
        for (CubaSideMenu.MenuItem menuItem : new ArrayList<>(component.getMenuItems())) {
            component.removeMenuItem(menuItem);
            unregisterItem(((MenuItemWrapper) menuItem).getMenuItem());
        }
    }

    @Override
    public void removeMenuItem(int index) {
        CubaSideMenu.MenuItem delegateItem = component.getMenuItems().get(index);
        component.removeMenuItem(index);
        unregisterItem(((MenuItemWrapper) delegateItem).getMenuItem());
    }

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
                .map(delegateItem -> ((MenuItemWrapper) delegateItem).getMenuItem())
                .collect(Collectors.toList());
    }

    @Override
    public boolean hasMenuItems() {
        return component.hasMenuItems();
    }

    @Override
    public void focus() {
        component.focus();
    }

    @Override
    public int getTabIndex() {
        return component.getTabIndex();
    }

    @Override
    public void setTabIndex(int tabIndex) {
        component.setTabIndex(tabIndex);
    }

    protected static class MenuItemWrapper extends CubaSideMenu.MenuItem {
        protected MenuItem menuItem;

        public MenuItemWrapper() {
        }

        public MenuItem getMenuItem() {
            return menuItem;
        }

        public void setMenuItem(MenuItem menuItem) {
            this.menuItem = menuItem;
        }
    }

    @Override
    public void setShowSingleExpandedMenu(boolean singleExpandedMenu) {
        component.setShowSingleExpandedMenu(singleExpandedMenu);
    }

    @Override
    public boolean isShowSingleExpandedMenu() {
        return component.isShowSingleExpandedMenu();
    }

    protected static class MenuItemImpl implements MenuItem {
        protected WebSideMenu menu;
        protected String id;
        protected CubaSideMenu.MenuItem delegateItem;
        protected Consumer<MenuItem> command;

        protected String icon;

        public MenuItemImpl(WebSideMenu menu, String id, CubaSideMenu.MenuItem delegateItem) {
            this.menu = menu;
            this.id = id;
            this.delegateItem = delegateItem;
        }

        @Override
        public String getId() {
            return id;
        }

        @Override
        public SideMenu getMenu() {
            return menu;
        }

        public CubaSideMenu.MenuItem getDelegateItem() {
            return delegateItem;
        }

        @Override
        public String getCaption() {
            return delegateItem.getCaption();
        }

        @Override
        public void setCaption(String caption) {
            delegateItem.setCaption(caption);
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
                Resource iconResource = menu.beanLocator.get(IconResolver.class)
                        .getIconResource(this.icon);
                delegateItem.setIcon(iconResource);
            } else {
                delegateItem.setIcon(null);
            }
        }

        @Override
        public boolean isCaptionAsHtml() {
            return delegateItem.isCaptionAsHtml();
        }

        @Override
        public void setCaptionAsHtml(boolean captionAsHtml) {
            delegateItem.setCaptionAsHtml(captionAsHtml);
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
        public boolean isExpanded() {
            return delegateItem.isExpanded();
        }

        @Override
        public void setExpanded(boolean expanded) {
            delegateItem.setExpanded(expanded);
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
        public void addStyleName(String styleName) {
            delegateItem.addStyleName(styleName);
        }

        @Override
        public void removeStyleName(String styleName) {
            delegateItem.removeStyleName(styleName);
        }

        @Override
        public String getBadgeText() {
            return delegateItem.getBadgeText();
        }

        @Override
        public void setBadgeText(String badgeText) {
            delegateItem.setBadgeText(badgeText);
        }

        protected String getCubaId() {
            return delegateItem.getCubaId();
        }

        protected void setCubaId(String cubaId) {
            delegateItem.setCubaId(cubaId);
        }

        @Override
        public Consumer<MenuItem> getCommand() {
            return command;
        }

        @Override
        public void setCommand(Consumer<MenuItem> command) {
            this.command = command;

            if (command != null) {
                delegateItem.setCommand(this::menuSelected);
            } else {
                delegateItem.setCommand(null);
            }
        }

        @Override
        public void addChildItem(MenuItem menuItem) {
            checkNotNullArgument(menuItem);
            menu.checkItemOwner(menuItem);

            delegateItem.addChildItem(((MenuItemImpl) menuItem).getDelegateItem());

            menu.registerMenuItem(menuItem);
        }

        @Override
        public void addChildItem(MenuItem menuItem, int index) {
            checkNotNullArgument(menuItem);
            menu.checkItemOwner(menuItem);

            delegateItem.addChildItem(((MenuItemImpl) menuItem).getDelegateItem(), index);

            menu.registerMenuItem(menuItem);
        }

        @Override
        public void removeChildItem(MenuItem menuItem) {
            checkNotNullArgument(menuItem);
            menu.checkItemOwner(menuItem);

            if (getChildren().contains(menuItem)) {
                menu.unregisterItem(menuItem);
            }

            delegateItem.removeChildItem(((MenuItemImpl) menuItem).getDelegateItem());
        }

        @Override
        public void removeAllChildItems() {
            for (CubaSideMenu.MenuItem menuItem : new ArrayList<>(delegateItem.getChildren())) {
                delegateItem.removeChildItem(menuItem);

                menu.unregisterItem(((MenuItemWrapper) menuItem).getMenuItem());
            }
        }

        @Override
        public void removeChildItem(int index) {
            CubaSideMenu.MenuItem menuItem = delegateItem.getChildren().get(index);
            delegateItem.removeChildItem(index);

            menu.unregisterItem(((MenuItemWrapper) menuItem).getMenuItem());
        }

        @Override
        public List<MenuItem> getChildren() {
            return delegateItem.getChildren().stream()
                    .map(delegateItem -> ((MenuItemWrapper) delegateItem).getMenuItem())
                    .collect(Collectors.toList());
        }

        @Override
        public boolean hasChildren() {
            return delegateItem.hasChildren();
        }

        @Nullable
        @Override
        public MenuItem getParent() {
            CubaSideMenu.MenuItem parent = delegateItem.getParent();
            return parent != null ? ((MenuItemWrapper) parent).getMenuItem() : null;
        }

        @Override
        public MenuItem getParentNN() {
            if (delegateItem.getParent() == null) {
                throw new IllegalArgumentException("Unable to find parent for menu item with id: " + id);
            }
            return ((MenuItemWrapper) delegateItem.getParent()).getMenuItem();
        }

        @SuppressWarnings("unused")
        protected void menuSelected(CubaSideMenu.MenuItemTriggeredEvent event) {
            this.command.accept(this);
        }
    }
}