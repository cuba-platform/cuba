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

import com.google.common.base.Strings;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.TreeTable;
import com.haulmont.cuba.gui.components.data.TableItems;
import com.haulmont.cuba.gui.components.data.table.ContainerTableItems;
import com.haulmont.cuba.gui.components.data.table.ContainerTreeTableItems;
import com.haulmont.cuba.gui.components.data.table.EmptyTreeTableItems;
import com.haulmont.cuba.gui.model.CollectionContainer;
import org.dom4j.Element;

public class TreeTableLoader extends AbstractTableLoader<TreeTable> {
    @Override
    public void createComponent() {
        resultComponent = factory.create(TreeTable.NAME);
        loadId(resultComponent, element);
        createButtonsPanel(resultComponent, element);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected ContainerTableItems createContainerTableSource(CollectionContainer container) {
        Element rowsEl = element.element("rows");
        String hierarchyProperty = element.attributeValue("hierarchyProperty");
        if (hierarchyProperty == null && rowsEl != null) {
            hierarchyProperty = rowsEl.attributeValue("hierarchyProperty");
        }

        if (Strings.isNullOrEmpty(hierarchyProperty)) {
            throw new GuiDevelopmentException("TreeTable doesn't have 'hierarchyProperty' attribute", context,
                    "TreeTable ID", element.attributeValue("id"));
        }

        String showOrphansAttr = element.attributeValue("showOrphans");
        boolean showOrphans = showOrphansAttr == null || Boolean.parseBoolean(showOrphansAttr);

        return new ContainerTreeTableItems(container, hierarchyProperty, showOrphans);
    }

    @Override
    protected TableItems createEmptyTableItems(MetaClass metaClass) {
        return new EmptyTreeTableItems(metaClass);
    }
}