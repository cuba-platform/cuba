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

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.desktop.sys.validation.ValidationAwareAction;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.*;
import org.apache.commons.lang.ObjectUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.*;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;
import static com.haulmont.cuba.gui.ComponentsHelper.findActionById;

/**
 */
public class DesktopAbstractActionsHolderComponent<C extends JComponent> extends DesktopAbstractComponent<C>
        implements Component.SecuredActionsHolder {

    protected List<Action> actionList = new LinkedList<>();

    protected final ShortcutsDelegate<KeyCombination> shortcutsDelegate;
    protected final ActionsPermissions actionsPermissions = new ActionsPermissions(this);

    public DesktopAbstractActionsHolderComponent() {
        shortcutsDelegate = new ShortcutsDelegate<KeyCombination>() {
            @Override
            protected KeyCombination attachShortcut(final String actionId, KeyCombination keyCombination) {
                impl.getInputMap().put(DesktopComponentsHelper.convertKeyCombination(keyCombination), actionId);
                impl.getActionMap().put(actionId, new ValidationAwareAction() {
                    @Override
                    public void actionPerformedAfterValidation(ActionEvent e) {
                        Action action = getAction(actionId);
                        if ((action != null) && (action.isEnabled()) && (action.isVisible())) {
                            action.actionPerform(DesktopAbstractActionsHolderComponent.this);
                        }
                    }
                });
                return keyCombination;
            }

            @Override
            protected void detachShortcut(Action action, KeyCombination shortcutDescriptor) {
                impl.getInputMap().remove(DesktopComponentsHelper.convertKeyCombination(shortcutDescriptor));
                impl.getActionMap().remove(action.getId());
            }

            @Override
            protected Collection<Action> getActions() {
                return DesktopAbstractActionsHolderComponent.this.getActions();
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

        actionList.add(index, action);

        shortcutsDelegate.addAction(null, action);

        attachAction(action);

        actionsPermissions.apply(action);
    }

    protected void attachAction(Action action) {
        action.refreshState();
    }

    @Override
    public void removeAction(@Nullable Action action) {
        if (actionList.remove(action)) {
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