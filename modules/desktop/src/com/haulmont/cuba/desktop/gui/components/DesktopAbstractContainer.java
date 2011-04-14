/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.Component;
import net.miginfocom.swing.MigLayout;

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
    protected MigLayout layout;

    protected Collection<Component> ownComponents = new HashSet<Component>();
    protected Map<String, Component> componentByIds = new HashMap<String, Component>();

    public DesktopAbstractContainer() {
        layout = new MigLayout();
        jComponent = new JPanel(layout);
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
    }

    public void setMargin(boolean enable) {
    }

    public void setMargin(boolean topEnable, boolean rightEnable, boolean bottomEnable, boolean leftEnable) {
    }

    public void setSpacing(boolean enabled) {
    }
}
