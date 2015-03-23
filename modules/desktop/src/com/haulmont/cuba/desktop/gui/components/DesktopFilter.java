/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.desktop.sys.layout.LayoutAdapter;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.Filter;
import com.haulmont.cuba.gui.components.FilterImpl;
import com.haulmont.cuba.gui.components.filter.FilterDelegate;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.security.entity.FilterEntity;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;
import org.dom4j.Element;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Generic filter implementation for the desktop-client.
 *
 * @author krivopustov
 * @version $Id$
 */
public class DesktopFilter extends DesktopAbstractComponent<JPanel> implements Filter, FilterImpl {

    protected FilterDelegate delegate;

    public DesktopFilter() {
        delegate = AppBeans.get(FilterDelegate.class);
        delegate.setFilter(this);
        LC topLc = new LC();
        topLc.hideMode(3);
        topLc.insetsAll("0");
        if (LayoutAdapter.isDebug())
            topLc.debug(1000);
        MigLayout topLayout = new MigLayout(topLc);
        impl = new JPanel(topLayout);

        Container layout = delegate.getLayout();
        JComponent unwrap = DesktopComponentsHelper.unwrap(layout);
        impl.add(unwrap, "width 100%");

        setWidth("100%");
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

    public void applySettings(Element element) {
        // logic moved to loadFiltersAndApplyDefault()
    }

    @Override
    public boolean saveSettings(Element element) {
        return delegate.saveSettings(element);
    }

    @Override
    public void setMargin(boolean enable) {
        //todo gorbunkov
//        component.setMargin(enable);
    }

    @Override
    public void setMargin(boolean topEnable, boolean rightEnable, boolean bottomEnable, boolean leftEnable) {
        //todo gorbunkov
//        component.setMargin(new MarginInfo(topEnable, rightEnable, bottomEnable, leftEnable));
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
    public void add(Component component) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void remove(Component component) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T extends Component> T getOwnComponent(String id) {
        return delegate.getOwnComponent(id);
    }

    @Nullable
    @Override
    public <T extends Component> T getComponent(String id) {
        return delegate.getComponent(id);
    }

    @Nonnull
    @Override
    public <T extends Component> T getComponentNN(String id) {
        T component = getComponent(id);
        if (component == null) {
            throw new IllegalArgumentException(String.format("Not found component with id '%s'", id));
        }
        return component;
    }

    @Override
    public Collection<Component> getOwnComponents() {
        return Collections.emptyList();
    }

    @Override
    public Collection<Component> getComponents() {
        return getOwnComponents();
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
    public void setEnabled(boolean enabled) {
        enableComponents(impl, enabled);
    }

    protected void enableComponents(java.awt.Container container, boolean enable) {
        java.awt.Component[] components = container.getComponents();
        for (java.awt.Component component : components) {
            component.setEnabled(enable);
            if (component instanceof java.awt.Container) {
                enableComponents((java.awt.Container)component, enable);
            }
        }
    }

}
