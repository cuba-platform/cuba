/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.desktop.sys.layout.LayoutAdapter;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.Component;

import javax.swing.*;
import java.util.*;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public abstract class DesktopAbstractContainer
        extends DesktopAbstractComponent<JPanel>
        implements Component.Container, Component.Expandable, Component.BelongToFrame, com.haulmont.cuba.gui.components.BoxLayout
{
    protected LayoutAdapter layoutAdapter;

    protected Collection<Component> ownComponents = new HashSet<Component>();
    protected Map<String, Component> componentByIds = new HashMap<String, Component>();

    public DesktopAbstractContainer() {
        jComponent = new JPanel();
        layoutAdapter = LayoutAdapter.create(jComponent);
    }

    public void add(Component component) {
        JComponent composition = DesktopComponentsHelper.getComposition(component);
        jComponent.add(composition);
//        setComponentAlignment(itmillComponent, WebComponentsHelper.convertAlignment(component.getAlignment()));

        if (component.getId() != null) {
            componentByIds.put(component.getId(), component);
            if (frame != null) {
                frame.registerComponent(component);
            }
        }
        ownComponents.add(component);
    }

    public void remove(Component component) {
        JComponent composition = DesktopComponentsHelper.getComposition(component);
        jComponent.remove(composition);

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

    public void expand(Component component, String height, String width) {
        JComponent composition = DesktopComponentsHelper.getComposition(component);
        layoutAdapter.expand(composition, height, width);
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
