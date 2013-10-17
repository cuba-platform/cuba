/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 30.12.2008 16:27:03
 * $Id$
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.ValueChangingListener;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.web.gui.data.ItemWrapper;
import com.vaadin.data.Property;
import org.apache.commons.lang.ObjectUtils;

import java.util.*;
import java.util.List;

public abstract class WebAbstractField<T extends com.vaadin.ui.Field>
    extends
        WebAbstractComponent<T>
    implements
        Component.HasValue, Component.Validatable
{
    protected Datasource<Entity> datasource;
    protected MetaProperty metaProperty;
    protected MetaPropertyPath metaPropertyPath;

    protected List<ValueListener> listeners = new ArrayList<ValueListener>();
    protected ValueChangingListener valueChangingListener;
    protected List<Field.Validator> validators = new ArrayList<Field.Validator>();

    protected boolean settingValue = false;

    protected String requiredMessage;

    public Datasource getDatasource() {
        return datasource;
    }

    public MetaProperty getMetaProperty() {
        return metaProperty;
    }

    public void setDatasource(Datasource datasource, String property) {
        this.datasource = datasource;

        final MetaClass metaClass = datasource.getMetaClass();
        metaPropertyPath = metaClass.getPropertyEx(property);
        try {
            metaProperty = metaPropertyPath.getMetaProperty();
        }
        catch (ArrayIndexOutOfBoundsException e) {
            throw new RuntimeException("Metaproperty name is possibly wrong: " + property, e);
        }

        final ItemWrapper wrapper = createDatasourceWrapper(datasource, Collections.singleton(metaPropertyPath));
        component.setPropertyDataSource(wrapper.getItemProperty(metaPropertyPath));

        setRequired(metaProperty.isMandatory());
    }

    protected ItemWrapper createDatasourceWrapper(Datasource datasource, Collection<MetaPropertyPath> propertyPaths) {
        return new ItemWrapper(datasource, propertyPaths);
    }

    public boolean isRequired() {
        return component.isRequired();
    }

    public void setRequired(boolean required) {
        component.setRequired(required);
    }

    public void setRequiredMessage(String msg) {
        requiredMessage = msg;
        component.setRequiredError(msg);
    }

    @Override
    public <T> T getValue() {
        return (T) component.getValue();
    }

    @Override
    public void setValue(Object value) {
        if (component.isReadOnly())
            return;
        component.setValue(value);
    }

    public String getCaption() {
        return component.getCaption();
    }

    public void setCaption(String caption) {
        component.setCaption(caption);
    }

    public String getDescription() {
        return component.getDescription();
    }

    public void setDescription(String description) {
        component.setDescription(description);
    }

    @Override
    public boolean isEditable() {
        return !component.isReadOnly();
    }

    @Override
    public void setEditable(boolean editable) {
        component.setReadOnly(!editable);
    }

    @Override
    public void addListener(ValueListener listener) {
        if (!listeners.contains(listener)) listeners.add(listener);
    }

    @Override
    public void removeListener(ValueListener listener) {
        listeners.remove(listener);
    }

    protected Object prevValue;

    protected void attachListener(T component) {
        component.addListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (settingValue)
                    return;

                settingValue = true;

                final Object value = getValue();
                Object newValue = fireValueChanging(prevValue, value);
                fireValueChanged(prevValue, newValue);
                prevValue = newValue;

                if (!ObjectUtils.equals(value, newValue))
                    WebAbstractField.this.component.setValue(newValue);

                settingValue = false;
            }
        });
    }

    @Override
    public void setValueChangingListener(ValueChangingListener listener) {
        valueChangingListener = listener;
    }

    @Override
    public void removeValueChangingListener() {
        valueChangingListener = null;
    }

    protected Object fireValueChanging(Object prevValue, Object value) {
        if (valueChangingListener != null)
            return valueChangingListener.valueChanging(this, "value", prevValue, value);
        else
            return value;
    }

    protected void fireValueChanged(Object prevValue, Object value) {
        for (ValueListener listener : listeners) {
            listener.valueChanged(this, "value", prevValue, value);
        }
    }

    public void addValidator(Field.Validator validator) {
        if (!validators.contains(validator))
            validators.add(validator);
    }

    public void removeValidator(Field.Validator validator) {
        validators.remove(validator);
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
        if (!isVisible() || !isEditable() || !isEnabled())
            return;

        Object value = getValue();
        if (isEmpty(value)) {
            if (isRequired())
                throw new RequiredValueMissingException(requiredMessage, this);
            else
                return;
        }

        for (Field.Validator validator : validators) {
            validator.validate(value);
        }
    }

    protected boolean isEmpty(Object value) {
        return value == null;
    }
}
