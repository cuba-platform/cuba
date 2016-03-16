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
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Filter;
import com.haulmont.cuba.gui.components.FilterImplementation;
import com.haulmont.cuba.gui.components.compatibility.ComponentExpandCollapseListenerWrapper;
import com.haulmont.cuba.gui.components.filter.FilterDelegate;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.security.entity.FilterEntity;
import com.haulmont.cuba.web.toolkit.ui.CubaVerticalActionsLayout;
import com.vaadin.shared.ui.MarginInfo;
import org.dom4j.Element;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Generic filter implementation for the web-client.
 *
 */
public class WebFilter extends WebAbstractComponent<CubaVerticalActionsLayout> implements Filter, FilterImplementation {

    protected FilterDelegate delegate;
    protected boolean settingsEnabled = true;

    protected List<ExpandedStateChangeListener> expandedStateChangeListeners;

    public WebFilter() {
        delegate = AppBeans.get(FilterDelegate.class);
        delegate.setFilter(this);
        component = new CubaVerticalActionsLayout();
        Container layout = delegate.getLayout();
        com.vaadin.ui.Component unwrap = WebComponentsHelper.getComposition(layout);
        component.addComponent(unwrap);
        component.setWidth("100%");

        delegate.addExpandedStateChangeListener(e -> fireExpandStateChange(e.isExpanded()));
    }

    @Override
    public CollectionDatasource getDatasource() {
        return delegate.getDatasource();
    }

    @Override
    public void setDatasource(CollectionDatasource datasource) {
        delegate.setDatasource(datasource);
    }

    @Override
    public void setFilterEntity(FilterEntity filterEntity) {
        delegate.setFilterEntity(filterEntity);
    }

    @Override
    public boolean apply(boolean isNewWindow) {
        return delegate.apply(isNewWindow);
    }

    @Override
    public void loadFiltersAndApplyDefault() {
        delegate.loadFiltersAndApplyDefault();
    }

    @Override
    public void setUseMaxResults(boolean useMaxResults) {
        delegate.setUseMaxResults(useMaxResults);
    }

    @Override
    public void setTextMaxResults(boolean textMaxResults) {
        delegate.setTextMaxResults(textMaxResults);
    }

    @Override
    public boolean getTextMaxResults() {
        return delegate.getTextMaxResults();
    }

    @Override
    public boolean getUseMaxResults() {
        return delegate.getUseMaxResults();
    }

    @Override
    public void setApplyTo(Component component) {
        delegate.setApplyTo(component);
    }

    @Override
    public Component getApplyTo() {
        return delegate.getApplyTo();
    }

    @Override
    public void setManualApplyRequired(Boolean manualApplyRequired) {
        delegate.setManualApplyRequired(manualApplyRequired);
    }

    @Override
    public Boolean getManualApplyRequired() {
        return delegate.getManualApplyRequired();
    }

    @Override
    public void setEditable(boolean editable) {
        delegate.setEditable(editable);
    }

    @Override
    public boolean isEditable() {
        return delegate.isEditable();
    }

    @Override
    public void setFolderActionsEnabled(boolean enabled) {
        delegate.setFolderActionsEnabled(enabled);
    }

    @Override
    public boolean isFolderActionsEnabled() {
        return delegate.isFolderActionsEnabled();
    }

    @Override
    public void applySettings(Element element) {
        if (isSettingsEnabled()) {
            delegate.applySettings(element);
        }
    }

    @Override
    public boolean saveSettings(Element element) {
        return isSettingsEnabled() && delegate.saveSettings(element);
    }

    @Override
    public boolean isSettingsEnabled() {
        return settingsEnabled;
    }

    @Override
    public void setSettingsEnabled(boolean settingsEnabled) {
        this.settingsEnabled = settingsEnabled;
    }

    @Override
    public void setMargin(boolean enable) {
        component.setMargin(enable);
    }

