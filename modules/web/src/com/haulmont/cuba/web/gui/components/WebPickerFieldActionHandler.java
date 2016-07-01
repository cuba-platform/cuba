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

public class WebPickerFieldActionHandler implements Action.Handler {

    private final int[] modifiers;

    private Map<ShortcutAction, com.haulmont.cuba.gui.components.Action> actionsMap = new HashMap<>();
    private List<Action> shortcuts = new ArrayList<>();
    private List<ShortcutAction> orderedShortcuts = new ArrayList<>();
    private PickerField component;

    protected List<com.haulmont.cuba.gui.components.Action> actionList = new ArrayList<>();

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

    public void addAction(com.haulmont.cuba.gui.components.Action action, int index) {
        actionList.add(index, action);

        updateOrderedShortcuts();

        KeyCombination combination = action.getShortcutCombination();
        if (combination != null) {
            int key = combination.getKey().getCode();
            int[] modifiers = KeyCombination.Modifier.codes(combination.getModifiers());
            ShortcutAction providedShortcut = new ShortcutAction(action.getCaption(), key, modifiers);
            shortcuts.add(providedShortcut);
            actionsMap.put(providedShortcut, action);
        }
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
        actionList.remove(action);

        updateOrderedShortcuts();
    }

    protected void updateOrderedShortcuts() {
        shortcuts.removeAll(orderedShortcuts);
        for (ShortcutAction orderedShortcut : orderedShortcuts) {
            actionsMap.remove(orderedShortcut);
        }

        for (int i = 0; i < actionList.size(); i++) {
            int keyCode = ShortcutAction.KeyCode.NUM1 + i;

            com.haulmont.cuba.gui.components.Action orderedAction = actionList.get(i);

            ShortcutAction orderedShortcut = new ShortcutAction(orderedAction.getCaption(), keyCode, modifiers);
            shortcuts.add(orderedShortcut);
            orderedShortcuts.add(orderedShortcut);
            actionsMap.put(orderedShortcut, orderedAction);
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