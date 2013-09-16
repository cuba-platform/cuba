/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Formatter;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.ValueChangingListener;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.web.gui.data.ItemWrapper;
import com.haulmont.cuba.web.toolkit.ui.CubaLabel;
import com.haulmont.cuba.web.toolkit.ui.converters.StringToDatatypeConverter;
import com.haulmont.cuba.web.toolkit.ui.converters.StringToEntityConverter;
import com.haulmont.cuba.web.toolkit.ui.converters.StringToEnumConverter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author abramov
 * @version $Id$
 */
public class WebLabel
        extends
            WebAbstractComponent<com.vaadin.ui.Label>
        implements
            Label, Component.Wrapper {

    protected List<ValueListener> listeners = new ArrayList<>();

    protected Datasource<Entity> datasource;
    protected MetaProperty metaProperty;
    protected MetaPropertyPath metaPropertyPath;

    protected Formatter formatter;

    public WebLabel() {
        component = new CubaLabel();
    }

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
        metaProperty = metaPropertyPath.getMetaProperty();

        switch (metaProperty.getType()) {
            case ASSOCIATION:
                component.setConverter(new StringToEntityConverter() {
                    @Override
                    public Formatter getFormatter() {
                        return WebLabel.this.formatter;
                    }
                });
                break;

            case DATATYPE:
                Datatype<?> datatype = Datatypes.get(metaProperty.getJavaType());
                if (datatype != null) {
                    component.setConverter(new StringToDatatypeConverter(datatype) {
                        @Override
                        public Formatter getFormatter() {
                            return WebLabel.this.formatter;
                        }
                    });
                } else {
                    component.setConverter(null);
                }
                break;

            case ENUM:
                //noinspection unchecked
                component.setConverter(new StringToEnumConverter((Class<Enum>) metaProperty.getJavaType()) {
                    @Override
                    public Formatter getFormatter() {
                        return WebLabel.this.formatter;
                    }
                });
                break;

            default:
                component.setConverter(new StringToDatatypeConverter(Datatypes.getNN(String.class)) {
                    @Override
                    public Formatter getFormatter() {
                        return WebLabel.this.formatter;
                    }
                });
                break;
        }

        final ItemWrapper wrapper = createDatasourceWrapper(datasource, Collections.singleton(metaPropertyPath));
        component.setPropertyDataSource(wrapper.getItemProperty(metaPropertyPath));
    }

    protected ItemWrapper createDatasourceWrapper(Datasource datasource, Collection<MetaPropertyPath> propertyPaths) {
        return new ItemWrapper(datasource, propertyPaths);
    }

    @Override
    public <T> T getValue() {
        return (T) component.getValue();
    }

    @Override
    public void setValue(Object value) {
        final Object prevValue = getValue();
        component.setValue((String) value);
        fireValueChanged(prevValue, value);
    }

    @Override
    public boolean isEditable() {
        return false;
    }

    @Override
    public void setEditable(boolean editable) {
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

    @Override
    public void setValueChangingListener(ValueChangingListener listener) {
    }

    @Override
    public void removeValueChangingListener() {
    }

    @Override
    public Formatter getFormatter() {
        return formatter;
    }

    @Override
    public void setFormatter(Formatter formatter) {
        this.formatter = formatter;
    }

    protected void fireValueChanged(Object prevValue, Object value) {
        for (ValueListener listener : listeners) {
            listener.valueChanged(this, "value", prevValue, value);
        }
    }
}