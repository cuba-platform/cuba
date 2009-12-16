/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 06.02.2009 12:21:48
 * $Id$
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.core.global.MessageProvider;

/**
 * Base class for actions
 */
public abstract class AbstractAction implements Action {

    private String id;

    private String icon;

    private boolean enabled = true;

    private Component.ActionOwner owner;

    protected AbstractAction(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getCaption() {
        return MessageProvider.getMessage(getClass(), id);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (owner != null && owner instanceof Component) {
            ((Component) owner).setEnabled(enabled);
        }
    }

    public Component.ActionOwner getOwner() {
        return owner;
    }

    public void setOwner(Component.ActionOwner actionOwner) {
        this.owner = actionOwner;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
