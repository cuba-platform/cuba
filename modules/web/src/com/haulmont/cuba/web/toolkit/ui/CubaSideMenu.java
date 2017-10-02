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
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.web.theme.HaloTheme;
import com.haulmont.cuba.web.toolkit.ui.client.verticalmenu.CubaSideMenuClientRpc;
import com.haulmont.cuba.web.toolkit.ui.client.verticalmenu.CubaSideMenuServerRpc;
import com.haulmont.cuba.web.toolkit.ui.client.verticalmenu.CubaSideMenuState;
import com.vaadin.server.KeyMapper;
import com.vaadin.server.Resource;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;
import elemental.json.Json;
import elemental.json.JsonArray;
import elemental.json.JsonObject;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nullable;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;
import java.util.function.Consumer;

public class CubaSideMenu extends AbstractComponent implements Component.Focusable {

    protected static final String MENU_ITEM_ID = "id";
    protected static final String MENU_ITEM_CAPTION = "caption";
    protected static final String MENU_ITEM_DESCRIPTION = "description";
    protected static final String MENU_ITEM_STYLE_NAME = "styleName";
    protected static final String MENU_ITEM_VISIBLE = "visible";
    protected static final String MENU_ITEM_ENABLED = "enabled";
    protected static final String MENU_ITEM_EXPANDED = "expanded";
    protected static final String MENU_ITEM_CAPTION_AS_HTML = "captionAsHtml";
    protected static final String MENU_ITEM_CUBA_ID = "cubaId";
    protected static final String MENU_ITEM_ICON = "icon";
    protected static final String MENU_ITEM_BADGE_TEXT = "badgeText";
    protected static final String MENU_ITEM_CHILDREN = "children";
    protected static final String MENU_ITEM_COMMAND = "command";

    protected List<MenuItem> menuItems = new ArrayList<>();
    protected MenuItem selectedItem;

    protected boolean structureChanged = true;
    protected boolean selectedChanged = true;

    protected KeyMapper<MenuItem> menuItemIdMapper = new KeyMapper<>();
    protected KeyMapper<Resource> menuIconsKeyMapper = new KeyMapper<>();
    protected Set<String> menuIconResourceKeys = new HashSet<>();
    protected List<MenuItem> badgeUpdates = new ArrayList<>();

    protected PropertyChangeListener itemsPropertyChangeListener = this::menuItemPropertyChanged;

    protected Consumer<MenuItem> beforeMenuItemTriggeredHandler = null;

    public CubaSideMenu() {
        CubaSideMenuServerRpc menuRpc = new CubaSideMenuServerRpc() {
            @Override
            public void menuItemTriggered(String itemId) {
                MenuItem menuItem = menuItemIdMapper.get(itemId);
                if (menuItem != null) {
                    if (isSelectOnClick()) {
                        selectedItem = menuItem;
                    }
                    if (beforeMenuItemTriggeredHandler != null) {
                        beforeMenuItemTriggeredHandler.accept(menuItem);
                    }
                    if (menuItem.getCommand() != null) {
                        menuItem.getCommand().accept(new MenuItemTriggeredEvent(CubaSideMenu.this, menuItem));
                    }
                    removeStyleName(HaloTheme.SIDEMENU_PANEL_OPEN);
                }
            }

            @Override
            public void headerItemExpandChanged(String itemId, boolean expanded) {
                MenuItem menuItem = menuItemIdMapper.get(itemId);
                if (menuItem != null) {
                    menuItem.expanded = expanded;
                }
            }
        };
        registerRpc(menuRpc);
    }

    public Consumer<MenuItem> getBeforeMenuItemTriggeredHandler() {
        return beforeMenuItemTriggeredHandler;
    }

    public void setBeforeMenuItemTriggeredHandler(Consumer<MenuItem> beforeMenuItemTriggeredHandler) {
        this.beforeMenuItemTriggeredHandler = beforeMenuItemTriggeredHandler;
    }

    @Override
    protected CubaSideMenuState getState() {
        return (CubaSideMenuState) super.getState();
    }

    @Override
    protected CubaSideMenuState getState(boolean markAsDirty) {
        return (CubaSideMenuState) super.getState(markAsDirty);
    }

