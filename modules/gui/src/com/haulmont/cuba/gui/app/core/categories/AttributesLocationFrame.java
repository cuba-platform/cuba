/*
 * Copyright (c) 2008-2019 Haulmont.
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

package com.haulmont.cuba.gui.app.core.categories;

import com.google.gson.Gson;
import com.haulmont.cuba.core.entity.CategoryAttribute;
import com.haulmont.cuba.core.entity.CategoryAttributeConfiguration;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.gui.UiComponents;
import com.haulmont.cuba.gui.components.AbstractFrame;
import com.haulmont.cuba.gui.components.DataGrid;
import com.haulmont.cuba.gui.components.HBoxLayout;
import com.haulmont.cuba.gui.components.LookupField;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AttributesLocationFrame extends AbstractFrame {
    public static String EMPTY_ATTRIBUTE_NAME = "   ";

    @Inject
    protected UiComponents uiComponents;
    @Inject
    protected MetadataTools metadataTools;
    @Inject
    protected Metadata metadata;
    @Inject
    protected GlobalConfig config;
    @Inject
    protected DataGrid<CategoryAttribute> sourceDataGrid;
    @Inject
    protected LookupField<Integer> columnsCountLookupField;
    @Inject
    protected HBoxLayout hBox;

    protected List<CategoryAttribute> sourceDataContainer = new ArrayList<>();
    protected List<List<CategoryAttribute>> dataContainers = new ArrayList<>();
    protected List<DataGrid<CategoryAttribute>> childComponents = new ArrayList<>();

    protected int[] rowsCounts;

    protected AttributesLocationFrame.Companion companion;

    public interface Companion {
        void initGridDragAndDrop(DataGrid<CategoryAttribute> dataGrid,
                                 List<CategoryAttribute> dataContainer,
                                 boolean isSourceDataGrid);

        void refreshSourceDataProvider();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void init(Map<String, Object> params) {
        super.init(params);

        companion = getCompanion();

        Collection<CategoryAttribute> attributes = (Collection<CategoryAttribute>) params.get("attributes");
        sourceDataContainer.addAll(attributes);
        sourceDataContainer.add(createEmptyAttribute());

        if (companion != null) {
            companion.initGridDragAndDrop(sourceDataGrid, sourceDataContainer, true);
        }

        int maxColumnIndex = sourceDataContainer.stream()
                .filter(e -> e.getConfiguration().getXCoordinate() != null)
                .mapToInt(e -> e.getConfiguration().getXCoordinate())
                .max()
                .orElse(0);

        rowsCounts = new int[maxColumnIndex + 1];

        for (CategoryAttribute entity : sourceDataContainer) {
            CategoryAttributeConfiguration conf = entity.getConfiguration();
            if (conf.getXCoordinate() != null
                    && conf.getYCoordinate() != null) {
                if (rowsCounts[conf.getXCoordinate()] <= conf.getYCoordinate()) {
                    rowsCounts[conf.getXCoordinate()] = conf.getYCoordinate() + 1;
                }
            }
        }

        columnsCountLookupField.setOptionsList(getLookupOptionsList());
        columnsCountLookupField.setValue(maxColumnIndex + 1);
        columnsCountLookupField.addValueChangeListener(e -> {
            if (e.getPrevValue() == null || e.getValue() == null) {
                return;
            }
            int range = e.getPrevValue() - e.getValue();
            if (range > 0) {
                for (int i = 1; i <= range; i++) {
                    removeColumn(e.getPrevValue() - i);
                }
            } else {
                for (int i = 0; i < -range; i++) {
                    addColumn(e.getPrevValue() + i, 0);
                }
            }
        });

        initTargetGrids(maxColumnIndex + 1);

    }

    protected void initTargetGrids(int columnsCount) {

        for (int i = 0; i < columnsCount; i++) {
            if (i < rowsCounts.length) {
                addColumn(i, rowsCounts[i]);
            } else {
                addColumn(i, 0);
            }

        }

        List<CategoryAttribute> removeFromSource = new ArrayList<>();
        for (CategoryAttribute entity : sourceDataContainer) {
            CategoryAttributeConfiguration conf = entity.getConfiguration();
            if (conf.getXCoordinate() != null && conf.getYCoordinate() != null) {
                dataContainers.get(conf.getXCoordinate()).set(conf.getYCoordinate(), entity);
                removeFromSource.add(entity);
            }
        }
        sourceDataContainer.removeAll(removeFromSource);

    }

    protected void addColumn(int index, int elementsCount) {
        DataGrid<CategoryAttribute> targetDataGrid = createDataGrid(index + 1);

        childComponents.add(targetDataGrid);
        hBox.add(targetDataGrid);

        List<CategoryAttribute> dataContainer;

        if (elementsCount > 0) {
            dataContainer = new ArrayList<>();
            for (int i = 0; i < elementsCount; i++) {
                dataContainer.add(createEmptyAttribute());
            }
        } else {
            dataContainer = new ArrayList<>();
        }
        dataContainers.add(dataContainer);

        if (companion != null) {
            companion.initGridDragAndDrop(targetDataGrid, dataContainer, false);
        }

    }

    protected void removeColumn(int index) {
        DataGrid<CategoryAttribute> columnToRemove = childComponents.get(index);
        List<CategoryAttribute> dataContainer = dataContainers.get(index);

        sourceDataContainer.addAll(dataContainer.stream()
                .filter(e -> !EMPTY_ATTRIBUTE_NAME.equals(e.getName()))
                .collect(Collectors.toList()));
        hBox.remove(columnToRemove);
        dataContainers.remove(index);
        childComponents.remove(index);

        if (companion != null) {
            companion.refreshSourceDataProvider();
        }
    }

    protected DataGrid<CategoryAttribute> createDataGrid(int i) {
        DataGrid<CategoryAttribute> dataGrid = uiComponents.create(DataGrid.NAME);
        DataGrid.Column<CategoryAttribute> column = dataGrid.addColumn("column",
                metadataTools.resolveMetaPropertyPath(metadata.getClassNN(CategoryAttribute.class), "localeName"));
        column.setSortable(false);
        column.setCaption(getMessage("attributesLocation.columnCaption") + " " + i);
        dataGrid.setSettingsEnabled(false);
        dataGrid.setColumnsCollapsingAllowed(false);
        dataGrid.setWidth("175px");

        return dataGrid;
    }

    protected CategoryAttribute createEmptyAttribute() {
        CategoryAttribute empty = new CategoryAttribute();
        empty.setName(EMPTY_ATTRIBUTE_NAME);
        return empty;
    }

    protected List<Integer> getLookupOptionsList() {
        int maxColumns = config.getDynamicAttributesPanelMaxColumnsCount();
        if (maxColumns < 1) {
            maxColumns = 1;
        }

        return IntStream.range(1, maxColumns + 1).boxed().collect(Collectors.toList());
    }

    public void saveCoordinates() {

        for (List<CategoryAttribute> currentList : dataContainers) {
            for (CategoryAttribute entity : currentList) {
                if (!EMPTY_ATTRIBUTE_NAME.equals(entity.getName())) {
                    entity.getConfiguration().setXCoordinate(dataContainers.indexOf(currentList));
                    entity.getConfiguration().setYCoordinate(currentList.indexOf(entity));

                    entity.setAttributeConfigurationJson(new Gson().toJson(entity.getConfiguration()));
                }
            }
        }

        for (CategoryAttribute entity : sourceDataContainer) {
            if (!EMPTY_ATTRIBUTE_NAME.equals(entity.getName())) {
                entity.getConfiguration().setXCoordinate(null);
                entity.getConfiguration().setYCoordinate(null);

                entity.setAttributeConfigurationJson(new Gson().toJson(entity.getConfiguration()));
            }
        }
    }
}
