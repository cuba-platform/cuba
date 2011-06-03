/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 20.01.2009 11:33:34
 * $Id$
 */
package com.haulmont.cuba.gui.components;

import java.beans.PropertyChangeListener;
import java.io.Serializable;

/**
 * A named listener to UI events
 */
public interface Action extends Serializable {

    public static final String PROP_CAPTION = "caption";
    public static final String PROP_ENABLED = "enabled";
    public static final String PROP_ICON = "icon";

    String getId();

    String getCaption();
    void setCaption(String caption);

    String getIcon();
    void setIcon(String icon);

    boolean isEnabled();
    void setEnabled(boolean enabled);

    Component.ActionOwner getOwner();
    void setOwner(Component.ActionOwner actionOwner);

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
