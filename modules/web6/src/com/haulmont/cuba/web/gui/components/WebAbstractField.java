/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.ValueChangingListener;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.web.gui.data.ItemWrapper;
import com.vaadin.data.Property;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import java.util.*;
import java.util.List;

/**
 * @author abramov
 * @version $Id$
 */
public abstract class WebAbstractField<T extends com.vaadin.ui.Field>
        extends
            WebAbstractComponent<T>
        implements
            Field {

    protected Datasource<Entity> datasource;
    protected MetaProperty metaProperty;
    protected MetaPropertyPath metaPropertyPath;

    protected List<ValueListener> listeners = new ArrayList<>();
    protected ValueChangingListener valueChangingListener;
    protected List<Field.Validator> validators = new ArrayList<>();

    protected boolean settingValue = false;

    protected Object prevValue;

    @Override
    public Datasource getDatasource() {
        return datasource;
    }

    @Override
    public MetaProperty getMetaProperty() {
        return metaProperty;
    }

    @Override
    public void setDatasource(Datasource datasource, String property) {
        this.datasource = datasource;

        final MetaClass metaClass = datasource.getMetaClass();
        metaPropertyPath = metaClass.getPropertyPath(property);
        try {
            metaProperty = metaPropertyPath.getMetaProperty();
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new RuntimeException("Metaproperty name is possibly wrong: " + property, e);
        }

        final ItemWrapper wrapper = createDatasourceWrapper(datasource, Collections.singleton(metaPropertyPath));
        component.setPropertyDataSource(wrapper.getItemProperty(metaPropertyPath));

        setRequired(metaProperty.isMandatory());
        if (StringUtils.isEmpty(getRequiredMessage())) {
            MessageTools messageTools = AppBeans.get(MessageTools.NAME);
            setRequiredMessage(messageTools.getDefaultRequiredMessage(metaProperty));
        }
    }

    protected ItemWrapper createDatasourceWrapper(Datasource datasource, Collection<MetaPropertyPath> propertyPaths) {
        return new ItemWrapper(datasource, propertyPaths);
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
    public <T> T getValue() {
        return (T) component.getValue();
    }

    @Override
    public void setValue(Object value) {
        if (component.isReadOnly())
            return;
        component.setValue(value);
    }

    @Override
    public String getCaption() {
        return component.getCaption();
    }

    @Override
    public void setCaption(String caption) {
        component.setCaption(caption);
    }

    @Override
    public String getDescription() {
        return component.getDescription();
    }

    @Override
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
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    @Override
    public void removeListener(ValueListener listener) {
        listeners.remove(listener);
    }

    protected void attachListener(T component) {
        component.addListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (settingValue)
                    return;

                settingValue = true;

                final Object value = getValue();
                Object newValue = fireValueChanging(prevValue, value);

                final Object oldValue = prevValue;
                prevValue = newValue;

                if (!ObjectUtils.equals(value, newValue)) {
                    WebAbstractField.this.component.setValue(newValue);
                }

                fireValueChanged(oldValue, newValue);

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
        if (!ObjectUtils.equals(prevValue, value)) {
            for (ValueListener listener : listeners) {
                listener.valueChanged(this, "value", prevValue, value);
            }
        }
    }

    @Override
    public void addValidator(Field.Validator validator) {
        if (!validators.contains(validator))
            validators.add(validator);
    }

    @Override
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
                throw new RequiredValueMissingException(getRequiredMessage(), this);
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

    @Override
    protected String getAlternativeDebugId() {
        if (id != null) {
            return id;
        }
        if (datasource != null && StringUtils.isNotEmpty(datasource.getId()) && metaPropertyPath != null) {
            return getClass().getSimpleName() + "_" + datasource.getId() + "_" + metaPropertyPath.toString();
        }

        return getClass().getSimpleName();
    }
}