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

import com.haulmont.bali.events.Subscription;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.compatibility.ComponentValueListenerWrapper;
import com.haulmont.cuba.gui.components.data.DatasourceValueSource;
import com.haulmont.cuba.gui.components.data.ValueBinder;
import com.haulmont.cuba.gui.components.data.ValueBinding;
import com.haulmont.cuba.gui.components.data.ValueSource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.ValueListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

// vaadin8 ??? Custom binding bridge for the new UI components ?
public abstract class WebV8AbstractField<T extends com.vaadin.ui.AbstractField, V>
        extends WebAbstractComponent<T> implements Field<V> {

    protected static final int VALIDATORS_LIST_INITIAL_CAPACITY = 2;
    protected List<Validator> validators; // lazily initialized list

    protected V internalValue;

    protected boolean editable = true;

    protected ValueBinding<V> valueBinding;

    protected EditableChangeListener parentEditableChangeListener;

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
        }
    }

    @Override
    public ValueSource<V> getValueSource() {
        return valueBinding != null ? valueBinding.getSource() : null;
    }

    @Override
    public Datasource getDatasource() {
        if (valueBinding == null) {
            return null;
        }

        return ((DatasourceValueSource) valueBinding.getSource()).getDatasource();
    }

    @Override
    public MetaProperty getMetaProperty() {
        if (valueBinding == null) {
            return null;
        }
        return ((DatasourceValueSource) valueBinding.getSource()).getMetaPropertyPath().getMetaProperty();
    }

    @Override
    public MetaPropertyPath getMetaPropertyPath() {
        if (valueBinding == null) {
            return null;
        }
        return ((DatasourceValueSource) valueBinding.getSource()).getMetaPropertyPath();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setDatasource(Datasource datasource, String property) {
        this.setValueSource(new DatasourceValueSource(datasource, property));
    }

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

    @SuppressWarnings("unchecked")
    @Override
    public V getValue() {
        return (V) component.getValue();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setValue(V value) {
        component.setValue(value);
    }

    @Override
    public void setParent(Component parent) {
        if (this.parent instanceof EditableChangeNotifier
                && parentEditableChangeListener != null) {
            ((EditableChangeNotifier) this.parent)
                    .removeEditableChangeListener(parentEditableChangeListener);

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

    @Override
    public Subscription addValueChangeListener(ValueChangeListener listener) {
        getEventRouter().addListener(ValueChangeListener.class, listener);
        // todo
        return () -> {};
    }

    @Override
    public void removeValueChangeListener(ValueChangeListener listener) {
        getEventRouter().removeListener(ValueChangeListener.class, listener);
    }

    @SuppressWarnings("unchecked")
    protected void attachListener(T component) {
        component.addValueChangeListener(this::componentValueChanged);
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

        if (!isVisible() || !isEditableWithParent() || !isEnabled()) {
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
    }

    @Override
    public Consumer<ContextHelpIconClickEvent> getContextHelpIconClickHandler() {
        return null; // todo vaadin8
    }

    @Override
    public void setContextHelpIconClickHandler(Consumer<ContextHelpIconClickEvent> handler) {
        // todo vaadin8
    }

    protected void componentValueChanged(com.vaadin.data.HasValue.ValueChangeEvent componentEvent) {
        V value = getValue();
        V oldValue = internalValue;
        internalValue = value;

        if (!InstanceUtils.propertyValueEquals(oldValue, value)) {
            if (hasValidationError()) {
                setValidationError(null);
            }

            ValueChangeEvent event = new ValueChangeEvent(this, oldValue, value);
            getEventRouter().fireEvent(ValueChangeListener.class, ValueChangeListener::valueChanged, event);
        }
    }
}