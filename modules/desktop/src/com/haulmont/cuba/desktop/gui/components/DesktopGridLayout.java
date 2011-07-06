/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.desktop.sys.layout.GridLayoutAdapter;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.GridLayout;

import javax.swing.*;
import java.util.*;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class DesktopGridLayout
        extends DesktopAbstractComponent<JPanel>
        implements GridLayout
{
    protected GridLayoutAdapter layoutAdapter;

    protected Collection<Component> ownComponents = new HashSet<Component>();
    protected Map<String, Component> componentByIds = new HashMap<String, Component>();

    public DesktopGridLayout() {
        impl = new JPanel();
        layoutAdapter = GridLayoutAdapter.create(impl);
    }

    public float getColumnExpandRatio(int col) {
        return 0;
    }

    public void setColumnExpandRatio(int col, float ratio) {
    }

    public float getRowExpandRatio(int col) {
        return 0;
    }

    public void setRowExpandRatio(int col, float ratio) {
    }

    public void add(Component component, int col, int row) {
        add(component, col, row, col, row);
    }

    public void add(Component component, int col, int row, int col2, int row2) {
        final JComponent composition = DesktopComponentsHelper.getComposition(component);
        impl.add(composition, layoutAdapter.getConstraints(component, col, row, col2, row2));
        //setComponentAlignment(itmillComponent, WebComponentsHelper.convertAlignment(component.getAlignment()));

        if (component.getId() != null) {
            componentByIds.put(component.getId(), component);
            if (frame != null) {
                frame.registerComponent(component);
            }
        }
        ownComponents.add(component);
    }

    public int getRows() {
        return layoutAdapter.getRows();
    }

    public void setRows(int rows) {
        layoutAdapter.setRows(rows);
    }

    public int getColumns() {
        return layoutAdapter.getColumns();
    }

    public void setColumns(int columns) {
        layoutAdapter.setColumns(columns);
    }

    public void add(Component component) {
        final JComponent composition = DesktopComponentsHelper.getComposition(component);
        impl.add(composition, layoutAdapter.getConstraints(component));
        //setComponentAlignment(itmillComponent, WebComponentsHelper.convertAlignment(component.getAlignment()));

        if (component.getId() != null) {
            componentByIds.put(component.getId(), component);
            if (frame != null) {
                frame.registerComponent(component);
            }
        }
        ownComponents.add(component);
    }

    public void remove(Component component) {
        impl.remove(DesktopComponentsHelper.getComposition(component));
        if (component.getId() != null) {
            componentByIds.remove(component.getId());
        }
        ownComponents.remove(component);
    }

    public <T extends Component> T getOwnComponent(String id) {
        return (T) componentByIds.get(id);
    }

    public <T extends Component> T getComponent(String id) {
        return DesktopComponentsHelper.<T>getComponent(this, id);
    }

    public Collection<Component> getOwnComponents() {
        return Collections.unmodifiableCollection(ownComponents);
    }

    public Collection<Component> getComponents() {
        return ComponentsHelper.getComponents(this);
    }

    public void setMargin(boolean enable) {
        layoutAdapter.setMargin(enable);
    }

    public void setMargin(boolean topEnable, boolean rightEnable, boolean bottomEnable, boolean leftEnable) {
        layoutAdapter.setMargin(topEnable, rightEnable, bottomEnable, leftEnable);
    }

    public void setSpacing(boolean enabled) {
        layoutAdapter.setSpacing(enabled);
    }
}
