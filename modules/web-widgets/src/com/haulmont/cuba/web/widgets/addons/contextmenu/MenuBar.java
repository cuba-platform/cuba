/*
 * Copyright 2000-2014 Vaadin Ltd.
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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Tag;

import com.vaadin.server.PaintException;
import com.vaadin.server.PaintTarget;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.menubar.MenuBarConstants;
import com.vaadin.shared.ui.menubar.MenuBarState;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component.Focusable;
import com.vaadin.ui.LegacyComponent;
import com.vaadin.ui.declarative.DesignAttributeHandler;
import com.vaadin.ui.declarative.DesignContext;

/**
 * <p>
 * A class representing a horizontal menu bar. The menu can contain MenuItem
 * objects, which in turn can contain more MenuBars. These sub-level MenuBars
 * are represented as vertical menu.
 * </p>
 */
@SuppressWarnings({ "serial", "deprecation" })
public class MenuBar extends AbstractComponent
        implements Menu, LegacyComponent, Focusable {

    private MenuItem moreItem;

    private boolean openRootOnHover;

    @Override
    protected MenuBarState getState() {
        return (MenuBarState) super.getState();
    }

    @Override
    protected MenuBarState getState(boolean markAsDirty) {
        return (MenuBarState) super.getState(markAsDirty);
    }

    /** Paint (serialise) the component for the client. */
    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        target.addAttribute(MenuBarConstants.OPEN_ROOT_MENU_ON_HOWER,
                openRootOnHover);

        if (isHtmlContentAllowed()) {
            target.addAttribute(MenuBarConstants.HTML_CONTENT_ALLOWED, true);
        }

        target.startTag("options");

        if (getWidth() > -1) {
            target.startTag("moreItem");
            target.addAttribute("text", moreItem.getText());
            if (moreItem.getIcon() != null) {
                target.addAttribute("icon", moreItem.getIcon());
            }
            target.endTag("moreItem");
        }

        target.endTag("options");
        target.startTag("items");

        // This generates the tree from the contents of the menu
        for (MenuItem item : getItems()) {
            paintItem(target, item);
        }

        target.endTag("items");
    }

    private void paintItem(PaintTarget target, MenuItem item)
            throws PaintException {
        if (!item.isVisible()) {
            return;
        }

        target.startTag("item");

        target.addAttribute("id", item.getId());

        if (item.getStyleName() != null) {
            target.addAttribute(MenuBarConstants.ATTRIBUTE_ITEM_STYLE,
                    item.getStyleName());
        }

        if (item.isSeparator()) {
            target.addAttribute("separator", true);
        } else {
            target.addAttribute("text", item.getText());

            Command command = item.getCommand();
            if (command != null) {
                target.addAttribute("command", true);
            }

            Resource icon = item.getIcon();
            if (icon != null) {
                target.addAttribute(MenuBarConstants.ATTRIBUTE_ITEM_ICON, icon);
            }

            if (!item.isEnabled()) {
                target.addAttribute(MenuBarConstants.ATTRIBUTE_ITEM_DISABLED,
                        true);
            }

            String description = item.getDescription();
            if (description != null && description.length() > 0) {
                target.addAttribute(MenuBarConstants.ATTRIBUTE_ITEM_DESCRIPTION,
                        description);
            }
            if (item.isCheckable()) {
                // if the "checked" attribute is present (either true or false),
                // the item is checkable
                target.addAttribute(MenuBarConstants.ATTRIBUTE_CHECKED,
                        item.isChecked());
            }
            if (item.hasChildren()) {
                for (MenuItem child : item.getChildren()) {
                    paintItem(target, child);
                }
            }

        }

        target.endTag("item");
    }

    /** Deserialize changes received from client. */
    @Override
    public void changeVariables(Object source, Map<String, Object> variables) {
        Stack<MenuItem> items = new Stack<MenuItem>();
        boolean found = false;

        if (variables.containsKey("clickedId")) {

            Integer clickedId = (Integer) variables.get("clickedId");
            Iterator<MenuItem> itr = getItems().iterator();
            while (itr.hasNext()) {
                items.push(itr.next());
            }

            MenuItem tmpItem = null;

            // Go through all the items in the menu
            while (!found && !items.empty()) {
                tmpItem = items.pop();
                found = (clickedId.intValue() == tmpItem.getId());

                if (tmpItem.hasChildren()) {
                    itr = tmpItem.getChildren().iterator();
                    while (itr.hasNext()) {
                        items.push(itr.next());
                    }
                }

            } // while

            // If we got the clicked item, launch the command.
            if (found && tmpItem.isEnabled()) {
                if (tmpItem.isCheckable()) {
                    tmpItem.setChecked(!tmpItem.isChecked());
                }
                if (null != tmpItem.getCommand()) {
                    tmpItem.getCommand().menuSelected(tmpItem);
                }
            }
        } // if
    }// changeVariables

    /**
     * Constructs an empty, horizontal menu
     */
    public MenuBar() {
        setMoreMenuItem(null);
    }

    @Override
    public int getTabIndex() {
        return getState(false).tabIndex;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.vaadin.ui.Component.Focusable#setTabIndex(int)
     */
    @Override
    public void setTabIndex(int tabIndex) {
        getState().tabIndex = tabIndex;
    }

    @Override
    public void focus() {
        // Overridden only to make public
        super.focus();
    }

    @Override
    public void writeDesign(Element design, DesignContext designContext) {
        super.writeDesign(design, designContext);
        for (MenuItem item : getItems()) {
            design.appendChild(createMenuElement(item, designContext));
        }

        // in many cases there seems to be an empty more menu item
        if (getMoreMenuItem() != null
                && !getMoreMenuItem().getText().isEmpty()) {
            Element moreMenu = createMenuElement(getMoreMenuItem(),
                    designContext);
            moreMenu.attr("more", "");
            design.appendChild(moreMenu);
        }

        if (!isHtmlContentAllowed()) {
            design.attr(DESIGN_ATTR_PLAIN_TEXT, "");
        }
    }

    protected Element createMenuElement(MenuItem item,
            DesignContext designContext) {
        Element menuElement = new Element(Tag.valueOf("menu"), "");
        // Defaults
        MenuItem def = new MenuItemImpl("", null, null);

        Attributes attr = menuElement.attributes();
        DesignAttributeHandler.writeAttribute("icon", attr, item.getIcon(),
                def.getIcon(), Resource.class, designContext);
        DesignAttributeHandler.writeAttribute("disabled", attr,
                !item.isEnabled(), !def.isEnabled(), boolean.class,
                designContext);
        DesignAttributeHandler.writeAttribute("visible", attr, item.isVisible(),
                def.isVisible(), boolean.class, designContext);
        DesignAttributeHandler.writeAttribute("separator", attr,
                item.isSeparator(), def.isSeparator(), boolean.class,
                designContext);
        DesignAttributeHandler.writeAttribute("checkable", attr,
                item.isCheckable(), def.isCheckable(), boolean.class,
                designContext);
        DesignAttributeHandler.writeAttribute("checked", attr, item.isChecked(),
                def.isChecked(), boolean.class, designContext);
        DesignAttributeHandler.writeAttribute("description", attr,
                item.getDescription(), def.getDescription(), String.class,
                designContext);
        DesignAttributeHandler.writeAttribute("style-name", attr,
                item.getStyleName(), def.getStyleName(), String.class,
                designContext);

        menuElement.append(item.getText());

        if (item.hasChildren()) {
            for (MenuItem subMenu : item.getChildren()) {
                menuElement
                        .appendChild(createMenuElement(subMenu, designContext));
            }
        }

        return menuElement;
    }

    protected MenuItem readMenuElement(Element menuElement, MenuItem parent) {
        Resource icon = null;
        if (menuElement.hasAttr("icon")) {
            icon = DesignAttributeHandler.getFormatter()
                    .parse(menuElement.attr("icon"), Resource.class);
        }

        String caption = "";
        List<Element> subMenus = new ArrayList<Element>();
        for (Node node : menuElement.childNodes()) {
            if (node instanceof Element
                    && ((Element) node).tagName().equals("menu")) {
                subMenus.add((Element) node);
            } else {
                caption += node.toString();
            }
        }

        MenuItemImpl menu = new MenuItemImpl(parent, caption.trim(), icon,
                null);

        Attributes attr = menuElement.attributes();
        if (menuElement.hasAttr("icon")) {
            menu.setIcon(DesignAttributeHandler.readAttribute("icon", attr,
                    Resource.class));
        }
        if (menuElement.hasAttr("disabled")) {
            menu.setEnabled(!DesignAttributeHandler.readAttribute("disabled",
                    attr, boolean.class));
        }
        if (menuElement.hasAttr("visible")) {
            menu.setVisible(DesignAttributeHandler.readAttribute("visible",
                    attr, boolean.class));
        }
        if (menuElement.hasAttr("separator")) {
            menu.setSeparator(DesignAttributeHandler.readAttribute("separator",
                    attr, boolean.class));
        }
        if (menuElement.hasAttr("checkable")) {
            menu.setCheckable(DesignAttributeHandler.readAttribute("checkable",
                    attr, boolean.class));
        }
        if (menuElement.hasAttr("checked")) {
            menu.setChecked(DesignAttributeHandler.readAttribute("checked",
                    attr, boolean.class));
        }
        if (menuElement.hasAttr("description")) {
            menu.setDescription(DesignAttributeHandler
                    .readAttribute("description", attr, String.class));
        }
        if (menuElement.hasAttr("style-name")) {
            menu.setStyleName(DesignAttributeHandler.readAttribute("style-name",
                    attr, String.class));
        }

        if (!subMenus.isEmpty()) {
            menu.setChildren(new ArrayList<MenuItem>());
        }

        for (Element subMenu : subMenus) {
            MenuItem newItem = readMenuElement(subMenu, menu);

            menu.getChildren().add(newItem);
        }

        return menu;
    }

    /**
     * Set the item that is used when collapsing the top level menu. All
     * "overflowing" items will be added below this. The item command will be
     * ignored. If set to null, the default item with a downwards arrow is used.
     *
     * The item command (if specified) is ignored.
     *
     * @param item
     */
    public void setMoreMenuItem(MenuItem item) {
        if (item != null) {
            moreItem = item;
        } else {
            moreItem = new MenuItemImpl("", null, null);
        }
        markAsDirty();
    }

    /**
     * Get the MenuItem used as the collapse menu item.
     *
     * @return
     */
    public MenuItem getMoreMenuItem() {
        return moreItem;
    }

    /**
     * Using this method menubar can be put into a special mode where top level
     * menus opens without clicking on the menu, but automatically when mouse
     * cursor is moved over the menu. In this mode the menu also closes itself
     * if the mouse is moved out of the opened menu.
     * <p>
     * Note, that on touch devices the menu still opens on a click event.
     *
     * @param autoOpenTopLevelMenu
     *            true if menus should be opened without click, the default is
     *            false
     */
    public void setAutoOpen(boolean autoOpenTopLevelMenu) {
        if (autoOpenTopLevelMenu != openRootOnHover) {
            openRootOnHover = autoOpenTopLevelMenu;
            markAsDirty();
        }
    }

    /**
     * Detects whether the menubar is in a mode where top level menus are
     * automatically opened when the mouse cursor is moved over the menu.
     * Normally root menu opens only by clicking on the menu. Submenus always
     * open automatically.
     *
     * @return true if the root menus open without click, the default is false
     */
    public boolean isAutoOpen() {
        return openRootOnHover;
    }

    @Override
    public void readDesign(Element design, DesignContext designContext) {
        super.readDesign(design, designContext);

        for (Element itemElement : design.children()) {
            if (itemElement.tagName().equals("menu")) {
                MenuItem menuItem = readMenuElement(itemElement, null);
                if (itemElement.hasAttr("more")) {
                    setMoreMenuItem(menuItem);
                } else {
                    getItems().add(menuItem);
                }
            }
        }

        setHtmlContentAllowed(!design.hasAttr(DESIGN_ATTR_PLAIN_TEXT));
    }

    @Override
    protected Collection<String> getCustomAttributes() {
        Collection<String> result = super.getCustomAttributes();
        result.add(DESIGN_ATTR_PLAIN_TEXT);
        result.add("html-content-allowed");
        return result;
    }

    /**** Delegates to AbstractMenu ****/

    private Menu menu = new AbstractMenu(this);

    @Override
    public MenuItem addItem(String caption, Command command) {
        return menu.addItem(caption, command);
    }

    @Override
    public MenuItem addItem(String caption, Resource icon, Command command) {
        return menu.addItem(caption, icon, command);
    }

    @Override
    public MenuItem addItemBefore(String caption, Resource icon,
            Command command, MenuItem itemToAddBefore) {
        return menu.addItemBefore(caption, icon, command, itemToAddBefore);
    }

    @Override
    public List<MenuItem> getItems() {
        return menu.getItems();
    }

    @Override
    public void removeItem(MenuItem item) {
        menu.removeItem(item);
    }

    @Override
    public void removeItems() {
        menu.removeItems();
    }

    @Override
    public int getSize() {
        return menu.getSize();
    }

    @Override
    public void setHtmlContentAllowed(boolean htmlContentAllowed) {
        menu.setHtmlContentAllowed(htmlContentAllowed);
    }

    @Override
    public boolean isHtmlContentAllowed() {
        return menu.isHtmlContentAllowed();
    }

    /**** End of deletates to AbstractMenu ****/

    // public class MenuItem extends MenuItemImpl implements Serializable {
    // public MenuItem(String caption, Resource icon, Command command) {
    // super(caption, icon, command);
    // // Auto-generated constructor stub
    // }
    // }
}// class MenuBar
