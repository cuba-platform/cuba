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
 */

package com.haulmont.cuba.gui.xml.layout.loaders;

import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributes;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesUtils;
import com.haulmont.cuba.core.entity.CategoryAttribute;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Configuration;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.GuiDevelopmentException;
import com.haulmont.cuba.gui.components.ButtonsPanel;
import com.haulmont.cuba.gui.components.Component.HasButtonsPanel;
import com.haulmont.cuba.gui.components.DataGrid;
import com.haulmont.cuba.gui.components.DataGrid.Column;
import com.haulmont.cuba.gui.components.RowsCount;
import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.gui.dynamicattributes.DynamicAttributesGuiTools;
import com.haulmont.cuba.gui.xml.layout.ComponentLoader;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class DataGridLoader extends ActionsHolderLoader<DataGrid> {

    protected MetadataTools metadataTools = AppBeans.get(MetadataTools.NAME);
    protected DynamicAttributesGuiTools dynamicAttributesGuiTools = AppBeans.get(DynamicAttributesGuiTools.NAME);
    protected DynamicAttributes dynamicAttributes = AppBeans.get(DynamicAttributes.NAME);
    protected ClientConfig clientConfig = AppBeans.get(Configuration.class).getConfig(ClientConfig.class);

    protected ComponentLoader buttonsPanelLoader;
    protected Element panelElement;

    @Override
    public void createComponent() {
        resultComponent = (DataGrid) factory.createComponent(DataGrid.NAME);
        loadId(resultComponent, element);
        createButtonsPanel(resultComponent, element);
    }

    protected void createButtonsPanel(HasButtonsPanel dataGrid, Element element) {
        panelElement = element.element("buttonsPanel");
        if (panelElement != null) {
            ButtonsPanelLoader loader = (ButtonsPanelLoader) getLoader(panelElement, ButtonsPanel.NAME);
            loader.createComponent();
            ButtonsPanel panel = loader.getResultComponent();

            dataGrid.setButtonsPanel(panel);

            buttonsPanelLoader = loader;
        }
    }

    @Override
    public void loadComponent() {
        loadEnable(resultComponent, element);
        loadVisible(resultComponent, element);
        loadSettingsEnabled(resultComponent, element);

        loadAlign(resultComponent, element);
        loadStyleName(resultComponent, element);

        loadHeight(resultComponent, element);
        loadWidth(resultComponent, element);

        loadIcon(resultComponent, element);
        loadCaption(resultComponent, element);
        loadDescription(resultComponent, element);

        loadActions(resultComponent, element);

        loadColumnsHidingAllowed(resultComponent, element);
        loadColumnResizeMode(resultComponent, element);
        loadSortable(resultComponent, element);
        loadReorderingAllowed(resultComponent, element);
        loadHeaderVisible(resultComponent, element);

        Element columnsElement = element.element("columns");

        loadButtonsPanel(resultComponent);

        loadRowsCount(resultComponent, element); // must be before datasource setting

        String datasource = element.attributeValue("datasource");
        if (StringUtils.isBlank(datasource)) {
            throw new GuiDevelopmentException("DataGrid element doesn't have 'datasource' attribute",
                    context.getCurrentFrameId(), "DataGrid ID", element.attributeValue("id"));
        }

        Datasource ds = context.getDsContext().get(datasource);
        if (ds == null) {
            throw new GuiDevelopmentException("Can't find datasource by name: " + datasource, context.getCurrentFrameId());
        }

        if (!(ds instanceof CollectionDatasource)) {
            throw new GuiDevelopmentException("Not a CollectionDatasource: " + datasource, context.getCurrentFrameId());
        }

        CollectionDatasource cds = (CollectionDatasource) ds;
        List<Column> availableColumns;

        if (columnsElement != null) {
            availableColumns = loadColumns(resultComponent, columnsElement, cds);
        } else {
            availableColumns = new ArrayList<>();
        }

        addDynamicAttributes(resultComponent, ds, availableColumns);

        resultComponent.setDatasource(cds);

        loadSelectionMode(resultComponent, element);
        loadFrozenColumnCount(resultComponent, element);
    }

    protected void loadColumnsHidingAllowed(DataGrid component, Element element) {
        String columnsCollapsingAllowed = element.attributeValue("columnsCollapsingAllowed");
        if (StringUtils.isNotEmpty(columnsCollapsingAllowed)) {
            component.setColumnsCollapsingAllowed(Boolean.parseBoolean(columnsCollapsingAllowed));
        }
    }

    protected void loadColumnResizeMode(DataGrid component, Element element) {
        String columnResizeMode = element.attributeValue("columnResizeMode");
        if (StringUtils.isNotEmpty(columnResizeMode)) {
            component.setColumnResizeMode(DataGrid.ColumnResizeMode.valueOf(columnResizeMode));
        }
    }

    protected void loadSortable(DataGrid component, Element element) {
        String sortable = element.attributeValue("sortable");
        if (StringUtils.isNotEmpty(sortable)) {
            component.setSortable(Boolean.parseBoolean(sortable));
        }
    }

    protected void loadReorderingAllowed(DataGrid component, Element element) {
        String reorderingAllowed = element.attributeValue("reorderingAllowed");
        if (StringUtils.isNotEmpty(reorderingAllowed)) {
            component.setColumnReorderingAllowed(Boolean.parseBoolean(reorderingAllowed));
        }
    }

    protected void loadHeaderVisible(DataGrid component, Element element) {
        String columnHeaderVisible = element.attributeValue("headerVisible");
        if (StringUtils.isNotEmpty(columnHeaderVisible)) {
            component.setHeaderVisible(Boolean.parseBoolean(columnHeaderVisible));
        }
    }

    protected void loadButtonsPanel(DataGrid component) {
        if (buttonsPanelLoader != null) {
            //noinspection unchecked
            buttonsPanelLoader.loadComponent();
            ButtonsPanel panel = (ButtonsPanel) buttonsPanelLoader.getResultComponent();

            Window window = ComponentsHelper.getWindowImplementation(component);
            String alwaysVisible = panelElement.attributeValue("alwaysVisible");
            panel.setVisible(!(window instanceof Window.Lookup) || "true".equals(alwaysVisible));
        }
    }

    protected void loadRowsCount(DataGrid component, Element element) {
        Element rowsCountElement = element.element("rowsCount");
        if (rowsCountElement != null) {
            RowsCount rowsCount = factory.createComponent(RowsCount.class);
            rowsCount.setOwner(component);
            component.setRowsCount(rowsCount);
        }
    }

    protected List<Column> loadColumns(DataGrid component, Element columnsElement, CollectionDatasource ds) {
        List<Column> columns = new ArrayList<>();
        //noinspection unchecked
        for (Element columnElement : (Collection<Element>) columnsElement.elements("column")) {
            columns.add(loadColumn(component, columnElement, ds));
        }
        return columns;
    }

    protected Column loadColumn(DataGrid component, Element element, Datasource ds) {
        String id = element.attributeValue("id");

        String property = element.attributeValue("property");

        Column column;
        if (property != null) {
            MetaPropertyPath metaPropertyPath = AppBeans.get(MetadataTools.NAME, MetadataTools.class)
                    .resolveMetaPropertyPath(ds.getMetaClass(), property);
            column = component.addColumn(id, metaPropertyPath);
        } else {
            column = component.addColumn(id, null);
        }

        String expandRatio = element.attributeValue("expandRatio");
        if (StringUtils.isNotEmpty(expandRatio)) {
            column.setExpandRatio(Integer.valueOf(expandRatio));
        }

        String collapsed = element.attributeValue("collapsed");
        if (StringUtils.isNotEmpty(collapsed)) {
            column.setCollapsed(Boolean.parseBoolean(collapsed));
        }

        String collapsible = element.attributeValue("collapsible");
        if (StringUtils.isNotEmpty(collapsible)) {
            column.setCollapsible(Boolean.parseBoolean(collapsible));
        }

        String collapsingToggleCaption = element.attributeValue("collapsingToggleCaption");
        if (StringUtils.isNotEmpty(collapsingToggleCaption)) {
            column.setCollapsingToggleCaption(collapsingToggleCaption);
        }

        String sortable = element.attributeValue("sortable");
        if (StringUtils.isNotEmpty(sortable)) {
            column.setSortable(Boolean.parseBoolean(sortable));
        }

        String resizable = element.attributeValue("resizable");
        if (StringUtils.isNotEmpty(resizable)) {
            column.setResizable(Boolean.parseBoolean(resizable));
        }

        // Default caption set to columns when it is added to a DataGrid,
        // so we need to set caption as null to get caption from
        // metaProperty if 'caption' attribute is empty
        column.setCaption(null);
        String caption = loadCaption(element);

        if (caption == null) {
            String columnCaption;
            if (column.getPropertyPath() != null) {
                MetaProperty metaProperty = column.getPropertyPath().getMetaProperty();
                String propertyName = metaProperty.getName();

                if (DynamicAttributesUtils.isDynamicAttribute(metaProperty)) {
                    CategoryAttribute categoryAttribute = DynamicAttributesUtils.getCategoryAttribute(metaProperty);
                    columnCaption = categoryAttribute != null ? categoryAttribute.getName() : propertyName;
                } else {
                    MetaClass propertyMetaClass = metadataTools.getPropertyEnclosingMetaClass(column.getPropertyPath());
                    columnCaption = messageTools.getPropertyCaption(propertyMetaClass, propertyName);
                }
            } else {
                Class<?> declaringClass = ds.getMetaClass().getJavaClass();
                String className = declaringClass.getName();
                int i = className.lastIndexOf('.');
                if (i > -1) {
                    className = className.substring(i + 1);
                }
                columnCaption = messages.getMessage(declaringClass, className + "." + id);
            }
            column.setCaption(columnCaption);
        } else {
            column.setCaption(caption);
        }

        column.setXmlDescriptor(element);

        Integer width = loadWidth(element, "width");
        if (width != null) {
            column.setWidth(width);
        }

        Integer minimumWidth = loadWidth(element, "minimumWidth");
        if (minimumWidth  != null) {
            column.setMinimumWidth(minimumWidth);
        }

        Integer maximumWidth = loadWidth(element, "maximumWidth");
        if (maximumWidth != null) {
            column.setMaximumWidth(maximumWidth);
        }

        column.setFormatter(loadFormatter(element));

        return column;
    }

    protected String loadCaption(Element element) {
        if (element.attribute("caption") != null) {
            String caption = element.attributeValue("caption");

            return loadResourceString(caption);
        }
        return null;
    }

    @Nullable
    protected Integer loadWidth(Element element, String propertyName) {
        String width = loadThemeString(element.attributeValue(propertyName));
        if (!StringUtils.isBlank(width)) {
            if (StringUtils.endsWith(width, "px")) {
                width = StringUtils.substring(width, 0, width.length() - 2);
            }
            try {
                // Only integer allowed in XML
                return Integer.parseInt(width);
            } catch (NumberFormatException e) {
                throw new GuiDevelopmentException("Property '" + propertyName + "' must contain only numeric value",
                        context.getCurrentFrameId(), propertyName, element.attributeValue("width"));
            }
        }
        return null;
    }

    protected void addDynamicAttributes(DataGrid component, Datasource ds, List<Column> availableColumns) {
        if (metadataTools.isPersistent(ds.getMetaClass())) {
            Set<CategoryAttribute> attributesToShow =
                    dynamicAttributesGuiTools.getAttributesToShowOnTheScreen(ds.getMetaClass(),
                            context.getFullFrameId(), component.getId());
            if (CollectionUtils.isNotEmpty(attributesToShow)) {
                ds.setLoadDynamicAttributes(true);
                for (CategoryAttribute attribute : attributesToShow) {
                    final MetaPropertyPath metaPropertyPath =
                            DynamicAttributesUtils.getMetaPropertyPath(ds.getMetaClass(), attribute);

                    Object columnWithSameId = CollectionUtils.find(availableColumns, column -> {
                        MetaPropertyPath propertyPath = ((Column) column).getPropertyPath();
                        return propertyPath != null && propertyPath.equals(metaPropertyPath);
                    });

                    if (columnWithSameId != null) {
                        continue;
                    }

                    final Column column =
                            component.addColumn(metaPropertyPath.getMetaProperty().getName(), metaPropertyPath);
                    column.setCaption(attribute.getName());
                }
            }

            dynamicAttributesGuiTools.listenDynamicAttributesChanges(ds);
        }
    }

    protected void loadSelectionMode(DataGrid component, Element element) {
        String selectionMode = element.attributeValue("selectionMode");
        if (StringUtils.isNotEmpty(selectionMode)) {
            component.setSelectionMode(DataGrid.SelectionMode.valueOf(selectionMode));
        }
    }

    protected void loadFrozenColumnCount(DataGrid component, Element element) {
        String frozenColumnCount = element.attributeValue("frozenColumnCount");
        if (StringUtils.isNotEmpty(frozenColumnCount)) {
            component.setFrozenColumnCount(Integer.valueOf(frozenColumnCount));
        }
    }
}
