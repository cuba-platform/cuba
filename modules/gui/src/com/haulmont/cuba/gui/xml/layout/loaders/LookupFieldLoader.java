/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 03.02.2009 11:47:37
 * $Id$
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.CaptionMode;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

public class LookupFieldLoader extends AbstractFieldLoader {
    public LookupFieldLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    @Override
    public Component loadComponent(ComponentsFactory factory, Element element) throws InstantiationException, IllegalAccessException {
        final LookupField component = (LookupField) super.loadComponent(factory, element);

        String captionProperty = element.attributeValue("captionProperty");
        if (!StringUtils.isEmpty(captionProperty)) {
            component.setCaptionMode(CaptionMode.PROPERTY);
            component.setCaptionProperty(captionProperty);
        }

        String nullName = element.attributeValue("nullName");
        if (!StringUtils.isEmpty(captionProperty)) {
            nullName = loadResourceString(nullName);
            component.setNullName(nullName);
        }

        return component;
    }

    @Override
    protected void loadDatasource(Field component, Element element) {
        final String datasource = element.attributeValue("optionsDatasource");
        if (!StringUtils.isEmpty(datasource)) {
            final Datasource ds = context.getDSContext().get(datasource);
            ((LookupField) component).setOptionsDatasource((CollectionDatasource) ds);
        }

        super.loadDatasource(component, element);
    }
}
