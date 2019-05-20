/*
 * Copyright (c) 2008-2018 Haulmont.
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
 */

package com.haulmont.cuba.web.widgets;

import com.vaadin.event.Action;
import com.vaadin.event.ActionManager;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.PaintException;
import com.vaadin.server.PaintTarget;
import com.vaadin.shared.Registration;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.LegacyComponent;

import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;

public class CubaComboBox<V> extends ComboBox<V> implements Action.Container, LegacyComponent {

    /**
     * Keeps track of the Actions added to this component, and manages the
     * painting and handling as well.
     */
    protected ActionManager shortcutsManager;

    protected BiFunction<V, V, Boolean> customValueEquals;

    public CubaComboBox() {
        setPopupWidth(null);
    }

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        if (shortcutsManager != null) {
            shortcutsManager.paintActions(null, target);
        }
    }

    @Override
    protected ActionManager getActionManager() {
        if (shortcutsManager == null) {
            shortcutsManager = new ActionManager(this);
        }
        return shortcutsManager;
    }

    @Override
    public void changeVariables(Object source, Map<String, Object> variables) {
        // Actions
        if (shortcutsManager != null) {
            shortcutsManager.handleActions(variables, this);
        }
    }

    @Override
    public Registration addShortcutListener(ShortcutListener listener) {
        getActionManager().addAction(listener);
        return () -> getActionManager().removeAction(listener);
    }

    @Override
    public void removeShortcutListener(ShortcutListener listener) {
        getActionManager().removeAction(listener);
    }

    @Override
    public void addActionHandler(Action.Handler actionHandler) {
        getActionManager().addActionHandler(actionHandler);
    }

    @Override
    public void removeActionHandler(Action.Handler actionHandler) {
        getActionManager().removeActionHandler(actionHandler);
    }

    @Override
    public boolean isSelected(V item) {
        V selectedItem = getSelectedItem().orElse(null);

        if (customValueEquals != null) {
            if (customValueEquals.apply(selectedItem, item)) {
                return true;
            }
        } else {
            if (Objects.equals(selectedItem, item)) {
                return true;
            }
        }

        if (item == null || selectedItem == null) {
            return false;
        }

        return Objects.equals(getDataProvider().getId(selectedItem),
                getDataProvider().getId(item));
    }

    public BiFunction<V, V, Boolean> getCustomValueEquals() {
        return customValueEquals;
    }

    public void setCustomValueEquals(BiFunction<V, V, Boolean> customValueEquals) {
        this.customValueEquals = customValueEquals;
    }
}