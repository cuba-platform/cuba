/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.Formatter;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.ValueChangingListener;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.gui.data.impl.DsListenerAdapter;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringEscapeUtils;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.haulmont.bali.util.Preconditions.checkNotNullArgument;

/**
 * @author krivopustov
 * @version $Id$
 */
public class DesktopLabel extends DesktopAbstractComponent<JLabel> implements Label {

    protected Datasource<Entity> datasource;
    protected MetaProperty metaProperty;
    protected MetaPropertyPath metaPropertyPath;

    protected List<ValueListener> listeners = new ArrayList<>();

    protected DefaultValueFormatter valueFormatter;

    protected Object prevValue;

    protected boolean updatingInstance = false;

    protected boolean htmlEnabled = false;

    protected String labelText = "";

    public DesktopLabel() {
        impl = new JLabel();
        impl.setFocusable(false);
        setAlignment(Alignment.MIDDLE_LEFT);

        UserSessionSource sessionSource = AppBeans.get(UserSessionSource.NAME);
        Locale locale = sessionSource.getLocale();
        valueFormatter = new DefaultValueFormatter(locale);
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

        if (datasource == null) {
            setValue(null);
            return;
        }

        final MetaClass metaClass = datasource.getMetaClass();
        metaPropertyPath = metaClass.getPropertyPath(property);

        checkNotNullArgument(metaPropertyPath, "Could not resolve property path '%s' in '%s'", property, metaClass);

        metaProperty = metaPropertyPath.getMetaProperty();

        valueFormatter.setMetaProperty(metaProperty);

        datasource.addListener(
                new DsListenerAdapter() {
                    @Override
                    public void itemChanged(Datasource ds, Entity prevItem, Entity item) {
                        if (updatingInstance) {
                            return;
                        }

                        Object value = InstanceUtils.getValueEx(item, metaPropertyPath.getPath());
                        updateComponent(value);
                        fireChangeListeners(value);
                    }

                    @Override
                    public void valueChanged(Entity source, String property, Object prevValue, Object value) {
                        if (updatingInstance) {
                            return;
                        }

                        if (property.equals(metaPropertyPath.toString())) {
                            updateComponent(value);
                            fireChangeListeners(value);
                        }
                    }
                }
        );

        if ((datasource.getState() == Datasource.State.VALID) && (datasource.getItem() != null)) {
            Object newValue = InstanceUtils.getValueEx(datasource.getItem(), metaPropertyPath.getPath());
            updateComponent(newValue);
            fireChangeListeners(newValue);
        }
    }

    @Override
    public boolean isEditable() {
        return false;
    }

    @Override
    public void setEditable(boolean editable) {
    }

    @Override
    public Formatter getFormatter() {
        return valueFormatter.getFormatter();
    }

    @Override
    public void setFormatter(Formatter formatter) {
        valueFormatter.setFormatter(formatter);
        updateComponent(prevValue);
    }

    @Override
    public <T> T getValue() {
        return (T) prevValue;
    }

    @Override
    public void setValue(Object value) {
        if (!ObjectUtils.equals(prevValue, value)) {
            updateInstance(value);
            updateComponent(value);
            fireChangeListeners(value);
        }
    }

    protected void updateInstance(Object value) {
        if (updatingInstance) {
            return;
        }

        if (ObjectUtils.equals(prevValue, value)) {
            return;
        }

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

    protected void updateComponent(Object value) {
        String text = valueFormatter.formatValue(value);
        this.labelText = text;
        updateLabel(text);
    }

    private void updateLabel(String text) {
        if (!htmlEnabled) {
            text = StringEscapeUtils.escapeHtml(text);
            if (getWidth() > 0 && getHeight() <= 0) {
                text = ComponentsHelper.preprocessHtmlMessage("<html>" + text + "</html>");
            } else {
                text = ComponentsHelper.preprocessHtmlMessage("<html><nobr>" + text + "</nobr></html>");
            }
        } else {
            text = "<html>" + text + "</html>";
        }
        impl.setText(text);
    }

    protected void fireChangeListeners(Object newValue) {
        Object oldValue = prevValue;
        prevValue = newValue;
        if (!ObjectUtils.equals(oldValue, newValue)) {
            fireValueChanged(oldValue, newValue);
        }
    }

    @Override
    public void setWidth(String width) {
        super.setWidth(width);

        updateLabel(this.labelText);
    }

    @Override
    public void setHeight(String height) {
        super.setHeight(height);

        updateLabel(this.labelText);
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

    protected void fireValueChanged(Object prevValue, Object value) {
        for (ValueListener listener : listeners) {
            listener.valueChanged(this, "value", prevValue, value);
        }
    }

    @Override
    public boolean isHtmlEnabled() {
        return htmlEnabled;
    }

    @Override
    public void setHtmlEnabled(boolean htmlEnabled) {
        this.htmlEnabled = htmlEnabled;
    }
}