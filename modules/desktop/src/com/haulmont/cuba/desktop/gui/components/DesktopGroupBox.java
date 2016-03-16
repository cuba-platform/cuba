/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.desktop.sys.layout.BoxLayoutAdapter;
import com.haulmont.cuba.desktop.sys.vcl.CollapsiblePanel;
import com.haulmont.cuba.gui.components.GroupBoxLayout;
import com.haulmont.cuba.gui.components.compatibility.ComponentExpandCollapseListenerWrapper;
import org.apache.commons.lang.BooleanUtils;
import org.dom4j.Element;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 */
public class DesktopGroupBox extends DesktopAbstractBox implements GroupBoxLayout, AutoExpanding {

    protected Orientation orientation = Orientation.VERTICAL;

    protected CollapsiblePanel collapsiblePanel;

    protected List<ExpandedStateChangeListener> expandedStateChangeListeners;

    protected boolean settingsEnabled = true;

    public DesktopGroupBox() {
        collapsiblePanel = new CollapsiblePanel(super.getComposition());
        collapsiblePanel.addCollapseListener(new CollapsiblePanel.CollapseListener() {
            @Override
            public void collapsed() {
                fireExpandStateChange(false);
            }

            @Override
            public void expanded() {
                fireExpandStateChange(true);
            }
        });
        layoutAdapter.setFlowDirection(BoxLayoutAdapter.FlowDirection.Y);

        setWidth("100%");
    }

    @Override
    public boolean isExpanded() {
        return collapsiblePanel.isExpanded();
    }

    @Override
    public ExpandDirection getExpandDirection() {
        return orientation == Orientation.HORIZONTAL ? ExpandDirection.HORIZONTAL : ExpandDirection.VERTICAL;
    }

    @Override
    public void setExpanded(boolean expanded) {
        collapsiblePanel.setExpanded(expanded);
    }

    @Override
    public boolean isCollapsable() {
        return collapsiblePanel.isCollapsable();
    }

    @Override
    public void setCollapsable(boolean collapsible) {
        collapsiblePanel.setCollapsible(collapsible);
    }

    @Override
    public void addListener(ExpandListener listener) {
        addExpandedStateChangeListener(new ComponentExpandCollapseListenerWrapper(listener));
    }

    @Override
    public void removeListener(ExpandListener listener) {
        removeExpandedStateChangeListener(new ComponentExpandCollapseListenerWrapper(listener));
    }

    @Override
    public void addListener(CollapseListener listener) {
        addExpandedStateChangeListener(new ComponentExpandCollapseListenerWrapper(listener));
    }

    @Override
    public void removeListener(CollapseListener listener) {
        removeExpandedStateChangeListener(new ComponentExpandCollapseListenerWrapper(listener));
    }

    @Override
    public void addExpandedStateChangeListener(ExpandedStateChangeListener listener) {
        if (expandedStateChangeListeners == null) {
            expandedStateChangeListeners = new ArrayList<>();
        }
        if (!expandedStateChangeListeners.contains(listener)) {
            expandedStateChangeListeners.add(listener);
        }
    }

    @Override
    public void removeExpandedStateChangeListener(ExpandedStateChangeListener listener) {
        if (expandedStateChangeListeners != null) {
            expandedStateChangeListeners.remove(listener);
        }
    }

    protected void fireExpandStateChange(boolean expanded) {
        if (expandedStateChangeListeners != null && !expandedStateChangeListeners.isEmpty()) {
            ExpandedStateChangeEvent event = new ExpandedStateChangeEvent(this, expanded);

            for (ExpandedStateChangeListener listener : new ArrayList<>(expandedStateChangeListeners)) {
                listener.expandedStateChanged(event);
            }
        }
    }

    @Override
    public String getCaption() {
        return collapsiblePanel.getCaption();
    }

    @Override
    public void setCaption(String caption) {
        collapsiblePanel.setCaption(caption);
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public void setDescription(String description) {
    }

    @Override
    public boolean isBorderVisible() {
        return true;
    }

    @Override
    public void setBorderVisible(boolean borderVisible) {
        throw new UnsupportedOperationException();
    }

    @Override
    public JComponent getComposition() {
        return collapsiblePanel;
    }

    @Override
    public void applySettings(Element element) {
        if (isSettingsEnabled()) {
            Element groupBoxElement = element.element("groupBox");
            if (groupBoxElement != null) {
                String expanded = groupBoxElement.attributeValue("expanded");
                if (expanded != null) {
                    setExpanded(Boolean.parseBoolean(expanded));
                }
            }
        }
    }

    @Override
    public boolean saveSettings(Element element) {
        if (!isSettingsEnabled()) {
            return false;
        }

        Element groupBoxElement = element.element("groupBox");
        if (groupBoxElement != null) {
            element.remove(groupBoxElement);
        }
        groupBoxElement = element.addElement("groupBox");
        groupBoxElement.addAttribute("expanded", BooleanUtils.toStringTrueFalse(isExpanded()));
        return true;
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
    public Orientation getOrientation() {
        return orientation;
    }

    @Override
    public void setOrientation(Orientation orientation) {
        Objects.requireNonNull(orientation);
        if (orientation == Orientation.VERTICAL) {
            layoutAdapter.setFlowDirection(BoxLayoutAdapter.FlowDirection.Y);
        } else {
            layoutAdapter.setFlowDirection(BoxLayoutAdapter.FlowDirection.X);
        }
        this.orientation = orientation;

        requestContainerUpdate();

        collapsiblePanel.revalidate();
        collapsiblePanel.repaint();
    }

    @Override
    public boolean expandsWidth() {
        return orientation == Orientation.VERTICAL;
    }

    @Override
    public boolean expandsHeight() {
        return orientation == Orientation.HORIZONTAL;
    }
}
