/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.datatypes.impl.DateDatatype;
import com.haulmont.chile.core.datatypes.impl.TimeDatatype;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.Range;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.web.gui.components.*;
import com.haulmont.cuba.web.toolkit.ui.CheckBox;
import com.haulmont.cuba.web.toolkit.ui.DateFieldWrapper;
import com.vaadin.data.Item;
import com.vaadin.data.Validator;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Select;
import com.vaadin.ui.TextField;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import javax.persistence.TemporalType;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;

public abstract class AbstractFieldFactory extends DefaultFieldFactory {

    private Security security = AppBeans.get(Security.NAME);

    /**
     * Creates fields for the Form
     */
    @Override
    public com.vaadin.ui.Field createField(Item item, Object propertyId, com.vaadin.ui.Component uiContext) {
        if (item != null && propertyId != null) {
            final com.vaadin.ui.Field field;
            com.haulmont.cuba.gui.components.Field cubaField = null;
            MetaPropertyPath propertyPath = (MetaPropertyPath) propertyId;

            final Range range = propertyPath.getRange();
            Element xmlDescriptor = getXmlDescriptor(propertyPath);
            if (range != null) {
                if (range.isClass()) {
                    final CollectionDatasource optionsDatasource = getOptionsDatasource(range.asClass(), propertyPath);
                    if (optionsDatasource != null) {
                        final WebLookupField lookupField = new WebLookupField();
                        lookupField.setOptionsDatasource(optionsDatasource);

                        if (xmlDescriptor != null) {
                            if (!StringUtils.isEmpty(xmlDescriptor.attributeValue("captionProperty"))) {
                                lookupField.setCaptionProperty(xmlDescriptor.attributeValue("captionProperty"));
                                lookupField.setCaptionMode(CaptionMode.PROPERTY);
                            }
                            if (!StringUtils.isEmpty(xmlDescriptor.attributeValue("descriptionProperty"))) {
                                lookupField.setDescriptionProperty(xmlDescriptor.attributeValue("descriptionProperty"));
                            }
                        }

                        cubaField = lookupField;
                        field = (com.vaadin.ui.Field) WebComponentsHelper.unwrap(lookupField);
                    } else if ("textField".equals(fieldType(propertyPath))) {
                        field = new com.haulmont.cuba.web.toolkit.ui.TextField();
                    } else {
                        final WebPickerField pickerField = new WebPickerField();
                        pickerField.addLookupAction();
                        pickerField.addClearAction();
                        pickerField.setMetaClass(range.asClass());
                        cubaField = pickerField;
                        field = (com.vaadin.ui.Field) WebComponentsHelper.unwrap(pickerField);
                    }
                } else if (range.isEnum()) {
                    final WebLookupField lookupField = new WebLookupField();
//                    if (propertyPath.get().length > 1) throw new UnsupportedOperationException();

                    lookupField.setDatasource(getDatasource(item), propertyPath.toString());
                    lookupField.setOptionsList(range.asEnumeration().getValues());

                    cubaField = lookupField;
                    field = (com.vaadin.ui.Field) WebComponentsHelper.unwrap(lookupField);
                } else {
                    // datatype
                    Class<?> type = item.getItemProperty(propertyId).getType();
                    if (Boolean.class.isAssignableFrom(type)) {
                        field = new CheckBox();
                    } else if (Date.class.isAssignableFrom(type)) {
                        Datatype datatype = range.asDatatype();
                        String dataTypeName = datatype.getName();
                        if (TimeDatatype.NAME.equals(dataTypeName) || "timeField".equals(fieldType(propertyPath))) {
                            final WebTimeField timeField = new WebTimeField();
                            timeField.setDatasource(getDatasource(item), propertyPath.getMetaProperty().getName());
                            cubaField = timeField;
                            field = (com.vaadin.ui.Field) WebComponentsHelper.unwrap(timeField);
                        } else {
                            WebDateField dateField = new WebDateField();
                            dateField.setDatasource(getDatasource(item), propertyPath.getMetaProperty().getName());
                            cubaField = dateField;
                            field = dateField.getComponent();
                        }
                    } else if (String.class.isAssignableFrom(type) &&
                                xmlDescriptor != null &&
                                xmlDescriptor.attribute("mask") != null) {
                        cubaField = new WebMaskedField();
                        field = (com.vaadin.ui.Field) WebComponentsHelper.unwrap(cubaField);
                    } else {
                        field = super.createField(item, propertyId, uiContext);
                        field.setInvalidAllowed(false);
                        field.addValidator(
                                new com.vaadin.data.Validator() {
                                    @Override
                                    public void validate(Object value) throws InvalidValueException {
                                        if (!isValid(value)) {
                                            field.requestRepaint();
                                            throw new InvalidValueException("Unable to parse value: " + value);
                                        }
                                    }

                                    @Override
                                    public boolean isValid(Object value) {
                                        Datatype datatype = range.asDatatype();
                                        if (value instanceof String && datatype != null) {
                                            try {
                                                datatype.parse((String) value, UserSessionProvider.getLocale());
                                            } catch (ParseException e) {
                                                return false;
                                            }
                                        }
                                        return true;
                                    }
                                }
                        );
                    }
                }
            } else {
                field = super.createField(item, propertyId, uiContext);
            }

            initField(field, cubaField, propertyPath, true);

            return field;
        } else {
            return null;
        }
    }

