/*
 * Copyright (c) 2008-2017 Haulmont.
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

package com.haulmont.cuba.web.widgets;

import com.haulmont.cuba.web.widgets.data.AggregationContainer;
import com.vaadin.ui.Component;
import com.vaadin.ui.Layout;

/**
 * Interface to generalize additional functionality in {@link CubaTable}, {@link CubaGroupTable} and {@link CubaTreeTable}
 */
public interface CubaEnhancedTable extends AggregationContainer {
    void setContextMenuPopup(Layout contextMenu);
    void hideContextMenuPopup();

    Component getPresentations();
    void setPresentations(Component presentations);
    void hidePresentationsPopup();

    Object[] getEditableColumns();
    void setEditableColumns(Object[] editableColumns);

    String getSortDescendingLabel();
    void setSortDescendingLabel(String tableSortDescendingLabel);

    String getSortAscendingLabel();
    void setSortAscendingLabel(String tableSortAscendingLabel);

    String getSortResetLabel();
    void setSortResetLabel(String tableSortResetLabel);

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

    void setAggregationStyle(AggregationStyle aggregationStyle);
    AggregationStyle getAggregationStyle();

    boolean isShowTotalAggregation();
    void setShowTotalAggregation(boolean showTotalAggregation);

    void setClickListener(Object propertyId, CellClickListener clickListener);
    void removeClickListener(Object propertyId);

    void showCustomPopup(Component popupComponent);

    boolean getCustomPopupAutoClose();
    void setCustomPopupAutoClose(boolean popupAutoClose);

    String getColumnDescription(Object columnId);
    void setColumnDescription(Object columnId, String description);

    String getAggregationDescription(Object columnId);
    void setAggregationDescription(Object columnId, String description);

    boolean getColumnSortable(Object columnId);
    void setColumnSortable(Object columnId, boolean sortable);

    void requestFocus(Object itemId, Object columnId);

    interface CellClickListener {
        void onClick(Object itemId, Object columnId);
    }

    void setBeforePaintListener(Runnable beforePaintListener);

    /**
     * Marker interface for generated columns which return String value.
     * Used to work with custom cacheRate and pageLength.
     */
    interface PlainTextGeneratedColumn {
    }

    enum AggregationStyle {
        TOP,
        BOTTOM
    }
}