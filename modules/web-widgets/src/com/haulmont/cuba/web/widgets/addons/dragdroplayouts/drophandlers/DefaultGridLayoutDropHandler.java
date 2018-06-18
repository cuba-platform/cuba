/*
 * Copyright 2015 John Ahlroos
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.haulmont.cuba.web.widgets.addons.dragdroplayouts.drophandlers;

import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.ui.AbsoluteLayout.ComponentPosition;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.SingleComponentContainer;

import com.haulmont.cuba.web.widgets.addons.dragdroplayouts.DDAbsoluteLayout;
import com.haulmont.cuba.web.widgets.addons.dragdroplayouts.DDGridLayout;
import com.haulmont.cuba.web.widgets.addons.dragdroplayouts.DDGridLayout.GridLayoutTargetDetails;
import com.haulmont.cuba.web.widgets.addons.dragdroplayouts.events.LayoutBoundTransferable;

/**
 * A default drop handler for GridLayout
 * 
 * @author John Ahlroos / www.jasoft.fi
 * @since 0.6.0
 */
@SuppressWarnings("serial")
public class DefaultGridLayoutDropHandler
        extends AbstractDefaultLayoutDropHandler {

    private final Alignment dropAlignment;

    /**
     * Default constructor
     */
    public DefaultGridLayoutDropHandler() {
        dropAlignment = Alignment.MIDDLE_CENTER;
    }

    /**
     * Constructor
     * 
     * @param dropCellAlignment
     *            The cell alignment of the component after it has been dropped
     */
    public DefaultGridLayoutDropHandler(Alignment dropCellAlignment) {
        this.dropAlignment = dropCellAlignment;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.haulmont.cuba.web.widgets.addons.dragdroplayouts.drophandlers.AbstractDefaultLayoutDropHandler
     * #handleComponentReordering(com.vaadin.event.dd.DragAndDropEvent)
     */
    @Override
    protected void handleComponentReordering(DragAndDropEvent event) {
        GridLayoutTargetDetails details = (GridLayoutTargetDetails) event
                .getTargetDetails();
        DDGridLayout layout = (DDGridLayout) details.getTarget();
        LayoutBoundTransferable transferable = (LayoutBoundTransferable) event
                .getTransferable();
        Component comp = transferable.getComponent();

        int row = details.getOverRow();
        int column = details.getOverColumn();
        if (layout.getComponent(column, row) == null) {
            layout.removeComponent(comp);
            addComponent(event, comp, column, row);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.haulmont.cuba.web.widgets.addons.dragdroplayouts.drophandlers.AbstractDefaultLayoutDropHandler
     * #handleDropFromLayout(com.vaadin.event.dd.DragAndDropEvent)
     */
    @Override
    protected void handleDropFromLayout(DragAndDropEvent event) {
        LayoutBoundTransferable transferable = (LayoutBoundTransferable) event
                .getTransferable();
        GridLayoutTargetDetails details = (GridLayoutTargetDetails) event
                .getTargetDetails();
        DDGridLayout layout = (DDGridLayout) details.getTarget();
        Component source = event.getTransferable().getSourceComponent();
        Component comp = transferable.getComponent();

        if (comp == layout) {
            // Dropping myself on myself, if parent is absolute layout then
            // move
            if (comp.getParent() instanceof DDAbsoluteLayout) {
                MouseEventDetails mouseDown = transferable.getMouseDownEvent();
                MouseEventDetails mouseUp = details.getMouseEvent();
                int movex = mouseUp.getClientX() - mouseDown.getClientX();
                int movey = mouseUp.getClientY() - mouseDown.getClientY();

                DDAbsoluteLayout parent = (DDAbsoluteLayout) comp.getParent();
                ComponentPosition position = parent.getPosition(comp);

                float x = position.getLeftValue() + movex;
                float y = position.getTopValue() + movey;
                position.setLeft(x, Sizeable.UNITS_PIXELS);
                position.setTop(y, Sizeable.UNITS_PIXELS);

                return;
            }

        } else {

            // Check that we are not dragging an outer layout into an inner
            // layout
            Component parent = layout.getParent();
            while (parent != null) {
                if (parent == comp) {
                    return;
                }
                parent = parent.getParent();
            }

            // Detach from old source
            if (source instanceof ComponentContainer) {
                ((ComponentContainer) source).removeComponent(comp);
            } else if (source instanceof SingleComponentContainer) {
                ((SingleComponentContainer) source).setContent(null);
            }
        }

        int row = details.getOverRow();
        int column = details.getOverColumn();
        addComponent(event, comp, column, row);
    }

    @Override
    protected void handleHTML5Drop(DragAndDropEvent event) {
        GridLayoutTargetDetails details = (GridLayoutTargetDetails) event
                .getTargetDetails();
        int row = details.getOverRow();
        int column = details.getOverColumn();
        addComponent(event, resolveComponentFromHTML5Drop(event), column, row);
    }

    protected void addComponent(DragAndDropEvent event, Component component,
            int column, int row) {
        GridLayoutTargetDetails details = (GridLayoutTargetDetails) event
                .getTargetDetails();
        DDGridLayout layout = (DDGridLayout) details.getTarget();

        // If no components exist in the grid, then just add the
        // component
        if (!layout.getComponentIterator().hasNext()) {
            layout.addComponent(component, column, row);
            return;
        }

        // If component was dropped on top of another component, abort
        if (layout.getComponent(column, row) != null) {
            return;
        }

        // Add the component

        layout.addComponent(component, column, row);
        // Add component alignment if given
        if (dropAlignment != null) {
            layout.setComponentAlignment(component, dropAlignment);
        }
    }

    @Override
    public Class<GridLayout> getTargetLayoutType() {
        return GridLayout.class;
    }

}
