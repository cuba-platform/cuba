/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.toolkit.ui;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.AbstractComponentContainer;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Layout;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public abstract class Box extends AbstractComponentContainer
        implements Layout.SpacingHandler {

    protected List<Component> components = new LinkedList<Component>();

    private boolean spacing = false;

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        if (isSpacing()) {
            target.addAttribute("spacing", true);
        }
        if (!components.isEmpty()) {
            for (final Component c : components) {
                if (c != null) {
                    c.paint(target);
                }
            }
        }
    }

    public void replaceComponent(Component oldComponent, Component newComponent) {
        //todo gorodnov: need to implement this method
    }

    public Iterator getComponentIterator() {
        return components.iterator();
    }

    @Override
    public void moveComponentsFrom(ComponentContainer source) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addComponent(Component c) {
        super.addComponent(c);
        components.add(c);
        requestRepaint();
    }

    @Override
    public void removeComponent(Component c) {
        super.removeComponent(c);
        components.remove(c);
        requestRepaint();
    }

    public void setSpacing(boolean enabled) {
        spacing = enabled;
    }

    @Deprecated
    public boolean isSpacingEnabled() {
        return spacing;
    }

    public boolean isSpacing() {
        return spacing;
    }
}
