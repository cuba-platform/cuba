/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.app.core.bulk;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.datatypes.impl.*;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;

import javax.annotation.Nullable;
import javax.persistence.TemporalType;
import java.util.HashMap;
import java.util.Map;

/**
 * @author artamonov
 * @version $Id$
 */
public class BulkEditorFieldFactory {

    protected ComponentsFactory componentsFactory = AppConfig.getFactory();
    protected Messages messages = AppBeans.get(Messages.class);

    protected static final int MAX_TEXTFIELD_STRING_LENGTH = 255;

    @Nullable
    public Field createField(Datasource datasource, MetaProperty property) {
        if (property.getRange().isDatatype()) {
            Datatype datatype = property.getRange().asDatatype();
            String typeName = datatype.getName();
            if (typeName.equals(StringDatatype.NAME)) {
                return createStringField(datasource, property);
            } else if (typeName.equals(BooleanDatatype.NAME)) {
                return createBooleanField(datasource, property);
            } else if (typeName.equals(DateDatatype.NAME) || typeName.equals(DateTimeDatatype.NAME)) {
                return createDateField(datasource, property);
            } else if (typeName.equals(TimeDatatype.NAME)) {
                return createTimeField(datasource, property);
            } else if (datatype instanceof NumberDatatype) {
                return createNumberField(datasource, property);
            }
        } else if (property.getRange().isClass()) {
            return createEntityField(datasource, property);
        } else if (property.getRange().isEnum()) {
            return createEnumField(datasource, property);
        }
        return null;
    }

    protected Field createStringField(Datasource datasource, MetaProperty property) {
        Integer textLength = (Integer) property.getAnnotations().get("length");
        boolean isLong = textLength == null || textLength > MAX_TEXTFIELD_STRING_LENGTH;

        TextInputField textField;
        if (!isLong) {
            textField = componentsFactory.createComponent(TextField.NAME);
        } else {
            TextArea textArea = componentsFactory.createComponent(TextArea.NAME);
            textArea.setRows(3);
            textField = textArea;
        }

        textField.setDatasource(datasource, property.getName());

        if (textLength != null) {
            ((TextInputField.MaxLengthLimited) textField).setMaxLength(textLength);
        }

        return textField;
    }

    protected Field createBooleanField(final Datasource datasource, MetaProperty property) {
        LookupField lookupField = componentsFactory.createComponent(LookupField.NAME);
        lookupField.setDatasource(datasource, property.getName());

        Map<String, Object> options = new HashMap<>();
        options.put(messages.getMessage(getClass(), "boolean.yes"), Boolean.TRUE);
        options.put(messages.getMessage(getClass(), "boolean.no"), Boolean.FALSE);

        lookupField.setOptionsMap(options);

        return lookupField;
    }

    protected Field createDateField(Datasource datasource, MetaProperty property) {
        Datatype<?> datatype = property.getRange().asDatatype();

        DateField dateField = componentsFactory.createComponent(DateField.NAME);
        dateField.setDatasource(datasource, property.getName());

        if (datatype.equals(Datatypes.get(DateTimeDatatype.NAME))) {
            dateField.setResolution(DateField.Resolution.DAY);
            dateField.setDateFormat(messages.getMainMessage("dateTimeFormat"));
        } else if (datatype.equals(Datatypes.get(DateDatatype.NAME))) {
            dateField.setResolution(DateField.Resolution.SEC);
            dateField.setDateFormat(messages.getMainMessage("dateFormat"));
        } else {
            throw new RuntimeException("Unknown datatype for " + property);
        }

        return dateField;
    }

    protected Field createTimeField(Datasource datasource, MetaProperty property) {
        TimeField timeField = componentsFactory.createComponent(TimeField.NAME);
        timeField.setDatasource(datasource, property.getName());
        timeField.setShowSeconds(true);
        return timeField;
    }

    protected Field createNumberField(Datasource datasource, MetaProperty property) {
        TextField textField = componentsFactory.createComponent(TextField.NAME);
        textField.setDatasource(datasource, property.getName());
        return textField;
    }

    protected Field createEntityField(Datasource datasource, MetaProperty property) {
        PickerField pickerField = componentsFactory.createComponent(PickerField.NAME);
        pickerField.addLookupAction();
        pickerField.addClearAction();

        pickerField.setDatasource(datasource, property.getName());

        return pickerField;
    }

    protected Field createEnumField(Datasource datasource, MetaProperty property) {
        LookupField lookupField = componentsFactory.createComponent(LookupField.NAME);
        lookupField.setDatasource(datasource, property.getName());

        return lookupField;
    }
}