/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 22.12.2008 18:16:37
 * $Id$
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Formatter;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.web.gui.data.DsManager;
import com.haulmont.cuba.web.gui.data.ItemWrapper;
import com.haulmont.cuba.web.gui.data.PropertyWrapper;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.Instance;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Collection;

public class WebLabel
    extends
        WebAbstractComponent<com.vaadin.ui.Label>
    implements
        Label, Component.Wrapper
{
    protected List<ValueListener> listeners = new ArrayList<ValueListener>();

    protected Datasource<Entity> datasource;
    protected MetaProperty metaProperty;
    protected MetaPropertyPath metaPropertyPath;

    protected Formatter formatter;

    protected DsManager dsManager;

    public WebLabel() {
        component = new com.vaadin.ui.Label();
    }

    public Datasource getDatasource() {
        return datasource;
    }

    public MetaProperty getMetaProperty() {
        return metaProperty;
    }

    public void setDatasource(Datasource datasource, String property) {
        this.datasource = datasource;
        this.dsManager = new DsManager(datasource, this);

        final MetaClass metaClass = datasource.getMetaClass();
        metaPropertyPath = metaClass.getPropertyPath(property);
        metaProperty = metaPropertyPath.getMetaProperty();

        final ItemWrapper wrapper = createDatasourceWrapper(datasource, Collections.singleton(metaPropertyPath), dsManager);
        component.setPropertyDataSource(wrapper.getItemProperty(metaPropertyPath));
    }

    protected ItemWrapper createDatasourceWrapper(Datasource datasource, Collection<MetaPropertyPath> propertyPaths, DsManager dsManager) {
        return new ItemWrapper(datasource, propertyPaths, dsManager) {
            @Override
            protected PropertyWrapper createPropertyWrapper(Object item, MetaPropertyPath propertyPath, DsManager dsManager) {
                return new PropertyWrapper(item, propertyPath, dsManager) {
                    @Override
                    public String toString() {
                        if (formatter != null) {
                            Object value = getValue();
                            if (value instanceof Instance)
                                value = ((Instance) value).getInstanceName();
                            return formatter.format(value);
                        } else {
                            return super.toString();
                        }
                    }
                };
            }
        };
    }

    public <T> T getValue() {
        return (T) component.getValue();
    }

    public void setValue(Object value) {
        final Object prevValue = getValue();
        component.setValue(value);
        fireValueChanged(prevValue, value);
    }

    public boolean isEditable() {
        return false;
    }

    public void setEditable(boolean editable) {
    }

    public void addListener(ValueListener listener) {
        if (!listeners.contains(listener)) listeners.add(listener);
    }

    public void removeListener(ValueListener listener) {
        listeners.remove(listener);
    }

    public Formatter getFormatter() {
        return formatter;
    }

    public void setFormatter(Formatter formatter) {
        this.formatter = formatter;
    }

    protected void fireValueChanged(Object prevValue, Object value) {
        for (ValueListener listener : listeners) {
            listener.valueChanged(this, "value", prevValue, value);
        }
    }
}
