/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.utils.InstanceUtils;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.UserSessionProvider;
import com.haulmont.cuba.gui.components.DateField;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.ValidationException;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.ValueListener;
import com.haulmont.cuba.gui.data.impl.DsListenerAdapter;
import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class DesktopDateField
    extends DesktopAbstractComponent<JXDatePicker>
    implements DateField
{
    private Resolution resolution;
    private Datasource datasource;
    private MetaPropertyPath metaPropertyPath;
    private MetaProperty metaProperty;

    private boolean updatingInstance;
    private boolean required;
    private String requiredMessage;

    public DesktopDateField() {
        impl = new JXDatePicker();
        String dateFormat = Datatypes.getFormatStrings(UserSessionProvider.getLocale()).getDateFormat();
        impl.setFormats(dateFormat);
    }

    @Override
    public Resolution getResolution() {
        return resolution;
    }

    @Override
    public void setResolution(Resolution resolution) {
        this.resolution = resolution;
    }

    @Override
    public String getDateFormat() {
        return null;
    }

    @Override
    public void setDateFormat(String dateFormat) {
    }

    @Override
    public boolean isRequired() {
        return required;
    }

    @Override
    public void setRequired(boolean required) {
        this.required = required;
    }

    @Override
    public void setRequiredMessage(String msg) {
        requiredMessage = msg;
    }

    @Override
    public <T> T getValue() {
        return (T) impl.getDate();
    }

    @Override
    public void setValue(Object value) {
        impl.setDate((Date) value);
    }

    @Override
    public void addListener(ValueListener listener) {
    }

    @Override
    public void removeListener(ValueListener listener) {
    }

    @Override
    public void addValidator(Validator validator) {
    }

    @Override
    public void removeValidator(Validator validator) {
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public void validate() throws ValidationException {
    }

    @Override
    public Datasource getDatasource() {
        return null;
    }

    @Override
    public MetaProperty getMetaProperty() {
        return null;
    }

    @Override
    public void setDatasource(Datasource datasource, String property) {
        this.datasource = datasource;

        final MetaClass metaClass = datasource.getMetaClass();
        metaPropertyPath = metaClass.getPropertyPath(property);
        metaProperty = metaPropertyPath.getMetaProperty();

        datasource.addListener(
                new DsListenerAdapter() {
                    @Override
                    public void itemChanged(Datasource ds, Entity prevItem, Entity item) {
                        if (updatingInstance)
                            return;
                        Date value = InstanceUtils.getValueEx(item, metaPropertyPath.getPath());
                        updatingInstance = true;
                        try {
                            impl.setDate(value);
                        } finally {
                            updatingInstance = false;
                        }
                    }

                    @Override
                    public void valueChanged(Entity source, String property, Object prevValue, Object value) {
                        if (updatingInstance)
                            return;
                        if (property.equals(metaPropertyPath.toString())) {
                            updatingInstance = true;
                            try {
                                impl.setDate((Date) value);
                            } finally {
                                updatingInstance = false;
                            }
                        }
                    }
                }
        );

        impl.addPropertyChangeListener(
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        if ("date".equals(evt.getPropertyName())) {
                            updateInstance();
                        }
                    }
                }
        );

        setRequired(metaProperty.isMandatory());
    }

    @Override
    public boolean isEditable() {
        return impl.isEditable();
    }

    @Override
    public void setEditable(boolean editable) {
        impl.setEditable(editable);
    }

    @Override
    public String getCaption() {
        return null;
    }

    @Override
    public void setCaption(String caption) {
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public void setDescription(String description) {
    }

    private void updateInstance() {
        if (updatingInstance || datasource == null || metaPropertyPath == null)
            return;

        updatingInstance = true;
        try {
            Date value = impl.getDate();
            InstanceUtils.setValueEx(datasource.getItem(), metaPropertyPath.getPath(), value);
        } finally {
            updatingInstance = false;
        }
    }
}
