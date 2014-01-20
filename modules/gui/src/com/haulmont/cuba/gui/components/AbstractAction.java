/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.security.global.UserSession;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nullable;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Base class for GUI actions.
 *
 * @author abramov
 * @version $Id$
 */
public abstract class AbstractAction implements Action {

    protected String id;

    protected String caption;

    protected String icon;

    protected boolean enabled = true;

    protected boolean visible = true;

    protected List<Component.ActionOwner> owners = new ArrayList<>();

    protected PropertyChangeSupport changeSupport;

    protected Messages messages;

    protected UserSession userSession;

    protected KeyCombination shortcut;

    protected AbstractAction(String id) {
        this.id = id;
        messages = AppBeans.get(Messages.class);
        userSession = AppBeans.get(UserSessionSource.class).getUserSession();
    }

    protected AbstractAction(String id, @Nullable String shortcut) {
        this(id);
        if (shortcut != null) {
            this.shortcut = KeyCombination.create(shortcut);
        }
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public KeyCombination getShortcut() {
        return shortcut;
    }

    @Override
    public void setShortcut(KeyCombination shortcut) {
        this.shortcut = shortcut;
    }

    @Override
    public void setShortcut(String shortcut) {
        if (shortcut != null) {
            this.shortcut = KeyCombination.create(shortcut);
        } else {
            this.shortcut = null;
        }
    }

    @Override
    public String getCaption() {
        return caption == null ? messages.getMessage(getClass(), id) : caption;
    }

    @Override
    public void setCaption(String caption) {
        String oldValue = this.caption;
        if (!StringUtils.equals(oldValue, caption)) {
            this.caption = caption;
            firePropertyChange(PROP_CAPTION, oldValue, caption);
        }
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        boolean oldValue = this.enabled;
        if (oldValue != enabled) {
            this.enabled = enabled;
            for (Component.ActionOwner owner : owners) {
                if (owner != null && owner instanceof Component) {
                    ((Component) owner).setEnabled(enabled);
                }
            }
            firePropertyChange(PROP_ENABLED, oldValue, enabled);
        }
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    @Override
    public void setVisible(boolean visible) {
        boolean oldValue = this.visible;
        if (oldValue != visible) {
            this.visible = visible;
            for (Component.ActionOwner owner : owners) {
                if (owner != null && owner instanceof Component) {
                    ((Component) owner).setVisible(visible);
                }
            }
            firePropertyChange(PROP_VISIBLE, oldValue, visible);
        }
    }

    @Override
    public Collection<Component.ActionOwner> getOwners() {
        return Collections.unmodifiableCollection(owners);
    }

    @Override
    public Component.ActionOwner getOwner() {
        return owners.isEmpty() ? null : owners.get(0);
    }

    @Override
    public void addOwner(Component.ActionOwner actionOwner) {
        if (!owners.contains(actionOwner)) {
            owners.add(actionOwner);
        }
    }

    @Override
    public void removeOwner(Component.ActionOwner actionOwner) {
        owners.remove(actionOwner);
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
        if (changeSupport == null || ObjectUtils.equals(oldValue, newValue)) {
            return;
        }
        changeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }

    @Override
    public String getIcon() {
        return icon;
    }

    @Override
    public void setIcon(String icon) {
        String oldValue = this.icon;
        if (!StringUtils.equals(oldValue, icon)) {
            this.icon = icon;
            firePropertyChange(PROP_ICON, oldValue, icon);
        }
    }

    @Override
    public void refreshState() {
    }
}