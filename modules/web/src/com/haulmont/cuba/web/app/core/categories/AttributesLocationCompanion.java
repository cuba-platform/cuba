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

package com.haulmont.cuba.web.app.core.categories;

import com.haulmont.cuba.core.entity.CategoryAttribute;
import com.haulmont.cuba.gui.app.core.categories.AttributesLocationFrame;
import com.haulmont.cuba.gui.components.DataGrid;
import com.haulmont.cuba.web.widgets.CubaGrid;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.server.SerializablePredicate;
import com.vaadin.shared.ui.grid.DropLocation;
import com.vaadin.shared.ui.grid.DropMode;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.components.grid.*;

import java.util.ArrayList;
import java.util.List;

public class AttributesLocationCompanion implements AttributesLocationFrame.Companion {

    protected CategoryAttribute draggedItem;
    protected boolean droppedSuccessful;
    protected int dropIndex = -1;
    protected AbstractComponent dropComponent;

    protected List<CategoryAttribute> sourceDataContainer = new ArrayList<>();
    protected DataProvider<CategoryAttribute, SerializablePredicate<CategoryAttribute>> sourceDataProvider;


    @Override
    @SuppressWarnings("unchecked")
    public void initGridDragAndDrop(DataGrid<CategoryAttribute> dataGrid,
                                    List<CategoryAttribute> dataContainer,
                                    boolean isSourceDataGrid) {

        DataProvider<CategoryAttribute, SerializablePredicate<CategoryAttribute>> dataProvider =  new ListDataProvider<>(dataContainer);

        if (isSourceDataGrid) {
            sourceDataContainer = dataContainer;
            sourceDataProvider = dataProvider;
        }

        CubaGrid<CategoryAttribute> grid = dataGrid.unwrap(CubaGrid.class);

        grid.setDataProvider(dataProvider);

        GridDragSource<CategoryAttribute> gridDragSource = new GridDragSource<>(grid);
        gridDragSource.addGridDragStartListener(this::onGridDragStart);
        gridDragSource.addGridDragEndListener(e -> onGridDragEnd(e, grid, isSourceDataGrid));

        GridDropTarget<CategoryAttribute> gridDropTarget = new GridDropTarget<>(grid, DropMode.BETWEEN);
        gridDropTarget.addGridDropListener(e -> onGridDrop(e, isSourceDataGrid));
    }

    @Override
    public void refreshSourceDataProvider() {
        if (sourceDataProvider != null) {
            sourceDataProvider.refreshAll();
        }
    }

    protected void onGridDragStart(GridDragStartEvent<CategoryAttribute> event) {
        draggedItem = event.getDraggedItems().get(0);
        droppedSuccessful = false;
        dropComponent = null;
        dropIndex = -1;
    }

    protected void onGridDragEnd(GridDragEndEvent<CategoryAttribute> event, CubaGrid grid, boolean isSourceGrid) {
        if (!droppedSuccessful || draggedItem == null) {
            return;
        }

        //noinspection unchecked
        List<CategoryAttribute> items = (List<CategoryAttribute>) ((ListDataProvider) grid.getDataProvider()).getItems();
        if (grid.equals(dropComponent) && dropIndex >= 0) {
            int removeIndex = items.indexOf(draggedItem) == dropIndex
                    ? items.lastIndexOf(draggedItem)
                    : items.indexOf(draggedItem);
            if (removeIndex >= 0 && removeIndex != dropIndex) {
                items.remove(removeIndex);
            }
        } else {
            items.remove(draggedItem);
        }

        if (isSourceGrid && AttributesLocationFrame.EMPTY_ATTRIBUTE_NAME.equals(draggedItem.getName())) {
            sourceDataContainer.add(createEmptyAttribute());
        }

        grid.getDataProvider().refreshAll();
    }

    protected void onGridDrop(GridDropEvent<CategoryAttribute> event, boolean isSourceGrid) {
        event.getDragSourceExtension().ifPresent(source -> {
            if (isSourceGrid && AttributesLocationFrame.EMPTY_ATTRIBUTE_NAME.equals(draggedItem.getName())) {
                droppedSuccessful = true;
                return;
            }

            if (source instanceof GridDragSource) {
                //noinspection unchecked
                ListDataProvider<CategoryAttribute> dataProvider = (ListDataProvider<CategoryAttribute>)
                        event.getComponent().getDataProvider();
                List<CategoryAttribute> items = (List<CategoryAttribute>) dataProvider.getItems();

                dropComponent = event.getComponent();

                int i = items.size();
                if (event.getDropTargetRow().isPresent()) {
                    i = items.indexOf(event.getDropTargetRow().get())
                            + (event.getDropLocation() == DropLocation.BELOW ? 1 : 0);
                }

                dropIndex = i;
                items.add(i, draggedItem);
                dataProvider.refreshAll();

                droppedSuccessful = true;
            }
        });
    }

    protected CategoryAttribute createEmptyAttribute() {
        CategoryAttribute empty = new CategoryAttribute();
        empty.setName(AttributesLocationFrame.EMPTY_ATTRIBUTE_NAME);
        return empty;
    }
}
