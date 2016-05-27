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

package com.haulmont.cuba.web.toolkit.ui;

import com.vaadin.data.Container;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.Action;
import com.vaadin.event.ActionManager;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.*;
import com.vaadin.ui.ComboBox;

import java.util.Map;

public class CubaComboBox extends ComboBox implements Action.Container {

    /**
     * Keeps track of the Actions added to this component, and manages the
     * painting and handling as well.
     */
    protected ActionManager shortcutsManager;

    public CubaComboBox() {
        setValidationVisible(false);
        setShowBufferedSourceException(false);
        setShowErrorForDisabledState(false);
    }

    @Override
    public ErrorMessage getErrorMessage() {
        ErrorMessage superError = super.getErrorMessage();
        if (!isReadOnly() && isRequired() && isEmpty()) {

            ErrorMessage error = AbstractErrorMessage.getErrorMessageForException(
                    new com.vaadin.data.Validator.EmptyValueException(getRequiredError()));
            if (error != null) {
                return new CompositeErrorMessage(superError, error);
            }
        }

        return superError;
    }

    @Override
    public void setContainerDataSource(Container newDataSource) {
        // CAUTION - copied from super method

        Object oldValue = getValue();

        if (newDataSource == null) {
            newDataSource = new IndexedContainer();
        }

        getCaptionChangeListener().clear();

        if (items != newDataSource) {

            // Removes listeners from the old datasource
            if (items != null) {
                if (items instanceof Container.ItemSetChangeNotifier) {
                    ((Container.ItemSetChangeNotifier) items)
                            .removeItemSetChangeListener(this);
                }
                if (items instanceof Container.PropertySetChangeNotifier) {
                    ((Container.PropertySetChangeNotifier) items)
                            .removePropertySetChangeListener(this);
                }
            }

            // Assigns new data source
            items = newDataSource;

            // Clears itemIdMapper also
            itemIdMapper.removeAll();

            // Adds listeners
            if (items != null) {
                if (items instanceof Container.ItemSetChangeNotifier) {
                    ((Container.ItemSetChangeNotifier) items)
                            .addItemSetChangeListener(this);
                }
                if (items instanceof Container.PropertySetChangeNotifier) {
                    ((Container.PropertySetChangeNotifier) items)
                            .addPropertySetChangeListener(this);
                }
            }

            /*
             * We expect changing the data source should also clean value. See
             * #810, #4607, #5281
             */
            // Haulmont API
            // #PL-3098
            if (!newDataSource.containsId(oldValue))
                setValue(null);

            markAsDirty();
        }
    }

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        super.paintContent(target);

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
        super.changeVariables(source, variables);

        // Actions
        if (shortcutsManager != null) {
            shortcutsManager.handleActions(variables, this);
        }
    }

    @Override
    public void addShortcutListener(ShortcutListener listener) {
        getActionManager().addAction(listener);
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
}