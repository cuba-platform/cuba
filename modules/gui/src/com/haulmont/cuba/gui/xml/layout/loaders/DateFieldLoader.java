/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 10.02.2009 10:40:28
 * $Id$
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.xml.layout.*;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.DateField;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.core.global.MessageProvider;
import org.dom4j.Element;
import org.apache.commons.lang.StringUtils;

import javax.persistence.TemporalType;

public class DateFieldLoader extends AbstractFieldLoader {
    public DateFieldLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    @Override
    public Component loadComponent(ComponentsFactory factory, Element element, Component parent) throws InstantiationException, IllegalAccessException {
        final DateField component = (DateField) super.loadComponent(factory, element, parent);

        TemporalType tt = null;
        if (component.getMetaProperty() != null) {
            tt = (TemporalType) component.getMetaProperty().getAnnotations().get("temporal");
        }

        final String resolution = element.attributeValue("resolution");
        if (!StringUtils.isEmpty(resolution)) {
            component.setResolution(DateField.Resolution.valueOf(resolution));
        } else if (tt == TemporalType.DATE) {
            component.setResolution(DateField.Resolution.DAY);
        }

        String dateFormat = element.attributeValue("dateFormat");
        if (!StringUtils.isEmpty(dateFormat)) {
               if (dateFormat.startsWith("msg://")) {
                dateFormat = MessageProvider.getMessage(
                        AppConfig.getInstance().getMessagesPack(), dateFormat.substring(6, dateFormat.length()));
            }
            component.setDateFormat(dateFormat);
        } else {
            String formatStr;
            if (tt == TemporalType.DATE) {
                formatStr = MessageProvider.getMessage(AppConfig.getInstance().getMessagesPack(), "dateFormat");
            } else {
                formatStr = MessageProvider.getMessage(AppConfig.getInstance().getMessagesPack(), "dateTimeFormat");
            }
            component.setDateFormat(formatStr);
        }

        return component;
    }
}
