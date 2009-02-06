/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 03.02.2009 11:47:37
 * $Id$
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.data.DsContext;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.LookupField;
import org.dom4j.Element;
import org.apache.commons.lang.StringUtils;

public class LookupFieldLoader extends AbstractFieldLoader {
    public LookupFieldLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    @Override
    public Component loadComponent(ComponentsFactory factory, Element element) throws InstantiationException, IllegalAccessException {
        final LookupField component = (LookupField) super.loadComponent(factory, element);
        
        final String datasource = element.attributeValue("lookupDatasource");
        if (!StringUtils.isEmpty(datasource)) {
            final Datasource ds = context.getDSContext().get(datasource);
            component.setLookupDatasource((CollectionDatasource) ds);
        }

        return component;
    }
}
