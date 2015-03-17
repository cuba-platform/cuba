/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.gui.components.Formatter;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.ValueChangingListener;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.web.gui.data.ItemWrapper;
import com.haulmont.cuba.web.gui.data.PropertyWrapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

/**
 * @author abramov
 * @version $Id$
 */
public class WebLabel extends WebAbstractComponent<com.vaadin.ui.Label> implements Label {

    protected List<ValueListener> listeners = new ArrayList<>();

    protected Datasource<Entity> datasource;
    protected MetaProperty metaProperty;
    protected MetaPropertyPath metaPropertyPath;

    protected Formatter formatter;

    public WebLabel() {
        component = new com.vaadin.ui.Label();
        component.setContentMode(com.vaadin.ui.Label.CONTENT_PREFORMATTED);

        component.setSizeUndefined();
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
    public MetaPropertyPath getMetaPropertyPath() {
        return metaPropertyPath;
    }

    @Override
    public void setDatasource(Datasource datasource, String property) {
        this.datasource = datasource;

        final MetaClass metaClass = datasource.getMetaClass();
        metaPropertyPath = metaClass.getPropertyPath(property);

        checkNotNullArgument(metaPropertyPath, "Could not resolve property path '%s' in '%s'", property, metaClass);

        metaProperty = metaPropertyPath.getMetaProperty();

        final ItemWrapper wrapper = createDatasourceWrapper(datasource, Collections.singleton(metaPropertyPath));
        component.setPropertyDataSource(wrapper.getItemProperty(metaPropertyPath));
    }

    protected ItemWrapper createDatasourceWrapper(Datasource datasource, Collection<MetaPropertyPath> propertyPaths) {
        return new ItemWrapper(datasource, datasource.getMetaClass(), propertyPaths) {
            @Override
            protected PropertyWrapper createPropertyWrapper(Object item, MetaPropertyPath propertyPath) {
                return new PropertyWrapper(item, propertyPath) {
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

    public String formatValue(Object value) {
        String text;
        if (formatter == null) {
            if (value == null) {
                text = "";
            } else {
                MetadataTools metadataTools = AppBeans.get(MetadataTools.NAME);

                if (metaProperty != null) {
                    text = metadataTools.format(value, metaProperty);
                } else {
                    text = metadataTools.format(value);
                }
            }
        } else {
            text = formatter.format(value);
        }
        return text;
    }

    @Override
    public <T> T getValue() {
        return (T) component.getValue();
    }

    @Override
    public void setValue(Object value) {
        final Object prevValue = getValue();
        if (metaProperty == null) {
            value = formatValue(value);
        }
        component.setValue(value);
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
        if (!listeners.contains(listener)) listeners.add(listener);
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

    @Override
    public boolean isHtmlEnabled() {
        return component.getContentMode() == com.vaadin.ui.Label.CONTENT_XHTML;
    }

    @Override
    public void setHtmlEnabled(boolean htmlEnabled) {
        component.setContentMode(htmlEnabled ? com.vaadin.ui.Label.CONTENT_XHTML : com.vaadin.ui.Label.CONTENT_PREFORMATTED);
    }
}