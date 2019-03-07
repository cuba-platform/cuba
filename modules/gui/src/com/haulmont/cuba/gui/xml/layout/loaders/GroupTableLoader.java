/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.GroupTable;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.components.data.table.ContainerTableItems;
import com.haulmont.cuba.gui.components.data.table.ContainerGroupTableItems;
import com.haulmont.cuba.gui.model.CollectionContainer;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

public class GroupTableLoader extends AbstractTableLoader<GroupTable> {
    @Override
    public void createComponent() {
        resultComponent = factory.create(GroupTable.NAME);
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
    protected List<Table.Column> loadColumns(Table component, Element columnsElement, MetaClass metaClasss, View view) {
        List<Table.Column> columns = new ArrayList<>();

        Element groupElement = columnsElement.element("group");
        if (groupElement != null) {
            columns.addAll(super.loadColumns(component, groupElement, metaClasss, view));
            final List<Object> groupProperties = new ArrayList<>(columns.size());
            for (Table.Column column : columns) {
                if (column.isCollapsed()) {
                    String msg = String.format("Can't group by collapsed column: %s", column.getId());
                    throw new GuiDevelopmentException(msg, context.getFullFrameId());
                }

                if (column.isGroupAllowed()) {
                    groupProperties.add(column.getId());
                }
            }
            context.addPostInitTask((context1, window) ->
                    ((GroupTable) component).groupBy(groupProperties.toArray())
            );
        }

        // check for duplicate
        String includeAll = columnsElement.attributeValue("includeAll");
        String includeByView = columnsElement.attributeValue("includeByView");
        if (StringUtils.isNotBlank(includeAll) || StringUtils.isNotBlank(includeByView)) {
            List<Table.Column> columnList = super.loadColumns(component, columnsElement, metaClasss, view);
            for (Table.Column column : columnList) {
                if (!columns.contains(column)) {
                    columns.add(column);
                }
            }
        } else {
            columns.addAll(super.loadColumns(component, columnsElement, metaClasss, view));
        }

        return columns;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected ContainerTableItems createContainerTableSource(CollectionContainer container) {
        return new ContainerGroupTableItems(container);
    }
}