    @Override
    public void beforeClientResponse(boolean initial) {
        super.beforeClientResponse(initial);

        if (initial || this.structureChanged) {
            JsonArray menuTreeJson = toJson(menuItems);

            getRpcProxy(CubaSideMenuClientRpc.class).buildMenu(menuTreeJson);

            this.structureChanged = false;
        }

        if (this.selectedChanged) {
            String key = selectedItem == null ? null : menuItemIdMapper.key(selectedItem);
            getRpcProxy(CubaSideMenuClientRpc.class).selectItem(key);

            this.selectedChanged = false;
        }

        if (!badgeUpdates.isEmpty()) {
            Map<String, String> updatedBadgeValues = new HashMap<>();
            for (MenuItem updatedItem : badgeUpdates) {
                // send null as empty string to remove badge
                String badgeText = updatedItem.getBadgeText();
                updatedBadgeValues.put(menuItemIdMapper.key(updatedItem), badgeText != null ? badgeText : "");
            }
            badgeUpdates.clear();
            getRpcProxy(CubaSideMenuClientRpc.class).updateBadge(updatedBadgeValues);
        }
    }

    @Override
    public void focus() {
        super.focus();
    }

    @Override
    public int getTabIndex() {
        return getState(false).tabIndex;
    }

    @Override
    public void setTabIndex(int tabIndex) {
        if (getTabIndex() != tabIndex) {
            getState().tabIndex = tabIndex;
        }
    }

    public boolean isSelectOnClick() {
        return getState(false).selectOnClick;
    }

    public void setSelectOnClick(boolean selectOnClick) {
        if (getState(false).selectOnClick != selectOnClick) {
            getState().selectOnClick = selectOnClick;
        }
    }

    public void setShowSingleExpandedMenu(boolean singleExpandedMenu) {
        if (getState(false).singleExpandedMenu != singleExpandedMenu) {
            getState().singleExpandedMenu = singleExpandedMenu;
        }
    }

    public boolean isShowSingleExpandedMenu() {
        return getState(false).singleExpandedMenu;
    }

    public MenuItem getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(MenuItem selectedItem) {
        if (this.selectedItem != selectedItem) {
            this.selectedItem = selectedItem;
            markAsDirty();

            this.selectedChanged = true;
        }
    }

    public void addMenuItem(MenuItem menuItem) {
        if (menuItems.contains(menuItem)) {
            menuItems.remove(menuItem);
        }

        menuItems.add(menuItem);

        addPropertyChangeListenerRecursive(menuItem, itemsPropertyChangeListener);

        markMenuStructureAsDirty();
    }

    public void addMenuItem(MenuItem menuItem, int index) {
        if (menuItems.contains(menuItem)) {
            int existingIndex = menuItems.indexOf(menuItem);
            if (index > existingIndex) {
                index--;
            }

            menuItems.remove(menuItem);
        }

        menuItems.add(index, menuItem);

        addPropertyChangeListenerRecursive(menuItem, itemsPropertyChangeListener);

        markMenuStructureAsDirty();
    }

    public void removeMenuItem(MenuItem menuItem) {
        menuItems.remove(menuItem);

        removePropertyChangeListenerRecursive(menuItem, itemsPropertyChangeListener);

        markMenuStructureAsDirty();
    }

    public void removeMenuItem(int index) {
        MenuItem menuItem = menuItems.remove(index);

        removePropertyChangeListenerRecursive(menuItem, itemsPropertyChangeListener);

        markMenuStructureAsDirty();
    }

    public List<MenuItem> getMenuItems() {
        return Collections.unmodifiableList(menuItems);
    }

    public boolean hasMenuItems() {
        return !menuItems.isEmpty();
    }

