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

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.web.gui.components.presentations.TablePresentations;
import com.haulmont.cuba.web.toolkit.data.AggregationContainer;
import com.vaadin.ui.Component;
import com.vaadin.ui.Layout;

/**
 * Interface to generalize additional functionality in {@link CubaTable}, {@link CubaGroupTable} and {@link CubaTreeTable}
 *
 */
public interface CubaEnhancedTable extends AggregationContainer {
    void setContextMenuPopup(Layout contextMenu);
    void hideContextMenuPopup();

    TablePresentations getPresentations();
    void setPresentations(TablePresentations presentations);
    void hidePresentationsPopup();

    Object[] getEditableColumns();
    void setEditableColumns(Object[] editableColumns);

    boolean isColumnEditable(Object columnId);

    void setMultiLineCells(boolean multiLineCells);
    boolean isMultiLineCells();

    boolean isContextMenuEnabled();
    void setContextMenuEnabled(boolean contextMenuEnabled);

    boolean isTextSelectionEnabled();
    void setTextSelectionEnabled(boolean textSelectionEnabled);

    boolean disableContentBufferRefreshing();
    void enableContentBufferRefreshing(boolean refreshContent);

    boolean isAutowirePropertyDsForFields();
    void setAutowirePropertyDsForFields(boolean autowirePropertyDsForFields);

    void refreshCellStyles();

    boolean isAggregatable();
    void setAggregatable(boolean aggregatable);

    void setAggregationStyle(Table.AggregationStyle aggregationStyle);
    Table.AggregationStyle getAggregationStyle();

    boolean isShowTotalAggregation();
    void setShowTotalAggregation(boolean showTotalAggregation);

    void setClickListener(Object propertyId, CellClickListener clickListener);
    void removeClickListener(Object propertyId);

    void showCustomPopup(Component popupComponent);

    boolean getCustomPopupAutoClose();
    void setCustomPopupAutoClose(boolean popupAutoClose);

    String getColumnDescription(Object columnId);
    void setColumnDescription(Object columnId, String description);

    boolean getColumnSortable(Object columnId);
    void setColumnSortable(Object columnId, boolean sortable);

    interface CellClickListener {
        void onClick(Object itemId, Object columnId);
    }

    /**
     * Marker interface for generated columns which return String value.
     * Used to work with custom cacheRate and pageLength.
     */
    interface PlainTextGeneratedColumn {
    }
}