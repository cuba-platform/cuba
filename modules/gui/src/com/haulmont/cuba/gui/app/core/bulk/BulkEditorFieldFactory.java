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

package com.haulmont.cuba.gui.app.core.bulk;

import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;

import javax.annotation.Nullable;
import java.sql.Time;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BulkEditorFieldFactory {

    protected ComponentsFactory componentsFactory = AppConfig.getFactory();
    protected Messages messages = AppBeans.get(Messages.NAME);

    protected static final int MAX_TEXTFIELD_STRING_LENGTH = 255;

    @Nullable
    public Field createField(Datasource datasource, MetaProperty property) {
        if (property.getRange().isDatatype()) {
            Class type = property.getRange().asDatatype().getJavaClass();
            if (type.equals(String.class)) {
                return createStringField(datasource, property);
            } else if (type.equals(Boolean.class)) {
                return createBooleanField(datasource, property);
            } else if (type.equals(java.sql.Date.class) || type.equals(Date.class)) {
                return createDateField(datasource, property);
            } else if (type.equals(Time.class)) {
                return createTimeField(datasource, property);
            } else if (Number.class.isAssignableFrom(type)) {
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
            textField = componentsFactory.createComponent(TextField.class);
        } else {
            TextArea textArea = componentsFactory.createComponent(TextArea.class);
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
        LookupField lookupField = componentsFactory.createComponent(LookupField.class);
        lookupField.setDatasource(datasource, property.getName());

        Map<String, Object> options = new HashMap<>();
        options.put(messages.getMessage(getClass(), "boolean.yes"), Boolean.TRUE);
        options.put(messages.getMessage(getClass(), "boolean.no"), Boolean.FALSE);

        lookupField.setOptionsMap(options);

        return lookupField;
    }

    protected Field createDateField(Datasource datasource, MetaProperty property) {
        Class type = property.getRange().asDatatype().getJavaClass();

        DateField dateField = componentsFactory.createComponent(DateField.class);
        dateField.setDatasource(datasource, property.getName());

        if (type.equals(Date.class)) {
            dateField.setResolution(DateField.Resolution.DAY);
            dateField.setDateFormat(messages.getMainMessage("dateTimeFormat"));
        } else if (type.equals(java.sql.Date.class)) {
            dateField.setResolution(DateField.Resolution.SEC);
            dateField.setDateFormat(messages.getMainMessage("dateFormat"));
        } else {
            throw new RuntimeException("Unknown type for " + property);
        }

        return dateField;
    }

    protected Field createTimeField(Datasource datasource, MetaProperty property) {
        TimeField timeField = componentsFactory.createComponent(TimeField.class);
        timeField.setDatasource(datasource, property.getName());
        timeField.setShowSeconds(true);
        return timeField;
    }

    protected Field createNumberField(Datasource datasource, MetaProperty property) {
        TextField textField = componentsFactory.createComponent(TextField.class);
        textField.setDatasource(datasource, property.getName());
        return textField;
    }

    protected Field createEntityField(Datasource datasource, MetaProperty property) {
        PickerField pickerField = componentsFactory.createComponent(PickerField.class);
        pickerField.addLookupAction();
        pickerField.addClearAction();

        pickerField.setDatasource(datasource, property.getName());

        return pickerField;
    }

    protected Field createEnumField(Datasource datasource, MetaProperty property) {
        LookupField lookupField = componentsFactory.createComponent(LookupField.class);
        lookupField.setDatasource(datasource, property.getName());

        return lookupField;
    }
}