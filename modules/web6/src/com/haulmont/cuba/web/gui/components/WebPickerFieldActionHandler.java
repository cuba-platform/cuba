/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.gui.components.PickerField;
import com.vaadin.event.Action;
import com.vaadin.event.ShortcutAction;
import org.apache.commons.lang.StringUtils;

import java.util.*;

/**
 * @author devyatkin
 * @version $Id$
 */
public class WebPickerFieldActionHandler implements Action.Handler {

    private final int[] modifiers;

    private Map<ShortcutAction, com.haulmont.cuba.gui.components.Action> actionsMap = new HashMap<>();

    private List<Action> shortcuts = new LinkedList<>();

    private PickerField component;

    private int actionsCount = 0;

    public WebPickerFieldActionHandler(PickerField component) {
        this.component = component;
        ClientConfig config = AppBeans.get(Configuration.class).getConfig(ClientConfig.class);
        String[] strModifiers = StringUtils.split(config.getPickerShortcutModifiers().toUpperCase(), "-");
        modifiers = new int[strModifiers.length];
        for (int i = 0; i < modifiers.length; i++) {
            modifiers[i] = com.haulmont.cuba.gui.components.ShortcutAction.Modifier.valueOf(strModifiers[i]).getCode();
        }
    }

    @Override
    public com.vaadin.event.Action[] getActions(Object target, Object sender) {
        return shortcuts.toArray(new com.vaadin.event.Action[shortcuts.size()]);
    }

    public void addAction(com.haulmont.cuba.gui.components.Action action) {
        int keyCode = ShortcutAction.KeyCode.NUM1 + actionsCount;
        ShortcutAction shortcut = new ShortcutAction(action.getCaption(), keyCode, modifiers);
        if (action instanceof com.haulmont.cuba.gui.components.ShortcutAction) {
            com.haulmont.cuba.gui.components.ShortcutAction.KeyCombination combination;
            combination = ((com.haulmont.cuba.gui.components.ShortcutAction) action).getKeyCombination();
            int key = combination.getKey().getCode();
            int[] modifiers = com.haulmont.cuba.gui.components.ShortcutAction.Modifier.codes(combination.getModifiers());
            ShortcutAction providedShortcut = new ShortcutAction(action.getCaption(), key, modifiers);
            shortcuts.add(providedShortcut);
            actionsMap.put(providedShortcut, action);
        }
        shortcuts.add(shortcut);
        actionsMap.put(shortcut, action);
        actionsCount++;
    }

    public void removeAction(com.haulmont.cuba.gui.components.Action action) {
        List<ShortcutAction> existActions = new LinkedList<>();
        for (Map.Entry<ShortcutAction, com.haulmont.cuba.gui.components.Action> entry : actionsMap.entrySet()) {
            if (entry.getValue().equals(action)) {
                existActions.add(entry.getKey());
            }
        }
        shortcuts.removeAll(existActions);
        for (ShortcutAction shortcut : existActions) {
            actionsMap.remove(shortcut);
        }
        if (existActions.size() > 0) {
            actionsCount--;
        }

    }

    @Override
    public void handleAction(com.vaadin.event.Action action, Object sender, Object target) {
        com.haulmont.cuba.gui.components.Action pickerAction = actionsMap.get(action);
        if (pickerAction != null) {
            pickerAction.actionPerform(component);
        }
    }

}
