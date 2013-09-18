/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.CaptionMode;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.WidgetsTree;
import com.haulmont.cuba.gui.data.HierarchicalDatasource;
import com.haulmont.cuba.web.gui.data.HierarchicalDsWrapper;
import com.haulmont.cuba.web.toolkit.ui.CubaWidgetsTree;

/**
 * @author gorodnov
 * @version $Id$
 */
public class WebWidgetsTree
        extends
            WebAbstractList<CubaWidgetsTree>
        implements
            WidgetsTree, Component.Wrapper {

    protected String hierarchyProperty;

    public WebWidgetsTree() {
        component = new CubaWidgetsTree();
        component.setSelectable(false);
        component.setImmediate(true); 
    }

    @Override
    public String getHierarchyProperty() {
        return hierarchyProperty;
    }

    @Override
    public void setDatasource(HierarchicalDatasource datasource) {
        this.datasource = datasource;
        this.hierarchyProperty = datasource.getHierarchyPropertyName();

        HierarchicalDsWrapper wrapper = new HierarchicalDsWrapper(datasource);
        component.setContainerDataSource(wrapper);

        for (Action action : getActions()) {
            action.refreshState();
        }
    }

    @Override
    public void expandTree() {
        com.vaadin.data.Container.Hierarchical container =
                (com.vaadin.data.Container.Hierarchical) component.getContainerDataSource();
        if (container != null) {
            for (Object id : container.rootItemIds()) {
                component.expandItemsRecursively(id);
            }
        }
    }

    @Override
    public void collapseTree() {
        com.vaadin.data.Container.Hierarchical container =
                (com.vaadin.data.Container.Hierarchical) component.getContainerDataSource();
        if (container != null) {
            for (Object id : container.rootItemIds()) {
                component.collapseItemsRecursively(id);
            }
        }
    }

    @Override
    public boolean isExpanded(Object itemId) {
        return component.isExpanded(itemId);
    }

    @Override
    public void expand(Object itemId) {
        component.expandItem(itemId);
    }

    @Override
    public void collapse(Object itemId) {
        component.collapseItem(itemId);
    }

    @Override
    public void setWidgetBuilder(final WidgetBuilder widgetBuilder) {
        if (widgetBuilder != null) {
            component.setWidgetBuilder(new CubaWidgetsTree.WidgetBuilder() {
                @Override
                public com.vaadin.ui.Component buildWidget(
                        CubaWidgetsTree source,
                        Object itemId,
                        boolean leaf
                ) {
                    Component widget = widgetBuilder.build((HierarchicalDatasource) datasource, itemId, leaf);
                    return WebComponentsHelper.unwrap(widget);
                }
            });
        } else {
            component.setWidgetBuilder(null);
        }
    }

    @Override
    public CaptionMode getCaptionMode() {
        return null;
    }

    @Override
    public void setCaptionMode(CaptionMode captionMode) {
        //do nothing
    }

    @Override
    public String getCaptionProperty() {
        return null;
    }

    @Override
    public void setCaptionProperty(String captionProperty) {
        //do nothing
    }

    @Override
    public boolean isEditable() {
        return !component.isReadOnly();
    }

    @Override
    public void setEditable(boolean editable) {
        component.setReadOnly(!editable);
    }
}