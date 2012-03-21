/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.datatypes.Enumeration;
import com.haulmont.chile.core.datatypes.impl.*;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.DateField;
import com.haulmont.cuba.gui.components.PickerField;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import javax.persistence.TemporalType;
import java.util.Map;
import java.util.TreeMap;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public abstract class AbstractFieldFactory {

    public Component createField(Datasource datasource, String property, Element xmlDescriptor) {
        MetaClass metaClass = datasource.getMetaClass();
        MetaPropertyPath mpp = metaClass.getPropertyPath(property);
        if (mpp != null) {
            if (mpp.getRange().isDatatype()) {
                Datatype datatype = mpp.getRange().asDatatype();
                String typeName = datatype.getName();
                if (typeName.equals(StringDatatype.NAME)) {
                    return createStringField(datasource, property, xmlDescriptor);
                } else if (typeName.equals(BooleanDatatype.NAME)) {
                    return createBooleanField(datasource, property);
                } else if (typeName.equals(DateDatatype.NAME) || typeName.equals(DateTimeDatatype.NAME)) {
                    return createDateField(datasource, property, mpp, xmlDescriptor);
                } else if (typeName.equals(TimeDatatype.NAME)) {
                    return createTimeField(datasource, property, mpp);
                } else if (datatype instanceof NumberDatatype) {
                    return createNumberField(datasource, property);
                }
            } else if (mpp.getRange().isClass()) {
                return createEntityField(datasource, property);
            } else if (mpp.getRange().isEnum()) {
                return createEnumField(datasource, property, mpp.getMetaProperty());
            }
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

    private Component createStringField(Datasource datasource, String property, Element xmlDescriptor) {
        DesktopTextField textField = new DesktopTextField();
        textField.setDatasource(datasource, property);
        MetaProperty metaProperty = textField.getMetaProperty();
        if (xmlDescriptor != null) {
            final String rows = xmlDescriptor.attributeValue("rows");
            if (!StringUtils.isEmpty(rows)) {
                textField.setRows(Integer.valueOf(rows));
            }
        }

        final String maxLength = xmlDescriptor != null ? xmlDescriptor.attributeValue("maxLength") : null;
        if (!StringUtils.isEmpty(maxLength)) {
            textField.setMaxLength(Integer.valueOf(maxLength));
        } else {
            Integer len = (Integer) metaProperty.getAnnotations().get("length");
            if (len != null) {
                textField.setMaxLength(len);
            }
        }

        textField.setVisible(true);
        return textField;
    }

    private Component createDateField(Datasource datasource, String property, MetaPropertyPath mpp,
                                      Element xmlDescriptor) {
        DesktopDateField dateField = new DesktopDateField();
        dateField.setDatasource(datasource, property);

        MetaProperty metaProperty = mpp.getMetaProperty();
        TemporalType tt = null;
        if (metaProperty != null) {
            if (metaProperty.getRange().asDatatype().equals(Datatypes.get(DateDatatype.NAME)))
                tt = TemporalType.DATE;
            else if (metaProperty.getAnnotations() != null)
                tt = (TemporalType) metaProperty.getAnnotations().get("temporal");
        }

        final String resolution = xmlDescriptor == null ? null : xmlDescriptor.attributeValue("resolution");

        if (!StringUtils.isEmpty(resolution)) {
            dateField.setResolution(DateField.Resolution.valueOf(resolution));
        } else if (tt == TemporalType.DATE) {
            dateField.setResolution(DateField.Resolution.DAY);
        }
        String dateFormat = xmlDescriptor == null ? null : xmlDescriptor.attributeValue("dateFormat");
        if (!StringUtils.isEmpty(dateFormat)) {
            if (dateFormat.startsWith("msg://")) {
                dateFormat = MessageProvider.getMessage(
                        AppConfig.getMessagesPack(), dateFormat.substring(6, dateFormat.length()));
            }
            dateField.setDateFormat(dateFormat);
        }
        return dateField;
    }

    private Component createTimeField(Datasource datasource, String property, MetaPropertyPath mpp) {
        DesktopTimeField timeField = new DesktopTimeField();
        timeField.setDatasource(datasource, property);
        return timeField;
    }

    private Component createEntityField(Datasource datasource, String property) {
        PickerField pickerField;

        CollectionDatasource optionsDatasource = getOptionsDatasource(datasource, property);
        if (optionsDatasource == null) {
            pickerField = new DesktopPickerField();
        } else {
            pickerField = new DesktopLookupPickerField();
            ((DesktopLookupPickerField) pickerField).setOptionsDatasource(optionsDatasource);
            if (pickerField.getAction(PickerField.LookupAction.NAME) != null)
                pickerField.removeAction(pickerField.getAction(PickerField.LookupAction.NAME));
        }
        pickerField.setDatasource(datasource, property);
        pickerField.addLookupAction();
        pickerField.addClearAction();
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
        label.setValue("TODO: " + (mpp != null ? mpp.getRange() : ""));
        return label;
    }

    protected abstract CollectionDatasource getOptionsDatasource(Datasource datasource, String property);
}
