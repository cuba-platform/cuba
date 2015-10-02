/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Link;
import org.apache.commons.lang.StringUtils;

/**
 * @author abramov
 * @version $Id$
 */
public class LinkLoader extends AbstractComponentLoader<Link> {
    @Override
    public void createComponent() {
        resultComponent = (Link) factory.createComponent(Link.NAME);
        loadId(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        assignXmlDescriptor(resultComponent, element);
        assignFrame(resultComponent);

        loadVisible(resultComponent, element);
        loadAlign(resultComponent, element);
        loadStyleName(resultComponent, element);
        loadDescription(resultComponent, element);
        loadCaption(resultComponent, element);

        String url = element.attributeValue("url");
        if (StringUtils.isNotEmpty(url)) {
            resultComponent.setUrl(url);
        }

        String target = element.attributeValue("target");
        if (StringUtils.isNotEmpty(target)) {
            resultComponent.setTarget(target);
        }

        String icon = element.attributeValue("icon");
        if (StringUtils.isNotEmpty(icon)) {
            resultComponent.setIcon(icon);
        }

        loadWidth(resultComponent, element, Component.AUTO_SIZE);
        loadHeight(resultComponent, element, Component.AUTO_SIZE);
    }
}