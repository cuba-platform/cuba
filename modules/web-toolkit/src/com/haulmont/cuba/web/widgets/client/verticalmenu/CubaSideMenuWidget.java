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

package com.haulmont.cuba.web.widgets.client.verticalmenu;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.*;
import com.vaadin.client.BrowserInfo;
import com.vaadin.client.WidgetUtil;
import com.vaadin.client.ui.Icon;
import elemental.json.JsonArray;
import elemental.json.JsonObject;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class CubaSideMenuWidget extends FocusableFlowPanel
        implements KeyPressHandler, KeyDownHandler, FocusHandler, HasEnabled, BlurHandler {

    protected static final String CLASS_NAME = "c-sidemenu";

    protected boolean enabled = true;
    protected boolean focused = true;

    protected MenuItemWidget focusedItem;
    protected MenuItemWidget selectedItem;

    public Consumer<String> menuItemClickHandler;
    public BiConsumer<String, Boolean> headerItemExpandHandler;
    public Function<String, Icon> menuItemIconSupplier;

    public boolean selectOnTrigger;
    public boolean singleExpandedMenu;

    public CubaSideMenuWidget() {
        setStylePrimaryName(CLASS_NAME);

        sinkEvents(Event.ONMOUSEOVER | Event.ONMOUSEOUT);

        // Navigation is only handled by the root bar
        addFocusHandler(this);
        addBlurHandler(this);

        /*
         * Firefox auto-repeat works correctly only if we use a key press
         * handler, other browsers handle it correctly when using a key down handler
         */
        if (BrowserInfo.get().isGecko() && BrowserInfo.get().getGeckoVersion() < 65.0) {
            addKeyPressHandler(this);
        } else {
            addKeyDownHandler(this);
        }
    }

    public void setFocusedItem(MenuItemWidget focusedItem) {
        if (focusedItem != this.focusedItem && this.focusedItem != null) {
            this.focusedItem.setFocused(false);
        }
        if (focusedItem != null) {
            focusedItem.setFocused(true);
        }
        this.focusedItem = focusedItem;
    }

    public MenuItemWidget getFocusedItem() {
        return focusedItem;
    }

    public MenuItemWidget getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(MenuItemWidget selectedItem) {
        if (selectedItem != this.selectedItem && this.selectedItem != null) {
            this.selectedItem.setSelected(false);
        }
        if (selectedItem != null) {
            selectedItem.setSelected(true);
        }
        this.selectedItem = selectedItem;
    }

    @Override
    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);

        if (isEnabled()) {
            switch (DOM.eventGetType(event)) {
                case Event.ONMOUSEOVER:
                    Element targetElement = DOM.eventGetTarget(event);
                    Object targetWidget = WidgetUtil.findWidget(targetElement, null);
                    if (targetWidget instanceof MenuItemWidget) {
                        setFocusedItem((MenuItemWidget) targetWidget);
                    }
                    break;
                case Event.ONMOUSEOUT:
                    if (!focused) {
                        setFocusedItem(null);
                    }
                    break;
            }
        }
    }

    @Override
    public void onFocus(FocusEvent event) {
        if (isEnabled() && getFocusedItem() == null) {
            if (getSelectedItem() != null) {
                setFocusedItem(getSelectedItem());
            } else if (getWidgetCount() > 0) {
                setFocusedItem((MenuItemWidget) getWidget(0));
            }
        }

        this.focused = true;
    }

    @Override
    public void onBlur(BlurEvent event) {
        setFocusedItem(null);

        this.focused = false;
    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        int keyCode = event.getNativeEvent().getKeyCode();
        if (keyCode == 0) {
            keyCode = event.getNativeEvent().getCharCode();
        }
        if (handleNavigation(keyCode, event.isControlKeyDown() || event.isMetaKeyDown(), event.isShiftKeyDown())) {
            event.preventDefault();
            event.stopPropagation();
        }
    }

    @Override
    public void onKeyPress(KeyPressEvent event) {
        int keyCode = event.getNativeEvent().getKeyCode();
        if (keyCode == 0) {
            keyCode = event.getNativeEvent().getCharCode();
        }
        if (handleNavigation(keyCode, event.isControlKeyDown() || event.isMetaKeyDown(), event.isShiftKeyDown())) {
            event.preventDefault();
            event.stopPropagation();
        }
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void buildMenu(JsonArray itemsJson) {
        setFocusedItem(null);
        setSelectedItem(null);

        Iterator<Widget> iterator = this.iterator();
        while (iterator.hasNext()) {
            iterator.next();
            iterator.remove();
        }

        addItems(itemsJson, this);
    }

    public String getTooltip(Element element) {
        Object widget = WidgetUtil.findWidget(element, null);
        if (widget instanceof MenuItemWidget) {
            return ((MenuItemWidget) widget).getDescription();
        }
        return null;
    }

    public void selectItem(String itemId) {
        if (itemId == null) {
            setSelectedItem(null);
        } else {
            walkItems(this, menuItemWidget -> {
                if (itemId.equals(menuItemWidget.getId())) {
                    setSelectedItem(menuItemWidget);
                    return true;
                }
                return false;
            });
        }
    }

    public void updateBadges(Map<String, String> badgeUpdates) {
        if (!badgeUpdates.isEmpty()) {
            Map<String, String> remainingUpdates = new HashMap<>(badgeUpdates);

            walkItems(this, menuItemWidget -> {
                String newBadgeText = remainingUpdates.remove(menuItemWidget.getId());

                if (newBadgeText != null) {
                    menuItemWidget.setBadgeText(newBadgeText);
                }
                return remainingUpdates.isEmpty();
            });
        }
    }

    protected boolean handleNavigation(int keyCode, boolean ctrl, boolean shift) {
        if (keyCode == KeyCodes.KEY_TAB) {
            setFocusedItem(null);
            return false;
        }

        if (ctrl || shift || !isEnabled()) {
            // Do not handle neither shift key, nor ctrl keys
            return false;
        }

        switch (keyCode) {
            case KeyCodes.KEY_UP:
                if (getFocusedItem() == null && getSelectedItem() != null) {
                    setFocusedItem(getSelectedItem());
                }
                if (getFocusedItem() == null && getChildren().size() > 0) {
                    MenuItemWidget widget = (MenuItemWidget) getChildren().get(0);
                    setFocusedItem(widget);
                } else {
                    // find previous sibling menu item widget
                    MenuItemWidget targetNextSibling = findPreviousMenuItem(getFocusedItem());
                    if (targetNextSibling != null) {
                        setFocusedItem(targetNextSibling);
                    }
                }
                // move up
                return true;
            case KeyCodes.KEY_DOWN:
                if (getFocusedItem() == null && getSelectedItem() != null) {
                    setFocusedItem(getSelectedItem());
                }
                if (getFocusedItem() == null) {
                    MenuItemWidget widget = (MenuItemWidget) getChildren().get(0);
                    setFocusedItem(widget);
                } else {
                    // find next sibling menu item widget
                    MenuItemWidget targetNextSibling = findNextMenuItem(getFocusedItem());
                    if (targetNextSibling != null) {
                        setFocusedItem(targetNextSibling);
                    }
                }
                // move down
                return true;
            case KeyCodes.KEY_ESCAPE:
                // collapse parent menu
                if (getFocusedItem() != null) {
                    Widget parent = getFocusedItem().getParent();
                    if (parent instanceof MenuContainerWidget) {
                        MenuItemWidget parentMenuItem = ((MenuContainerWidget) parent).getMenuItemWidget();
                        parentMenuItem.collapse();
                        setFocusedItem(parentMenuItem);
                    }
                }
                return true;
            case KeyCodes.KEY_LEFT:
                // collapse sub menu
                if (getFocusedItem() != null) {
                    getFocusedItem().collapse();
                }
                return true;
            case KeyCodes.KEY_RIGHT:
                // expand submenu
                if (getFocusedItem() != null) {
                    getFocusedItem().expand();
                }
                return true;
            case KeyCodes.KEY_SPACE:
            case KeyCodes.KEY_ENTER:
                // expand submenu
                if (getFocusedItem() != null) {
                    getFocusedItem().expandOrTrigger();
                }
                return true;
            default:
                return false;
        }
    }

    protected MenuItemWidget findPreviousMenuItem(MenuItemWidget currentItem) {
        // convert tree to flat list, find previous item
        List<MenuTreeNode> menuTree = buildVisibleTree(this);
        List<MenuItemWidget> menuItemWidgets = menuTreeToList(menuTree);

        int menuItemFlatIndex = menuItemWidgets.indexOf(currentItem);
        int previousMenuItemFlatIndex = menuItemFlatIndex - 1;
        if (previousMenuItemFlatIndex >= 0) {
            return menuItemWidgets.get(previousMenuItemFlatIndex);
        }
        return null;
    }

    protected MenuItemWidget findNextMenuItem(MenuItemWidget currentItem) {
        // convert tree to flat list, find next item
        List<MenuTreeNode> menuTree = buildVisibleTree(this);
        List<MenuItemWidget> menuItemWidgets = menuTreeToList(menuTree);

        int menuItemFlatIndex = menuItemWidgets.indexOf(currentItem);
        int nextMenuItemFlatIndex = menuItemFlatIndex + 1;
        if (nextMenuItemFlatIndex < menuItemWidgets.size()) {
            return menuItemWidgets.get(nextMenuItemFlatIndex);
        }
        return null;
    }

    protected void onMenuItemTriggered(MenuItemWidget item) {
        if (selectOnTrigger) {
            setSelectedItem(item);
        }
        if (menuItemClickHandler != null) {
            menuItemClickHandler.accept(item.getId());
        }
    }

    protected void onHeaderItemExpandChanged(MenuItemWidget item) {
        if (headerItemExpandHandler != null) {
            headerItemExpandHandler.accept(item.getId(), item.getSubMenu().isExpanded());
        }

        if (singleExpandedMenu && item.getSubMenu().isExpanded()) {
            List<MenuTreeNode> menuTree = buildVisibleTree(this);
            List<MenuItemWidget> menuItemWidgets = menuTreeToList(menuTree);

            for (MenuItemWidget itemWidget : menuItemWidgets) {
                if (itemWidget != item && itemWidget.getParent().equals(item.getParent())) {
                    itemWidget.collapse();
                }
            }
        }
    }

    protected void addItems(JsonArray items, HasWidgets container) {
        for (int i = 0; i < items.length(); i++) {
            JsonObject itemJson = items.getObject(i);

            Icon icon = null;
            String iconId = itemJson.getString("icon");
            if (menuItemIconSupplier != null && iconId != null) {
                icon = menuItemIconSupplier.apply(iconId);
            }

            boolean captionAsHtml = false;
            if (itemJson.hasKey("captionAsHtml")) {
                captionAsHtml = itemJson.getBoolean("captionAsHtml");
            }

            MenuItemWidget menuItemWidget = new MenuItemWidget(this,
                    itemJson.getString("id"),
                    icon,
                    itemJson.getString("styleName"),
                    itemJson.getString("caption"),
                    captionAsHtml);

            menuItemWidget.setDescription(itemJson.getString("description"));
            menuItemWidget.setCubaId(itemJson.getString("cubaId"));
            menuItemWidget.setBadgeText(itemJson.getString("badgeText"));

            container.add(menuItemWidget);

            JsonArray childrenItemsJson = itemJson.getArray("children");
            if (childrenItemsJson != null) {
                MenuContainerWidget menuContainerWidget = new MenuContainerWidget(this, menuItemWidget);
                addItems(childrenItemsJson, menuContainerWidget);

                container.add(menuContainerWidget);

                menuItemWidget.setSubMenu(menuContainerWidget);

                if (itemJson.hasKey("expanded")
                        && itemJson.getBoolean("expanded")) {
                    menuContainerWidget.setExpanded(true);
                }
            }
        }
    }

    protected boolean walkItems(ComplexPanel container, Function<MenuItemWidget, Boolean> walker) {
        for (Widget widget : container) {
            if (widget instanceof MenuItemWidget) {
                Boolean stopFlag = walker.apply((MenuItemWidget) widget);
                if (Boolean.TRUE.equals(stopFlag)) {
                    return true;
                }
            } else {
                MenuContainerWidget containerWidget = (MenuContainerWidget) widget;
                Boolean stopFlag = walkItems(containerWidget, walker);
                if (Boolean.TRUE.equals(stopFlag)) {
                    return true;
                }
            }
        }
        return false;
    }

    protected List<MenuTreeNode> buildVisibleTree(ComplexPanel container) {
        List<MenuTreeNode> nodes = new ArrayList<>();
        for (Widget subWidget : container) {
            if (subWidget instanceof MenuItemWidget) {
                MenuTreeNode node = new MenuTreeNode();
                node.widget = (MenuItemWidget) subWidget;

                MenuContainerWidget subMenu = node.widget.getSubMenu();
                if (subMenu != null && subMenu.isExpanded()) {
                    node.children = buildVisibleTree(subMenu);
                } else {
                    node.children = Collections.emptyList();
                }

                nodes.add(node);
            }
        }
        return nodes;
    }

    protected List<MenuItemWidget> menuTreeToList(List<MenuTreeNode> menuTree) {
        List<MenuItemWidget> list = new ArrayList<>();
        for (MenuTreeNode rootNode : menuTree) {
            menuTreeCollect(rootNode, list);
        }
        return list;
    }

    protected void menuTreeCollect(MenuTreeNode element, List<MenuItemWidget> list) {
        list.add(element.widget);
        for (MenuTreeNode data : element.children) {
            menuTreeCollect(data, list);
        }
    }

    public static class MenuItemWidget extends Widget implements ClickHandler {
        protected CubaSideMenuWidget menu;

        protected String id;
        protected Icon icon;
        protected String caption;
        protected String description;
        protected boolean focused;
        protected boolean selected;

        protected SpanElement badgeElement;
        protected SpanElement captionElement;
        protected Element thumbnailElement;

        protected MenuContainerWidget subMenu;

        public MenuItemWidget(CubaSideMenuWidget menu, String id, Icon icon, String styleName,
                              String caption, boolean captionAsHtml) {
            this.menu = menu;
            this.id = id;
            this.icon = icon;
            this.caption = caption;

            setElement(Document.get().createDivElement());

            setStylePrimaryName(menu.getStylePrimaryName() + "-item");
            addStyleDependentName("action");

            if (styleName != null) {
                for (String style : styleName.split(" ")) {
                    if (!style.isEmpty()) {
                        addStyleName(style);
                    }
                }
            }

            SpanElement wrapElement = Document.get().createSpanElement();
            wrapElement.setClassName(getStylePrimaryName() + "-wrap");

            if (icon != null) {
                wrapElement.appendChild(icon.getElement());
            }

            captionElement = createCaptionElement(caption, captionAsHtml);
            wrapElement.appendChild(captionElement);

            badgeElement = createBadgeElement();

            getElement().appendChild(wrapElement);

            addDomHandler(this, ClickEvent.getType());

            addAttachHandler(event -> {
                if (isAttached() && isRootItem()) {
                    addThumbnail();
                }
            });
        }

        protected void addThumbnail() {
            thumbnailElement = createThumbnailElement(icon, caption);

            getElement().getFirstChildElement()
                    .insertFirst(thumbnailElement);
        }

        protected boolean isRootItem() {
            Element parentElement = getElement().getParentElement();

            return parentElement != null && parentElement.hasClassName(CLASS_NAME);
        }

        protected Element createThumbnailElement(Icon icon, String caption) {
            Element thumbnailElement;
            if (icon != null) {
                thumbnailElement = (Element) icon.getElement().cloneNode(true);
                thumbnailElement.setClassName(getStylePrimaryName() + "-thumbnail-icon");
            } else {
                thumbnailElement = Document.get().createSpanElement();
                thumbnailElement.setClassName(getStylePrimaryName() + "-thumbnail");
                thumbnailElement.setInnerHTML(caption.substring(0, 1));
            }
            return thumbnailElement;
        }

        protected SpanElement createCaptionElement(String caption, boolean captionAsHtml) {
            SpanElement captionElement = Document.get().createSpanElement();
            captionElement.setClassName(getStylePrimaryName() + "-caption");
            if (caption != null) {
                if (captionAsHtml) {
                    captionElement.setInnerHTML(caption);
                } else {
                    captionElement.setInnerText(caption);
                }
            }
            return captionElement;
        }

        protected SpanElement createBadgeElement() {
            SpanElement badgeElement = Document.get().createSpanElement();
            badgeElement.setClassName(getStylePrimaryName() + "-badge");
            return badgeElement;
        }

        public void setFocused(boolean focused) {
            this.focused = focused;
            if (focused) {
                addStyleDependentName("focused");
            } else {
                removeStyleDependentName("focused");
            }
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
            if (selected) {
                addStyleDependentName("selected");
            } else {
                removeStyleDependentName("selected");
            }
        }

        public MenuContainerWidget getSubMenu() {
            return subMenu;
        }

        public void setSubMenu(MenuContainerWidget subMenu) {
            this.subMenu = subMenu;

            if (this.subMenu != null) {
                addStyleDependentName("header");
                removeStyleDependentName("action");
            } else {
                removeStyleDependentName("header");
                addStyleDependentName("action");
            }
        }

        public void setBadgeText(String badgeText) {
            if (badgeText == null || badgeText.isEmpty()) {
                if (badgeElement.getParentElement() != null) {
                    badgeElement.removeFromParent();
                }
            } else {
                if (badgeElement.getParentElement() == null) {
                    captionElement.appendChild(badgeElement);
                }
                badgeElement.setInnerText(badgeText);
            }
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setCubaId(String cubaId) {
            if (cubaId != null && !cubaId.isEmpty()) {
                getElement().setAttribute("cuba-id", cubaId);
            }
        }

        public String getId() {
            return id;
        }

        @Override
        public void onClick(ClickEvent event) {
            if (menu.isEnabled()) {
                if (subMenu == null) {
                    menu.setFocusedItem(null);
                    menu.onMenuItemTriggered(this);
                } else {
                    menu.setFocusedItem(this);
                    subMenu.triggerExpand();
                    menu.onHeaderItemExpandChanged(this);
                }
            }
        }

        public void expand() {
            if (subMenu != null) {
                subMenu.setExpanded(true);
                menu.onHeaderItemExpandChanged(this);
            }
        }

        public void collapse() {
            if (subMenu != null) {
                subMenu.setExpanded(false);
                menu.onHeaderItemExpandChanged(this);
            }
        }

        public void expandOrTrigger() {
            if (subMenu == null) {
                menu.onMenuItemTriggered(this);
            } else {
                subMenu.triggerExpand();
                menu.onHeaderItemExpandChanged(this);
            }
        }
    }

    public static class MenuContainerWidget extends FlowPanel {
        protected CubaSideMenuWidget menu;
        protected MenuItemWidget menuItemWidget;
        protected boolean expanded = false;

        public MenuContainerWidget(CubaSideMenuWidget menu, MenuItemWidget menuItemWidget) {
            this.menu = menu;
            this.menuItemWidget = menuItemWidget;
            setStylePrimaryName(menu.getStylePrimaryName() + "-submenu");
        }

        public boolean isExpanded() {
            return expanded;
        }

        public void setExpanded(boolean expanded) {
            if (this.expanded != expanded) {
                this.expanded = expanded;

                if (expanded) {
                    addStyleDependentName("open");
                    menuItemWidget.addStyleDependentName("header-open");
                } else {
                    removeStyleDependentName("open");
                    menuItemWidget.removeStyleDependentName("header-open");
                }
            }
        }

        public void triggerExpand() {
            setExpanded(!isExpanded());
        }

        public MenuItemWidget getMenuItemWidget() {
            return menuItemWidget;
        }
    }

    public static class MenuTreeNode {
        public MenuItemWidget widget;
        public List<MenuTreeNode> children;
    }
}