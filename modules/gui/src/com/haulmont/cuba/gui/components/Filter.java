/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.gui.components.filter.FilterDelegate;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.security.entity.FilterEntity;

import java.util.List;

/**
 * @author krivopustov
 * @version $Id$
 */
public interface Filter extends Component.Margin, Component.BelongToFrame, Component.HasNamedComponents,
                                Component.HasXmlDescriptor, Component.HasSettings, Component.HasCaption, Component.Collapsable {

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
     * Sets rows count restriction. Particularly useful when maxResults field is hidden.
     * 0 in case of no limits.
     * @param maxResults restriction on number of rows
     */
    void setMaxResults(int maxResults);
    int getMaxResults();

    /**
     * Sets filter mode switch visibility
     */
    void setModeSwitchVisible(boolean modeSwitchVisible);

    void switchFilterMode(FilterDelegate.FilterMode filterMode);

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

    /**
     * Sets the value to the filter parameter component. Do not use this method in init() method of screen controller,
     * because filter is not initialized by that time. The proper place to use the method is ready() method of
     * the controller.
     * @param paramName parameter name. It can be found at runtime in the filter editor window. Right click
     *                  at the necessary condition and select 'Show component name' item in the popup menu.
     *                  Component name there will be like 'component$genericFilter.email12482'. {@code paramName} parameter in
     *                  this method requires only the last part of this string, i.e. you should pass 'email12482'
     * @param value parameter value
     */
    void setParamValue(String paramName, Object value);

    /**
     * Gets the value of the filter parameter component. Do not use this method in init() method of screen controller,
     * because filter is not initialized by that time. The proper place to use the method is ready() method of
     * the controller.
     * @param paramName parameter name. It can be found at runtime in the filter editor window. Right click
     *                  at the necessary condition and select 'Show component name' item in the popup menu.
     *                  Component name there will be like 'component$genericFilter.email12482'. {@code paramName} parameter in
     *                  this method requires only the last part of this string, i.e. you should pass 'email12482'
     * @return parameter value
     */
    Object getParamValue(String paramName);

    void addFilterEntityChangeListener(FilterEntityChangeListener listener);
    List<FilterEntityChangeListener> getFilterEntityChangeListeners();

    /**
     * Number of conditions to be displayed in one row
     */
    void setColumnsCount(int columnsCount);
    int getColumnsCount();
}