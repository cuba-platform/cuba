/*
 * Copyright (c) 2008-2017 Haulmont.
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
 */

package com.haulmont.cuba.gui.components;

import com.haulmont.bali.util.ParamsMap;
import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.datatypes.impl.*;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.chile.core.model.Range;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesMetaProperty;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesUtils;
import com.haulmont.cuba.core.app.dynamicattributes.PropertyType;
import com.haulmont.cuba.core.entity.CategoryAttribute;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.dynamicattributes.DynamicAttributesGuiTools;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;

import javax.inject.Inject;
import javax.persistence.TemporalType;

@org.springframework.stereotype.Component(DataGridEditorFieldFactory.NAME)
public class DataGridEditorFieldFactoryImpl implements DataGridEditorFieldFactory {

    @Inject
    protected Messages messages;

    @Inject
    protected ComponentsFactory componentsFactory;

    @Override
    public Field createField(Datasource datasource, String property) {
        return createFieldComponent(datasource, property);
    }

    protected Field createFieldComponent(Datasource datasource, String property) {
        MetaClass metaClass = datasource.getMetaClass();
        MetaPropertyPath mpp = metaClass.getPropertyPath(property);

        if (mpp == null && DynamicAttributesUtils.isDynamicAttribute(property)) {
            mpp = DynamicAttributesUtils.getMetaPropertyPath(metaClass, property);
        }

        if (mpp != null) {
            Range mppRange = mpp.getRange();
            if (mppRange.isDatatype()) {
                Datatype datatype = mppRange.asDatatype();
                String typeName = datatype.getName();

                MetaProperty metaProperty = mpp.getMetaProperty();
                if (DynamicAttributesUtils.isDynamicAttribute(metaProperty)) {
                    CategoryAttribute categoryAttribute = DynamicAttributesUtils.getCategoryAttribute(metaProperty);
                    if (categoryAttribute != null && categoryAttribute.getDataType() == PropertyType.ENUMERATION) {
                        return createEnumField(datasource, property);
                    }
                }

                if (typeName.equals(StringDatatype.NAME)) {
                    return createStringField(datasource, property);
                } else if (typeName.equals(UUIDDatatype.NAME)) {
                    return createUuidField(datasource, property);
                } else if (typeName.equals(BooleanDatatype.NAME)) {
                    return createBooleanField(datasource, property);
                } else if (typeName.equals(DateDatatype.NAME) || typeName.equals(DateTimeDatatype.NAME)) {
                    return createDateField(datasource, property, mpp);
                } else if (typeName.equals(TimeDatatype.NAME)) {
                    return createTimeField(datasource, property);
                } else if (datatype instanceof NumberDatatype) {
                    return createNumberField(datasource, property);
                }
            } else if (mppRange.isClass()) {
                return createEntityField(datasource, property, mpp);
            } else if (mppRange.isEnum()) {
                return createEnumField(datasource, property);
            }
        }

        String exceptionMessage;
        if (mpp != null) {
            exceptionMessage = String.format("Can't create field \"%s\" with data type: %s",
                    property, mpp.getRange().asDatatype().getName());
        } else {
            exceptionMessage = String.format("Can't create field \"%s\" with given data type", property);
        }
        throw new UnsupportedOperationException(exceptionMessage);
    }

    protected Field createUuidField(Datasource datasource, String property) {
        MaskedField maskedField = componentsFactory.createComponent(MaskedField.class);
        maskedField.setDatasource(datasource, property);
        maskedField.setMask("hhhhhhhh-hhhh-hhhh-hhhh-hhhhhhhhhhhh");
        maskedField.setSendNullRepresentation(false);
        return maskedField;
    }

    protected Field createNumberField(Datasource datasource, String property) {
        TextField numberField = componentsFactory.createComponent(TextField.class);
        numberField.setDatasource(datasource, property);
        return numberField;
    }

    protected Field createBooleanField(Datasource datasource, String property) {
        CheckBox checkBox = componentsFactory.createComponent(CheckBox.class);
        checkBox.setDatasource(datasource, property);
        return checkBox;
    }

