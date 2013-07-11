/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

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
    public Component loadComponent(ComponentsFactory factory, Element element, Component parent)
            throws InstantiationException, IllegalAccessException
    {
        Tree component = factory.createComponent("tree");

        assignXmlDescriptor(component, element);
        loadId(component, element);
        loadVisible(component, element);

        loadHeight(component, element);
        loadWidth(component, element);

        loadStyleName(component, element);

        assignFrame(component);

        loadActions(component, element);

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
        if (id == null)
            throw new IllegalStateException("No action id provided");

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