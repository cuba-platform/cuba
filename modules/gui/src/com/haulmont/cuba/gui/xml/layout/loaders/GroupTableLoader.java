/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.cuba.gui.components.GroupTable;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gorodnov
 * @version $Id$
 */
public class GroupTableLoader extends AbstractTableLoader<GroupTable> {
    @Override
    public void createComponent() {
        resultComponent = (GroupTable) factory.createComponent(GroupTable.NAME);
        loadId(resultComponent, element);
        createButtonsPanel(resultComponent, element);
    }

    @Override
    public void loadComponent() {
        super.loadComponent();

        String fixedGroupingString = element.attributeValue("fixedGrouping");
        if (StringUtils.isNotEmpty(fixedGroupingString)) {
            resultComponent.setFixedGrouping(Boolean.parseBoolean(fixedGroupingString));
        }
    }

    @Override
    protected List<Table.Column> loadColumns(final Table component, Element columnsElement, CollectionDatasource ds) {
        List<Table.Column> columns = new ArrayList<>();

        Element groupElement = columnsElement.element("group");
        if (groupElement != null) {
            columns.addAll(super.loadColumns(component, groupElement, ds));
            final List<Object> groupProperties = new ArrayList<>(columns.size());
            for (Table.Column column : columns) {
                if (column.isGroupAllowed()) {
                    groupProperties.add(column.getId());
                }
            }
            context.addPostInitTask((context1, window) -> ((GroupTable) component).groupBy(groupProperties.toArray()));
        }

        columns.addAll(super.loadColumns(component, columnsElement, ds));

        return columns;
    }
}