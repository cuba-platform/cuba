/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

/**
 * @author abramov
 * @version $Id$
 */
public class OptionsGroupLoader extends AbstractFieldLoader {

    public OptionsGroupLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    @Override
    protected void initComponent(Field field, Element element, Component parent) {
        super.initComponent(field, element, parent);

        OptionsGroup component = (OptionsGroup) field;

        loadOrientation(component, element);
        loadCaptionProperty(component, element);
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
        } else if ("vertical".equalsIgnoreCase(orientation)) {
            component.setOrientation(OptionsGroup.Orientation.VERTICAL);
        } else {
            throw new GuiDevelopmentException("Invalid orientation value for option group: " +
                    orientation, context.getFullFrameId(), "OptionsGroup ID", component.getId());
        }
    }
}
