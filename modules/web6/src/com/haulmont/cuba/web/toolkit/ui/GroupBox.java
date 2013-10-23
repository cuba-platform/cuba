/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.toolkit.gwt.client.ui.VGroupBox;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.*;

import java.util.Iterator;
import java.util.Map;

/**
 * @author artamonov
 * @version $Id$
 */
@ClientWidget(VGroupBox.class)
public class GroupBox extends Panel implements ComponentContainer {

    protected ExpandChangeHandler expandChangeHandler = null;

    protected boolean expanded = true;
    protected boolean collapsable;

    public interface ExpandChangeHandler {
        void expandStateChanged(boolean expanded);
    }

    public GroupBox() {
        Layout content = new VerticalActionsLayout();
        setContent(content);

        setWidth(100, UNITS_PERCENTAGE);
    }

    @Override
    public void setWidth(float width, int unit) {
        super.setWidth(width, unit);

        if (getContent() != null) {
            if (width < 0) {
                getContent().setWidth(-1, UNITS_PIXELS);
            } else {
                getContent().setWidth(100, UNITS_PERCENTAGE);
            }
        }
    }

    @Override
    public void setHeight(float height, int unit) {
        super.setHeight(height, unit);

        if (getContent() != null) {
            if (height < 0) {
                getContent().setHeight(-1, UNITS_PIXELS);
            } else {
                getContent().setHeight(100, UNITS_PERCENTAGE);
            }
        }
    }

    @Override
    public void setContent(ComponentContainer content) {
        super.setContent(content);

        if (content != null) {
            if (getHeight() < 0) {
                getContent().setHeight(-1, UNITS_PIXELS);
            } else {
                getContent().setHeight(100, UNITS_PERCENTAGE);
            }

            if (getWidth() < 0) {
                getContent().setWidth(-1, UNITS_PIXELS);
            } else {
                getContent().setWidth(100, UNITS_PERCENTAGE);
            }
        }
    }

    public ExpandChangeHandler getExpandChangeHandler() {
        return expandChangeHandler;
    }

    public void setExpandChangeHandler(ExpandChangeHandler expandChangeHandler) {
        this.expandChangeHandler = expandChangeHandler;
    }

    public boolean isExpanded() {
        return !collapsable || expanded;
    }

    public void setExpanded(boolean expanded) {
        if (collapsable) {
            this.expanded = expanded;
            getContent().setVisible(expanded);
            requestRepaint();
        }
    }

    public boolean isCollapsable() {
        return collapsable;
    }

    public void setCollapsable(boolean collapsable) {
        this.collapsable = collapsable;
        if (collapsable) {
            setExpanded(true);
        }
    }

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        super.paintContent(target);
        target.addAttribute("collapsable", isCollapsable());
        if (isCollapsable()) {
            target.addAttribute("expanded", isExpanded());
        }
    }

    @Override
    public void changeVariables(Object source, Map variables) {
        super.changeVariables(source, variables);
        if (isCollapsable()) {
            if (variables.containsKey("expand")) {
                setExpanded(true);
                getContent().requestRepaintAll();

                fireExpandedStateChanged();

            } else if (variables.containsKey("collapse")) {
                setExpanded(false);

                fireExpandedStateChanged();
            }
        }
    }

    private void fireExpandedStateChanged() {
        if (expandChangeHandler != null) {
            expandChangeHandler.expandStateChanged(expanded);
        }
    }

    @Override
    public void addComponent(Component c) {
        getContent().addComponent(c);
    }

    @Override
    public void removeComponent(Component c) {
        getContent().addComponent(c);
    }

    @Override
    public void removeAllComponents() {
        getContent().removeAllComponents();
    }

    @Override
    public void replaceComponent(Component oldComponent, Component newComponent) {
        getContent().replaceComponent(oldComponent, newComponent);
    }

    @Override
    public Iterator<Component> getComponentIterator() {
        return getContent().getComponentIterator();
    }

    @Override
    public void moveComponentsFrom(ComponentContainer source) {
        getContent().moveComponentsFrom(source);
    }

    @Override
    public void addListener(ComponentAttachListener listener) {
        getContent().addListener(listener);
    }

    @Override
    public void removeListener(ComponentAttachListener listener) {
        getContent().removeListener(listener);
    }

    @Override
    public void addListener(ComponentDetachListener listener) {
        getContent().addListener(listener);
    }

    @Override
    public void removeListener(ComponentDetachListener listener) {
        getContent().removeListener(listener);
    }
}