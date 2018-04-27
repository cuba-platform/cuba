/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.components.DateField;
import com.haulmont.cuba.gui.components.TimeField;
import com.haulmont.cuba.gui.components.data.ConversionException;
import com.haulmont.cuba.gui.components.data.EntityValueSource;
import com.haulmont.cuba.gui.components.data.ValueSource;
import com.haulmont.cuba.gui.components.data.value.DatasourceValueSource;
import com.haulmont.cuba.gui.theme.ThemeConstants;
import com.haulmont.cuba.web.App;
import com.haulmont.cuba.web.widgets.CubaMaskedTextField;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WebTimeField extends WebV8AbstractField<CubaMaskedTextField, String, Date>
        implements TimeField, InitializingBean {

    protected boolean showSeconds;

    protected String placeholder;
    protected String timeFormat;
    protected DateField.Resolution resolution;

    public WebTimeField() {
        resolution = DateField.Resolution.MIN;

        component = new CubaMaskedTextField();
        component.setMaskedMode(true);
        component.setTimeMask(true);

        attachValueChangeListener(component);

        // vaadin8
//        component.setInvalidAllowed(false);
//        component.setInvalidCommitted(true);
        /*component.addValidator(value -> {
            if (!(!(value instanceof String) || checkStringValue((String) value))) {
                component.markAsDirty();
                throw new com.vaadin.v7.data.Validator.InvalidValueException("Unable to parse value: " + value);
            }
        });*/
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        UserSessionSource userSessionSource = applicationContext.getBean(UserSessionSource.class);
        timeFormat = Datatypes.getFormatStringsNN(userSessionSource.getLocale()).getTimeFormat();
        setShowSeconds(timeFormat.contains("ss"));

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
        int newWidth = isAmPmUsed() ? digitWidth + digitPadding : digitPadding;
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

    // VAADIN8: gg, do we need this method?
    protected boolean checkStringValue(String value) {
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
    protected Date convertToModel(String componentRawValue) throws ConversionException {
        if (StringUtils.isNotEmpty(componentRawValue) && !componentRawValue.equals(placeholder)) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
                sdf.setLenient(false);

                Date date = sdf.parse(componentRawValue);
                if (component.getComponentError() != null) {
                    component.setComponentError(null);
                }

                ValueSource<Date> valueSource = getValueSource();
                if (valueSource instanceof EntityValueSource) {
                    MetaPropertyPath metaPropertyPath = ((DatasourceValueSource) valueSource).getMetaPropertyPath();
                    MetaProperty metaProperty = metaPropertyPath.getMetaProperty();
                    if (metaProperty != null) {
                        Class javaClass = metaProperty.getRange().asDatatype().getJavaClass();
                        if (javaClass.equals(java.sql.Time.class)) {
                            return new Time(date.getTime());
                        }
                        if (javaClass.equals(java.sql.Date.class)) {
                            LoggerFactory.getLogger(WebTimeField.class).warn("Do not use java.sql.Date with time field");
                            return new java.sql.Date(date.getTime());
                        }
                    }
                }
                return date;
            } catch (Exception e) {
                LoggerFactory.getLogger(WebTimeField.class)
                        .debug("Unable to parse value of component {}:\n{}", getId(), e.getMessage());
                throw new ConversionException("Invalid value");
            }
        } else {
            return null;
        }
    }

    @Override
    protected String convertToPresentation(Date modelValue) throws ConversionException {
        if (modelValue != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
            return sdf.format(modelValue);
        } else {
            return "";
        }
    }

    @Override
    public boolean getShowSeconds() {
        return showSeconds;
    }

    @Override
    public void setFormat(String format) {
        timeFormat = format;
        showSeconds = timeFormat.contains("ss");
        updateTimeFormat();
        updateWidth();
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
            if (timeFormat.contains("mm")) {
                int minutesIndex = builder.indexOf("mm");
                builder.delete(minutesIndex > 0 ? --minutesIndex : minutesIndex, minutesIndex + 3);
                timeFormat = builder.toString();
            }
            setShowSeconds(false);
        }
    }

    @Override
    public void setShowSeconds(boolean showSeconds) {
        this.showSeconds = showSeconds;
        if (showSeconds) {
            if (!timeFormat.contains("ss")) {
                int minutesIndex = timeFormat.indexOf("mm");
                StringBuilder builder = new StringBuilder(timeFormat);
                builder.insert(minutesIndex + 2, ":ss");
                timeFormat = builder.toString();
            }
        } else {
            if (timeFormat.contains("ss")) {
                int secondsIndex = timeFormat.indexOf("ss");
                StringBuilder builder = new StringBuilder(timeFormat);
                builder.delete(secondsIndex > 0 ? --secondsIndex : secondsIndex, secondsIndex + 3);
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
//        vaadin8
//        component.setNullRepresentation(placeholder);
    }

//        vaadin8
/* todo
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
                        Object value = this.getValue();
                        if (value instanceof Date) {
                            SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
                            return sdf.format(value);
                        }

                        return super.getFormattedValue();
                    }

                    @Override
                    protected Object valueOf(Object newValue) throws Converter.ConversionException {
                        if (newValue instanceof String) {
                            if (StringUtils.isNotEmpty((String) newValue) && !newValue.equals(placeholder)) {
                                try {
                                    SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
                                    Date date = sdf.parse((String) newValue);
                                    if (component.getComponentError() != null) {
                                        component.setComponentError(null);
                                    }
                                    return date;
                                } catch (Exception e) {
                                    LoggerFactory.getLogger(WebTimeField.class).debug("Unable to parse value of component " + getId() + "\n" + e.getMessage());
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
    }*/

    @Override
    public int getTabIndex() {
        return component.getTabIndex();
    }

    @Override
    public void setTabIndex(int tabIndex) {
        component.setTabIndex(tabIndex);
    }

    @Override
    public void commit() {
        super.commit();
    }

    @Override
    public void discard() {
        super.discard();
    }

    @Override
    public boolean isBuffered() {
        return super.isBuffered();
    }

    @Override
    public void setBuffered(boolean buffered) {
        super.setBuffered(buffered);
    }

    @Override
    public boolean isModified() {
        return super.isModified();
    }
}