    @Override
    public void setMargin(boolean topEnable, boolean rightEnable, boolean bottomEnable, boolean leftEnable) {
        component.setMargin(new MarginInfo(topEnable, rightEnable, bottomEnable, leftEnable));
    }

    @Override
    public String getCaption() {
        return delegate.getCaption();
    }

    @Override
    public void setCaption(String caption) {
        delegate.setCaption(caption);
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public void setDescription(String description) {
        //do nothing
    }

    @Override
    public void setParamValue(String paramName, Object value) {
        delegate.setParamValue(paramName, value);
    }

    @Override
    public Object getParamValue(String paramName) {
        return delegate.getParamValue(paramName);
    }

    @Override
    public void addFilterEntityChangeListener(FilterEntityChangeListener listener) {
        delegate.addFilterEntityChangeListener(listener);
    }

    @Override
    public List<FilterEntityChangeListener> getFilterEntityChangeListeners() {
        return delegate.getFilterEntityChangeListeners();
    }

    @Override
    public void setColumnsCount(int columnsCount) {
        delegate.setColumnsCount(columnsCount);
    }

    @Override
    public int getColumnsCount() {
        return delegate.getColumnsCount();
    }

    @Override
    public boolean isExpanded() {
        return delegate.isExpanded();
    }

    @Override
    public void setExpanded(boolean expanded) {
        delegate.setExpanded(expanded);
    }

    @Override
    public boolean isCollapsable() {
        return delegate.isCollapsable();
    }

    @Override
    public void setCollapsable(boolean collapsable) {
        delegate.setCollapsable(collapsable);
    }

    @Override
    public void addListener(ExpandListener listener) {
        addExpandedStateChangeListener(new ComponentExpandCollapseListenerWrapper(listener));
    }

    @Override
    public void removeListener(ExpandListener listener) {
        removeExpandedStateChangeListener(new ComponentExpandCollapseListenerWrapper(listener));
    }

    @Override
    public void addListener(CollapseListener listener) {
        addExpandedStateChangeListener(new ComponentExpandCollapseListenerWrapper(listener));
    }

    @Override
    public void removeListener(CollapseListener listener) {
        removeExpandedStateChangeListener(new ComponentExpandCollapseListenerWrapper(listener));
    }

    @Override
    public void addExpandedStateChangeListener(ExpandedStateChangeListener listener) {
        if (expandedStateChangeListeners == null) {
            expandedStateChangeListeners = new ArrayList<>();
        }
        if (!expandedStateChangeListeners.contains(listener)) {
            expandedStateChangeListeners.add(listener);
        }
    }

    @Override
    public void removeExpandedStateChangeListener(ExpandedStateChangeListener listener) {
        if (expandedStateChangeListeners != null) {
            expandedStateChangeListeners.remove(listener);
        }
    }

    protected void fireExpandStateChange(boolean expanded) {
        if (expandedStateChangeListeners != null && !expandedStateChangeListeners.isEmpty()) {
            ExpandedStateChangeEvent event = new ExpandedStateChangeEvent(this, expanded);

            for (ExpandedStateChangeListener listener : expandedStateChangeListeners) {
                listener.expandedStateChanged(event);
            }
        }
    }

    @Nullable
    @Override
    public Component getComponent(String id) {
        return delegate.getComponent(id);
    }

    @Override
    public int getMaxResults() {
        return delegate.getMaxResults();
    }

    @Override
    public void setMaxResults(int maxResults) {
        delegate.setMaxResults(maxResults);
    }

    @Override
    public void requestFocus() {
        delegate.requestFocus();
    }

    @Override
    public void setModeSwitchVisible(boolean modeSwitchVisible) {
        delegate.setModeSwitchVisible(modeSwitchVisible);
    }

    @Override
    public void switchFilterMode(FilterDelegate.FilterMode filterMode) {
        delegate.switchFilterMode(filterMode);
    }

}