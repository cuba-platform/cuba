/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.gui.ComponentsHelper;
import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.ScrollBoxLayout;
import org.apache.commons.lang.ObjectUtils;

import javax.swing.*;
import java.util.Collection;
import java.util.Collections;

/**
 * <p>$Id$</p>
 *
 * @author Alexander Budarov
 */
public class DesktopScrollBoxLayout extends DesktopAbstractComponent<JScrollPane> implements ScrollBoxLayout, AutoExpanding {

    private Component component;

    public DesktopScrollBoxLayout() {
        impl = new JScrollPane();
        // by default it is turned off
        impl.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }

    @Override
    public void expand(Component component) {
        expand(component, "", "");
    }

    @Override
    public void expand(Component component, String height, String width) {
        if (component != this.component) {
            throw new RuntimeException("Component is not in scroll box");
        }
    }

    @Override
    public void add(Component component) {
        JComponent composition = DesktopComponentsHelper.getComposition(component);
        impl.setViewportView(composition);
        this.component = component;
    }

    @Override
    public void remove(Component component) {
        if (this.component == component) {
            impl.setViewportView(null);
            this.component = null;
        }
    }

    @Override
    public <T extends Component> T getOwnComponent(String id) {
        return component != null && ObjectUtils.equals(component.getId(), id) ? (T) component : null;
    }

    @Override
    public <T extends Component> T getComponent(String id) {
        return DesktopComponentsHelper.<T>getComponent(this, id);
    }

    @Override
    public Collection<Component> getOwnComponents() {
        return component == null ? Collections.<Component>emptyList() : Collections.singletonList(component);
    }

    @Override
    public Collection<Component> getComponents() {
        return ComponentsHelper.getComponents(this);
    }

    @Override
    public boolean expandsWidth() {
        return true;
    }

    @Override
    public boolean expandsHeight() {
        return true;
    }
}
