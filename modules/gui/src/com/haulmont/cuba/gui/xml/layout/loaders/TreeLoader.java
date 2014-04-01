/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.ListActionType;
import com.haulmont.cuba.gui.data.HierarchicalDatasource;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;
import com.haulmont.cuba.gui.xml.layout.LayoutLoaderConfig;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

/**
 * @author krivopustov
 * @version $Id$
 */
public class TreeLoader extends ComponentLoader {

    protected ComponentsFactory factory;
    protected LayoutLoaderConfig config;

    public TreeLoader(Context context, LayoutLoaderConfig config, ComponentsFactory factory) {
        super(context);
        this.config = config;
        this.factory = factory;
    }

    @Override
    public Component loadComponent(ComponentsFactory factory, Element element, Component parent) {
        Tree component = factory.createComponent("tree");

        assignXmlDescriptor(component, element);
        loadId(component, element);
        loadVisible(component, element);

        loadEnable(component, element);
        loadEditable(component, element);

        loadHeight(component, element);
        loadWidth(component, element);

        loadStyleName(component, element);

        assignFrame(component);

        loadActions(component, element);

        String multiselect = element.attributeValue("multiselect");
        if (StringUtils.isNotEmpty(multiselect)) {
            component.setMultiSelect(Boolean.valueOf(multiselect));
        }

        Element itemsElem = element.element("treechildren");
        String datasource = itemsElem.attributeValue("datasource");
        if (!StringUtils.isBlank(datasource)) {
            HierarchicalDatasource ds = context.getDsContext().get(datasource);
            component.setDatasource(ds);

            String captionProperty = itemsElem.attributeValue("captionProperty");
            if (!StringUtils.isEmpty(captionProperty)) {
                component.setCaptionProperty(captionProperty);
                component.setCaptionMode(CaptionMode.PROPERTY);
            }
        }

        return component;
    }

    @Override
    protected Action loadDeclarativeAction(Component.ActionsHolder actionsHolder, Element element) {
        String id = element.attributeValue("id");
        if (id == null) {
            Element component = element;
            for (int i = 0; i < 2; i++) {
                if (component.getParent() != null)
                    component = component.getParent();
                else
                    throw new GuiDevelopmentException("No action ID provided", context.getFullFrameId());
            }
            throw new GuiDevelopmentException("No action ID provided", context.getFullFrameId(),
                    "Tree ID", component.attributeValue("id"));
        }

        if (StringUtils.isBlank(element.attributeValue("invoke"))) {
            // Try to create a standard list action
            for (ListActionType type : ListActionType.values()) {
                if (type.getId().equals(id)) {
                    Action action = type.createAction((ListComponent) actionsHolder);
                    actionsHolder.addAction(action);
                    return action;
                }
            }
        }

        return super.loadDeclarativeAction(actionsHolder, element);
    }
}