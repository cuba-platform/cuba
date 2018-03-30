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
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.components.Formatter;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.components.data.DatasourceValueSource;
import com.haulmont.cuba.gui.components.data.ValueBinder;
import com.haulmont.cuba.gui.components.data.ValueBinding;
import com.haulmont.cuba.gui.components.data.ValueSource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.web.widgets.CubaLabel;
import com.vaadin.shared.ui.ContentMode;

public class WebLabel<V> extends WebAbstractComponent<com.vaadin.ui.Label> implements Label<V> {

    protected V internalValue;
    protected ValueBinding<V> valueBinding;

    protected Formatter formatter;

    public WebLabel() {
        component = new CubaLabel();
        component.setSizeUndefined();
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
        if (datasource != null) {
            this.setValueSource(new DatasourceValueSource(datasource, property));
        } else {
            this.setValueSource(null);
        }
    }

    protected void valueBindingActivated(ValueSource<V> valueSource) {
        // hook
    }

    protected void valueBindingConnected(ValueSource<V> valueSource) {
        // hook
    }

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
        String prevComponentValue = component.getValue();
        String newComponentValue = convertToPresentation(value);
        component.setValue(newComponentValue);

        componentValueChanged(prevComponentValue, newComponentValue);
    }

    protected void componentValueChanged(String prevComponentValue, String newComponentValue) {
        V value = convertToModel(newComponentValue);
        V oldValue = internalValue;
        internalValue = value;

        if (!fieldValueEquals(value, oldValue)) {
            if (hasValidationError()) {
                setValidationError(null);
            }

            ValueChangeEvent event = new ValueChangeEvent(this, oldValue, value); // todo isUserOriginated
            getEventRouter().fireEvent(ValueChangeListener.class, ValueChangeListener::valueChanged, event);
        }
    }

    @SuppressWarnings("unchecked")
    protected V convertToModel(String componentRawValue) {
        return (V) componentRawValue;
    }

    @SuppressWarnings("unchecked")
    protected String convertToPresentation(V modelValue) {
        // todo implement
        return (String) modelValue;
    }

    protected boolean fieldValueEquals(V value, V oldValue) {
        return InstanceUtils.propertyValueEquals(oldValue, value);
    }

    @Override
    public Formatter getFormatter() {
        return formatter;
    }

    @Override
    public void setFormatter(Formatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public boolean isHtmlEnabled() {
        return component.getContentMode() == ContentMode.HTML;
    }

    @Override
    public void setHtmlEnabled(boolean htmlEnabled) {
        component.setContentMode(htmlEnabled ? ContentMode.HTML : ContentMode.TEXT);
    }

    @Override
    public String getRawValue() {
        return component.getValue();
    }
}