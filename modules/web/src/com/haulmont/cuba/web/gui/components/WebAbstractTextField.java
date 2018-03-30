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
import com.haulmont.cuba.core.entity.annotation.ConversionType;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.components.Formatter;
import com.haulmont.cuba.gui.components.TextInputField;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.web.gui.components.converters.StringToDatatypeConverter;
import com.haulmont.cuba.web.gui.components.converters.StringToEntityConverter;
import com.haulmont.cuba.web.gui.components.converters.StringToEnumConverter;
import com.vaadin.v7.ui.AbstractTextField;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.validation.constraints.Size;
import java.text.ParseException;
import java.util.Locale;
import java.util.Map;

public abstract class WebAbstractTextField<T extends AbstractTextField, V>
        extends
            WebAbstractField<T, V>
        implements
            TextInputField<V> {

    protected Locale locale = AppBeans.<UserSessionSource>get(UserSessionSource.NAME).getLocale();

    public WebAbstractTextField() {
        this.component = createTextFieldImpl();
        this.component.setValidationVisible(false);
        this.component.setShowBufferedSourceException(false);

        component.setConverter(new TextFieldStringToDatatypeConverter(Datatypes.getNN(String.class)));

        attachListener(component);
        component.setNullRepresentation("");
        component.setInvalidAllowed(false);
        component.setInvalidCommitted(true);
    }

    protected abstract T createTextFieldImpl();

    @Override
    public V getValue() {
        String value = component.getValue();

        if (isTrimming()) {
            value = StringUtils.trim(value);
        }
        value = Strings.emptyToNull(value);

        Datatype datatype = getActualDatatype();
        if (value != null && datatype != null) {
            try {
                return (V) datatype.parse(value, locale);
            } catch (ParseException e) {
                Logger log = LoggerFactory.getLogger(WebAbstractTextField.class);
                log.debug("Unable to parse value of component {}\n{}", getId(), e.getMessage());
                return null;
            }
        } else {
            return (V) value;
        }
    }

    @Override
    public void setValue(V value) {
        if (value instanceof String) {
            component.setValueIgnoreReadOnly((String) value);
        } else {
            String formattedValue;

            Datatype<String> stringDatatype = Datatypes.getNN(String.class);
            Datatype datatype = getActualDatatype();

            if (datatype != null && stringDatatype != datatype) {
                formattedValue = datatype.format(value, locale);
            } else {
                MetadataTools metadataTools = AppBeans.get(MetadataTools.NAME);
                formattedValue = metadataTools.format(value);
            }

            component.setValueIgnoreReadOnly(formattedValue);
        }
    }

    @Nullable
    protected Datatype getActualDatatype() {
        if (getMetaProperty() != null) {
            return getMetaProperty().getRange().isDatatype() ? getMetaProperty().getRange().asDatatype() : null;
        } else {
            return Datatypes.getNN(String.class);
        }
    }

    // vaadin8 rework to setValueSource()
    @Override
    public void setDatasource(Datasource datasource, String property) {
        super.setDatasource(datasource, property);

        if (getMetaProperty() != null) {
            Map<String, Object> annotations = getMetaProperty().getAnnotations();

            if (this instanceof CaseConversionSupported
                    && ((CaseConversionSupported) this).getCaseConversion() == CaseConversion.NONE) {
                String caseConversionAnnotation = com.haulmont.cuba.core.entity.annotation.CaseConversion.class.getName();
                //noinspection unchecked
                Map<String, Object> caseConversion = (Map<String, Object>) annotations.get(caseConversionAnnotation);
                if (MapUtils.isNotEmpty(caseConversion)) {
                    ConversionType conversionType = (ConversionType) caseConversion.get("type");
                    CaseConversion conversion = CaseConversion.valueOf(conversionType.name());

                    ((CaseConversionSupported) this).setCaseConversion(conversion);
                }
            }

            if (this instanceof TextInputField.MaxLengthLimited) {
                MaxLengthLimited maxLengthLimited = (MaxLengthLimited) this;

                Integer maxLength = (Integer) annotations.get("length");
                if (maxLength != null) {
                    maxLengthLimited.setMaxLength(maxLength);
                }

                Integer sizeMax = (Integer) annotations.get(Size.class.getName() + "_max");
                if (sizeMax != null) {
                    maxLengthLimited.setMaxLength(sizeMax);
                }

                Integer lengthMax = (Integer) annotations.get(Length.class.getName() + "_max");
                if (lengthMax != null) {
                    maxLengthLimited.setMaxLength(lengthMax);
                }
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
        if (getMetaProperty() != null) {
            switch (getMetaProperty().getType()) {
                case ASSOCIATION:
                    component.setConverter(new StringToEntityConverter() {
                        @Override
                        public Formatter getFormatter() {
                            return WebAbstractTextField.this.getFormatter();
                        }
                    });
                    break;

                case DATATYPE:
                    component.setConverter(new TextFieldStringToDatatypeConverter(getMetaProperty().getRange().asDatatype()));
                    break;

                case ENUM:
                    //noinspection unchecked
                    component.setConverter(new StringToEnumConverter((Class<Enum>) getMetaProperty().getJavaType()){
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