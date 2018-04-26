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
    protected List<Validator> validators; // lazily initialized list

    protected boolean editable = true;

    protected EditableChangeNotifier.EditableChangeListener parentEditableChangeListener;

    @Override
    public boolean isRequired() {
        return component.isRequiredIndicatorVisible();
    }

    @Override
    public void setRequired(boolean required) {
        component.setRequiredIndicatorVisible(required);
    }

    @Override
    public void setRequiredMessage(String msg) {
//        vaadin8
//        component.setRequiredError(msg);
    }

    @Override
    public String getRequiredMessage() {
//        vaadin8
//        return component.getRequiredError();
        return "";
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
    public void addValidator(Validator validator) {
        if (validators == null) {
            validators = new ArrayList<>(VALIDATORS_LIST_INITIAL_CAPACITY);
        }
        if (!validators.contains(validator)) {
            validators.add(validator);
        }
    }

    @Override
    public void removeValidator(Validator validator) {
        if (validators != null) {
            validators.remove(validator);
        }
    }

    @Override
    public Collection<Validator> getValidators() {
        if (validators == null) {
            return Collections.emptyList();
        }

        return Collections.unmodifiableCollection(validators);
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
                for (Validator validator : validators) {
                    validator.validate(value);
                }
            } catch (ValidationException e) {
                setValidationError(e.getDetailsMessage());

                throw new ValidationFailedException(e.getDetailsMessage(), this, e);
            }
        }
    }

    protected void commit() {
//        vaadin8
//        component.commit();
    }

    protected void discard() {
//        vaadin8
//        component.discard();
    }

    protected boolean isBuffered() {
//        vaadin8
//        return component.isBuffered();
        return false;
    }

    protected void setBuffered(boolean buffered) {
//        vaadin8
//        component.setBuffered(buffered);
    }

    protected boolean isModified() {
//        vaadin8
//        return component.isModified();
        return false;
    }

    protected boolean isEmpty(Object value) {
        return value == null;
    }

    @Override
    public String getContextHelpText() {
        return ((AbstractComponent) component).getContextHelpText();
    }

    @Override
    public void setContextHelpText(String contextHelpText) {
        ((AbstractComponent) component).setContextHelpText(contextHelpText);
    }

    @Override
    public boolean isContextHelpTextHtmlEnabled() {
        return ((AbstractComponent) component).isContextHelpTextHtmlEnabled();
    }

    @Override
    public void setContextHelpTextHtmlEnabled(boolean enabled) {
        ((AbstractComponent) component).setContextHelpTextHtmlEnabled(enabled);
    }

    @Override
    public Consumer<ContextHelpIconClickEvent> getContextHelpIconClickHandler() {
        return null; // todo vaadin8
    }

    @Override
    public void setContextHelpIconClickHandler(Consumer<ContextHelpIconClickEvent> handler) {
        // todo vaadin8
    }
}