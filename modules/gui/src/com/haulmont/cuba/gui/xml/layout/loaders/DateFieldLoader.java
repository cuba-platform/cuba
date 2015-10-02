/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.chile.core.datatypes.Datatypes;
import com.haulmont.chile.core.datatypes.impl.DateDatatype;
import com.haulmont.cuba.gui.components.DateField;
import org.apache.commons.lang.StringUtils;

import javax.persistence.TemporalType;

/**
 * @author abramov
 * @version $Id$
 */
public class DateFieldLoader extends AbstractFieldLoader<DateField> {
    @Override
    public void createComponent() {
        resultComponent = (DateField) factory.createComponent(DateField.NAME);
        loadId(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        super.loadComponent();

        TemporalType tt = null;
        if (resultComponent.getMetaProperty() != null) {
            if (resultComponent.getMetaProperty().getRange().asDatatype().equals(Datatypes.get(DateDatatype.NAME))) {
                tt = TemporalType.DATE;
            } else if (resultComponent.getMetaProperty().getAnnotations() != null) {
                tt = (TemporalType) resultComponent.getMetaProperty().getAnnotations().get("temporal");
            }
        }

        final String resolution = element.attributeValue("resolution");
        String dateFormat = element.attributeValue("dateFormat");
        String mainDateFormat = null;
        if (StringUtils.isNotEmpty(resolution)) {
            DateField.Resolution res = DateField.Resolution.valueOf(resolution);
            resultComponent.setResolution(res);
            if (dateFormat == null) {
                if (res == DateField.Resolution.DAY) {
                    mainDateFormat = "dateFormat";
                } else if (res == DateField.Resolution.MIN) {
                    mainDateFormat = "dateTimeFormat";
                }
            }
        } else if (tt == TemporalType.DATE) {
            resultComponent.setResolution(DateField.Resolution.DAY);
        }

        String formatStr;
        if (StringUtils.isNotEmpty(dateFormat)) {
            formatStr = loadResourceString(dateFormat);
        } else if (StringUtils.isNotEmpty(mainDateFormat)) {
            formatStr = messages.getMainMessage(mainDateFormat);
        }else {
            if (tt == TemporalType.DATE) {
                formatStr = messages.getMainMessage("dateFormat");
            } else {
                formatStr = messages.getMainMessage("dateTimeFormat");
            }
        }
        resultComponent.setDateFormat(formatStr);
    }
}