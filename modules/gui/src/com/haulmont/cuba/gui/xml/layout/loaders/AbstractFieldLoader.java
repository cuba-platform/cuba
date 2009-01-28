/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 22.12.2008 18:20:37
 * $Id$
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.data.Datasource;
import org.dom4j.Element;
import org.apache.commons.lang.StringUtils;

public class AbstractFieldLoader extends ComponentLoader {
    protected LayoutLoaderConfig config;
    protected ComponentsFactory factory;
    protected DsContext dsContext;

    public AbstractFieldLoader(LayoutLoaderConfig config, ComponentsFactory factory, DsContext dsContext) {
        this.config = config;
        this.factory = factory;
        this.dsContext = dsContext;
    }

    public Component loadComponent(ComponentsFactory factory, Element element) throws InstantiationException, IllegalAccessException {
        final Field field = factory.createComponent(element.getName());

        assignXmlDescriptor(field, element);
        loadId(field, element);
        loadCaption(field, element);

        final String datasource = element.attributeValue("datasource");
        if (!StringUtils.isEmpty(datasource)) {
            final Datasource ds = dsContext.get(datasource);

            final String property = element.attributeValue("property");
            if (StringUtils.isEmpty(property))
                throw new IllegalStateException(
                        String.format(
                                "Can't set assign datasource '%s' for component '%s' due 'property' " +
                                "attribute is not defined",
                                datasource, field.getId()));

            field.setDatasource(ds, property);
        }

        loadHeight(field, element);
        loadWidth(field, element);

        return field;
    }
}