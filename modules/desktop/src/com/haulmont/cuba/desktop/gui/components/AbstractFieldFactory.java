/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.datatypes.Enumeration;
import com.haulmont.chile.core.datatypes.impl.*;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import javax.persistence.TemporalType;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author krivopustov
 * @version $Id$
 */
public abstract class AbstractFieldFactory implements FieldFactory {

    @Override
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
                    return createTimeField(datasource, property, xmlDescriptor);
                } else if (datatype instanceof NumberDatatype) {
                    return createNumberField(datasource, property);
                }
            } else if (mpp.getRange().isClass()) {
                return createEntityField(datasource, property, xmlDescriptor);
            } else if (mpp.getRange().isEnum()) {
                return createEnumField(datasource, property, mpp.getMetaProperty());
            }
        }
        return createUnsupportedField(mpp);
    }

    protected Component createNumberField(Datasource datasource, String property) {
        DesktopTextField textField = new DesktopTextField();
        textField.setDatasource(datasource, property);
        return textField;
    }

    protected Component createBooleanField(Datasource datasource, String property) {
        DesktopCheckBox checkBox = new DesktopCheckBox();
        checkBox.setDatasource(datasource, property);
        return checkBox;
    }

    protected Component createStringField(Datasource datasource, String property, Element xmlDescriptor) {
        DesktopAbstractTextField textField = null;

        if (xmlDescriptor != null) {
            final String rows = xmlDescriptor.attributeValue("rows");
            if (!StringUtils.isEmpty(rows)) {
                DesktopTextArea textArea = new DesktopTextArea();
                textArea.setRows(Integer.valueOf(rows));
                textField = textArea;
            }
        }

        if (textField == null) {
            textField = new DesktopTextField();
        }

        textField.setDatasource(datasource, property);
        MetaProperty metaProperty = textField.getMetaProperty();

        final String maxLength = xmlDescriptor != null ? xmlDescriptor.attributeValue("maxLength") : null;
        if (!StringUtils.isEmpty(maxLength)) {
            ((TextInputField.MaxLengthLimited) textField).setMaxLength(Integer.valueOf(maxLength));
        } else {
            Integer len = (Integer) metaProperty.getAnnotations().get("length");
            if (len != null) {
                ((TextInputField.MaxLengthLimited) textField).setMaxLength(len);
            }
        }

        textField.setVisible(true);
        return textField;
    }

    protected Component createDateField(Datasource datasource, String property, MetaPropertyPath mpp,
                                        Element xmlDescriptor) {
        DesktopDateField dateField = new DesktopDateField();
        dateField.setDatasource(datasource, property);

        MetaProperty metaProperty = mpp.getMetaProperty();
        TemporalType tt = null;
        if (metaProperty != null) {
            if (metaProperty.getRange().asDatatype().equals(Datatypes.get(DateDatatype.NAME))) {
                tt = TemporalType.DATE;
            } else if (metaProperty.getAnnotations() != null) {
                tt = (TemporalType) metaProperty.getAnnotations().get("temporal");
            }
        }

        final String resolution = xmlDescriptor == null ? null : xmlDescriptor.attributeValue("resolution");
        String dateFormat = xmlDescriptor == null ? null : xmlDescriptor.attributeValue("dateFormat");

        DateField.Resolution dateResolution = DateField.Resolution.MIN;

        if (!StringUtils.isEmpty(resolution)) {
            dateResolution = DateField.Resolution.valueOf(resolution);
            dateField.setResolution(dateResolution);
        } else if (tt == TemporalType.DATE) {
            dateField.setResolution(DateField.Resolution.DAY);
        }

        if (dateFormat == null) {
            if (dateResolution == DateField.Resolution.DAY) {
                dateFormat = "msg://dateFormat";
            } else if (dateResolution == DateField.Resolution.MIN) {
                dateFormat = "msg://dateTimeFormat";
            }
        }
        Messages messages = AppBeans.get(Messages.class);

        if (!StringUtils.isEmpty(dateFormat)) {
            if (dateFormat.startsWith("msg://")) {
                dateFormat = messages.getMainMessage(dateFormat.substring(6, dateFormat.length()));
            }
            dateField.setDateFormat(dateFormat);
        } else {
            String formatStr;
            if (tt == TemporalType.DATE) {
                formatStr = messages.getMainMessage("dateFormat");
            } else {
                formatStr = messages.getMainMessage("dateTimeFormat");
            }
            dateField.setDateFormat(formatStr);
        }

        return dateField;
    }

    protected Component createTimeField(Datasource datasource, String property, Element xmlDescriptor) {
        DesktopTimeField timeField = new DesktopTimeField();
        timeField.setDatasource(datasource, property);
        String showSeconds = xmlDescriptor.attributeValue("showSeconds");
        if (Boolean.valueOf(showSeconds)) {
            timeField.setShowSeconds(true);
        }
        return timeField;
    }

    protected Component createEntityField(Datasource datasource, String property, Element xmlDescriptor) {
        PickerField pickerField;

        CollectionDatasource optionsDatasource = getOptionsDatasource(datasource, property);
        if (optionsDatasource == null) {
            pickerField = new DesktopPickerField();
            pickerField.addLookupAction();
            pickerField.addClearAction();
        } else {
            pickerField = new DesktopLookupPickerField();
            ((DesktopLookupPickerField) pickerField).setOptionsDatasource(optionsDatasource);
            if (pickerField.getAction(PickerField.LookupAction.NAME) != null) {
                pickerField.removeAction(pickerField.getAction(PickerField.LookupAction.NAME));
            }
        }

        String captionProperty = xmlDescriptor.attributeValue("captionProperty");
        if (StringUtils.isEmpty(captionProperty)) {
            pickerField.setCaptionMode(CaptionMode.PROPERTY);
            pickerField.setCaptionProperty(captionProperty);
        }

        pickerField.setDatasource(datasource, property);
        return pickerField;
    }

    protected Component createEnumField(Datasource datasource, String property, MetaProperty metaProperty) {
        Map<String, Object> options = new TreeMap<>();
        Enumeration<Enum> enumeration = metaProperty.getRange().asEnumeration();
        for (Enum value : enumeration.getValues()) {
            String caption = AppBeans.get(Messages.class).getMessage(value);
            options.put(caption, value);
        }

        DesktopLookupField lookupField = new DesktopLookupField();
        lookupField.setOptionsMap(options);
        lookupField.setDatasource(datasource, property);
        return lookupField;
    }

    protected Component createUnsupportedField(MetaPropertyPath mpp) {
        DesktopLabel label = new DesktopLabel();
        label.setValue("TODO: " + (mpp != null ? mpp.getRange() : ""));
        return label;
    }

    protected abstract CollectionDatasource getOptionsDatasource(Datasource datasource, String property);
}