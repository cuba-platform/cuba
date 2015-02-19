/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.gui.WindowManager;

import java.beans.PropertyChangeListener;
import java.util.Collection;

/**
 * The <code>Action</code> interface abstracts away a function from a visual component.
 * When an action occurs, {@link #actionPerform(Component)} method is invoked.
 *
 * @author abramov
 * @version $Id$
 */
public interface Action {

    public static final String PROP_CAPTION = "caption";
    public static final String PROP_DESCRIPTION = "description";
    public static final String PROP_SHORTCUT = "shortcut";
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
     * @return  action's description
     */
    String getDescription();
    void setDescription(String description);

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
     * Invoked by owning component when an action occurs.
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

    public interface HasOpenType extends Action {

        WindowManager.OpenType getOpenType();
        void setOpenType(WindowManager.OpenType openType);
    }

    public interface UiPermissionAware extends Action {

        boolean isEnabledByUiPermissions();
        void setEnabledByUiPermissions(boolean enabledByUiPermissions);

        boolean isVisibleByUiPermissions();
        void setVisibleByUiPermissions(boolean visibleByUiPermissions);
    }

    public interface HasTarget extends Action {
        ListComponent getTarget();

        void setTarget(ListComponent target);
    }
}