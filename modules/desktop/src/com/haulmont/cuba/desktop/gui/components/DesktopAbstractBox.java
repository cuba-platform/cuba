/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.desktop.sys.layout.BoxLayoutAdapter;
import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.Component;

import javax.swing.*;
import java.util.*;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public abstract class DesktopAbstractBox
        extends DesktopAbstractComponent<JPanel>
        implements com.haulmont.cuba.gui.components.BoxLayout
{
    protected BoxLayoutAdapter layoutAdapter;

    protected Collection<Component> ownComponents = new HashSet<Component>();
    protected Map<String, Component> componentByIds = new HashMap<String, Component>();

    public DesktopAbstractBox() {
        impl = new JPanel();
        layoutAdapter = BoxLayoutAdapter.create(impl);
    }

    public void add(Component component) {
        JComponent composition = DesktopComponentsHelper.getComposition(component);
        impl.add(composition);
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
        impl.remove(composition);

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

    public void expand(Component component) {
        expand(component, "", "");
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
