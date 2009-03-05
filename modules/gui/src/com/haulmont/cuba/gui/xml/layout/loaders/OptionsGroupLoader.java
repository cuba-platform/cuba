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
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.components.OptionsGroup;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import org.dom4j.Element;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.BooleanUtils;

public class OptionsGroupLoader extends AbstractFieldLoader {
    public OptionsGroupLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    public Component loadComponent(ComponentsFactory factory, Element element) throws InstantiationException, IllegalAccessException {
        final OptionsGroup component = (OptionsGroup) super.loadComponent(factory, element);

        final String multiselect = element.attributeValue("multiselect");
        component.setMultiSelect(BooleanUtils.toBoolean(multiselect));

        String captionProperty = element.attributeValue("captionProperty");
        if (!StringUtils.isEmpty(captionProperty)) {
            component.setCaptionMode(LookupField.CaptionMode.PROPERTY);
            component.setCaptionProperty(captionProperty);
        }

        return component;
    }

    @Override
    protected void loadDatasource(Field component, Element element) {
        final String datasource = element.attributeValue("optionsDatasource");
        if (!StringUtils.isEmpty(datasource)) {
            final Datasource ds = context.getDSContext().get(datasource);
            ((OptionsGroup) component).setOptionsDatasource((CollectionDatasource) ds);
        }

        super.loadDatasource(component, element);
    }
}
