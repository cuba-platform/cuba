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
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.DropTarget;
import com.vaadin.event.dd.TargetDetails;
import com.vaadin.event.dd.acceptcriteria.AcceptAll;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.ui.AbsoluteLayout.ComponentPosition;
import com.vaadin.ui.Component;
import com.vaadin.ui.HasComponents;
import com.vaadin.ui.Label;

import com.haulmont.cuba.web.widgets.addons.dragdroplayouts.DDAbsoluteLayout;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.Constants;
import com.haulmont.cuba.web.widgets.addons.dragdroplayouts.events.LayoutBoundTransferable;

/**
 * Abstract class for layout drop handlers
 * 
 * @author John Ahlroos / www.jasoft.fi
 * @since 0.7.0
 */
@SuppressWarnings("serial")
public abstract class AbstractDefaultLayoutDropHandler implements DropHandler {

    protected abstract void handleComponentReordering(DragAndDropEvent event);

    protected abstract void handleDropFromLayout(DragAndDropEvent event);

    public abstract Class<? extends HasComponents> getTargetLayoutType();

    protected void handleHTML5Drop(DragAndDropEvent event) {
        throw new RuntimeException("HTML5 support not implemented by layout");
    }

    protected Component resolveComponentFromHTML5Drop(DragAndDropEvent event) {
        String text = event.getTransferable().getData("html5Data").toString();
        return new Label(text);
    }

    /**
     * Handles a drop by a component which has an absolute layout as parent. In
     * this case the component is moved.
     * 
     * @param event
     *            The drag and drop event
     */
    protected void handleDropFromAbsoluteParentLayout(DragAndDropEvent event) {
        LayoutBoundTransferable transferable = (LayoutBoundTransferable) event
                .getTransferable();
        TargetDetails details = event.getTargetDetails();
        MouseEventDetails mouseDown = transferable.getMouseDownEvent();
        MouseEventDetails mouseUp = MouseEventDetails.deSerialize(
                (String) details.getData(Constants.DROP_DETAIL_MOUSE_EVENT));
        int movex = mouseUp.getClientX() - mouseDown.getClientX();
        int movey = mouseUp.getClientY() - mouseDown.getClientY();
        Component comp = transferable.getComponent();

        DDAbsoluteLayout parent = (DDAbsoluteLayout) comp.getParent();
        ComponentPosition position = parent.getPosition(comp);

        if (position.getLeftValue() != null) {
        	float x = position.getLeftValue() + movex;
        	position.setLeft(x, Sizeable.UNITS_PIXELS);
        }

        if (position.getRightValue() != null) {
        	float x = position.getRightValue() - movex;
        	position.setRight(x, Sizeable.UNITS_PIXELS);
        }

        if (position.getTopValue() != null) {
	        float y = position.getTopValue() + movey;
	        position.setTop(y, Sizeable.UNITS_PIXELS);
        }

        if (position.getBottomValue() != null) {
        	float y = position.getBottomValue() - movey;
	        position.setBottom(y, Sizeable.UNITS_PIXELS);
        }
    }

    public void drop(DragAndDropEvent event) {
        // Get information about the drop
        TargetDetails details = event.getTargetDetails();
        DropTarget layout = details.getTarget();
        Component source = event.getTransferable().getSourceComponent();

        if (event.getTransferable().getData("html5Data") != null) {
            handleHTML5Drop(event);
        } else if (layout == source) {
            handleComponentReordering(event);
        } else if (event.getTransferable() instanceof LayoutBoundTransferable) {
            LayoutBoundTransferable transferable = (LayoutBoundTransferable) event
                    .getTransferable();
            Component comp = transferable.getComponent();
            if (comp == layout) {
                if (comp.getParent() instanceof DDAbsoluteLayout) {
                    handleDropFromAbsoluteParentLayout(event);
                }
            } else {
                handleDropFromLayout(event);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vaadin.event.dd.DropHandler#getAcceptCriterion()
     */
    public AcceptCriterion getAcceptCriterion() {
        return AcceptAll.get();
    }

}
