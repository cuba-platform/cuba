/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components;

import com.google.common.base.Preconditions;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.components.DateField;
import com.haulmont.cuba.gui.components.TimeField;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.gui.data.ItemWrapper;
import com.haulmont.cuba.web.gui.data.PropertyWrapper;
import com.haulmont.cuba.web.toolkit.ui.CubaMaskedTextField;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.server.UserError;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

/**
 * @author krivopustov
 * @version $Id$
 */
public class WebTimeField extends WebAbstractField<CubaMaskedTextField> implements TimeField {

    protected boolean showSeconds;

    protected String placeholder;
    protected String timeFormat;

    protected DateField.Resolution resolution;

    protected Log log = LogFactory.getLog(WebTimeField.class);

    public WebTimeField() {
        UserSessionSource uss = AppBeans.get(UserSessionSource.NAME);

        timeFormat = Datatypes.getFormatStringsNN(uss.getLocale()).getTimeFormat();
        resolution = DateField.Resolution.MIN;

        component = new CubaMaskedTextField();
        component.setMaskedMode(true);
        component.setImmediate(true);
        setShowSeconds(timeFormat.contains("ss"));

        component.setInvalidAllowed(false);
        component.setInvalidCommitted(true);
        component.addValidator(new com.vaadin.data.Validator() {
            @Override
            public void validate(Object value) throws InvalidValueException {
                if (!(!(value instanceof String) || checkStringValue((String) value))) {
                    component.markAsDirty();
                    throw new InvalidValueException("Unable to parse value: " + value);
                }
            }
        });
        attachListener(component);

        component.setConverter(new Converter<String, Date>() {
            @Override
            public Date convertToModel(String formattedValue, Class<? extends Date> targetType, Locale locale)
                    throws ConversionException {
                if (StringUtils.isNotEmpty(formattedValue) && !formattedValue.equals(placeholder)) {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
                        sdf.setLenient(false);

                        Date date = sdf.parse(formattedValue);
                        if (component.getComponentError() != null)
                            component.setComponentError(null);
                        if (targetType == java.sql.Time.class) {
                            return new Time(date.getTime());
                        }
                        if (targetType == java.sql.Date.class) {
                            log.warn("Do not use java.sql.Date with time field");
                            return new java.sql.Date(date.getTime());
                        }
                        return date;
                    } catch (Exception e) {
                        log.debug("Unable to parse value of component " + getId() + "\n" + e.getMessage());
                        throw new ConversionException("Invalid value");
                    }
                } else
                    return null;
            }

            @Override
            public String convertToPresentation(Date value, Class<? extends String> targetType, Locale locale)
                    throws ConversionException {
                if (value != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
                    return sdf.format(value);
                } else
                    return null;
            }

            @Override
            public Class<Date> getModelType() {
                return Date.class;
            }

            @Override
            public Class<String> getPresentationType() {
                return String.class;
            }
        });
    }

    public boolean isAmPmUsed() {
        return timeFormat.contains("a");
    }

    protected void updateWidth() {
        if (!App.isBound()) {
            return;
        }

        App app = App.getInstance();

        ThemeConstants theme = app.getThemeConstants();
        int digitWidth = theme.getInt("cuba.web.WebTimeField.digitWidth");
        int digitPadding = theme.getInt("cuba.web.WebTimeField.digitPadding");
        int separatorWidth = theme.getInt("cuba.web.WebTimeField.separatorWidth");

        int partsCount = isAmPmUsed() ? 1 : 0;
        int newWidth = isAmPmUsed() ? digitWidth + digitPadding: digitPadding;
        if (showSeconds) {
            newWidth = newWidth + digitWidth;
            partsCount += 1;
        }
        switch (resolution) {
            case HOUR:
                partsCount += 1;
                newWidth = digitWidth + newWidth;
                break;
            case MIN:
            case SEC:
                partsCount += 2;
                newWidth = digitWidth * 2 + newWidth;
                break;
        }

        newWidth += (partsCount - 1) * separatorWidth;

        component.setWidth(newWidth + "px");
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
                log.debug("Unable to parse value of component " + getId() + "\n" + e.getMessage());
                return null;
            }
        } else {
            return (T) value;
        }
    }

    @Override
    public void setValue(Object value) {
        Preconditions.checkArgument(value == null || value instanceof Date, "Value must be an instance of Date");
        if (value != null) {
            SimpleDateFormat format = new SimpleDateFormat(this.timeFormat);
            format.setLenient(false);

            super.setValue(format.format(value));
        } else {
            super.setValue(null);
        }
    }

    @Override
    public boolean getShowSeconds() {
        return showSeconds;
    }

    @Override
    public void setFormat(String format) {
        timeFormat = format;
        updateTimeFormat();
    }

    @Override
    public String getFormat() {
        return timeFormat;
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

    @Override
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

    protected void updateTimeFormat() {
        String mask = StringUtils.replaceChars(timeFormat, "Hhmsa", "####U");
        placeholder = StringUtils.replaceChars(mask, "#U", "__");
        component.setMask(mask);
        component.setNullRepresentation(placeholder);
    }

    @Override
    protected ItemWrapper createDatasourceWrapper(Datasource datasource, Collection<MetaPropertyPath> propertyPaths) {
        return new ItemWrapper(datasource, datasource.getMetaClass(), propertyPaths) {
            private static final long serialVersionUID = 1729450322469573679L;

            @Override
            protected PropertyWrapper createPropertyWrapper(Object item, MetaPropertyPath propertyPath) {
                return new PropertyWrapper(item, propertyPath) {
                    private static final long serialVersionUID = -4481934193197224070L;

                    @Override
                    public String getFormattedValue() {
                        Object value = getValue();
                        if (value instanceof Date)
                            return new SimpleDateFormat(timeFormat).format(value);

                        return super.getFormattedValue();
                    }

                    @Override
                    protected Object valueOf(Object newValue) throws Converter.ConversionException {
                        if (newValue instanceof String) {
                            if (StringUtils.isNotEmpty((String) newValue) && !newValue.equals(placeholder)) {
                                try {
                                    SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
                                    Date date = sdf.parse((String) newValue);
                                    if (component.getComponentError() != null)
                                        component.setComponentError(null);
                                    return date;
                                } catch (Exception e) {
                                    log.debug("Unable to parse value of component " + getId() + "\n" + e.getMessage());
                                    component.setComponentError(new UserError("Invalid value"));
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