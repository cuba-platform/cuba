/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 20.01.2009 11:33:34
 * $Id$
 */
package com.haulmont.cuba.gui.components;

/**
 * A named listener to UI events
 */
public interface Action {
    String getId();
    String getCaption();

    String getIcon();
    void setIcon(String icon);

    boolean isEnabled();
    void setEnabled(boolean enabled);

    Component.ActionOwner getOwner();
    void setOwner(Component.ActionOwner actionOwner);

    void actionPerform(Component component);
}
