/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

/**
 * @author artamonov
 * @version $Id$
 */
public class BulkEditorLoader extends ComponentLoader {

    public BulkEditorLoader(Context context) {
        super(context);
    }

    @Override
    public Component loadComponent(ComponentsFactory factory, Element element, Component parent) {
        final BulkEditor component = factory.createComponent(element.getName());

        assignXmlDescriptor(component, element);
        loadId(component, element);

        loadEnable(component, element);
        loadVisible(component, element);

        loadStyleName(component, element);

        loadCaption(component, element);
        loadDescription(component, element);
        loadIcon(component, element);

        loadWidth(component, element);
        loadAlign(component, element);

        assignFrame(component);

        if (!userSessionSource.getUserSession().isSpecificPermitted(BulkEditor.PERMISSION)) {
            component.setVisible(false);
        }

        String openType = element.attributeValue("openType");
        if (StringUtils.isNotEmpty(openType)) {
            component.setOpenType(WindowManager.OpenType.valueOf(openType));
        }

        String exclude = element.attributeValue("exclude");
        if (StringUtils.isNotBlank(exclude)) {
            component.setExcludePropertiesRegex(exclude);
        }

        final String listComponent = element.attributeValue("for");
        if (StringUtils.isEmpty(listComponent)) {
            throw new GuiDevelopmentException("'for' attribute of bulk editor is not specified",
                    context.getFullFrameId(), "componentId", component.getId());
        }

        context.addPostInitTask(new PostInitTask() {
            @Override
            public void execute(Context context, IFrame window) {
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

        return component;
    }
}