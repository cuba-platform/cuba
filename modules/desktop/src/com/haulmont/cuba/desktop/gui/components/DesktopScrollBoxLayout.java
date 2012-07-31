/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.ScrollBoxLayout;
import org.apache.commons.lang.ObjectUtils;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <p>$Id$</p>
 *
 * @author Alexander Budarov
 */
public class DesktopScrollBoxLayout extends DesktopAbstractComponent<JScrollPane> implements ScrollBoxLayout, AutoExpanding {

    protected List<Component> components = new ArrayList<>();
    private Orientation orientation = Orientation.VERTICAL;
    private DesktopAbstractBox content;

    public DesktopScrollBoxLayout() {
        impl = new JScrollPane();
        // by default it is turned off
        impl.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        impl.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        impl.setBorder(null);

        content = new DesktopVBox();
        impl.setViewportView(DesktopComponentsHelper.getComposition(content));
    }

    @Override
    public void add(Component component) {
        DesktopAbstractBox newContent = null;
        if (orientation == Orientation.VERTICAL && !(content instanceof DesktopVBox)) {
            newContent = new DesktopVBox();
        } else if (orientation == Orientation.HORIZONTAL && !(content instanceof DesktopHBox)) {
            newContent = new DesktopHBox();
        }

        if (newContent != null) {
            content = newContent;
            impl.setViewportView(DesktopComponentsHelper.getComposition(content));
        }

        content.add(component);
    }

    @Override
    public void remove(Component component) {
        content.remove(component);
    }

    @Override
    public <T extends Component> T getOwnComponent(String id) {
        return content.getOwnComponent(id);
    }

    @Override
    public <T extends Component> T getComponent(String id) {
        return content.getComponent(id);
    }

    @Override
    public Collection<Component> getOwnComponents() {
        return content.getOwnComponents();
    }

    @Override
    public Collection<Component> getComponents() {
        return content.getComponents();
    }

    @Override
    public boolean expandsWidth() {
        return !(content instanceof AutoExpanding) || ((AutoExpanding) content).expandsWidth();
    }

    @Override
    public boolean expandsHeight() {
        return !(content instanceof AutoExpanding) || ((AutoExpanding) content).expandsHeight();
    }

    @Override
    public Orientation getOrientation() {
        return orientation;
    }

    @Override
    public void setOrientation(Orientation orientation) {
        if (!ObjectUtils.equals(orientation, this.orientation)) {
            if (!components.isEmpty())
                throw new IllegalStateException("Unable to change scrollbox orientation after adding components to it");

            this.orientation = orientation;
        }
    }

    @Override
    public void setMargin(boolean enable) {
        content.setMargin(enable);
    }

    @Override
    public void setMargin(boolean topEnable, boolean rightEnable, boolean bottomEnable, boolean leftEnable) {
        content.setMargin(topEnable, rightEnable, bottomEnable, leftEnable);
    }

    @Override
    public void setSpacing(boolean enabled) {
        content.setSpacing(enabled);
    }
}
