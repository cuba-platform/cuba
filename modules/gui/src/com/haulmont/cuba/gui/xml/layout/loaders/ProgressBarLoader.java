/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.ProgressBar;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

/**
 * @author Alexander Budarov
 * @version $Id$
 */
public class ProgressBarLoader extends AbstractComponentLoader<ProgressBar> {
    @Override
    public void createComponent() {
        resultComponent = (ProgressBar) factory.createComponent(ProgressBar.NAME);
        loadId(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        assignXmlDescriptor(resultComponent, element);
        assignFrame(resultComponent);

        loadVisible(resultComponent, element);
        loadEditable(resultComponent, element);
        loadEnable(resultComponent, element);

        loadStyleName(resultComponent, element);

        loadHeight(resultComponent, element);
        loadWidth(resultComponent, element);
        loadAlign(resultComponent, element);

        loadIndeterminate(resultComponent, element);
    }

    protected void loadIndeterminate(ProgressBar component, Element element) {
        String indeterminate = element.attributeValue("indeterminate");
        if (StringUtils.isNotEmpty(indeterminate)) {
            component.setIndeterminate(Boolean.parseBoolean(indeterminate));
        }
    }
}