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
import com.haulmont.bali.util.Preconditions;
import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesUtils;
import com.haulmont.cuba.core.entity.annotation.CurrencyValue;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.components.CurrencyField;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.components.ValidationException;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.web.widgets.CubaTextField;

import java.util.Map;

public class WebCurrencyField<V> extends WebAbstractField<CubaCurrencyField, V> implements CurrencyField<V> {
    protected TextField textField;

    public WebCurrencyField() {
        // todo rework
        textField = AppBeans.get(ComponentsFactory.class).createComponent(TextField.class);

        this.component = new CubaCurrencyField(textField.unwrap(CubaTextField.class));

        com.haulmont.cuba.web.widgets.CurrencyLabelPosition currencyLabelPosition =
                com.haulmont.cuba.web.widgets.CurrencyLabelPosition.valueOf(CurrencyLabelPosition.RIGHT.name());
        this.component.setCurrencyLabelPosition(currencyLabelPosition);
    }

    @Override
    public void setCurrency(String currency) {
        component.setCurrency(currency);
    }

    @Override
    public String getCurrency() {
        return component.getCurrency();
    }

    @Override
    public void setShowCurrencyLabel(boolean showCurrencyLabel) {
        component.setShowCurrencyLabel(showCurrencyLabel);
    }

    @Override
    public boolean getShowCurrencyLabel() {
        return component.getShowCurrencyLabel();
    }

    @Override
    public void setCurrencyLabelPosition(CurrencyLabelPosition currencyLabelPosition) {
        Preconditions.checkNotNullArgument(currencyLabelPosition);

        com.haulmont.cuba.web.widgets.CurrencyLabelPosition wAlign =
                com.haulmont.cuba.web.widgets.CurrencyLabelPosition.valueOf(currencyLabelPosition.name());

        component.setCurrencyLabelPosition(wAlign);
    }

    @Override
    public CurrencyLabelPosition getCurrencyLabelPosition() {
        return CurrencyLabelPosition.valueOf(component.getCurrencyLabelPosition().name());
    }

    @Override
    public void setDatasource(Datasource datasource, String property) {
        textField.setDatasource(datasource, property);

        if (datasource != null && !DynamicAttributesUtils.isDynamicAttribute(property)) {
            MetaProperty metaProperty = datasource.getMetaClass().getPropertyNN(property);

            Object obj = metaProperty.getAnnotations().get(CurrencyValue.class.getName());
            if (obj == null)
                return;

            //noinspection unchecked
            Map<String, Object> currencyValue = (Map<String, Object>) obj;
            String currencyName = (String) currencyValue.get("currency");
            component.setCurrency(currencyName);
        }
    }

    @Override
    public Datasource getDatasource() {
        return textField.getDatasource();
    }

    @Override
    public MetaProperty getMetaProperty() {
        return textField.getMetaProperty();
    }

    @Override
    public MetaPropertyPath getMetaPropertyPath() {
        return textField.getMetaPropertyPath();
    }

    @Override
    public void setDatatype(Datatype datatype) {
        Preconditions.checkNotNullArgument(datatype);

        component.setDatatype(datatype);
    }

    @Override
    public Datatype getDatatype() {
        return component.getDatatype();
    }

    @Override
    public Subscription addValueChangeListener(ValueChangeListener listener) {
        textField.addValueChangeListener(listener);
        // todo
        return () -> {};
    }

    @Override
    public void removeValueChangeListener(ValueChangeListener listener) {
        textField.removeValueChangeListener(listener);
    }

    @Override
    public V getValue() {
        return (V) textField.getValue();
    }

    @Override
    public void setValue(V value) {
        textField.setValue(value);
    }

    @Override
    public void setBuffered(boolean buffered) {
        textField.setBuffered(buffered);
    }

    @Override
    public boolean isBuffered() {
        return textField.isBuffered();
    }

    @Override
    public void commit() {
        textField.commit();
    }

    @Override
    public void discard() {
        textField.discard();
    }

    @Override
    public boolean isModified() {
        return textField.isModified();
    }

    @Override
    public void validate() throws ValidationException {
        if (hasValidationError()) {
            setValidationError(null);
        }

        if (!isVisible() || !isEditableWithParent() || !isEnabled()) {
            return;
        }

        textField.validate();
    }

    @Override
    public boolean isRequired() {
        return textField.isRequired();
    }

    @Override
    public void setRequired(boolean required) {
        textField.setRequired(required);
    }
}
