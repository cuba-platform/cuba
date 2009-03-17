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
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.web.gui.data.ItemWrapper;
import com.haulmont.cuba.core.entity.Entity;
import com.itmill.toolkit.data.Property;

import java.util.Set;
import java.util.List;
import java.util.ArrayList;

public abstract class AbstractField<T extends com.itmill.toolkit.ui.Field>
    extends
        AbstractComponent<T>
    implements
        Component.Field
{
    protected Datasource<Entity> datasource;
    protected MetaProperty metaProperty;

    private boolean editable;
    protected List<ValueListener> listeners = new ArrayList<ValueListener>();

    public Datasource getDatasource() {
        return datasource;
    }

    public MetaProperty getMetaProperty() {
        return metaProperty;
    }

    public void setDatasource(Datasource datasource, String property) {
        this.datasource = datasource;

        final MetaClass metaClass = datasource.getMetaClass();
        this.metaProperty = metaClass.getProperty(property);

        final ItemWrapper wrapper = createDatasourceWrapper(datasource, metaClass);
        component.setPropertyDataSource(wrapper.getItemProperty(metaProperty));

        setRequired(metaProperty.isMandatory());
    }

    protected ItemWrapper createDatasourceWrapper(Datasource datasource, MetaClass metaClass) {
        return new ItemWrapper(datasource, metaClass.getProperties());
    }

    public boolean isRequired() {
        return component.isRequired();
    }

    public void setRequired(boolean required) {
        component.setRequired(required);
    }

    public <T> T getValue() {
        return (T) component.getValue();
    }

    public void setValue(Object value) {
        component.setValue(value);
    }

    public String getCaption() {
        return component.getCaption();
    }

    public void setCaption(String caption) {
        component.setCaption(caption);
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
        component.setReadOnly(!editable);
    }

    public void addListener(ValueListener listener) {
        if (!listeners.contains(listener)) listeners.add(listener);
    }

    public void removeListener(ValueListener listener) {
        listeners.remove(listener);
    }

    protected void attachListener(T component) {
        component.addListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {
                // TODO (abramov) suport prevValue
                fireValueChanged(null, getValue());
            }
        });
    }

    protected void fireValueChanged(Object prevValue, Object value) {
        for (ValueListener listener : listeners) {
            listener.valueChanged(this, "value", prevValue, value);
        }
    }
}