    protected JsonArray toJson(List<MenuItem> menuItems) {
        JsonArray array = Json.createArray();

        int i = 0;
        for (MenuItem menuItem : menuItems) {
            if (menuItem.isVisible()) {
                JsonObject item = Json.createObject();

                item.put(MENU_ITEM_ID, Json.create(menuItemIdMapper.key(menuItem)));

                if (menuItem.getCaption() != null) {
                    item.put(MENU_ITEM_CAPTION, Json.create(menuItem.getCaption()));
                }
                if (menuItem.getDescription() != null) {
                    item.put(MENU_ITEM_DESCRIPTION, Json.create(menuItem.getDescription()));
                }
                if (menuItem.getStyleName() != null) {
                    item.put(MENU_ITEM_STYLE_NAME, Json.create(menuItem.getStyleName()));
                }
                item.put(MENU_ITEM_EXPANDED, Json.create(menuItem.isExpanded()));
                item.put(MENU_ITEM_CAPTION_AS_HTML, Json.create(menuItem.isCaptionAsHtml()));

                if (menuItem.getBadgeText() != null) {
                    item.put(MENU_ITEM_BADGE_TEXT, Json.create(menuItem.getBadgeText()));
                }

                if (menuItem.getCubaId() != null) {
                    item.put(MENU_ITEM_CUBA_ID, Json.create(menuItem.getCubaId()));
                }

                if (menuItem.getIcon() != null) {
                    String resourceKey = menuIconsKeyMapper.key(menuItem.getIcon());

                    menuIconResourceKeys.add(resourceKey);

                    setResource(resourceKey, menuItem.getIcon());

                    item.put(MENU_ITEM_ICON, Json.create(resourceKey));
                }

                if (!menuItem.getChildren().isEmpty()) {
                    JsonArray childrenJsonArray = toJson(menuItem.getChildren());
                    item.put(MENU_ITEM_CHILDREN, childrenJsonArray);
                }

                array.set(i, item);

                i++;
            }
        }

        return array;
    }

    @SuppressWarnings("unchecked")
    protected void menuItemPropertyChanged(PropertyChangeEvent event) {
        if (event.getPropertyName() == null) {
            return;
        }

        if (!structureChanged && MENU_ITEM_BADGE_TEXT.equals(event.getPropertyName())) {
            badgeUpdates.add((MenuItem) event.getSource());
            markAsDirty();
        }

        if (MENU_ITEM_CHILDREN.equals(event.getPropertyName())) {
            List<MenuItem> oldItems = (List<MenuItem>) event.getOldValue();
            List<MenuItem> newItems = (List<MenuItem>) event.getNewValue();

            if (oldItems != null) {
                for (MenuItem oldItem : oldItems) {
                    oldItem.removePropertyChangeListener(itemsPropertyChangeListener);
                }
            }
            if (newItems != null) {
                for (MenuItem newItem : newItems) {
                    newItem.addPropertyChangeListener(itemsPropertyChangeListener);
                }
            }
        }

        switch (event.getPropertyName()) {
            case MENU_ITEM_CAPTION:
            case MENU_ITEM_DESCRIPTION:
            case MENU_ITEM_ICON:
            case MENU_ITEM_CHILDREN:
            case MENU_ITEM_STYLE_NAME:
            case MENU_ITEM_VISIBLE:
            case MENU_ITEM_ENABLED:
            case MENU_ITEM_CAPTION_AS_HTML:
            case MENU_ITEM_CUBA_ID:
            case MENU_ITEM_EXPANDED:
                markMenuStructureAsDirty();
                break;
        }
    }

    protected void markMenuStructureAsDirty() {
        markAsDirty();
        this.structureChanged = true;

        for (String menuIconsKey : menuIconResourceKeys) {
            Resource resource = getResource(menuIconsKey);
            menuIconsKeyMapper.remove(resource);
            setResource(menuIconsKey, null);
        }

        menuItemIdMapper.removeAll();
        menuIconsKeyMapper.removeAll();

        // all badges will be sent to client with items
        badgeUpdates.clear();
    }

    protected void addPropertyChangeListenerRecursive(MenuItem menuItem, PropertyChangeListener itemsPropertyChangeListener) {
        menuItem.addPropertyChangeListener(itemsPropertyChangeListener);
        for (MenuItem item : menuItem.getChildren()) {
            addPropertyChangeListenerRecursive(item, itemsPropertyChangeListener);
        }
    }

    protected void removePropertyChangeListenerRecursive(MenuItem menuItem, PropertyChangeListener itemsPropertyChangeListener) {
        menuItem.removePropertyChangeListener(itemsPropertyChangeListener);
        for (MenuItem item : menuItem.getChildren()) {
            removePropertyChangeListenerRecursive(item, itemsPropertyChangeListener);
        }
    }

    public static class MenuItemTriggeredEvent extends EventObject {
        private MenuItem menuItem;

        public MenuItemTriggeredEvent(CubaSideMenu source, MenuItem menuItem) {
            super(source);
            this.menuItem = menuItem;
        }

        public MenuItem getMenuItem() {
            return menuItem;
        }

        @Override
        public CubaSideMenu getSource() {
            return (CubaSideMenu) super.getSource();
        }
    }

    public static class MenuItem {
        protected String caption;
        protected String description;
        protected Resource icon;

