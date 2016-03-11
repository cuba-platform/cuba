/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Label;
import org.apache.commons.lang.StringUtils;

/**
 * @author abramov
 * @version $Id$
 */
public class LabelLoader extends AbstractDatasourceComponentLoader<Label> {
    @Override
    public void createComponent() {
        resultComponent = (Label) factory.createComponent(Label.NAME);
        loadId(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        assignXmlDescriptor(resultComponent, element);
        assignFrame(resultComponent);

        loadDatasource(resultComponent, element);

        loadVisible(resultComponent, element);
        loadAlign(resultComponent, element);
        loadStyleName(resultComponent, element);

        String htmlEnabled = element.attributeValue("htmlEnabled");
        if (StringUtils.isNotEmpty(htmlEnabled)) {
            resultComponent.setHtmlEnabled(Boolean.parseBoolean(htmlEnabled));
        }

        String caption = element.attributeValue("value");
        if (StringUtils.isNotEmpty(caption)) {
            caption = loadResourceString(caption);
            resultComponent.setValue(caption);
        }
        
        loadCaption(resultComponent, element);
        loadDescription(resultComponent, element);

        loadWidth(resultComponent, element, Component.AUTO_SIZE);
        loadHeight(resultComponent, element, Component.AUTO_SIZE);

        resultComponent.setFormatter(loadFormatter(element));
    }
}