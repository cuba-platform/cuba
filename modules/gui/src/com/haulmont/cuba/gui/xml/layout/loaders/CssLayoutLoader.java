/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.CssLayout;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

/**
 * @author petunin
 */
public class CssLayoutLoader extends ContainerLoader<CssLayout> {
    @Override
    public void createComponent() {
        resultComponent = (CssLayout) factory.createComponent(CssLayout.NAME);
        loadId(resultComponent, element);
        createSubComponents(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        assignXmlDescriptor(resultComponent, element);
        assignFrame(resultComponent);

        loadId(resultComponent, element);
        loadEnable(resultComponent, element);
        loadVisible(resultComponent, element);

        loadStyleName(resultComponent, element);

        loadResponsive(resultComponent, element);

        loadHeight(resultComponent, element);
        loadWidth(resultComponent, element);

        loadSubComponents();
    }

    protected void loadResponsive(CssLayout component, Element element) {
        String responsive = element.attributeValue("responsive");
        if (StringUtils.isNotEmpty(responsive)) {
            component.setResponsive(BooleanUtils.toBoolean(element.attributeValue("responsive")));
        }
    }
}
