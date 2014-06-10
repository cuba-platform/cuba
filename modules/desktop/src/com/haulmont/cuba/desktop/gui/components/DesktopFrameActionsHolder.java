/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.Component;
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

    private List<Action> actionList = new LinkedList<Action>();
    private Map<Action, KeyStroke> shortcutActions = new HashMap<>();

    private Component component;
    private JPanel panel;

    public DesktopFrameActionsHolder(Component component, JPanel panel) {
        this.component = component;
        this.panel = panel;
    }

    public void addAction(final Action action) {
        if (action.getShortcut() !=null ) {
            KeyStroke keyStroke = DesktopComponentsHelper.convertKeyCombination(action.getShortcut());
            InputMap inputMap = panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
            inputMap.put(keyStroke, action.getId());
            ActionMap actionMap = panel.getActionMap();
            actionMap.put(action.getId(), new javax.swing.AbstractAction() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    action.actionPerform(component);
                }
            });
            shortcutActions.put(action, keyStroke);
        }

        for (int i = 0; i < actionList.size(); i++) {
            Action a = actionList.get(i);
            if (ObjectUtils.equals(a.getId(), action.getId())) {
                actionList.set(i, action);
                return;
            }
        }
        actionList.add(action);
    }

    public void removeAction(Action action) {
        if (action.getShortcut() != null) {
            InputMap inputMap = panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
            ActionMap actionMap = panel.getActionMap();
            KeyStroke keyStroke = shortcutActions.get(action);
            if (keyStroke != null) {
                inputMap.remove(keyStroke);
                actionMap.remove(action.getId());
            }
        }
        actionList.remove(action);
    }

    public void removeAction(String id) {
        Action action = getAction(id);
        if (action != null) {
            removeAction(action);
        }
    }

    public void removeAllActions() {
        for (Action action : new ArrayList<>(actionList)) {
            removeAction(action);
        }
    }

    public Collection<Action> getActions() {
        return Collections.unmodifiableCollection(actionList);
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