    private Datasource getDatasource(Item item) {
        if (item instanceof WebFieldGroup.FieldGroupItemWrapper)
            return ((WebFieldGroup.FieldGroupItemWrapper) item).getDatasource();
        else
            return getDatasource();
    }

    /**
     * Creates fields for the Table
     */
    @Override
    public com.vaadin.ui.Field createField(com.vaadin.data.Container container, Object itemId, Object propertyId, com.vaadin.ui.Component uiContext) {
        final com.vaadin.ui.Field field;
        com.haulmont.cuba.gui.components.Field cubaField = null;
        MetaPropertyPath propertyPath = (MetaPropertyPath) propertyId;

        final Range range = propertyPath.getRange();
        if (range != null) {
            if (range.isClass()) {
                final CollectionDatasource optionsDatasource = getOptionsDatasource(range.asClass(), propertyPath);
                final WebLookupField lookupField = new WebLookupField();
                lookupField.setOptionsDatasource(optionsDatasource);

                field = (com.vaadin.ui.Field) WebComponentsHelper.unwrap(lookupField);
            } else if (range.isEnum()) {
                final WebLookupField lookupField = new WebLookupField();
                if (propertyPath.get().length > 1) throw new UnsupportedOperationException();

                lookupField.setDatasource(getDatasource(), propertyPath.getMetaProperty().getName());
                lookupField.setOptionsList(range.asEnumeration().getValues());

                field = (com.vaadin.ui.Field) WebComponentsHelper.unwrap(lookupField);
            } else {
                Class<Object> type = range.<Object>asDatatype().getJavaClass();
                if (Boolean.class.isAssignableFrom(type)) {
                    field = new CheckBox();
                } else if (Date.class.isAssignableFrom(type)) {
                    cubaField = new WebDateField();
                    field = ((WebDateField) cubaField).getComponent();
                } else {
                    field = super.createField(container, itemId, propertyId, uiContext);
                }
            }
        } else {
            field = super.createField(container, itemId, propertyId, uiContext);
        }

        initField(field, cubaField, propertyPath, false);

        if (!field.isReadOnly()) {
            field.setReadOnly(!security.isEntityAttrModificationPermitted(propertyPath.getMetaProperty()));
        }

        return field;
    }

    protected void setCaption(com.vaadin.ui.Field field, MetaPropertyPath propertyPath) {
        field.setCaption(AppBeans.get(MessageTools.class).getPropertyCaption(
                propertyPath.getMetaClass(),
                propertyPath.toString()));
    }

    protected void initField(final com.vaadin.ui.Field field, Field cubaField, MetaPropertyPath propertyPath,
                             boolean validationVisible) {
        setCaption(field, propertyPath);

        if (field instanceof com.vaadin.ui.AbstractField) {
            ((com.vaadin.ui.AbstractField) field).setImmediate(true);
        }

        initCommon(field, cubaField, propertyPath);

        initRequired(field, cubaField, propertyPath);

        initValidators(field, cubaField, propertyPath, validationVisible);
    }

    protected void initCommon(com.vaadin.ui.Field field, Field cubaField, MetaPropertyPath propertyPath) {
        if (field instanceof TextField) {
            ((TextField) field).setNullRepresentation("");
            field.setWidth("100%");
        } else if (cubaField instanceof WebDateField && getFormatter(propertyPath) != null) {
            String format = getFormat(propertyPath);
            if (format != null) {
                ((WebDateField) cubaField).setDateFormat(format);
            }
        } else if (field instanceof Select) {
            field.setWidth("100%");
        } else if (field instanceof WebPickerField) {
            field.setWidth("100%");
        }
    }

    protected void initRequired(com.vaadin.ui.Field field, Field cubaField, MetaPropertyPath propertyPath) {
        boolean required = required(propertyPath);
        field.setRequired(required);
        if (required)
            field.setRequiredError(requiredMessage(propertyPath));
    }

