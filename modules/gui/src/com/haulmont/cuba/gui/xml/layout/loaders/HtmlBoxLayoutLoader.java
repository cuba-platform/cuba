/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.HtmlBoxLayout;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

/**
 * @author gorodnov
 * @version $Id$
 */
public class HtmlBoxLayoutLoader extends ContainerLoader<HtmlBoxLayout> {

    @Override
    public void createComponent() {
        resultComponent = (HtmlBoxLayout) factory.createComponent(HtmlBoxLayout.NAME);
        loadId(resultComponent, element);
        createSubComponents(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        assignXmlDescriptor(resultComponent, element);
        assignFrame(resultComponent);

        loadVisible(resultComponent, element);
        loadEnable(resultComponent, element);

        loadStyleName(resultComponent, element);

        loadAlign(resultComponent, element);

        loadTemplate(resultComponent, element);

        loadHeight(resultComponent, element);
        loadWidth(resultComponent, element);
    }

    protected void loadTemplate(HtmlBoxLayout htmlBox, Element element) {
        String template = element.attributeValue("template");
        if (!StringUtils.isEmpty(template)) {
            htmlBox.setTemplateName(template);
            return;
        }
        throw new GuiDevelopmentException("'template' attribute is required", context.getFullFrameId());
    }
}