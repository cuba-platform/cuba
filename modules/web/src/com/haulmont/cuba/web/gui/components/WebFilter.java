/*
 * Copyright (c) 2008-2014 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Filter;
import com.haulmont.cuba.gui.components.FilterImplementation;
import com.haulmont.cuba.gui.components.filter.FilterDelegate;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.security.entity.FilterEntity;
import com.haulmont.cuba.web.toolkit.ui.CubaVerticalActionsLayout;
import com.vaadin.shared.ui.MarginInfo;
import org.dom4j.Element;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Generic filter implementation for the web-client.
 *
 * @author krivopustov
 * @version $Id$
 */
public class WebFilter extends WebAbstractComponent<CubaVerticalActionsLayout> implements Filter, FilterImplementation {

    protected FilterDelegate delegate;
    protected boolean settingsEnabled = true;

    public WebFilter() {
        delegate = AppBeans.get(FilterDelegate.class);
        delegate.setFilter(this);
        component = new CubaVerticalActionsLayout();
        Container layout = delegate.getLayout();
        com.vaadin.ui.Component unwrap = WebComponentsHelper.getComposition(layout);
        component.addComponent(unwrap);
        component.setWidth("100%");
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
        delegate.addListener(listener);
    }

    @Override
    public void removeListener(ExpandListener listener) {
        delegate.removeListener(listener);
    }

    @Override
    public void addListener(CollapseListener listener) {
        delegate.addListener(listener);
    }

    @Override
    public void removeListener(CollapseListener listener) {
        delegate.removeListener(listener);
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
}