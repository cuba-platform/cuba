/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Formatter;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.web.gui.data.ItemWrapper;
import com.haulmont.cuba.web.gui.data.PropertyWrapper;
import com.haulmont.cuba.web.toolkit.ui.converters.EntityToStringConverter;
import com.haulmont.cuba.web.toolkit.ui.converters.StringToStringConverter;
import com.vaadin.data.util.converter.Converter;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.ParseException;
import java.util.Collection;
import java.util.Locale;

/**
 * @param <T>
 * @author abramov
 * @version $Id$
 */
public abstract class WebAbstractTextField<T extends com.haulmont.cuba.web.toolkit.ui.TextField>
    extends
        WebAbstractField<T>
    implements
        TextField, Component.Wrapper {

    private static Log log = LogFactory.getLog(WebAbstractTextField.class);

    private Datatype datatype;

    private Locale locale = AppBeans.get(UserSessionSource.class).getLocale();

    protected Formatter formatter;

    protected boolean trimming = true;

    public WebAbstractTextField() {
        this.component = createTextFieldImpl();
        this.component.setValidationVisible(false);

        component.setConverter(new Converter<String, Object>() {
            @Override
            public Object convertToModel(String value, Locale locale) throws ConversionException {
                if (datatype != null) {
                    try {
                        return datatype.parse(value, locale);
                    } catch (ParseException e) {
                        log.warn("Unable to parse value of component " + getId() + "\n" + e.getMessage());
                        return null;
                    }
                } else {
                    return value;
                }
            }

            @Override
            public String convertToPresentation(Object value, Locale locale) throws ConversionException {
                if (datatype != null && value != null) {
                    return datatype.format(value, locale);
                } else if (value != null) {
                    return value.toString();
                } else {
                    return null;
                }
            }

            @Override
            public Class<Object> getModelType() {
                return Object.class;
            }

            @Override
            public Class<String> getPresentationType() {
                return String.class;
            }
        });

        attachListener(component);
        component.setImmediate(true);
        component.setNullRepresentation("");
        component.setInvalidAllowed(false);
        component.setInvalidCommitted(true);
        component.addValidator(
                new com.vaadin.data.Validator() {
                    @Override
                    public void validate(Object value) throws InvalidValueException {
                        if (!isValid(value)) {
                            component.markAsDirty();
                            throw new InvalidValueException("Unable to parse value: " + value);
                        }
                    }

                    public boolean isValid(Object value) {
                        Datatype datatype = getActualDatatype();
                        if (value instanceof String && datatype != null) {
                            try {
                                datatype.parse((String) value, locale);
                            } catch (ParseException e) {
                                return false;
                            }
                        }
                        return true;
                    }
                }
        );
    }

    protected abstract T createTextFieldImpl();

    public int getRows() {
        return 0;
//        vaadin7
//        return component.getRows();
    }

    public void setRows(int rows) {
//        component.setRows(rows);
//        vaadin7
    }

    public int getColumns() {
        return component.getColumns();
    }

    public void setColumns(int columns) {
        component.setColumns(columns);
    }

    public boolean isSecret() {
//        vaadin7
//        return component.isSecret();
        return false;
    }

    public void setSecret(boolean secret) {
//        vaadin7
//        component.setSecret(secret);
    }

    @Override
    public int getMaxLength() {
        return component.getMaxLength();
    }

    @Override
    public void setMaxLength(int value) {
        component.setMaxLength(value);
    }

    @Override
    public Datatype getDatatype() {
        return datatype;
    }

    @Override
    public void setDatatype(Datatype datatype) {
        this.datatype = datatype;
    }

    @Override
    public <T> T getValue() {
        Object value = super.getValue();
        Datatype datatype = getActualDatatype();
        if (value instanceof String && datatype != null) {
            try {
                return (T) datatype.parse((String) value, locale);
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
        Datatype datatype = getActualDatatype();
        if (!(value instanceof String) && datatype != null) {
            String str = datatype.format(value, locale);
            super.setValue(str);
        } else {
            super.setValue(value);
        }
    }

    protected Datatype getActualDatatype() {
        if (metaProperty != null) {
            return metaProperty.getRange().isDatatype() ? metaProperty.getRange().asDatatype() : null;
        } else {
            return datatype;
        }
    }

    @Override
    public void setDatasource(Datasource datasource, String property) {
        super.setDatasource(datasource, property);
        Integer len = (Integer) metaProperty.getAnnotations().get("length");
        if (len != null) {
            component.setMaxLength(len);
        }

        if (metaProperty.getType() == MetaProperty.Type.ASSOCIATION)
            component.setConverter(new EntityToStringConverter());
        else
            component.setConverter(new StringToStringConverter());
    }

    @Override
    protected ItemWrapper createDatasourceWrapper(Datasource datasource, Collection<MetaPropertyPath> propertyPaths) {
        return new ItemWrapper(datasource, propertyPaths) {
            private static final long serialVersionUID = -5672549961402055473L;

            @Override
            protected PropertyWrapper createPropertyWrapper(Object item, MetaPropertyPath propertyPath) {
                return new PropertyWrapper(item, propertyPath) {
                    private static final long serialVersionUID = -6484626348078235396L;

                    @Override
                    public void setValue(Object newValue) throws ReadOnlyException, Converter.ConversionException {
                        if (newValue instanceof String && trimming)
                            newValue = ((String) newValue).trim();
                        super.setValue(newValue);
                    }

                    @Override
                    public String getFormattedValue() {
                        if (formatter != null) {
                            Object value = getValue();
                            if (value instanceof Instance)
                                value = ((Instance) value).getInstanceName();
                            return formatter.format(value);
                        } else
                            return super.getFormattedValue();
                    }
                };
            }
        };
    }

    @Override
    public Formatter getFormatter() {
        return formatter;
    }

    @Override
    public void setFormatter(Formatter formatter) {
        this.formatter = formatter;
    }

    @Override
    protected boolean isEmpty(Object value) {
        if (value instanceof String)
            return StringUtils.isBlank((String) value);
        else
            return value == null;
    }

    @Override
    public boolean isTrimming() {
        return trimming;
    }

    @Override
    public void setTrimming(boolean trimming) {
        this.trimming = trimming;
    }
}