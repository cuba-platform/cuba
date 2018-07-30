package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.TreeDataGrid;

public class TreeDataGridLoader extends AbstractDataGridLoader<TreeDataGrid> {

    @Override
    protected TreeDataGrid createComponentInternal() {
        return (TreeDataGrid) factory.createComponent(TreeDataGrid.NAME);
    }
}
