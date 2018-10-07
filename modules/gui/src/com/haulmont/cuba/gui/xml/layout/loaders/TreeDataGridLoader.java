package com.haulmont.cuba.gui.xml.layout.loaders;

import com.google.common.base.Strings;
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.TreeDataGrid;
import com.haulmont.cuba.gui.components.data.DataGridItems;
import com.haulmont.cuba.gui.components.data.datagrid.ContainerTreeDataGridItems;
import com.haulmont.cuba.gui.model.CollectionContainer;

public class TreeDataGridLoader extends AbstractDataGridLoader<TreeDataGrid> {

    @Override
    protected TreeDataGrid createComponentInternal() {
        return factory.create(TreeDataGrid.NAME);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected DataGridItems createContainerDataGridSource(CollectionContainer container) {
        String hierarchyProperty = element.attributeValue("hierarchyProperty");
        if (Strings.isNullOrEmpty(hierarchyProperty)) {
            throw new GuiDevelopmentException("TreeDataGrid doesn't have 'hierarchyProperty' attribute", context.getCurrentFrameId(),
                    "TreeDataGrid ID", element.attributeValue("id"));
        }
        return new ContainerTreeDataGridItems(container, hierarchyProperty);
    }
}
