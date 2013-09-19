/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui;

import com.haulmont.cuba.web.toolkit.ui.client.groupbox.CubaGroupBoxServerRpc;
import com.haulmont.cuba.web.toolkit.ui.client.groupbox.CubaGroupBoxState;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Panel;

import java.util.Iterator;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaGroupBox extends Panel implements ComponentContainer {
    private ExpandChangeHandler expandChangeHandler = null;

    public CubaGroupBox() {
        CubaGroupBoxServerRpc rpc = new CubaGroupBoxServerRpc() {
            @Override
            public void expand() {
                setExpanded(true);
            }

            @Override
            public void collapse() {
                if (getState().collapsable)
                    setExpanded(false);
            }
        };
        registerRpc(rpc);

        Layout content = new CubaVerticalActionsLayout();
        setContent(content);

        setWidth(100, Unit.PERCENTAGE);
    }

    @Override
    protected CubaGroupBoxState getState() {
        return (CubaGroupBoxState) super.getState();
    }

    @Override
    protected CubaGroupBoxState getState(boolean markAsDirty) {
        return (CubaGroupBoxState) super.getState(markAsDirty);
    }

    @Override
    public void setWidth(float width, Unit unit) {
        super.setWidth(width, unit);

        if (getContent() != null) {
            if (width < 0) {
                getContent().setWidth(-1, Unit.PIXELS);
            } else {
                getContent().setWidth(100, Unit.PERCENTAGE);
            }
        }
    }

    @Override
    public void setHeight(float height, Unit unit) {
        super.setHeight(height, unit);

        if (getContent() != null) {
            if (height < 0) {
                getContent().setHeight(-1, Unit.PIXELS);
            } else {
                getContent().setHeight(100, Unit.PERCENTAGE);
            }
        }
    }

    @Override
    public void setContent(Component content) {
        super.setContent(content);

        if (content != null) {
            if (getHeight() < 0) {
                getContent().setHeight(-1, Unit.PIXELS);
            } else {
                getContent().setHeight(100, Unit.PERCENTAGE);
            }

            if (getWidth() < 0) {
                getContent().setWidth(-1, Unit.PIXELS);
            } else {
                getContent().setWidth(100, Unit.PERCENTAGE);
            }
        }
    }

    public boolean isExpanded() {
        return !getState(false).collapsable || getState(false).expanded;
    }

    public void setExpanded(boolean expanded) {
        if (expanded != getState(false).expanded) {
            getContent().setVisible(expanded);
            markAsDirtyRecursive();
        }

        getState().expanded = expanded;
        if (expandChangeHandler != null)
            expandChangeHandler.expandStateChanged(expanded);
    }

    public boolean isCollapsable() {
        return getState(false).collapsable;
    }

    public void setCollapsable(boolean collapsable) {
        getState().collapsable = collapsable;
        if (collapsable)
            setExpanded(true);
    }

    public ExpandChangeHandler getExpandChangeHandler() {
        return expandChangeHandler;
    }

    public void setExpandChangeHandler(ExpandChangeHandler expandChangeHandler) {
        this.expandChangeHandler = expandChangeHandler;
    }

    @Override
    public ComponentContainer getContent() {
        return (ComponentContainer) super.getContent();
    }

    @Override
    public void addComponent(Component c) {
        getContent().addComponent(c);
    }

    @Override
    public void addComponents(Component... components) {
        getContent().addComponents(components);
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
        return getContent().iterator();
    }

    @Override
    public void moveComponentsFrom(ComponentContainer source) {
        getContent().moveComponentsFrom(source);
    }

    @Override
    public void addListener(ComponentAttachListener listener) {
        getContent().addComponentAttachListener(listener);
    }

    @Override
    public void removeListener(ComponentAttachListener listener) {
        getContent().removeComponentAttachListener(listener);
    }

    @Override
    public void addListener(ComponentDetachListener listener) {
        getContent().addComponentDetachListener(listener);
    }

    @Override
    public void removeListener(ComponentDetachListener listener) {
        getContent().removeComponentDetachListener(listener);
    }

    public interface ExpandChangeHandler {
        void expandStateChanged(boolean expanded);
    }
}