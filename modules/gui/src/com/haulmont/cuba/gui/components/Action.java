/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components;

import java.beans.PropertyChangeListener;
import java.util.Collection;

/**
 * A named listener to UI events
 *
 * @author abramov
 * @version $Id$
 */
public interface Action {

    public static final String PROP_CAPTION = "caption";
    public static final String PROP_ICON = "icon";
    public static final String PROP_ENABLED = "enabled";
    public static final String PROP_VISIBLE = "visible";

    /**
     * @return  action's identifier
     */
    String getId();

    /**
     * @return  action's localized caption
     */
    String getCaption();
    void setCaption(String caption);

    /**
     *
     * @return action's shortcut
     */
    KeyCombination getShortcut();
    void setShortcut(KeyCombination shortcut);

    /**
     * Set shortcut from string representation.
     *
     * @param shortcut string of type "Modifiers-Key", e.g. "Alt-N". Case-insensitive.
     */
    void setShortcut(String shortcut);

    /**
     * @return  action's icon
     */
    String getIcon();
    void setIcon(String icon);

    /**
     * @return  whether the action is currently enabled
     */
    boolean isEnabled();
    void setEnabled(boolean enabled);

    /**
     * @return  whether the action is currently visible
     */
    boolean isVisible();
    void setVisible(boolean visible);

    /**
     * Refresh internal state of the action to initialize enabled, visible, caption, icon, etc. properties depending
     * on programmatically set values and user permissions set at runtime.
     *
     * <p/> For example, this method is called by visual components holding actions when they are connected to
     * datasources. At this moment the action can find out what entity it is connected to and change its state
     * according to the user permissions.
     */
    void refreshState();

    /**
     * @return  a single component owning the action. If there are several owners, first will be returned.
     */
    Component.ActionOwner getOwner();

    /**
     * @return the collection of owners
     */
    Collection<Component.ActionOwner> getOwners();

    /**
     * Add an owner component.
     * @param actionOwner   owner component
     */
    void addOwner(Component.ActionOwner actionOwner);

    /**
     * Remove an owner component.
     * @param actionOwner   owner component
     */
    void removeOwner(Component.ActionOwner actionOwner);

    /**
     * This method is invoked by owning component.
     * @param component invoking component
     */
    void actionPerform(Component component);

    /**
     * Adds a listener to be notified about Enabled, Caption or Icon property changes.
     *
     * @param listener  a <code>PropertyChangeListener</code> object
     */
    public void addPropertyChangeListener(PropertyChangeListener listener);

    /**
     * Removes a listener.
     *
     * @param listener  a <code>PropertyChangeListener</code> object
     * @see #addPropertyChangeListener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener);
}