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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.haulmont.cuba.web.widgets.addons.contextmenu.Menu.Command;
import com.vaadin.server.Resource;

/**
 * A composite class for menu items and sub-menus. You can set commands to be
 * fired on user click by implementing the {@link Command} interface. You
 * can also add multiple MenuItems to a MenuItem and create a sub-menu.
 * 
 */
@SuppressWarnings("serial")
class MenuItemImpl implements Serializable, MenuItem {

    /** Private members * */
    private final int itsId;
    private Command itsCommand;
    private String itsText;
    private List<MenuItem> itsChildren;
    private Resource itsIcon;
    private MenuItem itsParent;
    private boolean enabled = true;
    private boolean visible = true;
    private boolean isSeparator = false;
    private String styleName;
    private String description;
    private boolean checkable = false;
    private boolean checked = false;

    private void markAsDirty() {
        // FIXME we need to delegate this to the MenuBar or better convert the
        // menubar to Vaadin 7 communication
        // and remove this method
    }

    /**
     * Constructs a new menu item that can optionally have an icon and a command
     * associated with it. Icon and command can be null, but a caption must be
     * given.
     * 
     * @param caption
     *            The text associated with the command
     * @param command
     *            The command to be fired
     * @throws IllegalArgumentException
     */
    public MenuItemImpl(String caption, Resource icon, Command command) {
        if (caption == null) {
            throw new IllegalArgumentException("caption cannot be null");
        }

        itsId = getNextId();
        itsText = caption;
        itsIcon = icon;
        itsCommand = command;
    }

    public MenuItemImpl(MenuItem parent, String trim, Resource icon,
            Command object) {
        this(trim, icon, object);
        setParent(parent);
    }

