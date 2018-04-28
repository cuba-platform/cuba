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

package com.haulmont.cuba.web.gui.components;

import com.haulmont.bali.events.Subscription;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.data.ConversionException;
import com.haulmont.cuba.gui.components.data.HasValueBinding;
import com.haulmont.cuba.gui.components.data.ValueBinding;
import com.haulmont.cuba.gui.components.data.ValueSource;
import com.haulmont.cuba.gui.components.data.value.ValueBinder;
import org.springframework.context.ApplicationContext;

import javax.inject.Inject;

public abstract class WebAbstractViewComponent<T extends com.vaadin.ui.Component, P, V>
        extends WebAbstractComponent<T> implements HasValue<V>, HasValueBinding<V> {

    @Inject
    protected ApplicationContext applicationContext;

    protected V internalValue;
    protected ValueBinding<V> valueBinding;


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
            ValueBinder binder = applicationContext.getBean(ValueBinder.NAME, ValueBinder.class);

            this.valueBinding = binder.bind(this, valueSource);

            valueBindingConnected(valueSource);

            this.valueBinding.activate();

            valueBindingActivated(valueSource);
        }
    }

    protected void valueBindingConnected(ValueSource<V> valueSource) {
        // hook
    }

    protected void valueBindingActivated(ValueSource<V> valueSource) {
        // hook
    }


    @Override
    public Subscription addValueChangeListener(ValueChangeListener listener) {
        getEventRouter().addListener(ValueChangeListener.class, listener);
        return () -> getEventRouter().removeListener(ValueChangeListener.class, listener);
    }

    @Override
    public void removeValueChangeListener(ValueChangeListener listener) {
        getEventRouter().removeListener(ValueChangeListener.class, listener);
    }

    @Override
    public V getValue() {
        return internalValue;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setValue(V value) {
        setValueToPresentation(convertToPresentation(value));

        V oldValue = internalValue;
        this.internalValue = value;

        if (!fieldValueEquals(value, oldValue)) {
            ValueChangeEvent event = new ValueChangeEvent(this, oldValue, value); // todo isUserOriginated
            getEventRouter().fireEvent(ValueChangeListener.class, ValueChangeListener::valueChanged, event);
        }
    }

    protected abstract void setValueToPresentation(P value);

    @SuppressWarnings("unchecked")
    protected V convertToModel(P componentRawValue) throws ConversionException {
        return (V) componentRawValue;
    }

    @SuppressWarnings("unchecked")
    protected P convertToPresentation(V modelValue) throws ConversionException {
        return (P) modelValue;
    }

    protected boolean fieldValueEquals(V value, V oldValue) {
        return InstanceUtils.propertyValueEquals(oldValue, value);
    }

}
