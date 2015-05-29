/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.desktop.sys.layout.BoxLayoutAdapter;
import com.haulmont.cuba.desktop.sys.vcl.CollapsiblePanel;
import com.haulmont.cuba.gui.components.GroupBoxLayout;
import org.apache.commons.lang.BooleanUtils;
import org.dom4j.Element;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author krivopustov
 * @version $Id$
 */
public class DesktopGroupBox extends DesktopAbstractBox implements GroupBoxLayout, AutoExpanding {

    protected Orientation orientation = Orientation.VERTICAL;

    protected CollapsiblePanel collapsiblePanel;

    protected List<ExpandListener> expandListeners = null;
    protected List<CollapseListener> collapseListeners = null;
    protected boolean settigsEnabled = true;

    public DesktopGroupBox() {
        collapsiblePanel = new CollapsiblePanel(super.getComposition());
        collapsiblePanel.addCollapseListener(new CollapsiblePanel.CollapseListener() {
            @Override
            public void collapsed() {
                fireCollapseListeners();
            }

            @Override
            public void expanded() {
                fireExpandListeners();
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
        if (expandListeners == null) {
            expandListeners = new ArrayList<>();
        }
        expandListeners.add(listener);
    }

    @Override
    public void removeListener(ExpandListener listener) {
        if (expandListeners != null) {
            expandListeners.remove(listener);
            if (expandListeners.isEmpty()) {
                expandListeners = null;
            }
        }
    }

    private void fireExpandListeners() {
        if (expandListeners != null) {
            for (final ExpandListener expandListener : expandListeners) {
                expandListener.onExpand(this);
            }
        }
    }

    @Override
    public void addListener(CollapseListener listener) {
        if (collapseListeners == null) {
            collapseListeners = new ArrayList<>();
        }
        collapseListeners.add(listener);
    }

    @Override
    public void removeListener(CollapseListener listener) {
        if (collapseListeners != null) {
            collapseListeners.remove(listener);
            if (collapseListeners.isEmpty()) {
                collapseListeners = null;
            }
        }
    }

    private void fireCollapseListeners() {
        if (collapseListeners != null) {
            for (final CollapseListener collapseListener : collapseListeners) {
                collapseListener.onCollapse(this);
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
                    setExpanded(BooleanUtils.toBoolean(expanded));
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
        return settigsEnabled;
    }

    @Override
    public void setSettingsEnabled(boolean settingsEnabled) {
        this.settigsEnabled = settingsEnabled;
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
