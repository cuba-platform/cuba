/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components;

import com.google.common.base.Strings;
import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.model.Instance;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.components.Formatter;
import com.haulmont.cuba.gui.components.TextInputField;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.web.gui.data.AbstractPropertyWrapper;
import com.haulmont.cuba.web.gui.data.ItemWrapper;
import com.haulmont.cuba.web.gui.data.PropertyWrapper;
import com.vaadin.data.Property;
import com.vaadin.data.util.PropertyFormatter;
import com.vaadin.ui.AbstractTextField;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.Nullable;
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
        TextInputField {

    protected static Log log = LogFactory.getLog(WebAbstractTextField.class);

    protected Locale locale = AppBeans.get(UserSessionSource.class).getLocale();

    protected Formatter formatter;

    protected boolean trimming = true;

    public WebAbstractTextField() {
        this.component = createTextFieldImpl();
        this.component.setValidationVisible(false);

        final Property p = new AbstractPropertyWrapper() {
            public Class<?> getType() {
                return String.class;
            }
        };

        component.setPropertyDataSource(new PropertyFormatter(p) {
            @Override
            public String format(Object value) {
                Datatype datatype = getActualDatatype();
                if (datatype != null) {
                    return datatype.format(value, locale);
                } else if (value != null) {
                    return value.toString();
                } else {
                    return null;
                }
            }

            @Override
            public Object parse(String formattedValue) throws Exception {
                Datatype datatype = getActualDatatype();
                if (datatype != null) {
                    try {
                        return datatype.parse(formattedValue,locale);
                    } catch (ParseException e) {
                        log.warn("Unable to parse value of component " + getId() + "\n" + e.getMessage());
                        return null;
                    }
                } else {
                    return formattedValue;
                }
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
                            component.requestRepaint();
                            throw new InvalidValueException("Unable to parse value: " + value);
                        }
                    }

                    @Override
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
            value = Strings.emptyToNull((String) value);
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
        if (!isEditable())
            return;

        Datatype datatype = getActualDatatype();
        if (!(value instanceof String) && datatype != null) {
            String str = datatype.format(value, locale);
            super.setValue(str);
        } else {
            super.setValue(value);
        }
    }

    @Nullable
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

        Integer maxLength = (Integer) metaProperty.getAnnotations().get("length");
        if (maxLength != null && this instanceof TextInputField.MaxLengthLimited) {
            ((TextInputField.MaxLengthLimited)this).setMaxLength(maxLength);
        }
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
                    public void setValue(Object newValue) throws ReadOnlyException, ConversionException {
                        if (newValue instanceof String && trimming)
                            newValue = ((String) newValue).trim();
                        if (newValue instanceof String) {
                            newValue = Strings.emptyToNull((String) newValue);
                        }

                        super.setValue(newValue);
                    }

                    @Override
                    public String toString() {
                        if (formatter != null) {
                            Object value = getValue();
                            if (value instanceof Instance)
                                value = ((Instance) value).getInstanceName();
                            return formatter.format(value);
                        } else {
                            return super.toString();
                        }
                    }
                };
            }
        };
    }

    @Override
    protected boolean isEmpty(Object value) {
        if (value instanceof String)
            return StringUtils.isBlank((String) value);
        else
            return value == null;
    }
}