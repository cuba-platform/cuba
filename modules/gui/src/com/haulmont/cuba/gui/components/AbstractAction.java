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
 *
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
import java.util.*;

/**
 * Abstract class for GUI actions.
 */
public abstract class AbstractAction implements Action {

    protected String id;

    protected String caption;

    protected String description;

    protected String icon;

    protected boolean enabled = true;

    protected boolean visible = true;

    protected List<Component.ActionOwner> owners = new ArrayList<>();

    protected PropertyChangeSupport changeSupport;

    protected Messages messages = AppBeans.get(Messages.NAME);

    protected UserSession userSession;

    protected KeyCombination shortcut;

    protected boolean primary = false;

    protected AbstractAction(String id) {
        this.id = id;

        UserSessionSource sessionSource = AppBeans.get(UserSessionSource.NAME);
        userSession = sessionSource.getUserSession();
    }

    protected AbstractAction(String id, @Nullable String shortcut) {
        this(id);
        if (shortcut != null) {
            this.shortcut = KeyCombination.create(shortcut);
        }
    }

    protected AbstractAction(String id, Status status) {
        this(id);

        this.primary = status == Status.PRIMARY;
    }

    @Override
    public String getId() {
        return id;
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
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        String oldValue = this.description;
        if (!StringUtils.equals(oldValue, description)) {
            this.description = description;
            firePropertyChange(PROP_DESCRIPTION, oldValue, description);
        }
    }

    @Override
    public KeyCombination getShortcutCombination() {
        return shortcut;
    }

    @Override
    public void setShortcutCombination(KeyCombination shortcut) {
        KeyCombination oldValue = this.shortcut;
        if (!Objects.equals(oldValue, shortcut)) {
            this.shortcut = shortcut;
            firePropertyChange(PROP_SHORTCUT, oldValue, shortcut);
        }
    }

    @Override
    public void setShortcut(String shortcut) {
        if (shortcut != null) {
            setShortcutCombination(KeyCombination.create(shortcut));
        } else {
            setShortcutCombination(null);
        }
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
    public void refreshState() {
    }

    public boolean isPrimary() {
        return primary;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }
}