    protected void initValidators(final com.vaadin.ui.Field field, Field cubaField, MetaPropertyPath propertyPath, boolean validationVisible) {
        Collection<Field.Validator> validators = getValidators(propertyPath);
        if (validators != null) {
            for (final Field.Validator validator : validators) {

                if (field instanceof com.vaadin.ui.AbstractField) {
                    field.addValidator(new Validator() {
                        public void validate(Object value) throws InvalidValueException {
                            if ((!field.isRequired() && value == null))
                                return;
                            try {
                                validator.validate(value);
                            } catch (ValidationException e) {
                                throw new InvalidValueException(e.getMessage());
                            }
                        }

                        public boolean isValid(Object value) {
                            try {
                                validate(value);
                                return true;
                            } catch (InvalidValueException e) {
                                return false;
                            }
                        }
                    });
                    ((com.vaadin.ui.AbstractField) field).setValidationVisible(validationVisible);
                }
            }
        }
    }

    protected void initTextField(com.vaadin.ui.TextField field, MetaProperty metaProperty, Element xmlDescriptor) {
        if (xmlDescriptor == null)
            return;
        final String cols = xmlDescriptor.attributeValue("cols");
        if (!StringUtils.isEmpty(cols)) {
            field.setColumns(Integer.valueOf(cols));
        }
        final String rows = xmlDescriptor.attributeValue("rows");
        if (!StringUtils.isEmpty(rows)) {
            field.setRows(Integer.valueOf(rows));
        }
        final String maxLength = xmlDescriptor.attributeValue("maxLength");
        if (!StringUtils.isEmpty(maxLength)) {
            field.setMaxLength(Integer.valueOf(maxLength));
        } else {
            Integer len = (Integer) metaProperty.getAnnotations().get("length");
            if (len != null) {
                field.setMaxLength(len);
            }
        }
    }

    protected void initDateField(com.vaadin.ui.Field field, MetaProperty metaProperty, Element xmlDescriptor) {
        WebDateField cubaField = ((DateFieldWrapper) field).getCubaField();
        TemporalType tt = null;
        if (metaProperty != null) {
            if (metaProperty.getRange().asDatatype().equals(Datatypes.get(DateDatatype.NAME)))
                tt = TemporalType.DATE;
            else if (metaProperty.getAnnotations() != null)
                tt = (TemporalType) metaProperty.getAnnotations().get("temporal");
        }

        final String resolution = xmlDescriptor == null ? null : xmlDescriptor.attributeValue("resolution");
        String dateFormat = xmlDescriptor == null ? null : xmlDescriptor.attributeValue("dateFormat");

        if (!StringUtils.isEmpty(resolution)) {
            com.haulmont.cuba.gui.components.DateField.Resolution res = com.haulmont.cuba.gui.components.DateField.Resolution.valueOf(resolution);
            cubaField.setResolution(res);

            if (dateFormat == null) {
                if (res == com.haulmont.cuba.gui.components.DateField.Resolution.DAY) {
                    dateFormat = "msg://dateFormat";
                } else if (res == com.haulmont.cuba.gui.components.DateField.Resolution.MIN) {
                    dateFormat = "msg://dateTimeFormat";
                }
            }

        } else if (tt == TemporalType.DATE) {
            cubaField.setResolution(com.haulmont.cuba.gui.components.DateField.Resolution.DAY);
        }

        if (!StringUtils.isEmpty(dateFormat)) {
            if (dateFormat.startsWith("msg://")) {
                dateFormat = MessageProvider.getMessage(
                        AppConfig.getMessagesPack(), dateFormat.substring(6, dateFormat.length()));
            }
            cubaField.setDateFormat(dateFormat);
        } else {
            String formatStr;
            if (tt == TemporalType.DATE) {
                formatStr = MessageProvider.getMessage(AppConfig.getMessagesPack(),
                        "dateFormat");
            } else {
                formatStr = MessageProvider.getMessage(AppConfig.getMessagesPack(),
                        "dateTimeFormat");
            }
            cubaField.setDateFormat(formatStr);
        }
    }

    protected void initMaskedField(WebMaskedField field, MetaProperty metaProperty, Element xmlDescriptor) {

        String mask = xmlDescriptor.attributeValue("mask");
        if (!StringUtils.isEmpty(mask)) {
            field.setMask(mask);
        }
        String valueModeStr = xmlDescriptor.attributeValue("valueMode", MaskedField.ValueMode.CLEAR.getId());
        field.setValueMode(MaskedField.ValueMode.fromId(valueModeStr));
    }

    protected abstract Datasource getDatasource();

    protected abstract CollectionDatasource getOptionsDatasource(MetaClass metaClass, MetaPropertyPath propertyPath);

    protected abstract Collection<Field.Validator> getValidators(MetaPropertyPath propertyPath);

    protected abstract boolean required(MetaPropertyPath propertyPath);

    protected abstract String requiredMessage(MetaPropertyPath propertyPath);

    protected abstract Formatter getFormatter(MetaPropertyPath propertyPath);

    protected abstract String getFormat(MetaPropertyPath propertyPath);

    protected abstract String fieldType(MetaPropertyPath propertyPath);

    protected abstract Element getXmlDescriptor(MetaPropertyPath propertyPath);
}
