/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Field;
import com.haulmont.cuba.gui.components.ProgressBar;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

/**
 * @author Alexander Budarov
 * @version $Id$
 */
public class ProgressBarLoader extends AbstractFieldLoader {

    public ProgressBarLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context, config, factory);
    }

    @Override
    protected void initComponent(Field field, Element element, Component parent) {
        ProgressBar component = (ProgressBar) field;

        assignXmlDescriptor(component, element);
        loadId(component, element);

        loadVisible(component, element);
        loadEditable(component, element);
        loadEnable(component, element);

        loadStyleName(component, element);

        loadHeight(component, element);
        loadWidth(component, element);
        loadAlign(component, element);

        loadIndeterminate(component, element);

        assignFrame(component);
    }

    private void loadIndeterminate(ProgressBar component, Element element) {
        final String indeterminate = element.attributeValue("indeterminate");
        if (!StringUtils.isEmpty(indeterminate)) {
            component.setIndeterminate(BooleanUtils.toBoolean(indeterminate));
        }
    }
}
