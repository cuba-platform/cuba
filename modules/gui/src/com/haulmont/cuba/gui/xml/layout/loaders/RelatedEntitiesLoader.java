/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Frame;
import com.haulmont.cuba.gui.components.ListComponent;
import com.haulmont.cuba.gui.components.RelatedEntities;
import com.haulmont.cuba.gui.config.WindowConfig;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

/**
 * @author artamonov
 * @version $Id$
 */
public class RelatedEntitiesLoader extends ComponentLoader {

    public RelatedEntitiesLoader(Context context) {
        super(context);
    }

    @Override
    public Component loadComponent(ComponentsFactory factory, Element element, Component parent) {
        final RelatedEntities component = (RelatedEntities) factory.createComponent(RelatedEntities.NAME);

        initComponent(component, element, parent);

        return component;
    }

    protected void initComponent(final RelatedEntities component, Element element, Component parent) {
        loadId(component, element);
        loadCaption(component, element);
        loadWidth(component, element);

        loadStyleName(component, element);
        loadEnable(component, element);
        loadVisible(component, element);
        loadAlign(component, element);

        assignFrame(component);

        String openType = element.attributeValue("openType");
        if (StringUtils.isNotEmpty(openType)) {
            component.setOpenType(WindowManager.OpenType.valueOf(openType));
        }

        String exclude = element.attributeValue("exclude");
        if (StringUtils.isNotBlank(exclude)) {
            component.setExcludePropertiesRegex(exclude);
        }

        for (Object routeObject : element.elements("property")) {
            Element routeElement = (Element) routeObject;

            String property = routeElement.attributeValue("name");
            if (StringUtils.isEmpty(property)) {
                throw new GuiDevelopmentException("Name attribute for related entities property is not specified",
                        context.getFullFrameId(), "componentId", component.getId());
            }

            String caption = loadResourceString(routeElement.attributeValue("caption"));
            String filterCaption = loadResourceString(routeElement.attributeValue("filterCaption"));
            String screen = routeElement.attributeValue("screen");

            if (StringUtils.isNotEmpty(screen)) {
                WindowConfig windowConfig = AppBeans.get(WindowConfig.NAME);
                if (windowConfig.findWindowInfo(screen) == null) {
                    throw new GuiDevelopmentException("Screen for custom route in related entities not found",
                            context.getFullFrameId(), "componentId", component.getId());
                }
            }

            component.addPropertyOption(property, screen, caption, filterCaption);
        }

        final String listComponent = element.attributeValue("for");
        if (StringUtils.isEmpty(listComponent)) {
            throw new GuiDevelopmentException("for' attribute of related entities is not specified",
                    context.getFullFrameId(), "componentId", component.getId());
        }

        context.addPostInitTask(new PostInitTask() {
            @Override
            public void execute(Context context, Frame window) {
                if (component.getListComponent() == null) {
                    Component bindComponent = component.getFrame().getComponent(listComponent);
                    if (!(bindComponent instanceof ListComponent)) {
                        throw new GuiDevelopmentException("Specify 'for' attribute: id of table or tree",
                                context.getFullFrameId(), "componentId", component.getId());
                    }

                    component.setListComponent((ListComponent) bindComponent);
                }
            }
        });
    }
}