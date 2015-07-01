/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.security.entity.FilterEntity;

import java.util.List;

/**
 * @author krivopustov
 * @version $Id$
 */
public interface Filter extends Component.Margin, Component.BelongToFrame, Component.HasNamedComponents,
                                Component.HasXmlDescriptor, Component.HasSettings, Component.HasCaption {

    String NAME = "filter";

    /**
     * Action registered with parent frame to apply filter.
     */
    String APPLY_ACTION_ID = "applyFilter";

    /**
     * Action registered with parent frame to open filter select menu.
     */
    String SELECT_ACTION_ID = "selectFilter";

    interface FilterEntityChangeListener {
        void filterEntityChanged(FilterEntity filterEntity);
    }

    CollectionDatasource getDatasource();
    void setDatasource(CollectionDatasource datasource);

    void setFilterEntity(FilterEntity filterEntity);

    boolean apply(boolean isNewWindow);

    /**
     * Whether to show field for rows count restriction.
     * <p>Automatically set to false for {@code HierarchicalDatasource}.
     */
    void setUseMaxResults(boolean useMaxResults);
    boolean getUseMaxResults();

    /**
     * Whether to use a text field for entering a max results value.
     * LookupField is used by default.
     * @param textMaxResults true if use TextField
     */
    void setTextMaxResults(boolean textMaxResults);
    boolean getTextMaxResults();

    void setApplyTo(Component component);
    Component getApplyTo();
    
    void setManualApplyRequired(Boolean manualApplyRequired);
    Boolean getManualApplyRequired();

    void setEditable(boolean editable);
    boolean isEditable();

    void setFolderActionsEnabled(boolean enabled);
    boolean isFolderActionsEnabled();

    void setParamValue(String paramName, Object value);
    Object getParamValue(String paramName);

    void addFilterEntityChangeListener(FilterEntityChangeListener listener);
    List<FilterEntityChangeListener> getFilterEntityChangeListeners();

    /**
     * Number of conditions to be displayed in one row
     */
    void setColumnsCount(int columnsCount);
    int getColumnsCount();
}