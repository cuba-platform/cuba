/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.web.gui.components.presentations.TablePresentations;
import com.haulmont.cuba.web.toolkit.data.AggregationContainer;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Table;

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

    void setAllowMultiStringCells(boolean allowMultiStringCells);
    boolean isAllowMultiStringCells();

    boolean isAllowPopupMenu();
    void setAllowPopupMenu(boolean allowPopupMenu);

    boolean isTextSelectionEnabled();
    void setTextSelectionEnabled(boolean textSelectionEnabled);

    boolean disableContentBufferRefreshing();
    void enableContentBufferRefreshing(boolean refreshContent);

    boolean isAutowirePropertyDsForFields();
    void setAutowirePropertyDsForFields(boolean autowirePropertyDsForFields);

    void refreshCellStyles();

    boolean isAggregatable();
    void setAggregatable(boolean aggregatable);

    boolean isShowTotalAggregation();
    void setShowTotalAggregation(boolean showTotalAggregation);
}