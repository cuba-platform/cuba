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
package com.haulmont.cuba.gui.components;

import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.client.ClientConfig;
import com.haulmont.cuba.gui.components.filter.FilterDelegate;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.model.BaseCollectionLoader;
import com.haulmont.cuba.gui.model.DataLoader;
import com.haulmont.cuba.security.entity.FilterEntity;

import java.util.List;

/**
 * Generic filter component.
 */
public interface Filter extends HasMargin, Component.BelongToFrame, HasNamedComponents, HasSettings, HasDataLoadingSettings, Component.HasCaption, HasContextHelp,
        Component.HasIcon, Collapsable, Component.Focusable, HasHtmlCaption, HasHtmlDescription {

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

    interface BeforeFilterAppliedHandler {
        boolean beforeFilterApplied();
    }

    interface AfterFilterAppliedHandler {
        void afterFilterApplied();
    }

    BeforeFilterAppliedHandler getBeforeFilterAppliedHandler();

    /**
     * Sets the handler that will be invoked before the filter is applied. If the {@link
     * BeforeFilterAppliedHandler#beforeFilterApplied()} returns false, then the filter won't be applied
     *
     * @param beforeFilterAppliedHandler handler
     */
    void setBeforeFilterAppliedHandler(BeforeFilterAppliedHandler beforeFilterAppliedHandler);

    AfterFilterAppliedHandler getAfterFilterAppliedHandler();

    void setAfterFilterAppliedHandler(AfterFilterAppliedHandler afterFilterAppliedHandler);

    /**
     * Returns {@link DataLoader} which the filter is applied to.
     */
    BaseCollectionLoader getDataLoader();

    /**
     * Sets {@link DataLoader} which the filter is applied to.
     */
    void setDataLoader(BaseCollectionLoader loader);

    /**
     * Recreates filter layout
     */
    void createLayout();

    CollectionDatasource getDatasource();

    void setDatasource(CollectionDatasource datasource);

    void setFilterEntity(FilterEntity filterEntity);

    /**
     * Applies the filter. Before the filter is applied, conditions correctness is checked. If invalid conditions are
     * found (i.e. empty required conditions) then the filter will not be applied.
     *
     * @param options an options object
     */
    boolean apply(FilterOptions options);

    /**
     * Applies the filter. Before the filter is applied, conditions correctness is checked. If invalid conditions are
     * found (i.e. empty required conditions) then the filter will not be applied.
     *
     * @param notifyInvalidConditions whether a notification about invalid conditions values should be displayed
     * @return true if the filter was applied, false otherwise
     */
    boolean apply(boolean notifyInvalidConditions);

    /**
     * Sets rows count restriction. Particularly useful when maxResults field is hidden. 0 in case of no limits.
     *
     * @param maxResults restriction on number of rows
     */
    void setMaxResults(int maxResults);

    int getMaxResults();

    /**
     * Sets filter mode switch visibility
     */
    void setModeSwitchVisible(boolean modeSwitchVisible);

    /**
     * Changes the filter mode and repaints the filter layout
     */
    void switchFilterMode(FilterDelegate.FilterMode filterMode);

    /**
     * Whether to show field for rows count restriction. <p>Automatically set to false for {@code
     * HierarchicalDatasource}.
     */
    void setUseMaxResults(boolean useMaxResults);

    boolean getUseMaxResults();

    /**
     * Whether to use a text field for entering a max results value. LookupField is used by default.
     *
     * @param textMaxResults true if use TextField
     */
    void setTextMaxResults(boolean textMaxResults);

    boolean getTextMaxResults();

    /**
     * Sets the component associated with the filter.
     */
    void setApplyTo(Component component);

    Component getApplyTo();

    /**
     * Defines when the filter will be applied. If the attribute value is false, the filter (default or empty) will be
     * applied when the screen is opened. It means that the datasource will be refreshed and linked components (e.g.
     * Table) will display data. If the value is true, the filter will be applied only after the Search button is
     * clicked.
     */
    void setManualApplyRequired(Boolean manualApplyRequired);

    Boolean getManualApplyRequired();

    void setEditable(boolean editable);

    boolean isEditable();

    void setFolderActionsEnabled(boolean enabled);

    boolean isFolderActionsEnabled();

    /**
     * Sets the value to the filter parameter component. Do not use this method in init() method of screen controller,
     * because filter is not initialized by that time. The proper place to use the method is ready() method of the
     * controller.
     *
     * @param paramName parameter name. It can be found at runtime in the filter editor window. Right click at the
     *                  necessary condition and select 'Show component name' item in the popup menu. Component name
     *                  there will be like 'component$genericFilter.email12482'. {@code paramName} parameter in this
     *                  method requires only the last part of this string, i.e. you should pass 'email12482'
     * @param value     parameter value
     */
    void setParamValue(String paramName, Object value);

    /**
     * Gets the value of the filter parameter component. Do not use this method in init() method of screen controller,
     * because filter is not initialized by that time. The proper place to use the method is ready() method of the
     * controller.
     *
     * @param paramName parameter name. It can be found at runtime in the filter editor window. Right click at the
     *                  necessary condition and select 'Show component name' item in the popup menu. Component name
     *                  there will be like 'component$genericFilter.email12482'. {@code paramName} parameter in this
     *                  method requires only the last part of this string, i.e. you should pass 'email12482'
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

    /**
     * Sets whether border is visible.
     *
     * @param visible <code>true</code> to show the border, <code>false</code> to hide it
     */
    void setBorderVisible(boolean visible);

    /**
     * Determines whether or not border is visible.
     *
     * @return <code>true</code> if the border is visible
     */
    boolean isBorderVisible();

    /**
     * Enables to setup which properties should be available for filtering.
     *
     * @param predicate properties filter predicate
     */
    void setPropertiesFilterPredicate(PropertiesFilterPredicate predicate);

    /**
     * @return properties filter predicate
     */
    PropertiesFilterPredicate getPropertiesFilterPredicate();

    /**
     * If {@code windowCaptionUpdateEnabled} is true then window caption will contain a filter name, e.g. "Some Browser: filterName", if the
     * parameter value is false, when window caption will not be modified when the filter is selected
     */
    void setWindowCaptionUpdateEnabled(boolean windowCaptionUpdateEnabled);

    boolean isWindowCaptionUpdateEnabled();

    /**
     * Set to true if filter should be applied immediately after every parameter's value changing. True, by default.
     * Default value can be managed by using {@link ClientConfig#getGenericFilterApplyImmediately()} property.
     *
     * @param immediately immediately option
     */
    void setApplyImmediately(boolean immediately);

    /**
     * @return true if filter should be applied immediately after every parameter's value changing
     */
    boolean isApplyImmediately();

    /**
     * Returns a template for filter controls layout. See {@link ClientConfig#getGenericFilterControlsLayout()}
     */
    String getControlsLayoutTemplate();

    /**
     * Sets a template for filter controls layout. See {@link ClientConfig#getGenericFilterControlsLayout()}
     */
    void setControlsLayoutTemplate(String controlsLayoutTemplate);

    /**
     * A predicate that tests whether a property with the given path should be available for filtering.
     */
    @FunctionalInterface
    interface PropertiesFilterPredicate {

        /**
         * @param metaPropertyPath {@link MetaPropertyPath} instance
         * @return true if property with given {@code metaPropertyPath} should be available for filtering or false otherwise
         */
        boolean test(MetaPropertyPath metaPropertyPath);
    }

    class FilterOptions {

        protected boolean notifyInvalidConditions;
        protected boolean loadData = true;

        public static FilterOptions create() {
            return new FilterOptions();
        }

        /**
         * @return whether a notification about invalid conditions values should be displayed
         */
        public boolean isNotifyInvalidConditions() {
            return notifyInvalidConditions;
        }

        /**
         * Sets whether a notification about invalid conditions values should be displayed.
         *
         * @param notifyInvalidConditions whether a notification about invalid conditions values should be displayed
         * @return this object
         */
        public FilterOptions setNotifyInvalidConditions(boolean notifyInvalidConditions) {
            this.notifyInvalidConditions = notifyInvalidConditions;
            return this;
        }

        /**
         * @return whether data needs to be loaded after filter is applied
         */
        public boolean isLoadData() {
            return loadData;
        }

        /**
         * Sets whether data needs to be loaded after filter is applied.
         *
         * @param loadData whether data needs to be loaded after filter is applied
         * @return this object
         */
        public FilterOptions setLoadData(boolean loadData) {
            this.loadData = loadData;
            return this;
        }
    }
}