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

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesUtils;
import com.haulmont.cuba.core.app.dynamicattributes.PropertyType;
import com.haulmont.cuba.core.entity.CategoryAttribute;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.entity.annotation.Lookup;
import com.haulmont.cuba.core.entity.annotation.LookupType;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.GuiActionSupport;
import com.haulmont.cuba.gui.components.data.options.ContainerOptions;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.dynamicattributes.DynamicAttributeCustomFieldGenerator;
import com.haulmont.cuba.gui.model.CollectionContainer;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.model.DataComponents;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import org.apache.commons.lang3.BooleanUtils;

import javax.annotation.Nullable;
import java.sql.Time;
import java.time.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BulkEditorFieldFactory {

    protected ComponentsFactory componentsFactory = AppConfig.getFactory();
    protected Messages messages = AppBeans.get(Messages.NAME);

    protected static final int MAX_TEXTFIELD_STRING_LENGTH = 255;

    @Nullable
    public Field createField(Datasource datasource, MetaProperty property) {
        if (DynamicAttributesUtils.isDynamicAttribute(property)) {
            CategoryAttribute attribute = DynamicAttributesUtils.getCategoryAttribute(property);
            if (attribute.getDataType().equals(PropertyType.ENUMERATION)
                    && BooleanUtils.isNotTrue(attribute.getIsCollection())) {
                return createEnumField(datasource, property);
            } else if (BooleanUtils.isTrue(attribute.getIsCollection())) {
                return createListEditorField(datasource, property);
            }
        }

        if (property.getRange().isDatatype()) {
            Class type = property.getRange().asDatatype().getJavaClass();
            if (type.equals(String.class)) {
                return createStringField(datasource, property);
            } else if (type.equals(Boolean.class)) {
                return createBooleanField(datasource, property);
            } else if (type.equals(java.sql.Date.class)
                    || type.equals(Date.class)
                    || type.equals(LocalDate.class)
                    || type.equals(LocalDateTime.class)
                    || type.equals(OffsetDateTime.class)) {
                return createDateField(datasource, property);
            } else if (type.equals(Time.class)
                    || type.equals(LocalTime.class)
                    || type.equals(OffsetTime.class)) {
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

        if (type.equals(Date.class)
                || type.equals(LocalDateTime.class)
                || type.equals(OffsetDateTime.class)) {
            dateField.setResolution(DateField.Resolution.MIN);
            dateField.setDateFormat(messages.getMainMessage("dateTimeFormat"));
        } else if (type.equals(java.sql.Date.class)
                || type.equals(java.time.LocalDate.class)) {
            dateField.setResolution(DateField.Resolution.DAY);
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
        Lookup lookup = property.getAnnotatedElement().getAnnotation(Lookup.class);
        if (lookup != null && lookup.type() == LookupType.DROPDOWN) {
            DataComponents dataComponents = AppBeans.get(DataComponents.class);
            Metadata metadata = AppBeans.get(Metadata.class);

            MetaClass metaClass = metadata.getClassNN(property.getJavaType());
            CollectionContainer<Entity> container = dataComponents.createCollectionContainer(metaClass.getJavaClass());
            CollectionLoader<Entity> loader = dataComponents.createCollectionLoader();
            loader.setQuery("select e from " + metaClass.getName() + " e");
            loader.setView(View.MINIMAL);
            loader.setContainer(container);
            loader.load();

            LookupPickerField<Entity> lookupPickerField = componentsFactory.createComponent(LookupPickerField.NAME);
            lookupPickerField.setDatasource(datasource, property.getName());
            lookupPickerField.setOptions(new ContainerOptions(container));

            GuiActionSupport guiActionSupport = AppBeans.get(GuiActionSupport.NAME);
            guiActionSupport.createActionsByMetaAnnotations(lookupPickerField);

            return lookupPickerField;
        }

        PickerField<Entity> pickerField = componentsFactory.createComponent(PickerField.NAME);
        pickerField.setDatasource(datasource, property.getName());
        GuiActionSupport guiActionSupport = AppBeans.get(GuiActionSupport.NAME);
        guiActionSupport.createActionById(pickerField, PickerField.ActionType.LOOKUP.getId());
        if (lookup == null || !guiActionSupport.createActionsByMetaAnnotations(pickerField)) {
            guiActionSupport.createActionById(pickerField, PickerField.ActionType.CLEAR.getId());
        }

        return pickerField;
    }

    protected Field createEnumField(Datasource datasource, MetaProperty property) {
        LookupField lookupField = componentsFactory.createComponent(LookupField.class);
        lookupField.setDatasource(datasource, property.getName());

        return lookupField;
    }

    protected Field createListEditorField(Datasource datasource, MetaProperty property) {
        DynamicAttributeCustomFieldGenerator generator = new DynamicAttributeCustomFieldGenerator();

        //noinspection UnnecessaryLocalVariable
        ListEditor editor = (ListEditor) generator.generateField(datasource, property.getName());
        return editor;
    }
}