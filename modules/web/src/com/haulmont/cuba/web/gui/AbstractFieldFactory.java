/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 23.06.2010 18:13:08
 *
 * $Id$
 */
package com.haulmont.cuba.web.gui;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.Range;
import com.haulmont.cuba.core.global.MessageUtils;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.Formatter;
import com.haulmont.cuba.gui.components.ValidationException;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.web.gui.components.WebComponentsHelper;
import com.haulmont.cuba.web.gui.components.WebLookupField;
import com.haulmont.cuba.web.gui.components.WebPickerField;
import com.haulmont.cuba.web.toolkit.ui.CheckBox;
import com.vaadin.data.Item;
import com.vaadin.data.Validator;
import com.vaadin.ui.DateField;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Select;
import com.vaadin.ui.TextField;

import java.util.Collection;

public abstract class AbstractFieldFactory extends DefaultFieldFactory {

    /**
     * Creates fields for the Form
     */

    @Override
    public com.vaadin.ui.Field createField(Item item, Object propertyId, com.vaadin.ui.Component uiContext) {
        if (item != null && propertyId != null) {
            final com.vaadin.ui.Field field;
            MetaPropertyPath propertyPath = (MetaPropertyPath) propertyId;

            final Range range = propertyPath.getRange();
            if (range != null) {
                if (range.isClass()) {
                    final CollectionDatasource optionsDatasource = getOptionsDatasource(range.asClass(), propertyPath);
                    if (optionsDatasource != null) {
                        final WebLookupField lookupField = new WebLookupField();
                        lookupField.setOptionsDatasource(optionsDatasource);

                        field = (com.vaadin.ui.Field) WebComponentsHelper.unwrap(lookupField);
                    } else {
                        final WebPickerField pickerField = new WebPickerField();
                        pickerField.setMetaClass(range.asClass());
                        field = (com.vaadin.ui.Field) WebComponentsHelper.unwrap(pickerField);
                    }
                } else if (range.isEnum()) {
                    final WebLookupField lookupField = new WebLookupField();
                    if (propertyPath.get().length > 1) throw new UnsupportedOperationException();

                    lookupField.setDatasource(getDatasource(), propertyPath.getMetaProperty().getName());
                    lookupField.setOptionsList(range.asEnumeration().getValues());

                    field = (com.vaadin.ui.Field) WebComponentsHelper.unwrap(lookupField);
                } else {
                    Class<?> type = item.getItemProperty(propertyId).getType();
                    if (Boolean.class.isAssignableFrom(type)) {
                        field = new CheckBox();
                    } else {
                        field = super.createField(item, propertyId, uiContext);
                    }
                }
            } else {
                field = super.createField(item, propertyId, uiContext);
            }

            field.setCaption(MessageUtils.getPropertyCaption(propertyPath.getMetaClass(),
                    propertyPath.toString()));

            initField(field, propertyPath, true);

            return field;
        } else {
            return null;
        }
    }

    /**
     * Creates fields for the Table
     */
    @Override
    public com.vaadin.ui.Field createField(com.vaadin.data.Container container, Object itemId, Object propertyId, com.vaadin.ui.Component uiContext) {
        final com.vaadin.ui.Field field;
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
                field = super.createField(container, itemId, propertyId, uiContext);
            }
        } else {
            field = super.createField(container, itemId, propertyId, uiContext);
        }

        initField(field, propertyPath, false);

        return field;
    }

    protected void initField(final com.vaadin.ui.Field field, MetaPropertyPath propertyPath, boolean validationVisible) {
        if (field instanceof com.vaadin.ui.AbstractField) {
            ((com.vaadin.ui.AbstractField) field).setImmediate(true);
        }

        initCommon(field, propertyPath);

        initRequired(field, propertyPath);

        initValidators(field, propertyPath, validationVisible);
    }

    protected void initCommon(com.vaadin.ui.Field field, MetaPropertyPath propertyPath) {
        if (field instanceof TextField) {
            ((TextField) field).setNullRepresentation("");
            field.setWidth("100%");
        } else if (field instanceof DateField && getFormatter(propertyPath) != null) {
            String format = getFormat(propertyPath);
            if (format != null) {
                ((DateField) field).setDateFormat(format);
            }
        } else if (field instanceof Select) {
            field.setWidth("100%");
        } else if (field instanceof WebPickerField) {
            field.setWidth("100%");
        }
    }

    protected void initRequired(com.vaadin.ui.Field field, MetaPropertyPath propertyPath) {
        boolean required = required(propertyPath);
        field.setRequired(required);
        if (required)
            field.setRequiredError(requiredMessage(propertyPath));
    }

    protected void initValidators(final com.vaadin.ui.Field field, MetaPropertyPath propertyPath, boolean validationVisible) {
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

    protected abstract Datasource getDatasource();

    protected abstract CollectionDatasource getOptionsDatasource(MetaClass metaClass, MetaPropertyPath propertyPath);

    protected abstract Collection<Field.Validator> getValidators(MetaPropertyPath propertyPath);

    protected abstract boolean required(MetaPropertyPath propertyPath);

    protected abstract String requiredMessage(MetaPropertyPath propertyPath);

    protected abstract Formatter getFormatter(MetaPropertyPath propertyPath);

    protected abstract String getFormat(MetaPropertyPath propertyPath);
}
