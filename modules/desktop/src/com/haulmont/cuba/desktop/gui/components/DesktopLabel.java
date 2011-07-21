/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.chile.core.model.*;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.UserSessionProvider;
import com.haulmont.cuba.gui.components.Formatter;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.gui.data.impl.DsListenerAdapter;
import org.apache.commons.lang.ObjectUtils;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class DesktopLabel extends DesktopAbstractComponent<JLabel> implements Label
{
    protected Datasource<Entity> datasource;
    protected MetaProperty metaProperty;
    protected MetaPropertyPath metaPropertyPath;

    protected Formatter formatter;

    protected List<ValueListener> listeners = new ArrayList<ValueListener>();

    private Locale locale = UserSessionProvider.getLocale();

    private Object prevValue;

    private boolean updatingInstance = false;

    public DesktopLabel() {
        impl = new JLabel();
        setAlignment(Alignment.MIDDLE_LEFT);
    }

    public Datasource getDatasource() {
        return datasource;
    }

    public MetaProperty getMetaProperty() {
        return metaProperty;
    }

    public void setDatasource(Datasource datasource, String property) {
        this.datasource = datasource;

        if (datasource == null) {
            setValue(null);
            return;
        }

        final MetaClass metaClass = datasource.getMetaClass();
        metaPropertyPath = metaClass.getPropertyPath(property);
        metaProperty = metaPropertyPath.getMetaProperty();

        datasource.addListener(
                new DsListenerAdapter() {
                    @Override
                    public void itemChanged(Datasource ds, Entity prevItem, Entity item) {
                        Object value = InstanceUtils.getValueEx(item, metaPropertyPath.getPath());
                        String text = formatValue(value);
                        impl.setText(text);
                        fireChangeListeners();
                    }

                    @Override
                    public void valueChanged(Entity source, String property, Object prevValue, Object value) {
                        if (property.equals(metaPropertyPath.toString())) {
                            String text = formatValue(value);
                            impl.setText(text);
                            fireChangeListeners();
                        }
                    }
                }
        );

        if ((datasource.getState() == Datasource.State.VALID) && (datasource.getItem() != null)) {
            Object newValue = InstanceUtils.getValueEx(datasource.getItem(), metaPropertyPath.getPath());
            setValue(newValue);
        }
    }

    private String formatValue(Object value) {
        String text;
        if (metaProperty != null)
            text = formatValue(value, metaProperty);
        else
            text = value == null ? "" : String.valueOf(value);
        return text;
    }

    private String formatValue(Object value, MetaProperty metaProperty) {
        String text;
        if (value == null) {
            text = "";
        } else if (formatter == null) {
            Range range = metaProperty.getRange();
            if (range.isDatatype()) {
                text = range.asDatatype().format(value, locale);
            } else if (range.isEnum()) {
                text = value.toString();
            } else if (range.isClass()) {
                text = InstanceUtils.getInstanceName((Instance) value);
            } else
                text = value.toString();
        } else {
            text = formatter.format(value);
        }
        return text;
    }

    public boolean isEditable() {
        return false;
    }

    public void setEditable(boolean editable) {
    }

    public Formatter getFormatter() {
        return formatter;
    }

    public void setFormatter(Formatter formatter) {
        this.formatter = formatter;
    }

    public <T> T getValue() {
        return (T) impl.getText();
    }

    public void setValue(Object value) {
        impl.setText(formatValue(value));
        updateInstance(value);
        fireChangeListeners();
    }

    private void updateInstance(Object value) {
        if (updatingInstance)
            return;

        if (ObjectUtils.equals(prevValue, value))
            return;

        updatingInstance = true;
        try {
            if ((datasource != null) && (metaPropertyPath != null)) {
                if (datasource.getItem() != null) {
                    InstanceUtils.setValueEx(datasource.getItem(), metaPropertyPath.getPath(), value);
                }
            }
        } finally {
            updatingInstance = false;
        }
    }

    private void fireChangeListeners() {
        Object newValue = getValue();
        if (!ObjectUtils.equals(prevValue, newValue)) {
            fireValueChanged(prevValue, newValue);
            prevValue = newValue;
        }
    }

    public void addListener(ValueListener listener) {
        if (!listeners.contains(listener))
            listeners.add(listener);
    }

    public void removeListener(ValueListener listener) {
        listeners.remove(listener);
    }

    protected void fireValueChanged(Object prevValue, Object value) {
        for (ValueListener listener : listeners) {
            listener.valueChanged(this, "value", prevValue, value);
        }
    }
}
