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
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.ValidationException;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.web.gui.data.DsManager;
import com.haulmont.cuba.web.gui.data.ItemWrapper;
import com.vaadin.data.Property;
import com.vaadin.data.Validator;

import java.util.*;

public abstract class WebAbstractField<T extends com.vaadin.ui.Field>
    extends
        WebAbstractComponent<T>
    implements
        Component.HasValue
{
    protected Datasource<Entity> datasource;
    protected MetaProperty metaProperty;
    protected MetaPropertyPath metaPropertyPath;

    protected List<ValueListener> listeners = new ArrayList<ValueListener>();
    protected Map<com.haulmont.cuba.gui.components.Field.Validator, Validator> validators =
            new HashMap<com.haulmont.cuba.gui.components.Field.Validator, Validator>();

    protected DsManager dsManager;

    public Datasource getDatasource() {
        return datasource;
    }

    public MetaProperty getMetaProperty() {
        return metaProperty;
    }

    public void setDatasource(Datasource datasource, String property) {
        this.datasource = datasource;

        dsManager = new DsManager(datasource, this);

        final MetaClass metaClass = datasource.getMetaClass();
        metaPropertyPath = metaClass.getPropertyEx(property);
        try {
            metaProperty = metaPropertyPath.getMetaProperty();
        }
        catch (ArrayIndexOutOfBoundsException e) {
            throw new RuntimeException("Metaproperty name is possibly wrong: " + property, e);
        }

        final ItemWrapper wrapper = createDatasourceWrapper(datasource, Collections.singleton(metaPropertyPath), dsManager);
        component.setPropertyDataSource(wrapper.getItemProperty(metaPropertyPath));

        setRequired(metaProperty.isMandatory());
    }

    protected ItemWrapper createDatasourceWrapper(Datasource datasource, Collection<MetaPropertyPath> propertyPaths, DsManager dsManager) {
        return new ItemWrapper(datasource, propertyPaths, dsManager);
    }

    public boolean isRequired() {
        return component.isRequired();
    }

    public void setRequired(boolean required) {
        component.setRequired(required);
    }

    public void setRequiredMessage(String msg) {
        component.setRequiredError(msg);
    }

    public <T> T getValue() {
        return (T) component.getValue();
    }

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

    public boolean isEditable() {
        return !component.isReadOnly();
    }

    public void setEditable(boolean editable) {
        component.setReadOnly(!editable);
    }

    public void addListener(ValueListener listener) {
        if (!listeners.contains(listener)) listeners.add(listener);
    }

    public void removeListener(ValueListener listener) {
        listeners.remove(listener);
    }

    protected Object prevValue;

    protected void attachListener(T component) {
        component.addListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {
                final Object value = getValue();
                fireValueChanged(prevValue, value);
                prevValue = value;
            }
        });
    }

    protected void fireValueChanged(Object prevValue, Object value) {
        for (ValueListener listener : listeners) {
            listener.valueChanged(this, "value", prevValue, value);
        }
    }

    public void addValidator(final com.haulmont.cuba.gui.components.Field.Validator validator) {
        if (!validators.containsKey(validator)) {
            final Validator componentValidator = new Validator() {

                public void validate(Object value) throws InvalidValueException {
                    if ((!isRequired() && value == null))
                        return;
                    try {
                        validator.validate(value);
                    } catch (ValidationException e) {
                        throw new InvalidValueException(e.getMessage());
                    }
                }

                public boolean isValid(Object value) {
                    try {
                        validate(value);
                        return true;
                    } catch (InvalidValueException e) {
                        getFrame().showNotification(e.getMessage(), IFrame.NotificationType.HUMANIZED);
                        return false;
                    }
                }
            };

            component.addValidator(componentValidator);
            validators.put(validator, componentValidator);
        }
    }

    public void removeValidator(com.haulmont.cuba.gui.components.Field.Validator validator) {
        component.removeValidator(validators.get(validator));
        validators.remove(validator);
    }

    public boolean isValid() {
        return component.isValid();
    }

    public void validate() throws ValidationException {
        try {
            component.validate();
        } catch (Validator.InvalidValueException e) {
            throw new ValidationException(e.getMessage());
        }
    }
}