    protected int getNextId() {
        // FIXME is this good enough? maybe just random?
        return UUID.randomUUID().hashCode();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.example.contextmenu.menubar.MenuItem#hasChildren()
     */
    @Override
    public boolean hasChildren() {
        return !isSeparator() && itsChildren != null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.example.contextmenu.menubar.MenuItem#addSeparator()
     */
    @Override
    public MenuItem addSeparator() {
        MenuItem item = addItem(true, "", null, null);
        return item;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.example.contextmenu.menubar.MenuItem#addSeparatorBefore(com.example
     * .contextmenu.menubar.MenuItem)
     */
    @Override
    public MenuItem addSeparatorBefore(MenuItem itemToAddBefore) {
        MenuItem item = addItemBefore(true, "", null, null, itemToAddBefore);
        return item;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.example.contextmenu.menubar.MenuItem#addItem(java.lang.String,
     * com.example.contextmenu.menubar.Menu.Command)
     */
    @Override
    public MenuItem addItem(String caption, Command command) {
        return addItem(caption, null, command);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.example.contextmenu.menubar.MenuItem#addItem(java.lang.String,
     * com.vaadin.server.Resource, com.example.contextmenu.menubar.Menu.Command)
     */
    @Override
    public MenuItem addItem(String caption, Resource icon, Command command)
            throws IllegalStateException {
        return addItem(false, caption, icon, command);
    }

    private MenuItem addItem(boolean separator, String caption, Resource icon,
            Command command) throws IllegalStateException {
        if (isSeparator()) {
            throw new UnsupportedOperationException(
                    "Cannot add items to a separator");
        }
        if (isCheckable()) {
            throw new IllegalStateException(
                    "A checkable item cannot have children");
        }
        if (caption == null) {
            throw new IllegalArgumentException("Caption cannot be null");
        }

        if (itsChildren == null) {
            itsChildren = new ArrayList<MenuItem>();
        }

        MenuItemImpl newItem = new MenuItemImpl(this, caption, icon, command);

        newItem.setSeparator(separator);

        itsChildren.add(newItem);

        markAsDirty();

        return newItem;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.example.contextmenu.menubar.MenuItem#addItemBefore(java.lang.String,
     * com.vaadin.server.Resource, com.example.contextmenu.menubar.Menu.Command,
     * com.example.contextmenu.menubar.MenuItem)
     */
    @Override
    public MenuItem addItemBefore(String caption, Resource icon,
            Command command, MenuItem itemToAddBefore) {
        return addItemBefore(false, caption, icon, command, itemToAddBefore);
    }

    private MenuItem addItemBefore(boolean separator, String caption,
            Resource icon, Command command, MenuItem itemToAddBefore)
            throws IllegalStateException {
        if (isCheckable()) {
            throw new IllegalStateException(
                    "A checkable item cannot have children");
        }
        MenuItem newItem = null;

        if (hasChildren() && itsChildren.contains(itemToAddBefore)) {
            int index = itsChildren.indexOf(itemToAddBefore);
            newItem = new MenuItemImpl(this, caption, icon, command);
            itsChildren.add(index, newItem);
        } else {
            newItem = addItem(caption, icon, command);
        }

        markAsDirty();

        return newItem;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.example.contextmenu.menubar.MenuItem#getCommand()
     */
    @Override
    public Command getCommand() {
        return itsCommand;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.example.contextmenu.menubar.MenuItem#getIcon()
     */
    @Override
    public Resource getIcon() {
        return itsIcon;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.example.contextmenu.menubar.MenuItem#getParent()
     */
    @Override
    public MenuItem getParent() {
        return itsParent;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.example.contextmenu.menubar.MenuItem#getChildren()
     */
    @Override
    public List<MenuItem> getChildren() {
        return itsChildren;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.example.contextmenu.menubar.MenuItem#getText()
     */
    @Override
    public String getText() {
        return itsText;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.example.contextmenu.menubar.MenuItem#getSize()
     */
    @Override
    public int getSize() {
        if (itsChildren != null) {
            return itsChildren.size();
        }
        return -1;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.example.contextmenu.menubar.MenuItem#getId()
     */
    @Override
    public int getId() {
        return itsId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.example.contextmenu.menubar.MenuItem#setCommand(com.example.
     * contextmenu .menubar.Menu.Command)
     */
    @Override
    public void setCommand(Command command) {
        itsCommand = command;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.example.contextmenu.menubar.MenuItem#setIcon(com.vaadin.server.
     * Resource )
     */
    @Override
    public void setIcon(Resource icon) {
        itsIcon = icon;
        markAsDirty();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.example.contextmenu.menubar.MenuItem#setText(java.lang.String)
     */
    @Override
    public void setText(String text) {
        if (text != null) {
            itsText = text;
        }
        markAsDirty();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.example.contextmenu.menubar.MenuItem#removeChild(com.example.
     * contextmenu .menubar.MenuItem)
     */
    @Override
    public void removeChild(MenuItem item) {
        if (item != null && itsChildren != null) {
            itsChildren.remove(item);
            if (itsChildren.isEmpty()) {
                itsChildren = null;
            }
            markAsDirty();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.example.contextmenu.menubar.MenuItem#removeChildren()
     */
    @Override
    public void removeChildren() {
        if (itsChildren != null) {
            itsChildren.clear();
            itsChildren = null;
            markAsDirty();
        }
    }

    /**
     * Set the parent of this item. This is called by the addItem method.
     * 
     * @param parent
     *            The parent item
     */
    protected void setParent(MenuItem parent) {
        itsParent = parent;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.example.contextmenu.menubar.MenuItem#setEnabled(boolean)
     */
    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        markAsDirty();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.example.contextmenu.menubar.MenuItem#isEnabled()
     */
    @Override
    public boolean isEnabled() {
        return enabled;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.example.contextmenu.menubar.MenuItem#setVisible(boolean)
     */
    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
        markAsDirty();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.example.contextmenu.menubar.MenuItem#isVisible()
     */
    @Override
    public boolean isVisible() {
        return visible;
    }

    protected void setSeparator(boolean isSeparator) {
        this.isSeparator = isSeparator;
        markAsDirty();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.example.contextmenu.menubar.MenuItem#isSeparator()
     */
    @Override
    public boolean isSeparator() {
        return isSeparator;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.example.contextmenu.menubar.MenuItem#setStyleName(java.lang.String)
     */
    @Override
    public void setStyleName(String styleName) {
        this.styleName = styleName;
        markAsDirty();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.example.contextmenu.menubar.MenuItem#getStyleName()
     */
    @Override
    public String getStyleName() {
        return styleName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.example.contextmenu.menubar.MenuItem#setDescription(java.lang.String)
     */
    @Override
    public void setDescription(String description) {
        this.description = description;
        markAsDirty();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.example.contextmenu.menubar.MenuItem#getDescription()
     */
    @Override
    public String getDescription() {
        return description;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.example.contextmenu.menubar.MenuItem#isCheckable()
     */
    @Override
    public boolean isCheckable() {
        return checkable;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.example.contextmenu.menubar.MenuItem#setCheckable(boolean)
     */
    @Override
    public void setCheckable(boolean checkable) throws IllegalStateException {
        if (hasChildren()) {
            throw new IllegalStateException(
                    "A menu item with children cannot be checkable");
        }
        this.checkable = checkable;
        markAsDirty();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.example.contextmenu.menubar.MenuItem#isChecked()
     */
    @Override
    public boolean isChecked() {
        return checked;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.example.contextmenu.menubar.MenuItem#setChecked(boolean)
     */
    @Override
    public void setChecked(boolean checked) {
        this.checked = checked;
        markAsDirty();
    }

    protected void setChildren(List<MenuItem> children) {
        this.itsChildren = children;
    }
}// class MenuItem