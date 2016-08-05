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

import com.google.common.base.Strings;
import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.components.Formatter;
import com.haulmont.cuba.gui.components.TextInputField;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.web.toolkit.ui.converters.StringToDatatypeConverter;
import com.haulmont.cuba.web.toolkit.ui.converters.StringToEntityConverter;
import com.haulmont.cuba.web.toolkit.ui.converters.StringToEnumConverter;
import com.vaadin.ui.AbstractTextField;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.text.ParseException;
import java.util.Locale;

/**
 * @param <T>
 */
public abstract class WebAbstractTextField<T extends AbstractTextField>
        extends
            WebAbstractField<T>
        implements
            TextInputField {

    private static Logger log = LoggerFactory.getLogger(WebAbstractTextField.class);

    protected Locale locale = AppBeans.<UserSessionSource>get(UserSessionSource.NAME).getLocale();

    public WebAbstractTextField() {
        this.component = createTextFieldImpl();
        this.component.setValidationVisible(false);
        this.component.setShowBufferedSourceException(false);

        component.setConverter(new TextFieldStringToDatatypeConverter(Datatypes.getNN(String.class)));

        attachListener(component);
        component.setImmediate(true);
        component.setNullRepresentation("");
        component.setInvalidAllowed(false);
        component.setInvalidCommitted(true);

        // todo artamonov remove unnecessary validator #PL-4506
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
    public <V> V getValue() {
        String value = super.getValue();
        if (isTrimming()) {
            value = StringUtils.trim(value);
        }
        value = Strings.emptyToNull(value);

        Datatype datatype = getActualDatatype();
        if (value != null && datatype != null) {
            try {
                return (V) datatype.parse(value, locale);
            } catch (ParseException e) {
                log.debug("Unable to parse value of component " + getId() + "\n" + e.getMessage());
                return null;
            }
        } else {
            return (V) value;
        }
    }

    @Override
    public void setValue(Object value) {
        if (!(value instanceof String)) {
            String formattedValue;

            Datatype<String> stringDatatype = Datatypes.getNN(String.class);
            Datatype datatype = getActualDatatype();

            if (datatype != null && stringDatatype != datatype) {
                formattedValue = datatype.format(value, locale);
            } else {
                MetadataTools metadataTools = AppBeans.get(MetadataTools.NAME);
                formattedValue = metadataTools.format(value);
            }

            super.setValue(formattedValue);
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

        if (metaProperty != null) {
            Integer maxLength = (Integer) metaProperty.getAnnotations().get("length");
            if (maxLength != null && this instanceof TextInputField.MaxLengthLimited) {
                ((TextInputField.MaxLengthLimited) this).setMaxLength(maxLength);
            }
        }
    }

    protected Formatter getFormatter() {
        return null;
    }

    protected boolean isTrimming() {
        return false;
    }

    @Override
    protected void initFieldConverter() {
        if (metaProperty != null) {
            switch (metaProperty.getType()) {
                case ASSOCIATION:
                    component.setConverter(new StringToEntityConverter() {
                        @Override
                        public Formatter getFormatter() {
                            return WebAbstractTextField.this.getFormatter();
                        }
                    });
                    break;

                case DATATYPE:
                    component.setConverter(new TextFieldStringToDatatypeConverter(metaProperty.getRange().asDatatype()));
                    break;

                case ENUM:
                    //noinspection unchecked
                    component.setConverter(new StringToEnumConverter((Class<Enum>) metaProperty.getJavaType()){
                        @Override
                        public Formatter getFormatter() {
                            return WebAbstractTextField.this.getFormatter();
                        }

                        @Override
                        public boolean isTrimming() {
                            return WebAbstractTextField.this.isTrimming();
                        }
                    });
                    break;

                default:
                    component.setConverter(new TextFieldStringToDatatypeConverter(Datatypes.getNN(String.class)));
                    break;
            }
        } else {
            component.setConverter(new TextFieldStringToDatatypeConverter(Datatypes.getNN(String.class)));
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

    protected class TextFieldStringToDatatypeConverter extends StringToDatatypeConverter {
        public TextFieldStringToDatatypeConverter(Datatype datatype) {
            super(datatype);
        }

        @Override
        public Formatter getFormatter() {
            return WebAbstractTextField.this.getFormatter();
        }

        @Override
        public boolean isTrimming() {
            return WebAbstractTextField.this.isTrimming();
        }
    }
}