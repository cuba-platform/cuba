/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.WindowManager.OpenType;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.ListComponent;
import com.haulmont.cuba.gui.components.RelatedEntities;
import com.haulmont.cuba.gui.config.WindowConfig;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

/**
 * @author artamonov
 */
public class RelatedEntitiesLoader extends AbstractComponentLoader<RelatedEntities> {

    @Override
    public void createComponent() {
        resultComponent = (RelatedEntities) factory.createComponent(RelatedEntities.NAME);
        loadId(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        assignFrame(resultComponent);

        loadCaption(resultComponent, element);
        loadWidth(resultComponent, element);

        loadStyleName(resultComponent, element);
        loadEnable(resultComponent, element);
        loadVisible(resultComponent, element);
        loadAlign(resultComponent, element);

        String openType = element.attributeValue("openType");
        if (StringUtils.isNotEmpty(openType)) {
            resultComponent.setOpenType(OpenType.valueOf(openType));
        }

        String exclude = element.attributeValue("exclude");
        if (StringUtils.isNotBlank(exclude)) {
            resultComponent.setExcludePropertiesRegex(exclude);
        }

        for (Object routeObject : element.elements("property")) {
            Element routeElement = (Element) routeObject;

            String property = routeElement.attributeValue("name");
            if (StringUtils.isEmpty(property)) {
                throw new GuiDevelopmentException("Name attribute for related entities property is not specified",
                        context.getFullFrameId(), "componentId", resultComponent.getId());
            }

            String caption = loadResourceString(routeElement.attributeValue("caption"));
            String filterCaption = loadResourceString(routeElement.attributeValue("filterCaption"));
            String screen = routeElement.attributeValue("screen");

            if (StringUtils.isNotEmpty(screen)) {
                WindowConfig windowConfig = AppBeans.get(WindowConfig.NAME);
                if (windowConfig.findWindowInfo(screen) == null) {
                    throw new GuiDevelopmentException("Screen for custom route in related entities not found",
                            context.getFullFrameId(), "componentId", resultComponent.getId());
                }
            }

            resultComponent.addPropertyOption(property, screen, caption, filterCaption);
        }

        String listComponent = element.attributeValue("for");
        if (StringUtils.isEmpty(listComponent)) {
            throw new GuiDevelopmentException("for' attribute of related entities is not specified",
                    context.getFullFrameId(), "componentId", resultComponent.getId());
        }

        context.addPostInitTask((context1, window) -> {
            if (resultComponent.getListComponent() == null) {
                Component bindComponent = resultComponent.getFrame().getComponent(listComponent);
                if (!(bindComponent instanceof ListComponent)) {
                    throw new GuiDevelopmentException("Specify 'for' attribute: id of table or tree",
                            context1.getFullFrameId(), "componentId", resultComponent.getId());
                }

                resultComponent.setListComponent((ListComponent) bindComponent);
            }
        });
    }
}