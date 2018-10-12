/*
 * Copyright (c) 2008-2017 Haulmont.
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
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.data.ConversionException;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.UserError;
import com.vaadin.ui.AbstractComponent;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * Base class for Vaadin8 based input components.
 *
 * @param <T> type of underlying Vaadin component
 * @param <P> type of value of presentation
 * @param <V> type of value of model
 */
public abstract class WebV8AbstractField<T extends com.vaadin.ui.Component & com.vaadin.data.HasValue<P>, P, V>
        extends WebAbstractValueComponent<T, P, V> implements Field<V> {

    protected static final int VALIDATORS_LIST_INITIAL_CAPACITY = 2;
    protected List<Consumer> validators; // lazily initialized list

    protected boolean editable = true;

    // VAADIN8: gg, replace with Subscription
    protected Consumer<EditableChangeNotifier.EditableChangeEvent> parentEditableChangeListener;

    @Override
    public boolean isRequired() {
        return component.isRequiredIndicatorVisible();
    }

    @Override
    public void setRequired(boolean required) {
        component.setRequiredIndicatorVisible(required);

        setupComponentErrorProvider(required, component);
    }

    protected void setupComponentErrorProvider(boolean required, T component) {
        AbstractComponent abstractComponent = (AbstractComponent) component;
        if (required) {
            abstractComponent.setComponentErrorProvider(this::getErrorMessage);
        } else {
            abstractComponent.setComponentErrorProvider(null);
        }
    }

    protected ErrorMessage getErrorMessage() {
        return (isEditableWithParent() && isRequired() && isEmpty())
                ? new UserError(getRequiredMessage())
                : null;
    }

    @Override
    public void setRequiredMessage(String msg) {
        ((AbstractComponent) component).setRequiredError(msg);
    }

    @Override
    public String getRequiredMessage() {
        return ((AbstractComponent) component).getRequiredError();
    }

    @Override
    public void setParent(Component parent) {
        if (this.parent instanceof EditableChangeNotifier
                && parentEditableChangeListener != null) {
            ((EditableChangeNotifier) this.parent).removeEditableChangeListener(parentEditableChangeListener);

            parentEditableChangeListener = null;
        }

        super.setParent(parent);

        if (parent instanceof EditableChangeNotifier) {
            parentEditableChangeListener = event -> {
                boolean parentEditable = event.getSource().isEditable();
                boolean finalEditable = parentEditable && editable;
                setEditableToComponent(finalEditable);
            };
            ((EditableChangeNotifier) parent).addEditableChangeListener(parentEditableChangeListener);

            Editable parentEditable = (Editable) parent;
            if (!parentEditable.isEditable()) {
                setEditableToComponent(false);
            }
        }
    }

    @Override
    public boolean isEditable() {
        return editable;
    }

    @Override
    public void setEditable(boolean editable) {
        if (this.editable == editable) {
            return;
        }

        this.editable = editable;

        boolean parentEditable = true;
        if (parent instanceof ChildEditableController) {
            parentEditable = ((ChildEditableController) parent).isEditable();
        }
        boolean finalEditable = parentEditable && editable;

        setEditableToComponent(finalEditable);
    }

    protected void setEditableToComponent(boolean editable) {
        component.setReadOnly(!editable);
    }

    @SuppressWarnings("unchecked")
    protected void attachValueChangeListener(T component) {
        component.addValueChangeListener(event ->
                componentValueChanged(event.getOldValue(), event.getValue(), event.isUserOriginated())
        );
    }

    @Override
    public void addValidator(Consumer<? super V> validator) {
        if (validators == null) {
            validators = new ArrayList<>(VALIDATORS_LIST_INITIAL_CAPACITY);
        }
        if (!validators.contains(validator)) {
            validators.add(validator);
        }
    }

    @Override
    public void removeValidator(Consumer<V> validator) {
        if (validators != null) {
            validators.remove(validator);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<Consumer<V>> getValidators() {
        if (validators == null) {
            return Collections.emptyList();
        }

        return (Collection) Collections.unmodifiableCollection(validators);
    }

    @Override
    public boolean isValid() {
        try {
            validate();
            return true;
        } catch (ValidationException e) {
            return false;
        }
    }

    @Override
    public void validate() throws ValidationException {
        if (hasValidationError()) {
            setValidationError(null);
        }

        if (!isVisibleRecursive() || !isEditableWithParent() || !isEnabledRecursive()) {
            return;
        }

        try {
            // if we cannot convert current presentation value into model - UI value is invalid
            convertToModel(component.getValue());
        } catch (ConversionException ce) {
            LoggerFactory.getLogger(getClass()).trace("Unable to convert presentation value to model", ce);

            setValidationError(ce.getLocalizedMessage());

            throw new ValidationException(ce.getLocalizedMessage());
        }

        if (isEmpty()) {
            if (isRequired()) {
                throw new RequiredValueMissingException(getRequiredMessage(), this);
            } else {
                // vaadin8 rework this PL-10701
                return;
            }
        }

        V value = getValue();
        triggerValidators(value);
    }

    protected void triggerValidators(V value) throws ValidationFailedException {
        if (validators != null) {
            try {
                for (Consumer validator : validators) {
                    validator.accept(value);
                }
            } catch (ValidationException e) {
                setValidationError(e.getDetailsMessage());

                throw new ValidationFailedException(e.getDetailsMessage(), this, e);
            }
        }
    }

    protected boolean isEmpty(Object value) {
        return value == null;
    }
}