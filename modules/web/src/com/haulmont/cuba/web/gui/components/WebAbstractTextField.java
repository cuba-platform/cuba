/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.TextInputField;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.web.gui.data.ItemWrapper;
import com.haulmont.cuba.web.gui.data.PropertyWrapper;
import com.haulmont.cuba.web.toolkit.ui.converters.StringToDatatypeConverter;
import com.haulmont.cuba.web.toolkit.ui.converters.StringToEntityConverter;
import com.haulmont.cuba.web.toolkit.ui.converters.StringToEnumConverter;
import com.haulmont.cuba.web.toolkit.ui.converters.StringToStringConverter;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.ui.AbstractTextField;
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
public abstract class WebAbstractTextField<T extends AbstractTextField>
        extends
            WebAbstractField<T>
        implements
            TextInputField, Component.Wrapper {

    private static Log log = LogFactory.getLog(WebAbstractTextField.class);

    protected Locale locale = AppBeans.get(UserSessionSource.class).getLocale();

    public WebAbstractTextField() {
        this.component = createTextFieldImpl();
        this.component.setValidationVisible(false);

        component.setConverter(new StringToDatatypeConverter(Datatypes.getNN(String.class)));

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

    @Override
    public <T> T getValue() {
        Object value = super.getValue();
        Datatype datatype = getActualDatatype();
        if (value instanceof String && datatype != null) {
            try {
                return (T) datatype.parse((String) value, locale);
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
            return Datatypes.getNN(String.class);
        }
    }

    @Override
    public void setDatasource(Datasource datasource, String property) {
        super.setDatasource(datasource, property);

        Integer len = (Integer) metaProperty.getAnnotations().get("length");
        if (len != null) {
            component.setMaxLength(len);
        }
    }

    @Override
    protected void initFieldConverter() {
        if (metaProperty != null) {
            switch (metaProperty.getType()) {
                case ASSOCIATION:
                    component.setConverter(new StringToEntityConverter());
                    break;

                case DATATYPE:
                    Datatype<?> datatype = Datatypes.get(metaProperty.getJavaType());
                    if (datatype != null) {
                        component.setConverter(new StringToDatatypeConverter(datatype));
                    } else {
                        component.setConverter(new StringToDatatypeConverter(Datatypes.getNN(String.class)));
                    }
                    break;

                case ENUM:
                    //noinspection unchecked
                    component.setConverter(new StringToEnumConverter((Class<Enum>) metaProperty.getJavaType()));
                    break;

                default:
                    component.setConverter(new StringToStringConverter());
                    break;
            }
        } else {
            component.setConverter(new StringToDatatypeConverter(Datatypes.getNN(String.class)));
        }
    }

    @Override
    protected ItemWrapper createDatasourceWrapper(Datasource datasource, Collection<MetaPropertyPath> propertyPaths) {
        if (this instanceof TrimSupported) {
            return new TextItemWrapper(datasource, propertyPaths);
        } else {
            return super.createDatasourceWrapper(datasource, propertyPaths);
        }
    }

    protected class TextItemWrapper extends ItemWrapper {
        public TextItemWrapper(Object item, Collection<MetaPropertyPath> properties) {
            super(item, properties);
        }

        @Override
        protected PropertyWrapper createPropertyWrapper(Object item, MetaPropertyPath propertyPath) {
            return new TextPropertyWrapper(item, propertyPath);
        }
    }

    protected class TextPropertyWrapper extends PropertyWrapper {
        public TextPropertyWrapper(Object item, MetaPropertyPath propertyPath) {
            super(item, propertyPath);
        }

        @Override
        public void setValue(Object newValue) throws ReadOnlyException, Converter.ConversionException {
            WebAbstractTextField<T> impl = WebAbstractTextField.this;
            if ((newValue instanceof String) && (impl instanceof TrimSupported)) {
                if (((TrimSupported) impl).isTrimming()) {
                    newValue = ((String) newValue).trim();
                }
            }
            super.setValue(newValue);
        }
    }

    @Override
    protected boolean isEmpty(Object value) {
        if (value instanceof String) {
            return StringUtils.isBlank((String) value);
        } else {
            return value == null;
        }
    }
}