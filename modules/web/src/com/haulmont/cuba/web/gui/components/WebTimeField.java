/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 09.12.2010 17:11:28
 *
 * $Id$
 */
package com.haulmont.cuba.web.gui.components;

import com.google.common.base.Preconditions;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.TimeField;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.web.gui.data.AbstractPropertyWrapper;
import com.haulmont.cuba.web.gui.data.DsManager;
import com.haulmont.cuba.web.gui.data.ItemWrapper;
import com.haulmont.cuba.web.gui.data.PropertyWrapper;
import com.haulmont.cuba.web.toolkit.ui.MaskedTextField;
import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.data.util.PropertyFormatter;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

public class WebTimeField extends WebAbstractField<MaskedTextField> implements TimeField, Component.Wrapper
{
    private boolean showSeconds;

    private String mask = "##:##";
    private String placeholder = "__:__";

    private String timeFormat = "HH:mm";

    private Log log = LogFactory.getLog(WebTimeField.class);

    public WebTimeField() {
        component = new MaskedTextField();
        component.setImmediate(true);
        component.setMask(mask);
        component.setNullRepresentation(placeholder);
        component.setInvalidAllowed(false);
        component.setInvalidCommitted(true);
        attachListener(component);

        final Property p = new AbstractPropertyWrapper() {
            public Class<?> getType() {
                return Date.class;
            }
        };

        component.setPropertyDataSource(
                new PropertyFormatter(p) {

                    @Override
                    public String format(Object value) {
                        if (value != null) {
                            SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
                            return sdf.format(value);
                        } else {
                            return null;
                        }
                    }

                    @Override
                    public Object parse(String formattedValue) throws Exception {
                        if (StringUtils.isNotEmpty(formattedValue) && !formattedValue.equals(placeholder)) {
                            try {
                                if (!checkStringValue(formattedValue)) {
                                    component.setComponentError(new com.vaadin.data.Validator.InvalidValueException("Invalid value"));
                                    return null;
                                }

                                SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
                                Date date = sdf.parse(formattedValue);
                                if (component.getComponentError() != null)
                                    component.setComponentError(null);
                                return date;
                            } catch (Exception e) {
                                log.warn("Unable to parse value of component " + getId() + "\n" + e.getMessage());
                                component.setComponentError(new com.vaadin.data.Validator.InvalidValueException("Invalid value"));
                                return null;
                            }
                        } else
                            return null;
                    }
                }
        );
    }

    private boolean checkStringValue(String value) throws NumberFormatException {
        String[] parts = value.split(":");
        int hours = Integer.parseInt(parts[0]);
        int mins = Integer.parseInt(parts[1]);

        if (hours > 23 || mins > 59)
            return false;
        else {
            if (parts.length > 2) {
                int secs = Integer.parseInt(parts[2]);
                if (secs > 59)
                    return false;
            }
        }
        return true;
    }

    @Override
    public <T> T getValue() {
        Object value = super.getValue();
        if (value instanceof String) {
            try {
                return (T) new SimpleDateFormat(timeFormat).parse((String) value);
            } catch (ParseException e) {
                log.warn("Unable to parse value of component " + getId() + "\n" + e.getMessage());
                return null;
            }
        } else {
            return (T) value;
        }
    }

    @Override
    public void setValue(Object value) {
        Preconditions.checkArgument(value == null || value instanceof Date, "Value must be an instance of Date");
        if (datasource == null && value != null) {
            String s = new SimpleDateFormat(timeFormat).format(value);
            super.setValue(s);
        } else
            super.setValue(value);
    }

    public boolean getShowSeconds() {
        return showSeconds;
    }

    public void setShowSeconds(boolean showSeconds) {
        this.showSeconds = showSeconds;
        mask = showSeconds ? "##:##:##" : "##:##";
        placeholder = showSeconds ? "__:__:__" : "__:__";
        timeFormat = showSeconds ? "HH:mm:ss" : "HH:mm";
        component.setMask(mask);
        component.setNullRepresentation(placeholder);
    }

    @Override
    protected ItemWrapper createDatasourceWrapper(Datasource datasource, Collection<MetaPropertyPath> propertyPaths, DsManager dsManager) {
        return new ItemWrapper(datasource, propertyPaths, dsManager) {
            private static final long serialVersionUID = 1729450322469573679L;

            @Override
            protected PropertyWrapper createPropertyWrapper(Object item, MetaPropertyPath propertyPath, DsManager dsManager) {
                return new PropertyWrapper(item, propertyPath, dsManager) {
                    private static final long serialVersionUID = -4481934193197224070L;

                    @Override
                    public String toString() {
                        Object value = getValue();
                        if (value instanceof Date) {
                            return new SimpleDateFormat(timeFormat).format(value);
                        } else
                            return super.toString();
                    }

                    @Override
                    protected Object valueOf(Object newValue) throws ConversionException {
                        if (newValue instanceof String) {
                            if (StringUtils.isNotEmpty((String) newValue) && !newValue.equals(placeholder)) {
                                try {
                                    if (!checkStringValue((String) newValue)) {
                                        component.setComponentError(new com.vaadin.data.Validator.InvalidValueException("Invalid value"));
                                        return null;
                                    }
                                    SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
                                    Date date = sdf.parse((String) newValue);
                                    if (component.getComponentError() != null)
                                        component.setComponentError(null);
                                    return date;
                                } catch (Exception e) {
                                    log.warn("Unable to parse value of component " + getId() + "\n" + e.getMessage());
                                    component.setComponentError(new com.vaadin.data.Validator.InvalidValueException("Invalid value"));
                                    return null;
                                }
                            } else
                                return null;
                        } else
                            return newValue;
                    }
                };
            }
        };
    }
}
