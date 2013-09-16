/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.toolkit.ui;

import com.vaadin.data.Container;
import com.vaadin.server.PaintException;
import com.vaadin.server.PaintTarget;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;

import java.io.Serializable;
import java.util.*;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaWidgetsTree extends com.vaadin.ui.Tree implements ComponentContainer {

    protected WidgetBuilder widgetBuilder;

    protected List<Component> nodeWidgets;

    protected List<Object> itemIds;

    @Override
    public void containerItemSetChange(Container.ItemSetChangeEvent event) {
        super.containerItemSetChange(event);

        refreshRenderedComponents();
    }

    @Override
    public void containerPropertySetChange(Container.PropertySetChangeEvent event) {
        super.containerPropertySetChange(event);

        refreshRenderedComponents();
    }

    protected void refreshRenderedComponents() {
        nodeWidgets = new ArrayList<>();
        itemIds = new ArrayList<>();

        if (widgetBuilder != null) {
            // Iterates through hierarchical tree using a stack of iterators
            final Stack<Iterator<?>> iteratorStack = new Stack<>();
            Collection<?> ids = rootItemIds();

            if (ids != null) {
                iteratorStack.push(ids.iterator());
            }

            while (!iteratorStack.isEmpty()) {

                // Gets the iterator for current tree level
                final Iterator<?> i = iteratorStack.peek();

                // If the level is finished, back to previous tree level
                if (!i.hasNext()) {

                    // Removes used iterator from the stack
                    iteratorStack.pop();
                } else {
                    final Object itemId = i.next();

                    itemIds.add(itemId);

                    Component c = widgetBuilder.buildWidget(this, itemId, areChildrenAllowed(itemId));
                    c.setParent(this);
                    c.markAsDirty();

                    nodeWidgets.add(c);

                    if (hasChildren(itemId) && areChildrenAllowed(itemId)) {
                        iteratorStack.push(getChildren(itemId).iterator());
                    }
                }
            }
        }
    }

    @Override
    protected void paintItem(
            PaintTarget target,
            Object itemId,
            LinkedList<String> selectedKeys,
            LinkedList<String> expandedKeys
    ) throws PaintException {
        super.paintItem(target, itemId, selectedKeys, expandedKeys);

        if (itemIds != null && itemIds.indexOf(itemId) >= 0) {
            target.addAttribute("widgetIndex", itemIds.indexOf(itemId));
        }
    }

    public WidgetBuilder getWidgetBuilder() {
        return widgetBuilder;
    }

    public void setWidgetBuilder(WidgetBuilder widgetBuilder) {
        if (this.widgetBuilder != widgetBuilder) {
            this.widgetBuilder = widgetBuilder;
            refreshRenderedComponents();
        }
    }

    @Override
    public Iterator<Component> iterator() {
        if (nodeWidgets != null)
            return nodeWidgets.iterator();
        else
            return Collections.<Component>emptyList().iterator();
    }

    @Override
    public Iterator<Component> getComponentIterator() {
        return iterator();
    }

    @Override
    public int getComponentCount() {
        if (nodeWidgets != null) {
            return nodeWidgets.size();
        } else {
            return 0;
        }
    }

    @Override
    public void addComponent(Component c) {
        // do nothing
    }

    @Override
    public void addComponents(Component... nodeWidgets) {
        // do nothing
    }

    @Override
    public void removeComponent(Component c) {
        // do nothing
    }

    @Override
    public void removeAllComponents() {
        // do nothing
    }

    @Override
    public void replaceComponent(Component oldComponent, Component newComponent) {
        // do nothing
    }

    @Override
    public void moveComponentsFrom(ComponentContainer source) {
        // do nothing
    }

    @Override
    public void addListener(ComponentAttachListener listener) {
        // do nothing
    }

    @Override
    public void removeListener(ComponentAttachListener listener) {
        // do nothing
    }

    @Override
    public void addListener(ComponentDetachListener listener) {
        // do nothing
    }

    @Override
    public void removeListener(ComponentDetachListener listener) {
        // do nothing
    }

    @Override
    public void addComponentAttachListener(ComponentAttachListener listener) {
        // do nothing
    }

    @Override
    public void removeComponentAttachListener(ComponentAttachListener listener) {
        // do nothing
    }

    @Override
    public void addComponentDetachListener(ComponentDetachListener listener) {
        // do nothing
    }

    @Override
    public void removeComponentDetachListener(ComponentDetachListener listener) {
        // do nothing
    }

    public interface WidgetBuilder extends Serializable {
        Component buildWidget(CubaWidgetsTree source, Object itemId, boolean leaf);
    }
}