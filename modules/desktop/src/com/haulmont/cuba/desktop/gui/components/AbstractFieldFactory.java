/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Enumeration;
import com.haulmont.chile.core.datatypes.impl.*;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.PickerField;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;

import java.util.Map;
import java.util.TreeMap;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public abstract class AbstractFieldFactory {

    public Component createField(Datasource datasource, String property) {
        MetaClass metaClass = datasource.getMetaClass();
        MetaPropertyPath mpp = metaClass.getPropertyPath(property);
        if (mpp.getRange().isDatatype()) {
            Datatype datatype = mpp.getRange().asDatatype();
            String typeName = datatype.getName();
            if (typeName.equals(StringDatatype.NAME)) {
                return createStringField(datasource, property);
            } else if (typeName.equals(BooleanDatatype.NAME)) {
                return createBooleanField(datasource, property);
            } else if (typeName.equals(DateDatatype.NAME) || typeName.equals(DateTimeDatatype.NAME)) {
                return createDateField(datasource, property);
            } else if (datatype instanceof NumberDatatype) {
                return createNumberField(datasource, property);
            }
        } else if (mpp.getRange().isClass()) {
            return createEntityField(datasource, property);
        } else if (mpp.getRange().isEnum()) {
            return createEnumField(datasource, property, mpp.getMetaProperty());
        }
        return createUnsupportedField(mpp);
    }

    private Component createNumberField(Datasource datasource, String property) {
        DesktopTextField textField = new DesktopTextField();
        textField.setDatasource(datasource, property);
        return textField;
    }

    private Component createBooleanField(Datasource datasource, String property) {
        DesktopCheckBox checkBox = new DesktopCheckBox();
        checkBox.setDatasource(datasource, property);
        return checkBox;
    }

    private Component createStringField(Datasource datasource, String property) {
        DesktopTextField textField = new DesktopTextField();
        textField.setDatasource(datasource, property);
        return textField;
    }

    private Component createDateField(Datasource datasource, String property) {
        DesktopDateField dateField = new DesktopDateField();
        dateField.setDatasource(datasource, property);
        return dateField;
    }

    private Component createEntityField(Datasource datasource, String property) {
        PickerField pickerField;

        CollectionDatasource optionsDatasource = getOptionsDatasource(datasource, property);
        if (optionsDatasource == null) {
            pickerField = new DesktopPickerField();
        } else {
            pickerField = new DesktopLookupPickerField();
            ((DesktopLookupPickerField) pickerField).setOptionsDatasource(optionsDatasource);
        }
        pickerField.setDatasource(datasource, property);
        return pickerField;
    }

    private Component createEnumField(Datasource datasource, String property, MetaProperty metaProperty) {
        Map<String, Object> options = new TreeMap<String, Object>();
        Enumeration<Enum> enumeration = metaProperty.getRange().asEnumeration();
        for (Enum value : enumeration.getValues()) {
            String caption = MessageProvider.getMessage(value);
            options.put(caption, value);
        }

        DesktopLookupField lookupField = new DesktopLookupField();
        lookupField.setOptionsMap(options);
        lookupField.setDatasource(datasource, property);
        return lookupField;
    }

    private Component createUnsupportedField(MetaPropertyPath mpp) {
        DesktopLabel label = new DesktopLabel();
        label.setValue("TODO: " + mpp.getRange());
        return label;
    }

    protected abstract CollectionDatasource getOptionsDatasource(Datasource datasource, String property);
}
