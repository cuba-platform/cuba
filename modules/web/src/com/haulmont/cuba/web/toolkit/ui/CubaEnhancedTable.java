/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
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
 * @author artamonov
 * @version $Id$
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

    void addColumnCollapseListener(ColumnCollapseListener listener);
    void removeColumnCollapseListener(ColumnCollapseListener listener);

    void setClickListener(Object propertyId, CellClickListener clickListener);
    void removeClickListener(Object propertyId);

    void showCustomPopup(Component popupComponent);

    boolean getCustomPopupAutoClose();
    void setCustomPopupAutoClose(boolean popupAutoClose);

    String getColumnDescription(Object columnId);
    void setColumnDescription(Object columnId, String description);

    interface ColumnCollapseListener {
        void columnCollapsed(Object columnId, boolean collapsed);
    }

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