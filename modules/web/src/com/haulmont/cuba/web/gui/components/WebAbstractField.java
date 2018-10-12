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

import com.haulmont.bali.events.Subscription;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.data.ValueSource;
import com.haulmont.cuba.gui.components.data.meta.ValueBinding;
import com.haulmont.cuba.gui.components.data.value.ValueBinder;
import com.haulmont.cuba.web.widgets.compatibility.CubaValueChangeEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public abstract class WebAbstractField<T extends com.vaadin.v7.ui.AbstractField, V>
        extends WebAbstractComponent<T> implements Field<V> /* todo ds: move to Field */ {

    protected static final int VALIDATORS_LIST_INITIAL_CAPACITY = 4;

    protected List<Consumer> validators; // lazily initialized list

    protected boolean editable = true;

    protected V internalValue;
    protected ValueBinding<V> valueBinding;

    // VAADIN8: gg, replace with Subscription
    protected Consumer<EditableChangeNotifier.EditableChangeEvent> parentEditableChangeListener;

    @Override
    public ValueSource<V> getValueSource() {
        return valueBinding != null ? valueBinding.getSource() : null;
    }

    @Override
    public void setValueSource(ValueSource<V> valueSource) {
        if (this.valueBinding != null) {
            valueBinding.unbind();

            this.valueBinding = null;
        }

        if (valueSource != null) {
            // todo use ApplicationContextAware and lookup
            ValueBinder binder = AppBeans.get(ValueBinder.class);

            this.valueBinding = binder.bind(this, valueSource);

            valueBindingConnected(valueSource);

            this.valueBinding.activate();

            valueBindingActivated(valueSource);
        }
    }

    protected void valueBindingActivated(ValueSource<V> valueSource) {
        // hook
    }

    protected void valueBindingConnected(ValueSource<V> valueSource) {
        // hook
    }

    protected void initFieldConverter() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public Subscription addValueChangeListener(Consumer<ValueChangeEvent<V>> listener) {
        return getEventHub().subscribe(ValueChangeEvent.class, (Consumer) listener);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void removeValueChangeListener(Consumer<ValueChangeEvent<V>> listener) {
        unsubscribe(ValueChangeEvent.class, (Consumer) listener);
    }

    @Override
    public boolean isRequired() {
        return component.isRequired();
    }

    @Override
    public void setRequired(boolean required) {
        component.setRequired(required);
    }

    @Override
    public void setRequiredMessage(String msg) {
        component.setRequiredError(msg);
    }

    @Override
    public String getRequiredMessage() {
        return component.getRequiredError();
    }

    @Override
    public V getValue() {
        //noinspection unchecked
        return (V) component.getValue();
    }

    @Override
    public void setValue(V value) {
        setValueToPresentation(convertToPresentation(value));
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

    protected void setValueToPresentation(Object value) {
        if (hasValidationError()) {
            setValidationError(null);
        }

        component.setValue(value);
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
    protected void attachListener(T component) {
        component.addValueChangeListener(event -> {
            Object value = event.getProperty().getValue();
            componentValueChanged(value, event instanceof CubaValueChangeEvent
                    && ((CubaValueChangeEvent) event).isUserOriginated());
        });
    }

    protected void componentValueChanged(Object newComponentValue, boolean userOriginated) {
        V value = convertToModel(newComponentValue);
        V oldValue = internalValue;
        internalValue = value;

        if (!fieldValueEquals(value, oldValue)) {
            if (hasValidationError()) {
                setValidationError(null);
            }

            ValueChangeEvent<V> event = new ValueChangeEvent<>(this, oldValue, value, userOriginated);
            publish(ValueChangeEvent.class, event);
        }
    }

    @SuppressWarnings("unchecked")
    protected V convertToModel(Object componentRawValue) {
        return (V) componentRawValue;
    }

    @SuppressWarnings("unchecked")
    protected Object convertToPresentation(V modelValue) {
        return modelValue;
    }

    protected boolean fieldValueEquals(V value, V oldValue) {
        return InstanceUtils.propertyValueEquals(oldValue, value);
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

        Object value = getValue();
        if (isEmpty(value)) {
            if (isRequired()) {
                throw new RequiredValueMissingException(getRequiredMessage(), this);
            } else {
                return;
            }
        }

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

    protected void commit() {
        component.commit();
    }

    protected void discard() {
        component.discard();
    }

    protected boolean isBuffered() {
        return component.isBuffered();
    }

    protected void setBuffered(boolean buffered) {
        component.setBuffered(buffered);
    }

    protected boolean isModified() {
        return component.isModified();
    }

    protected boolean isEmpty(Object value) {
        return value == null;
    }

    /*@Override
    public String getContextHelpText() {
        return component.getContextHelpText();
    }

    @Override
    public void setContextHelpText(String contextHelpText) {
        component.setContextHelpText(contextHelpText);
    }

    @Override
    public boolean isContextHelpTextHtmlEnabled() {
        return component.isContextHelpTextHtmlEnabled();
    }

    @Override
    public void setContextHelpTextHtmlEnabled(boolean enabled) {
        component.setContextHelpTextHtmlEnabled(enabled);
    }*/

    /*@Override
    public Consumer<ContextHelpIconClickEvent> getContextHelpIconClickHandler() {
        return contextHelpIconClickHandler;
    }

    @Override
    public void setContextHelpIconClickHandler(Consumer<ContextHelpIconClickEvent> handler) {
        if (!Objects.equals(this.contextHelpIconClickHandler, handler)) {
            this.contextHelpIconClickHandler = handler;

            if (handler == null) {
//                todo vaadin8
                component.removeContextHelpIconClickListener(contextHelpIconClickListener);
                contextHelpIconClickListener = null;
            } else {
                if (contextHelpIconClickListener == null) {
                    contextHelpIconClickListener = (ContextHelpIconClickListener) e -> {
                        ContextHelpIconClickEvent event = new ContextHelpIconClickEvent(WebAbstractField.this);
                        fireContextHelpIconClick(event);
                    };
                    component.addContextHelpIconClickListener(contextHelpIconClickListener);
                }
            }
        }
    }

    protected void fireContextHelpIconClick(ContextHelpIconClickEvent event) {
        if (contextHelpIconClickHandler != null) {
            contextHelpIconClickHandler.accept(event);
        }
    }*/
}