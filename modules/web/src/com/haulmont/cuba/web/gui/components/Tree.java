/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 29.01.2009 13:01:17
 *
 * $Id$
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.web.gui.data.TreeDatasourceWrapper;
import com.itmill.toolkit.data.Property;

import java.util.Set;

public class Tree
    extends
        AbstractListComponent<com.itmill.toolkit.ui.Tree>
    implements
        com.haulmont.cuba.gui.components.Tree, Component.Wrapper
{
    private String hierarchyProperty;

    public Tree() {
        component = new com.itmill.toolkit.ui.Tree();
        component.setMultiSelect(false);
        component.setImmediate(true);

        component.addActionHandler(new ActionsAdapter());
        component.addListener(
                new Property.ValueChangeListener()
                {
                    public void valueChange(Property.ValueChangeEvent event) {
                        Set itemIds = getSelected();
                        if (itemIds.isEmpty()) {
                            datasource.setItem(null);
                        } else if (itemIds.size() == 1) {
                            datasource.setItem(datasource.getItem(itemIds.iterator().next()));
                        } else {
                            throw new UnsupportedOperationException();
                        }
                    }
                }
        );
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

    public String getHierarchyProperty() {
        return hierarchyProperty;
    }

    public CollectionDatasource getDatasource() {
        return datasource;
    }

    public void setDatasource(CollectionDatasource datasource, String showProperty, String hierarchyProperty)
    {
        this.datasource = datasource;
        this.hierarchyProperty = hierarchyProperty != null ? hierarchyProperty : "parent";

        // if showProperty is null, the Tree will use itemId.toString
        MetaProperty metaProperty = showProperty == null ? null : datasource.getMetaClass().getProperty(showProperty);
        component.setItemCaptionPropertyId(metaProperty);

        TreeDatasourceWrapper wrapper =
                new TreeDatasourceWrapper(datasource, hierarchyProperty);
        component.setContainerDataSource(wrapper);
    }
}
