/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.desktop.sys.validation.ValidationAwareAction;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.Action;
import org.apache.commons.lang.ObjectUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

/**
 * @author krivopustov
 * @version $Id$
 */
public class DesktopAbstractActionsHolderComponent<C extends JComponent> extends DesktopAbstractComponent<C>
        implements Component.SecuredActionsHolder {

    protected java.util.List<Action> actionList = new LinkedList<>();

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
        checkNotNullArgument(action, "action must be non null");

        Action oldAction = getAction(action.getId());

        for (int i = 0; i < actionList.size(); i++) {
            Action a = actionList.get(i);
            if (ObjectUtils.equals(a.getId(), action.getId())) {
                actionList.set(i, action);
                return;
            }
        }
        actionList.add(action);

        shortcutsDelegate.addAction(oldAction, action);

        action.refreshState();

        actionsPermissions.apply(action);
    }

    @Override
    public void removeAction(Action action) {
        actionList.remove(action);

        shortcutsDelegate.removeAction(action);
    }

    @Override
    public void removeAction(String id) {
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
    public Action getAction(String id) {
        for (Action action : getActions()) {
            if (ObjectUtils.equals(action.getId(), id)) {
                return action;
            }
        }
        return null;
    }

    @Override
    public ActionsPermissions getActionsPermissions() {
        return actionsPermissions;
    }
}