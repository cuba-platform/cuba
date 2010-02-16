/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Maksim Tulupov
 * Created: 15.02.2010 11:12:07
 *
 * $Id$
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.DateField;
import com.haulmont.cuba.gui.components.RuntimePropertyGridLayout;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import com.haulmont.cuba.gui.AppConfig;
import com.haulmont.cuba.core.global.MessageProvider;
import com.haulmont.chile.core.datatypes.impl.DateDatatype;
import com.haulmont.chile.core.datatypes.Datatypes;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.util.List;

public class RuntimePropertyGridLayoutLoader extends GridLayoutLoader {

    public RuntimePropertyGridLayoutLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    public Component loadComponent(ComponentsFactory factory, Element element, Component parent)
            throws InstantiationException, IllegalAccessException {
        final RuntimePropertyGridLayout component = factory.createComponent("runtimepropertygrid");

        loadId(component, element);
        loadVisible(component, element);

        loadStyleName(component, element);

        final Element columnsElement = element.element("columns");

        if (columnsElement != null) {
            final List<Element> columnElements = columnsElement.elements("column");
            component.setColumns(columnElements.size());
            int i = 0;
            for (Element columnElement : columnElements) {
                final String flex = columnElement.attributeValue("flex");
                if (!StringUtils.isEmpty(flex)) {
                    component.setColumnExpandRatio(i, Float.parseFloat(flex));
                }
                i++;
            }
        } else {
            component.setColumns(2);
        }

        loadSpacing(component, element);
        loadMargin(component, element);

        loadWidth(component, element);
        loadHeight(component, element);

        loadExpandable(component, element);
        loadDatasource(component, element);
        loadAttributeProperty(component, element);
        loadComponentWidth(component, element);
        loadDateFormat(component, element);

        final String resolution = element.attributeValue("resolution");
        if (!StringUtils.isEmpty(resolution)) {
            component.setResolution(DateField.Resolution.valueOf(resolution));
        }

        assignFrame(component);

        return component;
    }

    protected void loadDatasource(RuntimePropertyGridLayout propertyGridLayout, Element element) {
        String s = element.attributeValue("datasource");
        if (!StringUtils.isEmpty(s)) {
            CollectionDatasource ds = context.getDsContext().get(s);
            propertyGridLayout.setAttributesDs(ds);
        }
    }

    protected void loadAttributeProperty(RuntimePropertyGridLayout propertyGridLayout, Element element) {
        String s = element.attributeValue("attributeProperty");
        if (!StringUtils.isEmpty(s)) {
            propertyGridLayout.setAttributeProperty(s);
        }
    }

    protected void loadComponentWidth(RuntimePropertyGridLayout propertyGridLayout, Element element) {
        String s = element.attributeValue("innerComponentWidth");
        if (!StringUtils.isEmpty(s)) {
            propertyGridLayout.setInnerComponentWidth(s);
        }
    }

    protected void loadDateFormat(RuntimePropertyGridLayout propertyGridLayout, Element element) {
        String dateFormat = element.attributeValue("dateFormat");
        if (!StringUtils.isEmpty(dateFormat)) {
            if (dateFormat.startsWith("msg://")) {
                dateFormat = MessageProvider.getMessage(
                        AppConfig.getInstance().getMessagesPack(), dateFormat.substring(6, dateFormat.length()));
            }
            propertyGridLayout.setDateFormat(dateFormat);
        } else {
            DateDatatype dateDatatype = Datatypes.getInstance().get(DateDatatype.NAME);
            if (dateDatatype.getFormatPattern() != null)
                propertyGridLayout.setDateFormat(dateDatatype.getFormatPattern());
        }
    }
}
