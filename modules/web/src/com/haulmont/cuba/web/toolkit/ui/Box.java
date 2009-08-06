/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 05.08.2009 18:21:08
 *
 * $Id$
 */
package com.haulmont.cuba.web.toolkit.ui;

import com.itmill.toolkit.ui.AbstractComponentContainer;
import com.itmill.toolkit.ui.Component;
import com.itmill.toolkit.ui.ComponentContainer;
import com.itmill.toolkit.ui.Layout;
import com.itmill.toolkit.terminal.PaintTarget;
import com.itmill.toolkit.terminal.PaintException;

import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;

public abstract class Box extends AbstractComponentContainer
        implements Layout.SpacingHandler{

    protected List<Component> components = new LinkedList<Component>();

    private boolean spacing = false;

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        if (isSpacingEnabled()) {
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

    public boolean isSpacingEnabled() {
        return spacing;
    }
}
