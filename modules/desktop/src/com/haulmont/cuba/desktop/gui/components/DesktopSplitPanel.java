/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.IFrame;
import com.haulmont.cuba.gui.components.SplitPanel;
import org.dom4j.Element;

import javax.swing.*;
import java.util.*;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class DesktopSplitPanel
        extends DesktopAbstractComponent<JSplitPane>
        implements SplitPanel, Component.HasSettings
{
    protected Map<String, Component> componentByIds = new HashMap<String, Component>();
    protected Collection<Component> ownComponents = new HashSet<Component>();

    public DesktopSplitPanel() {
        impl = new JSplitPane();
        impl.setResizeWeight(0.5);
    }

    @Override
    public int getOrientation() {
        return impl.getOrientation() == JSplitPane.HORIZONTAL_SPLIT ? ORIENTATION_HORIZONTAL : ORIENTATION_VERTICAL;
    }

    @Override
    public void setOrientation(int orientation) {
        impl.setOrientation(orientation == ORIENTATION_HORIZONTAL ? JSplitPane.HORIZONTAL_SPLIT : JSplitPane.VERTICAL_SPLIT);
    }

    @Override
    public void setSplitPosition(int pos) {
        setSplitPosition(pos, 0);
    }

    @Override
    public void setSplitPosition(int pos, int unit) {
        if (pos < 0 || pos > 100)
            throw new IllegalArgumentException("Split position must be between 0 and 100");
        impl.setResizeWeight(pos / 100.0);
    }

    @Override
    public void setLocked(boolean locked) {
    }

    @Override
    public boolean isLocked() {
        return false;
    }

    @Override
    public void add(Component component) {
        JComponent jComponent = DesktopComponentsHelper.getComposition(component);
        if (ownComponents.isEmpty())
            impl.setLeftComponent(jComponent);
        else
            impl.setRightComponent(jComponent);

        if (component.getId() != null) {
            componentByIds.put(component.getId(), component);
            if (frame != null) {
                frame.registerComponent(component);
            }
        }
        ownComponents.add(component);
    }

    @Override
    public void remove(Component component) {
        JComponent jComponent = DesktopComponentsHelper.getComposition(component);
        impl.remove(jComponent);

        if (component.getId() != null) {
            componentByIds.remove(component.getId());
        }
        ownComponents.remove(component);
    }

    @Override
    public <T extends Component> T getOwnComponent(String id) {
        return (T) componentByIds.get(id);
    }

    @Override
    public <T extends Component> T getComponent(String id) {
        return DesktopComponentsHelper.<T>getComponent(this, id);
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
    public void applySettings(Element element) {
    }

    @Override
    public boolean saveSettings(Element element) {
        return false;
    }
}
