package com.haulmont.cuba.gui.xml.layout.loaders;

import com.google.common.base.Strings;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.TreeDataGrid;
import com.haulmont.cuba.gui.components.data.DataGridItems;
import com.haulmont.cuba.gui.components.data.datagrid.ContainerTreeDataGridItems;
import com.haulmont.cuba.gui.components.data.datagrid.EmptyTreeDataGridItems;
import com.haulmont.cuba.gui.model.CollectionContainer;
import org.dom4j.Element;

public class TreeDataGridLoader extends AbstractDataGridLoader<TreeDataGrid> {

    @Override
    protected TreeDataGrid createComponentInternal() {
        return factory.create(TreeDataGrid.NAME);
    }

    @Override
    public void loadComponent() {
        super.loadComponent();

        loadHierarchyColumn(resultComponent, element);
    }

    protected void loadHierarchyColumn(TreeDataGrid component, Element element) {
        String hierarchyColumn = element.attributeValue("hierarchyColumn");
        if (!Strings.isNullOrEmpty(hierarchyColumn)) {
            component.setHierarchyColumn(hierarchyColumn);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected DataGridItems createContainerDataGridSource(CollectionContainer container) {
        String hierarchyProperty = element.attributeValue("hierarchyProperty");
        if (Strings.isNullOrEmpty(hierarchyProperty)) {
            throw new GuiDevelopmentException("TreeDataGrid doesn't have 'hierarchyProperty' attribute",
                    context, "TreeDataGrid ID", element.attributeValue("id"));
        }
        return new ContainerTreeDataGridItems(container, hierarchyProperty);
    }

    @Override
    protected DataGridItems createEmptyDataGridItems(MetaClass metaClass) {
        return new EmptyTreeDataGridItems(metaClass);
    }
}
