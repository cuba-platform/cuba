/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.gui.components.KeyCombination;
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

    protected final int[] modifiers;

    protected Map<ShortcutAction, com.haulmont.cuba.gui.components.Action> actionsMap = new HashMap<>();

    protected List<Action> shortcuts = new LinkedList<>();

    protected PickerField component;

    protected int actionsCount = 0;

    public WebPickerFieldActionHandler(PickerField component) {
        this.component = component;
        Configuration configuration = AppBeans.get(Configuration.NAME);
        ClientConfig config = configuration.getConfig(ClientConfig.class);
        String[] strModifiers = StringUtils.split(config.getPickerShortcutModifiers().toUpperCase(), "-");
        modifiers = new int[strModifiers.length];
        for (int i = 0; i < modifiers.length; i++) {
            modifiers[i] = KeyCombination.Modifier.valueOf(strModifiers[i]).getCode();
        }
    }

    @Override
    public com.vaadin.event.Action[] getActions(Object target, Object sender) {
        return shortcuts.toArray(new com.vaadin.event.Action[shortcuts.size()]);
    }

    public void addAction(com.haulmont.cuba.gui.components.Action action) {
        int keyCode = ShortcutAction.KeyCode.NUM1 + actionsCount;
        ShortcutAction shortcut = new ShortcutAction(action.getCaption(), keyCode, modifiers);
        KeyCombination combination = action.getShortcut();
        if (combination != null) {
            int key = combination.getKey().getCode();
            int[] modifiers = KeyCombination.Modifier.codes(combination.getModifiers());
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