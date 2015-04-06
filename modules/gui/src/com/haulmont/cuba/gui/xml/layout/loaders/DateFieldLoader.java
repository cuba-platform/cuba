/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.datatypes.impl.DateDatatype;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.DateField;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import javax.persistence.TemporalType;

/**
 * @author abramov
 * @version $Id$
 */
public class DateFieldLoader extends AbstractFieldLoader {
    public DateFieldLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    @Override
    protected void initComponent(Field field, Element element, Component parent) {
        super.initComponent(field, element, parent);

        DateField component = (DateField) field;

        TemporalType tt = null;
        if (component.getMetaProperty() != null) {
            if (component.getMetaProperty().getRange().asDatatype().equals(Datatypes.get(DateDatatype.NAME))) {
                tt = TemporalType.DATE;
            } else if (component.getMetaProperty().getAnnotations() != null) {
                tt = (TemporalType) component.getMetaProperty().getAnnotations().get("temporal");
            }
        }

        final String resolution = element.attributeValue("resolution");
        String dateFormat = element.attributeValue("dateFormat");
        if (StringUtils.isNotEmpty(resolution)) {
            DateField.Resolution res = DateField.Resolution.valueOf(resolution);
            component.setResolution(res);
            if (dateFormat == null) {
                if (res == DateField.Resolution.DAY) {
                    dateFormat = "msg://dateFormat";
                } else if (res == DateField.Resolution.MIN) {
                    dateFormat = "msg://dateTimeFormat";
                }
            }
        } else if (tt == TemporalType.DATE) {
            component.setResolution(DateField.Resolution.DAY);
        }

        if (StringUtils.isNotEmpty(dateFormat)) {
            dateFormat = loadResourceString(dateFormat);
            component.setDateFormat(dateFormat);
        } else {
            String formatStr;
            if (tt == TemporalType.DATE) {
                formatStr = messages.getMainMessage("dateFormat");
            } else {
                formatStr = messages.getMainMessage("dateTimeFormat");
            }
            component.setDateFormat(formatStr);
        }
    }
}