        protected boolean captionAsHtml = false;
        protected boolean visible = true;
        protected boolean expanded = false;

        protected String badgeText;
        protected String cubaId;

        protected List<String> styles;

        protected Consumer<MenuItemTriggeredEvent> command;

        protected List<MenuItem> children = new ArrayList<>(4);

        protected MenuItem parent;

        protected PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

        public MenuItem() {
        }

        public MenuItem(String caption) {
            this.caption = caption;
        }

        public MenuItem(String caption, Resource icon) {
            this.caption = caption;
            this.icon = icon;
        }

        public MenuItem(String caption, @Nullable Resource icon, @Nullable Consumer<MenuItemTriggeredEvent> command) {
            this.caption = caption;
            this.icon = icon;
            this.command = command;
        }

        public String getCaption() {
            return caption;
        }

        public void setCaption(String caption) {
            if (!Objects.equals(this.caption, caption)) {
                String oldCaption = this.caption;
                this.caption = caption;
                propertyChangeSupport.firePropertyChange(MENU_ITEM_CAPTION, oldCaption, caption);
            }
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            if (!Objects.equals(this.description, description)) {
                String oldDescription = this.description;
                this.description = description;
                propertyChangeSupport.firePropertyChange(MENU_ITEM_DESCRIPTION, oldDescription, description);
            }
        }

        public Resource getIcon() {
            return icon;
        }

        public void setIcon(Resource icon) {
            if (this.icon != icon) {
                Resource oldIcon = this.icon;
                this.icon = icon;
                propertyChangeSupport.firePropertyChange(MENU_ITEM_ICON, oldIcon, icon);
            }
        }

        public boolean isCaptionAsHtml() {
            return captionAsHtml;
        }

        public void setCaptionAsHtml(boolean captionAsHtml) {
            if (this.captionAsHtml != captionAsHtml) {
                boolean oldCaptionAsHtml = this.captionAsHtml;
                this.captionAsHtml = captionAsHtml;
                propertyChangeSupport.firePropertyChange(MENU_ITEM_CAPTION_AS_HTML, oldCaptionAsHtml, captionAsHtml);
            }
        }

        public boolean isVisible() {
            return visible;
        }

        public void setVisible(boolean visible) {
            if (this.visible != visible) {
                boolean oldVisible = this.visible;
                this.visible = visible;
                propertyChangeSupport.firePropertyChange(MENU_ITEM_VISIBLE, oldVisible, visible);
            }
        }

        public boolean isExpanded() {
            return expanded;
        }

        public void setExpanded(boolean expanded) {
            if (this.expanded != expanded) {
                boolean oldExpanded = this.expanded;
                this.expanded = expanded;
                propertyChangeSupport.firePropertyChange(MENU_ITEM_EXPANDED, oldExpanded, expanded);
            }
        }

        public String getStyleName() {
            String s = "";
            if (this.styles != null) {
                s = StringUtils.join(this.styles, " ");
            }
            return s;
        }

        public void setStyleName(String style) {
            String oldStyleName = this.getStyleName();

            if (style == null || "".equals(style)) {
                this.styles = null;
                String newStyleName = getStyleName();

                if (!Objects.equals(oldStyleName, newStyleName)) {
                    propertyChangeSupport.firePropertyChange(MENU_ITEM_STYLE_NAME, oldStyleName, newStyleName);
                }
                return;
            }
            if (this.styles == null) {
                this.styles = new ArrayList<>();
            }

            List<String> styles = this.styles;
            styles.clear();
            StringTokenizer tokenizer = new StringTokenizer(style, " ");
            while (tokenizer.hasMoreTokens()) {
                styles.add(tokenizer.nextToken());
            }

            String newStyleName = getStyleName();
            if (!Objects.equals(oldStyleName, newStyleName)) {
                propertyChangeSupport.firePropertyChange(MENU_ITEM_STYLE_NAME, oldStyleName, newStyleName);
            }
        }

        public String getCubaId() {
            return cubaId;
        }

        public void setCubaId(String cubaId) {
            if (!Objects.equals(this.cubaId, cubaId)) {
                String oldCubaId = this.cubaId;
                this.cubaId = cubaId;
                propertyChangeSupport.firePropertyChange(MENU_ITEM_CUBA_ID, oldCubaId, cubaId);
            }
        }

        public String getBadgeText() {
            return badgeText;
        }

