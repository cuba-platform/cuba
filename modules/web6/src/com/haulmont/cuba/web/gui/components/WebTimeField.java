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
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.global.UserSessionProvider;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.DateField;
import com.haulmont.cuba.gui.components.TimeField;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.web.gui.data.AbstractPropertyWrapper;
import com.haulmont.cuba.web.gui.data.ItemWrapper;
import com.haulmont.cuba.web.gui.data.PropertyWrapper;
import com.haulmont.cuba.web.toolkit.ui.MaskedTextField;
import com.vaadin.data.Property;
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

    private String mask;
    private String placeholder;
    private String timeFormat;

    private DateField.Resolution resolution;

    private Log log = LogFactory.getLog(WebTimeField.class);

    protected static final int DIGIT_WIDTH = 23;
    
    public WebTimeField() {
        timeFormat = Datatypes.getFormatStrings(UserSessionProvider.getLocale()).getTimeFormat();
        resolution = DateField.Resolution.MIN;
        component = new MaskedTextField();
        component.setImmediate(true);
        component.setMaskedMode(true);
        setShowSeconds(timeFormat.contains("ss"));
        component.setInvalidAllowed(false);
        component.setInvalidCommitted(true);
        component.addValidator(new com.vaadin.data.Validator() {
            @Override
            public void validate(Object value) throws InvalidValueException {
                if (!isValid(value)) {
                    component.requestRepaint();
                    throw new InvalidValueException("Unable to parse value: " + value);
                }
            }

            @Override
            public boolean isValid(Object value) {

                return (!(value instanceof String) || checkStringValue((String) value));
            }
        });
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

    public boolean isAmPmUsed() {
        return timeFormat.contains("a");
    }

    private void updateWidth() {
        int width = isAmPmUsed() ? DIGIT_WIDTH : 0;
        if (showSeconds) {
            width = width + DIGIT_WIDTH;
        }
        switch (resolution) {
            case HOUR:
                component.setWidth((DIGIT_WIDTH + width) + "px");
                break;
            case MIN:
            case SEC:
                component.setWidth((DIGIT_WIDTH * 2 + width) + "px");

        }
    }

    private boolean checkStringValue(String value) {
        if (value.equals(placeholder) || StringUtils.isEmpty(value))
            return true;
        SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
        sdf.setLenient(false);
        try {
            sdf.parse(value);
            return true;
        } catch (ParseException e) {
            return false;
        }
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

    public void setFormat(String format) {
        timeFormat = format;
        updateTimeFormat();
    }

    public void setResolution(DateField.Resolution resolution) {
        this.resolution = resolution;
        if (resolution.ordinal() <= DateField.Resolution.SEC.ordinal()) {
            setShowSeconds(true);
        } else if (resolution.ordinal() <= DateField.Resolution.MIN.ordinal()) {
            setShowSeconds(false);
        } else if (resolution.ordinal() <= DateField.Resolution.HOUR.ordinal()) {
            StringBuilder builder = new StringBuilder(timeFormat);
            int minutesIndex = builder.indexOf(":mm");
            builder.delete(minutesIndex, minutesIndex + 3);
            timeFormat = builder.toString();
            setShowSeconds(false);
        }
    }

    public void setShowSeconds(boolean showSeconds) {
        this.showSeconds = showSeconds;
        if (showSeconds) {
            if (!timeFormat.contains(":ss")) {
                int minutesIndex = timeFormat.indexOf("mm");
                StringBuilder builder = new StringBuilder(timeFormat);
                builder.insert(minutesIndex + 2, ":ss");
                timeFormat = builder.toString();
            }
        } else {
            if (timeFormat.contains(":ss")) {
                int secondsIndex = timeFormat.indexOf(":ss");
                StringBuilder builder = new StringBuilder(timeFormat);
                builder.delete(secondsIndex, secondsIndex + 3);
                timeFormat = builder.toString();
            }
        }
        updateTimeFormat();
        updateWidth();
    }

    private void updateTimeFormat() {
        mask = StringUtils.replaceChars(timeFormat, "Hhmsa", "####U");
        placeholder = StringUtils.replaceChars(mask, "#U", "__");
        component.setMask(mask);
        component.setNullRepresentation(placeholder);
    }

    @Override
    protected ItemWrapper createDatasourceWrapper(Datasource datasource, Collection<MetaPropertyPath> propertyPaths) {
        return new ItemWrapper(datasource, propertyPaths) {
            private static final long serialVersionUID = 1729450322469573679L;

            @Override
            protected PropertyWrapper createPropertyWrapper(Object item, MetaPropertyPath propertyPath) {
                return new PropertyWrapper(item, propertyPath) {
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