    protected Field createStringField(Datasource datasource, String property) {
        TextField textField = componentsFactory.createComponent(TextField.class);
        textField.setDatasource(datasource, property);
        return textField;
    }

    protected Field createDateField(Datasource datasource, String property, MetaPropertyPath mpp) {
        DateField dateField = componentsFactory.createComponent(DateField.class);
        dateField.setDatasource(datasource, property);

        MetaProperty metaProperty = mpp.getMetaProperty();
        TemporalType tt = null;
        if (metaProperty != null) {
            if (metaProperty.getRange().asDatatype().equals(Datatypes.get(DateDatatype.NAME))) {
                tt = TemporalType.DATE;
            } else if (metaProperty.getAnnotations() != null) {
                tt = (TemporalType) metaProperty.getAnnotations().get(MetadataTools.TEMPORAL_ANN_NAME);
            }
        }

        if (tt == TemporalType.DATE) {
            dateField.setResolution(DateField.Resolution.DAY);
        }
        String formatStr;
        if (tt == TemporalType.DATE) {
            formatStr = messages.getMainMessage("dateFormat");
        } else {
            formatStr = messages.getMainMessage("dateTimeFormat");
        }
        dateField.setDateFormat(formatStr);

        return dateField;
    }

    protected Field createTimeField(Datasource datasource, String property) {
        TimeField timeField = componentsFactory.createComponent(TimeField.class);
        timeField.setDatasource(datasource, property);
        return timeField;
    }

    protected Field createEntityField(Datasource datasource, String property, MetaPropertyPath mpp) {
        CollectionDatasource optionsDatasource = null;

        if (DynamicAttributesUtils.isDynamicAttribute(mpp.getMetaProperty())) {
            DynamicAttributesMetaProperty metaProperty = (DynamicAttributesMetaProperty) mpp.getMetaProperty();
            CategoryAttribute attribute = metaProperty.getAttribute();
            if (Boolean.TRUE.equals(attribute.getLookup())) {
                DynamicAttributesGuiTools dynamicAttributesGuiTools = AppBeans.get(DynamicAttributesGuiTools.class);
                optionsDatasource = dynamicAttributesGuiTools.createOptionsDatasourceForLookup(metaProperty.getRange()
                        .asClass(), attribute.getJoinClause(), attribute.getWhereClause());
            }
        }

        PickerField pickerField;
        if (optionsDatasource == null) {
            pickerField = componentsFactory.createComponent(PickerField.class);
            pickerField.setDatasource(datasource, property);
            PickerField.LookupAction lookupAction = pickerField.addLookupAction();
            // Opening lookup screen in another mode will close editor
            lookupAction.setLookupScreenOpenType(WindowManager.OpenType.DIALOG);
            // In case of adding special logic for lookup screen opened from DataGrid editor
            lookupAction.setLookupScreenParams(ParamsMap.of("dataGridEditor", true));
            if (DynamicAttributesUtils.isDynamicAttribute(mpp.getMetaProperty())) {
                DynamicAttributesGuiTools dynamicAttributesGuiTools = AppBeans.get(DynamicAttributesGuiTools.class);
                DynamicAttributesMetaProperty dynamicAttributesMetaProperty = (DynamicAttributesMetaProperty) mpp.getMetaProperty();
                dynamicAttributesGuiTools.initEntityPickerField(pickerField, dynamicAttributesMetaProperty.getAttribute());
            }
            boolean actionsByMetaAnnotations = ComponentsHelper.createActionsByMetaAnnotations(pickerField);
            if (!actionsByMetaAnnotations) {
                pickerField.addClearAction();
            }
        } else {
            LookupPickerField lookupPickerField = componentsFactory.createComponent(LookupPickerField.class);
            lookupPickerField.setDatasource(datasource, property);
            lookupPickerField.setOptionsDatasource(optionsDatasource);

            pickerField = lookupPickerField;

            ComponentsHelper.createActionsByMetaAnnotations(pickerField);
        }

        return pickerField;
    }

    protected Field createEnumField(Datasource datasource, String property) {
        LookupField lookupField = componentsFactory.createComponent(LookupField.class);
        lookupField.setDatasource(datasource, property);
        return lookupField;
    }
}
