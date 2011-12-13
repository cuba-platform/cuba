/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.ShortcutAction;
import org.apache.commons.lang.ObjectUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.*;

/**
 * Encapsulates {@link com.haulmont.cuba.gui.components.Component.ActionsHolder} functionality for desktop frames and
 * windows.
 *
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class DesktopFrameActionsHolder {

    private List<Action> actionsOrder = new LinkedList<Action>();
    private Map<ShortcutAction,KeyStroke> shortcutActions = new HashMap<ShortcutAction,KeyStroke>();

    private Component component;
    private JPanel panel;

    public DesktopFrameActionsHolder(Component component, JPanel panel) {
        this.component = component;
        this.panel = panel;
    }

    public void addAction(final Action action) {
        if (action instanceof ShortcutAction) {
            ShortcutAction.KeyCombination combination = ((ShortcutAction) action).getKeyCombination();

            KeyStroke keyStroke = DesktopComponentsHelper.convertKeyCombination(combination);
            InputMap inputMap = panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
            inputMap.put(keyStroke, action.getId());
            ActionMap actionMap = panel.getActionMap();
            actionMap.put(action.getId(), new javax.swing.AbstractAction() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    action.actionPerform(component);
                }
            });
            shortcutActions.put((ShortcutAction) action, keyStroke);
        }
        actionsOrder.add(action);
    }

    public void removeAction(Action action) {
        if (action instanceof ShortcutAction) {
            InputMap inputMap = panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
            ActionMap actionMap = panel.getActionMap();
            KeyStroke keyStroke = shortcutActions.get(action);
            if (keyStroke != null) {
                inputMap.remove(keyStroke);
                actionMap.remove(action.getId());
            }
        }
        actionsOrder.remove(action);
    }

    public Collection<Action> getActions() {
        return Collections.unmodifiableCollection(actionsOrder);
    }

    public Action getAction(String id) {
        for (com.haulmont.cuba.gui.components.Action action : getActions()) {
            if (ObjectUtils.equals(action.getId(), id)) {
                return action;
            }
        }
        return null;
    }
}
