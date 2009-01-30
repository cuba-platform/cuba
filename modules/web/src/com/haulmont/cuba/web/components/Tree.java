/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 29.01.2009 13:01:17
 *
 * $Id$
 */
package com.haulmont.cuba.web.components;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.web.data.TreeDatasourceWrapper;
import com.haulmont.chile.core.model.MetaProperty;
import com.itmill.toolkit.data.Property;

import java.util.Set;

public class Tree
        extends AbstractComponent<com.itmill.toolkit.ui.Tree>
        implements com.haulmont.cuba.gui.components.Tree, Component.Wrapper
{
    protected CollectionDatasource datasource;

    public Tree() {
        component = new com.itmill.toolkit.ui.Tree();
        component.setMultiSelect(false);
        component.setImmediate(true);
        component.addListener(
                new Property.ValueChangeListener()
                {
                    public void valueChange(Property.ValueChangeEvent event) {
                        Object itemId = getSelected();
                        if (itemId == null) {
                            datasource.setItem(null);
                        }
                        else {
                            datasource.setItem(datasource.getItem(itemId));
                        }
                    }
                }
        );
    }

    public <T> T getSelected() {
        return (T) component.getValue();
    }

    public void expandTree() {
        com.itmill.toolkit.data.Container.Hierarchical container =
                (com.itmill.toolkit.data.Container.Hierarchical) component.getContainerDataSource();
        if (container != null) {
            for (Object id : container.rootItemIds()) {
                component.expandItemsRecursively(id);
            }
        }
    }

    public CollectionDatasource getDatasource() {
        return datasource;
    }

    public void setDatasource(CollectionDatasource datasource, String showProperty, String parentProperty)
    {
        this.datasource = datasource;
        parentProperty = parentProperty != null ? parentProperty : "parent";

        // if showProperty is null, the Tree will use itemId.toString
        MetaProperty metaProperty = showProperty == null ? null : datasource.getMetaClass().getProperty(showProperty);
        component.setItemCaptionPropertyId(metaProperty);

        TreeDatasourceWrapper wrapper =
                new TreeDatasourceWrapper(datasource, parentProperty);
        component.setContainerDataSource(wrapper);
    }
}
