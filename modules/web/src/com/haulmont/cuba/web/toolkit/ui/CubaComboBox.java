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

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class CubaComboBox extends ComboBox implements Action.Container {

    /**
     * Keeps track of the Actions added to this component, and manages the
     * painting and handling as well.
     */
    protected ActionManager shortcutsManager;
    protected OptionIconProvider optionIconProvider;

    protected BiFunction<Object, Object, Boolean> customValueEquals;
    protected BiFunction<String, String, Boolean> filterPredicate;

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
    public Resource getItemIcon(Object itemId) {
        if (optionIconProvider != null) {
            Resource itemIcon = optionIconProvider.getItemIcon(itemId);
            if (itemIcon != null) {
                return itemIcon;
            }
        }

        return super.getItemIcon(itemId);
    }

    public OptionIconProvider getOptionIconProvider() {
        return optionIconProvider;
    }

    public void setOptionIconProvider(OptionIconProvider optionIconProvider) {
        if (this.optionIconProvider != optionIconProvider) {
            this.optionIconProvider = optionIconProvider;
            markAsDirty();
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

    @Override
    protected boolean fieldValueEquals(Object value1, Object value2) {
        if (customValueEquals != null) {
            Boolean equals = customValueEquals.apply(value1, value2);
            if (equals != null) {
                return equals;
            }
        }
        // only if instance the same,
        // we can set instance of entity with the same id but different property values
        return super.fieldValueEquals(value1, value2);
    }

    public BiFunction<Object, Object, Boolean> getCustomValueEquals() {
        return customValueEquals;
    }

    public void setCustomValueEquals(BiFunction<Object, Object, Boolean> customValueEquals) {
        this.customValueEquals = customValueEquals;
    }

    public void setFilterPredicate(BiFunction<String, String, Boolean> filterPredicate) {
        this.filterPredicate = filterPredicate;
    }

    @Override
    protected List<?> getFilteredOptions() {
        if (filterPredicate != null) {
            Collection<?> items = getItemIds();
            if (items == null) {
                return Collections.emptyList();
            }

            return items.stream()
                    .filter(item -> filterPredicate.apply(getItemCaption(item), filterstring))
                    .collect(Collectors.toList());
        }
        return super.getFilteredOptions();
    }

    public interface OptionIconProvider {

        Resource getItemIcon(Object item);
    }
}