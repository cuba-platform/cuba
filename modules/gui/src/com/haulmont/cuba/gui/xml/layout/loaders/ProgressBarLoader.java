/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.ProgressBar;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

/**
 * @author Alexander Budarov
 * @version $Id$
 */
public class ProgressBarLoader extends ComponentLoader {

    public ProgressBarLoader(Context context) {
        super(context);
    }

    @Override
    public Component loadComponent(ComponentsFactory factory, Element element, Component parent) {
        final ProgressBar component = (ProgressBar) factory.createComponent(element.getName());

        initComponent(component, element, parent);

        return component;
    }

    protected void initComponent(ProgressBar component, Element element, Component parent) {
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