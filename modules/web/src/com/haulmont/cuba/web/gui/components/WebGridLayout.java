/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.GridLayout;
import com.haulmont.cuba.web.toolkit.ui.CubaGridLayout;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Layout;

import java.util.*;

/**
 * @author abramov
 * @version $Id$
 */
public class WebGridLayout extends WebAbstractComponent<CubaGridLayout>
        implements GridLayout, Component.Wrapper {

    protected String id;

    protected Map<String, Component> componentByIds = new HashMap<>();
    protected Collection<Component> ownComponents = new HashSet<>();

    protected Alignment alignment = Alignment.TOP_LEFT;

    protected boolean expandable = true;

    public WebGridLayout() {
        component = new CubaGridLayout();
    }

    @Override
    public void add(Component childComponent) {
        final com.vaadin.ui.Component vComponent = WebComponentsHelper.getComposition(childComponent);

        component.addComponent(vComponent);
        component.setComponentAlignment(vComponent, WebComponentsHelper.convertAlignment(childComponent.getAlignment()));

        if (component.getId() != null) {
            componentByIds.put(childComponent.getId(), childComponent);
            if (frame != null) {
                frame.registerComponent(childComponent);
            }
        }
        ownComponents.add(childComponent);
    }

    @Override
    public float getColumnExpandRatio(int col) {
        return component.getColumnExpandRatio(col);
    }

    @Override
    public void setColumnExpandRatio(int col, float ratio) {
        component.setColumnExpandRatio(col, ratio);
    }

    @Override
    public float getRowExpandRatio(int row) {
        return component.getRowExpandRatio(row);
    }

    @Override
    public void setRowExpandRatio(int row, float ratio) {
        component.setRowExpandRatio(row, ratio);
    }

    @Override
    public void add(Component component, int col, int row) {
        add(component, col, row, col, row);
    }

    @Override
    public void add(Component childComponent, int col, int row, int col2, int row2) {
        final com.vaadin.ui.Component itmillComponent = WebComponentsHelper.getComposition(childComponent);

        component.addComponent(itmillComponent, col, row, col2, row2);
        component.setComponentAlignment(itmillComponent, WebComponentsHelper.convertAlignment(childComponent.getAlignment()));

        if (childComponent.getId() != null) {
            componentByIds.put(childComponent.getId(), childComponent);
            if (frame != null) {
                frame.registerComponent(childComponent);
            }
        }
        ownComponents.add(childComponent);
    }

    @Override
    public int getRows() {
        return component.getRows();
    }

    @Override
    public void setRows(int rows) {
        component.setRows(rows);
    }

    @Override
    public int getColumns() {
        return component.getColumns();
    }

    @Override
    public void setColumns(int columns) {
        component.setColumns(columns);
    }

    @Override
    public void remove(Component childComponent) {
        component.removeComponent(WebComponentsHelper.getComposition(childComponent));
        if (childComponent.getId() != null) {
            componentByIds.remove(childComponent.getId());
        }
        ownComponents.remove(childComponent);
    }

    @Override
    public <T extends Component> T getOwnComponent(String id) {
        return (T) componentByIds.get(id);
    }

    @Override
    public <T extends Component> T getComponent(String id) {
        return WebComponentsHelper.getComponent(this, id);
    }

    @Override
    public Collection<Component> getOwnComponents() {
        return Collections.unmodifiableCollection(ownComponents);
    }

    @Override
    public Collection<Component> getComponents() {
        return ComponentsHelper.getComponents(this);
    }

    @Override
    public void requestFocus() {
    }

    @Override
    public boolean isExpandable() {
        return expandable;
    }

    @Override
    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }

    @Override
    public Alignment getAlignment() {
        return alignment;
    }

    @Override
    public void setAlignment(Alignment alignment) {
        this.alignment = alignment;
        final com.vaadin.ui.Component parentComponent = component.getParent();
        if (parentComponent instanceof Layout.AlignmentHandler) {
            ((Layout.AlignmentHandler) parentComponent).setComponentAlignment(component,
                    WebComponentsHelper.convertAlignment(alignment));
        }
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
    public void setSpacing(boolean enabled) {
        component.setSpacing(enabled);
    }
}