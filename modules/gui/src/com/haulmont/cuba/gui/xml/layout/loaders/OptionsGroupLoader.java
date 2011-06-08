/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 05.03.2009 17:49:39
 * $Id$
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.xml.layout.*;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import org.dom4j.Element;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.BooleanUtils;

public class OptionsGroupLoader extends AbstractFieldLoader {
    public OptionsGroupLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    public Component loadComponent(ComponentsFactory factory, Element element, Component parent) throws InstantiationException, IllegalAccessException {
        final OptionsGroup component = (OptionsGroup) super.loadComponent(factory, element, parent);

        String captionProperty = element.attributeValue("captionProperty");
        if (!StringUtils.isEmpty(captionProperty)) {
            component.setCaptionMode(CaptionMode.PROPERTY);
            component.setCaptionProperty(captionProperty);
        }

        return component;
    }

    @Override
    protected void loadDatasource(DatasourceComponent component, Element element) {
        final String multiselect = element.attributeValue("multiselect");
        ((OptionsGroup) component).setMultiSelect(BooleanUtils.toBoolean(multiselect));

        final String datasource = element.attributeValue("optionsDatasource");
        if (!StringUtils.isEmpty(datasource)) {
            final Datasource ds = context.getDsContext().get(datasource);
            ((OptionsGroup) component).setOptionsDatasource((CollectionDatasource) ds);
        }

        super.loadDatasource(component, element);
    }
}
