/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.ActionsPermissions;
import com.haulmont.cuba.gui.components.KeyCombination;
import com.haulmont.cuba.gui.components.ShortcutsDelegate;
import com.vaadin.event.ShortcutListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;
import static com.haulmont.cuba.gui.ComponentsHelper.findActionById;

/**
 * Base class for action holders with shortcuts support
 *
 * @param <T>
 * @author abramov
 */
public abstract class WebAbstractActionsHolderComponent<T extends com.vaadin.ui.Component & com.vaadin.event.Action.ShortcutNotifier>
        extends WebAbstractComponent<T> implements com.haulmont.cuba.gui.components.Component.SecuredActionsHolder {

    protected final List<Action> actionList = new ArrayList<>();

    protected VerticalLayout contextMenuPopup;
    protected final List<ContextMenuButton> contextMenuButtons = new LinkedList<>();

    protected final ShortcutsDelegate<ShortcutListener> shortcutsDelegate;
    protected final ActionsPermissions actionsPermissions = new ActionsPermissions(this);

    protected WebAbstractActionsHolderComponent() {
        contextMenuPopup = new VerticalLayout();
        contextMenuPopup.setCubaId("cubaContextMenu");
        contextMenuPopup.setSizeUndefined();
        contextMenuPopup.setStyleName("cuba-context-menu-container");

        shortcutsDelegate = new ShortcutsDelegate<ShortcutListener>() {
            @Override
            protected ShortcutListener attachShortcut(final String actionId, KeyCombination keyCombination) {
                ShortcutListener shortcut = new ShortcutListener(actionId, keyCombination.getKey().getCode(),
                        KeyCombination.Modifier.codes(keyCombination.getModifiers())) {

                    @Override
                    public void handleAction(Object sender, Object target) {
                        if (target == component) {
                            Action action = getAction(actionId);
                            if (action != null && action.isEnabled() && action.isVisible()) {
                                action.actionPerform(WebAbstractActionsHolderComponent.this);
                            }
                        }
                    }
                };
                component.addShortcutListener(shortcut);
                return shortcut;
            }

            @Override
            protected void detachShortcut(Action action, ShortcutListener shortcutDescriptor) {
                component.removeShortcutListener(shortcutDescriptor);
            }

            @Override
            protected Collection<Action> getActions() {
                return WebAbstractActionsHolderComponent.this.getActions();
            }
        };
    }

    @Override
    public void addAction(Action action) {
        int index = findActionById(actionList, action.getId());
        if (index < 0) {
            index = actionList.size();
        }

        addAction(action, index);
    }

    @Override
    public void addAction(Action action, int index) {
        checkNotNullArgument(action, "action must be non null");

        int oldIndex = findActionById(actionList, action.getId());
        if (oldIndex >= 0) {
            removeAction(actionList.get(oldIndex));
            if (index > oldIndex) {
                index--;
            }
        }

        if (StringUtils.isNotEmpty(action.getCaption())) {
            ContextMenuButton contextMenuButton = createContextMenuButton();
            contextMenuButton.setStyleName("cuba-context-menu-button");
            contextMenuButton.setAction(action);

            contextMenuButtons.add(contextMenuButton);

            Component newVButton = WebComponentsHelper.unwrap(contextMenuButton);

            int visibleActionsIndex = 0;
            int i = 0;
            while (i < index && i < actionList.size()) {
                if (StringUtils.isNotEmpty(actionList.get(i).getCaption())) {
                    visibleActionsIndex++;
                }

                i++;
            }

            contextMenuPopup.addComponent(newVButton, visibleActionsIndex);
        }

        actionList.add(index, action);

        shortcutsDelegate.addAction(null, action);

        attachAction(action);

        actionsPermissions.apply(action);
    }

    protected void attachAction(Action action) {
        action.refreshState();
    }

    protected abstract ContextMenuButton createContextMenuButton();

    @Override
    public void removeAction(@Nullable Action action) {
        if (actionList.remove(action)) {
            ContextMenuButton actionButton = null;
            for (ContextMenuButton button : contextMenuButtons) {
                if (button.getAction() == action) {
                    actionButton = button;
                    break;
                }
            }

            if (actionButton != null) {
                actionButton.setAction(null);
                contextMenuButtons.remove(actionButton);

                contextMenuPopup.removeComponent(WebComponentsHelper.unwrap(actionButton));
            }

            shortcutsDelegate.removeAction(action);
        }
    }

    @Override
    public void removeAction(@Nullable String id) {
        Action action = getAction(id);
        if (action != null) {
            removeAction(action);
        }
    }

    @Override
    public void removeAllActions() {
        for (Action action : new ArrayList<>(actionList)) {
            removeAction(action);
        }
    }

    @Override
    public Collection<Action> getActions() {
        return Collections.unmodifiableCollection(actionList);
    }

    @Override
    @Nullable
    public Action getAction(String id) {
        for (Action action : getActions()) {
            if (ObjectUtils.equals(action.getId(), id)) {
                return action;
            }
        }
        return null;
    }

    @Nonnull
    @Override
    public Action getActionNN(String id) {
        Action action = getAction(id);
        if (action == null) {
            throw new IllegalStateException("Unable to find action with id " + id);
        }
        return action;
    }

    @Override
    public ActionsPermissions getActionsPermissions() {
        return actionsPermissions;
    }
}