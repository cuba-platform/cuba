/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 03.08.2010 17:10:03
 *
 * $Id$
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.CaptionMode;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.WidgetsTree;
import com.haulmont.cuba.gui.data.HierarchicalDatasource;
import com.haulmont.cuba.web.gui.data.HierarchicalDsWrapper;

public class WebWidgetsTree
        extends
            WebAbstractList<com.haulmont.cuba.web.toolkit.ui.WidgetsTree>
        implements
            WidgetsTree, Component.Wrapper
{

    private String hierarchyProperty;

    public WebWidgetsTree() {
        component = new com.haulmont.cuba.web.toolkit.ui.WidgetsTree();
        component.setSelectable(false);
        component.setImmediate(true); 
    }

    public String getHierarchyProperty() {
        return hierarchyProperty;
    }

    public void setDatasource(HierarchicalDatasource datasource) {
        this.datasource = datasource;
        this.hierarchyProperty = datasource.getHierarchyPropertyName();

        HierarchicalDsWrapper wrapper = new HierarchicalDsWrapper(datasource);
        component.setContainerDataSource(wrapper);
    }

    public void expandTree() {
        com.vaadin.data.Container.Hierarchical container =
                (com.vaadin.data.Container.Hierarchical) component.getContainerDataSource();
        if (container != null) {
            for (Object id : container.rootItemIds()) {
                component.expandItemsRecursively(id);
            }
        }
    }

    public void collapseTree() {
        com.vaadin.data.Container.Hierarchical container =
                (com.vaadin.data.Container.Hierarchical) component.getContainerDataSource();
        if (container != null) {
            for (Object id : container.rootItemIds()) {
                component.collapseItemsRecursively(id);
            }
        }
    }

    public boolean isExpanded(Object itemId) {
        return component.isExpanded(itemId);
    }

    public void expand(Object itemId) {
        component.expandItem(itemId);
    }

    public void collapse(Object itemId) {
        component.collapseItem(itemId);
    }

    public void setWidgetBuilder(final WidgetBuilder widgetBuilder) {
        if (widgetBuilder != null) {
            component.setWidgetBuilder(new com.haulmont.cuba.web.toolkit.ui.WidgetsTree.WidgetBuilder() {
                public com.vaadin.ui.Component buildWidget(
                        com.haulmont.cuba.web.toolkit.ui.WidgetsTree source,
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

    public CaptionMode getCaptionMode() {
        return null;
    }

    public void setCaptionMode(CaptionMode captionMode) {
        //do nothing
    }

    public String getCaptionProperty() {
        return null;
    }

    public void setCaptionProperty(String captionProperty) {
        //do nothing
    }
}
