/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.Formatter;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.gui.data.impl.DsListenerAdapter;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

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
                    }
                    @Override
                    public void valueChanged(Entity source, String property, Object prevValue, Object value) {
                        if (property.equals(metaPropertyPath.toString())) {
                            String text = formatValue(value);
                            impl.setText(text);
                        }
                    }
                }
        );
    }

    private String formatValue(Object value) {
        String text;
        if (value == null) {
            text = "";
        } else if (formatter == null) {
            if (value instanceof Instance) {
                text = InstanceUtils.getInstanceName((Instance) value);
            } else {
                text = value.toString();
            }
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

    public String getCaption() {
        return null;
    }

    public void setCaption(String caption) {
    }

    public String getDescription() {
        return null;
    }

    public void setDescription(String description) {
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
        impl.setText((String) value);
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
