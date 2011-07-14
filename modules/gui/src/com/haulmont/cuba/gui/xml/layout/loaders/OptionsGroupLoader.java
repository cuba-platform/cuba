/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 05.03.2009 17:49:39
 * $Id$
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.CaptionMode;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.DatasourceComponent;
import com.haulmont.cuba.gui.components.OptionsGroup;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

public class OptionsGroupLoader extends AbstractFieldLoader {
    public OptionsGroupLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    public Component loadComponent(ComponentsFactory factory, Element element, Component parent) throws InstantiationException, IllegalAccessException {
        final OptionsGroup component = (OptionsGroup) super.loadComponent(factory, element, parent);

        loadOrientation(component, element);
        loadCaptionProperty(component, element);

        return component;
    }

    protected void loadCaptionProperty(OptionsGroup component, Element element) {
        String captionProperty = element.attributeValue("captionProperty");
        if (!StringUtils.isEmpty(captionProperty)) {
            component.setCaptionMode(CaptionMode.PROPERTY);
            component.setCaptionProperty(captionProperty);
        }
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

    protected void loadOrientation(OptionsGroup component, Element element) {
        String orientation = element.attributeValue("orientation");

        if (orientation == null) {
            return;
        }

        if ("horizontal".equalsIgnoreCase(orientation)) {
            component.setOrientation(OptionsGroup.Orientation.HORIZONTAL);
        }
        else if ("vertical".equalsIgnoreCase(orientation)) {
            component.setOrientation(OptionsGroup.Orientation.VERTICAL);
        }
        else {
            throw new IllegalStateException("Invalid orientation value for option group: " + orientation);
        }
    }
}