        public void setBadgeText(String badgeText) {
            if (!Objects.equals(this.badgeText, badgeText)) {
                String oldBadgeText = this.badgeText;
                this.badgeText = badgeText;
                propertyChangeSupport.firePropertyChange(MENU_ITEM_BADGE_TEXT, oldBadgeText, badgeText);
            }
        }

        public Consumer<MenuItemTriggeredEvent> getCommand() {
            return command;
        }

        public void setCommand(Consumer<MenuItemTriggeredEvent> command) {
            if (this.command != command) {
                Consumer<MenuItemTriggeredEvent> oldCommand = this.command;
                this.command = command;
                propertyChangeSupport.firePropertyChange(MENU_ITEM_COMMAND, oldCommand, command);
            }
        }

        public void addChildItem(MenuItem menuItem) {
            if (children.contains(menuItem)) {
                children.remove(menuItem);
            }

            List<MenuItem> childrenOld = new ArrayList<>(children);

            children.add(menuItem);
            menuItem.setParent(this);

            propertyChangeSupport.firePropertyChange(MENU_ITEM_CHILDREN, childrenOld,
                    Collections.unmodifiableList(children));
        }

        public void addChildItem(MenuItem menuItem, int index) {
            if (children.contains(menuItem)) {
                int existingIndex = children.indexOf(menuItem);
                if (index > existingIndex) {
                    index--;
                }

                children.remove(menuItem);
            }

            List<MenuItem> childrenOld = new ArrayList<>(children);

            children.add(index, menuItem);
            menuItem.setParent(this);

            propertyChangeSupport.firePropertyChange(MENU_ITEM_CHILDREN, childrenOld,
                    Collections.unmodifiableList(children));
        }

        public void removeChildItem(MenuItem menuItem) {
            List<MenuItem> childrenOld = new ArrayList<>(children);

            children.remove(menuItem);
            menuItem.setParent(null);

            propertyChangeSupport.firePropertyChange(MENU_ITEM_CHILDREN, childrenOld,
                    Collections.unmodifiableList(children));
        }

        public void removeChildItem(int index) {
            List<MenuItem> childrenOld = new ArrayList<>(children);

            MenuItem removed = children.remove(index);
            removed.setParent(null);

            propertyChangeSupport.firePropertyChange(MENU_ITEM_CHILDREN, childrenOld,
                    Collections.unmodifiableList(children));
        }

        public List<MenuItem> getChildren() {
            return Collections.unmodifiableList(children);
        }

        public boolean hasChildren() {
            return !children.isEmpty();
        }

        public MenuItem getParent() {
            return parent;
        }

        public void setParent(MenuItem parent) {
            this.parent = parent;
        }

        public void addPropertyChangeListener(PropertyChangeListener listener) {
            propertyChangeSupport.addPropertyChangeListener(listener);
        }

        public void removePropertyChangeListener(PropertyChangeListener listener) {
            propertyChangeSupport.removePropertyChangeListener(listener);
        }

        public void removeStyleName(String style) {
            String oldStyleName = this.getStyleName();

            if (this.styles != null) {
                StringTokenizer tokenizer = new StringTokenizer(style, " ");
                while (tokenizer.hasMoreTokens()) {
                    this.styles.remove(tokenizer.nextToken());
                }
            }

            String newStyleName = getStyleName();
            if (!Objects.equals(oldStyleName, newStyleName)) {
                propertyChangeSupport.firePropertyChange(MENU_ITEM_STYLE_NAME, oldStyleName, newStyleName);
            }
        }

        public void addStyleName(String style) {
            if (style == null || "".equals(style)) {
                return;
            }
            if (style.contains(" ")) {
                // Split space separated style names and add them one by one.
                StringTokenizer tokenizer = new StringTokenizer(style, " ");
                while (tokenizer.hasMoreTokens()) {
                    addStyleName(tokenizer.nextToken());
                }
                return;
            }

            String oldStyleName = this.getStyleName();
            if (this.styles == null) {
                this.styles = new ArrayList<>();
            }
            List<String> styles = this.styles;
            if (!styles.contains(style)) {
                styles.add(style);
            }

            String newStyleName = getStyleName();
            if (!Objects.equals(oldStyleName, newStyleName)) {
                propertyChangeSupport.firePropertyChange(MENU_ITEM_STYLE_NAME, oldStyleName, newStyleName);
            }
        }
    }
}