/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.gui.components;

import com.haulmont.cuba.desktop.sys.vcl.CollapsiblePanel;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.GroupBox;
import org.apache.commons.lang.BooleanUtils;
import org.dom4j.Element;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <p>$Id$</p>
 *
 * @author krivopustov
 */
public class DesktopGroupBox
        extends DesktopVBox
        implements GroupBox {

    private CollapsiblePanel collapsiblePanel;

    private List<ExpandListener> expandListeners = null;
    private List<CollapseListener> collapseListeners = null;

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

        if (isLayoutDebugEnabled()) {
            collapsiblePanel.setBorder(
                    BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(Color.BLUE),
                            BorderFactory.createEmptyBorder(0, 5, 5, 5)
                    )
            );
        }
    }

    @Override
    public boolean isExpanded() {
        return collapsiblePanel.isExpanded();
    }

    @Override
    public void setExpanded(boolean expanded) {
        collapsiblePanel.setExpanded(expanded);
    }

    @Override
    public boolean isCollapsible() {
        return collapsiblePanel.isCollapsable();
    }

    @Override
    public void setCollapsible(boolean collapsible) {
        collapsiblePanel.setCollapsible(collapsible);
    }

    @Override
    public void addListener(ExpandListener listener) {
        if (expandListeners == null) {
            expandListeners = new ArrayList<ExpandListener>();
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
            collapseListeners = new ArrayList<CollapseListener>();
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
    public void addAction(Action action) {
    }

    @Override
    public void removeAction(Action action) {
    }

    @Override
    public Collection<Action> getActions() {
        return null;
    }

    @Override
    public Action getAction(String id) {
        return null;
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
    public void expandLayout(boolean expandLayout) {
    }

    @Override
    public JComponent getComposition() {
        return collapsiblePanel;
    }

    public void applySettings(Element element) {
        Element groupBoxElement = element.element("groupBox");
        if (groupBoxElement != null) {
            String expanded = groupBoxElement.attributeValue("expanded");
            if (expanded != null) {
                setExpanded(BooleanUtils.toBoolean(expanded));
            }
        }
    }

    public boolean saveSettings(Element element) {
        Element groupBoxElement = element.element("groupBox");
        if (groupBoxElement != null) {
            element.remove(groupBoxElement);
        }
        groupBoxElement = element.addElement("groupBox");
        groupBoxElement.addAttribute("expanded", BooleanUtils.toStringTrueFalse(isExpanded()));
        return true;
    }
}
