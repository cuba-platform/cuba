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
package com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.formlayout;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ui.dd.VAcceptCallback;
import com.vaadin.client.ui.dd.VDragEvent;

import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.VDDAbstractDropHandler;

public class VDDFormLayoutDropHandler
        extends VDDAbstractDropHandler<VDDFormLayout> {

    public VDDFormLayoutDropHandler(ComponentConnector connector) {
        super(connector);
    }

    @Override
    protected void dragAccepted(VDragEvent drag) {
        dragOver(drag);
    }

    @Override
    public boolean drop(VDragEvent drag) {

        // Un-emphasis any selections
        getLayout().emphasis(null, null);

        // Update the details
        getLayout().updateDragDetails(getTableRowWidgetFromDragEvent(drag),
                drag);
        return getLayout().postDropHook(drag) && super.drop(drag);
    };

    private Widget getTableRowWidgetFromDragEvent(VDragEvent event) {

        /**
         * Find the widget of the row
         */
        Element e = event.getElementOver();

        if (getLayout().table.getRowCount() == 0) {
            /*
             * Empty layout
             */
            return getLayout();
        }

        /**
         * Check if element is inside one of the table widgets
         */
        for (int i = 0; i < getLayout().table.getRowCount(); i++) {
            Element caption = getLayout().table
                    .getWidget(i, getLayout().COLUMN_CAPTION).getElement();
            Element error = getLayout().table
                    .getWidget(i, getLayout().COLUMN_ERRORFLAG).getElement();
            Element widget = getLayout().table
                    .getWidget(i, getLayout().COLUMN_WIDGET).getElement();
            if (caption.isOrHasChild(e) || error.isOrHasChild(e)
                    || widget.isOrHasChild(e)) {
                return getLayout().table.getWidget(i,
                        getLayout().COLUMN_WIDGET);
            }
        }

        /*
         * Is the element a element outside the row structure but inside the
         * layout
         */
        Element rowElement = getLayout().getRowFromChildElement(e,
                getLayout().getElement());
        if (rowElement != null) {
            Element tableElement = rowElement.getParentElement();
            for (int i = 0; i < tableElement.getChildCount(); i++) {
                Element r = tableElement.getChild(i).cast();
                if (r.equals(rowElement)) {
                    return getLayout().table.getWidget(i,
                            getLayout().COLUMN_WIDGET);
                }
            }
        }

        /*
         * Element was not found in rows so defaulting to the form layout
         * instead
         */
        return getLayout();
    }

    @Override
    public void dragOver(VDragEvent drag) {

        // Remove any emphasis
        getLayout().emphasis(null, null);

        // Update the drop details so we can validate the drop
        Widget c = getTableRowWidgetFromDragEvent(drag);
        if (c != null) {
            getLayout().updateDragDetails(c, drag);
        } else {
            getLayout().updateDragDetails(getLayout(), drag);
        }

        getLayout().postOverHook(drag);

        // Validate the drop
        validate(new VAcceptCallback() {
            public void accepted(VDragEvent event) {
                Widget c = getTableRowWidgetFromDragEvent(event);
                if (c != null) {
                    getLayout().emphasis(c, event);
                } else {
                    getLayout().emphasis(getLayout(), event);
                }
            }
        }, drag);
    };

    @Override
    public void dragEnter(VDragEvent drag) {
        getLayout().emphasis(null, null);

        Widget c = getTableRowWidgetFromDragEvent(drag);
        if (c != null) {
            getLayout().updateDragDetails(c, drag);
        } else {
            getLayout().updateDragDetails(getLayout(), drag);
        }

        getLayout().postEnterHook(drag);

        super.dragEnter(drag);
    }

    @Override
    public void dragLeave(VDragEvent drag) {
        getLayout().emphasis(null, drag);
        getLayout().postLeaveHook(drag);
    }
}
