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

import com.haulmont.cuba.gui.components.GroupTable;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

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