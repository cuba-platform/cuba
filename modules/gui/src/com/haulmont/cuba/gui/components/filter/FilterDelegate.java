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

package com.haulmont.cuba.gui.components.filter;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Filter;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.security.entity.FilterEntity;
import org.dom4j.Element;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Encapsulates common generic filter behaviour. All filter components delegates their method invocations
 * to implementation of this interface.
 *
 */
public interface FilterDelegate {
    String NAME = "cuba_FilterDelegate";

    enum FilterMode {
        GENERIC_MODE,
        FTS_MODE
    }

    void setFilter(Filter filter);

    void loadFiltersAndApplyDefault();

    void setFilterEntity(FilterEntity filterEntity);

    Component.Container getLayout();

    void setDatasource(CollectionDatasource datasource);

    CollectionDatasource getDatasource();

    boolean apply(boolean isNewWindow);

    String getCaption();

    void setCaption(String caption);

    void setMaxResults(int maxResults);

    int getMaxResults();

    void setUseMaxResults(boolean useMaxResults);

    boolean getUseMaxResults();

    void setTextMaxResults(boolean textMaxResults);

    boolean getTextMaxResults();

    void setManualApplyRequired(Boolean manualApplyRequired);

    Boolean getManualApplyRequired();

    @Nullable
    Component getOwnComponent(String id);

    @Nullable
    Component getComponent(String id);

    boolean saveSettings(Element element);

    Component getApplyTo();

    void setApplyTo(Component applyTo);

    void setFolderActionsEnabled(boolean folderActionsEnabled);

    boolean isFolderActionsEnabled();

    void setEditable(boolean editable);

    boolean isEditable();

    Object getParamValue(String paramName);

    void setParamValue(String paramName, Object value);

    void addFilterEntityChangeListener(Filter.FilterEntityChangeListener listener);

    List<Filter.FilterEntityChangeListener> getFilterEntityChangeListeners();

    Integer getColumnsCount();

    void setColumnsCount(int columnsCount);

    void applySettings(Element element);

    boolean isExpanded();
    void setExpanded(boolean expanded);

    boolean isCollapsable();
    void setCollapsable(boolean collapsable);

    void setModeSwitchVisible(boolean modeSwitchVisible);

    void switchFilterMode(FilterMode filterMode);

    void requestFocus();

    class FDExpandedStateChangeEvent {
        private final FilterDelegate delegate;
        private final boolean expanded;

        public FDExpandedStateChangeEvent(FilterDelegate delegate, boolean expanded) {
            this.delegate = delegate;
            this.expanded = expanded;
        }

        public FilterDelegate getComponent() {
            return delegate;
        }

        /**
         * @return true if Component has been expanded.
         */
        public boolean isExpanded() {
            return expanded;
        }
    }

    /**
     * Listener to expanded state change events.
     */
    interface FDExpandedStateChangeListener {
        void expandedStateChanged(FDExpandedStateChangeEvent e);
    }

    void addExpandedStateChangeListener(FDExpandedStateChangeListener listener);
    void removeExpandedStateChangeListener(FDExpandedStateChangeListener listener);
}