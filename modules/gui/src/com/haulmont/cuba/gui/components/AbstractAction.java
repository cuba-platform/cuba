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
import org.apache.commons.lang.ObjectUtils;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Base class for actions
 */
public abstract class AbstractAction implements Action {

    protected String id;

    protected String caption;

    protected String icon;

    protected boolean enabled = true;

    protected Component.ActionOwner owner;

    protected PropertyChangeSupport changeSupport;

    protected AbstractAction(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getCaption() {
        return caption == null ? MessageProvider.getMessage(getClass(), id) : caption;
    }

    public void setCaption(String caption) {
        String oldValue = this.caption;
        if (!ObjectUtils.equals(oldValue, caption)) {
            this.caption = caption;
            firePropertyChange(PROP_CAPTION, oldValue, caption);
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        boolean oldValue = this.enabled;
        if (oldValue != enabled) {
            this.enabled = enabled;
            if (owner != null && owner instanceof Component) {
                ((Component) owner).setEnabled(enabled);
            }
            firePropertyChange(PROP_ENABLED, oldValue, enabled);
        }
    }

    public Component.ActionOwner getOwner() {
        return owner;
    }

    public void setOwner(Component.ActionOwner actionOwner) {
        this.owner = actionOwner;
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        if (changeSupport == null) {
	        changeSupport = new PropertyChangeSupport(this);
        }
        changeSupport.addPropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        if (changeSupport == null) {
            return;
        }
        changeSupport.removePropertyChangeListener(listener);
    }

    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        if (changeSupport == null || ObjectUtils.equals(oldValue, newValue))
            return;
        changeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        String oldValue = this.icon;
        if (!ObjectUtils.equals(oldValue, icon)) {
            this.icon = icon;
            firePropertyChange(PROP_ICON, oldValue, icon);
        }
    }
}
