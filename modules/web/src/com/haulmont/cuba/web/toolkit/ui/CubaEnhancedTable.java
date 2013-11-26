/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.web.gui.components.presentations.TablePresentations;
import com.vaadin.ui.Table;

/**
 * Interface to generalize additional functionality in {@link CubaTable}, {@link CubaGroupTable} and {@link CubaTreeTable}
 *
 * @author artamonov
 * @version $Id$
 */
public interface CubaEnhancedTable {
    TablePresentations getPresentations();

    void setPresentations(TablePresentations presentations);
    void hidePresentationsPopup();

    Object[] getEditableColumns();
    void setEditableColumns(Object[] editableColumns);

    boolean isAllowPopupMenu();
    void setAllowPopupMenu(boolean allowPopupMenu);

    boolean isTextSelectionEnabled();
    void setTextSelectionEnabled(boolean textSelectionEnabled);

    void disableContentBufferRefreshing();
    void enableContentBufferRefreshing(boolean refreshContent);

    boolean isAutowirePropertyDsForFields();
    void setAutowirePropertyDsForFields(boolean autowirePropertyDsForFields);

    /**
     * Just add generated column to table without checks and without cells refresh. <br/>
     * <b>For internal use only.</b>
     */
    void addGeneratedColumnInternal(Object id, Table.ColumnGenerator generatedColumn);
}