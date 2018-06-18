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
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.event.dd.acceptcriteria.Not;
import com.vaadin.shared.ui.dd.VerticalDropLocation;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.SingleComponentContainer;
import com.vaadin.ui.TabSheet.Tab;

import com.haulmont.cuba.web.widgets.addons.dragdroplayouts.DDAccordion;
import com.haulmont.cuba.web.widgets.addons.dragdroplayouts.details.AccordionTargetDetails;
import com.haulmont.cuba.web.widgets.addons.dragdroplayouts.events.LayoutBoundTransferable;
import com.haulmont.cuba.web.widgets.addons.dragdroplayouts.events.VerticalLocationIs;

/**
 * A default drop handler for an accordion
 * 
 * @author John Ahlroos / www.jasoft.fi
 * @since 0.6.0
 */
@SuppressWarnings("serial")
public class DefaultAccordionDropHandler
        extends AbstractDefaultLayoutDropHandler {

    /**
     * Called when tabs are being rearranged
     * 
     * @param event
     *            A drag and drop event
     */
    @Override
    protected void handleComponentReordering(DragAndDropEvent event) {
        AccordionTargetDetails details = (AccordionTargetDetails) event
                .getTargetDetails();
        DDAccordion acc = (DDAccordion) details.getTarget();
        VerticalDropLocation location = details.getDropLocation();
        LayoutBoundTransferable transferable = (LayoutBoundTransferable) event
                .getTransferable();
        Component c = transferable.getComponent();
        int idx = details.getOverIndex();

        Tab tab = acc.getTab(c);

        if (location == VerticalDropLocation.TOP) {
            // Left of previous tab
            int originalIndex = acc.getTabPosition(tab);
            if (originalIndex > idx) {
                acc.setTabPosition(tab, idx);
            } else if (idx - 1 >= 0) {
                acc.setTabPosition(tab, idx - 1);
            }

        } else if (location == VerticalDropLocation.BOTTOM) {
            // Right of previous tab
            int originalIndex = acc.getTabPosition(tab);
            if (originalIndex > idx) {
                acc.setTabPosition(tab, idx + 1);
            } else {
                acc.setTabPosition(tab, idx);
            }
        }
    }

    /**
     * Adds a new tab from the drop
     * 
     * @param event
     *            The drag and drop event
     */
    @Override
    protected void handleDropFromLayout(DragAndDropEvent event) {
        LayoutBoundTransferable transferable = (LayoutBoundTransferable) event
                .getTransferable();

        // Get the target details
        AccordionTargetDetails details = (AccordionTargetDetails) event
                .getTargetDetails();
        DDAccordion acc = (DDAccordion) details.getTarget();
        Component c = transferable.getComponent();
        int idx = details.getOverIndex();
        VerticalDropLocation location = details.getDropLocation();

        // Detach from old source
        Component source = transferable.getSourceComponent();
        if (source instanceof ComponentContainer) {
            ((ComponentContainer) source).removeComponent(c);
        } else if (source instanceof SingleComponentContainer) {
            ((SingleComponentContainer) source).setContent(null);
        }

        if (location == VerticalDropLocation.TOP) {
            acc.addTab(c, idx);
        } else if (location == VerticalDropLocation.BOTTOM) {
            acc.addTab(c, idx + 1);
        } else {
            acc.addTab(c);
        }
    }

    @Override
    protected void handleHTML5Drop(DragAndDropEvent event) {
        AccordionTargetDetails details = (AccordionTargetDetails) event
                .getTargetDetails();
        VerticalDropLocation location = details.getDropLocation();
        DDAccordion acc = (DDAccordion) details.getTarget();
        int idx = details.getOverIndex();

        Component c = resolveComponentFromHTML5Drop(event);
        c.setCaption(resolveCaptionFromHTML5Drop(event));

        if (location == VerticalDropLocation.TOP) {
            acc.addTab(c, idx);
        } else if (location == VerticalDropLocation.BOTTOM) {
            acc.addTab(c, idx + 1);
        } else {
            acc.addTab(c);
        }
    }

    protected String resolveCaptionFromHTML5Drop(DragAndDropEvent event) {
        return event.getTransferable().getData("html5Data").toString();
    }

    @Override
    public AcceptCriterion getAcceptCriterion() {
        return new Not(VerticalLocationIs.MIDDLE);
    }

    @Override
    public Class<Accordion> getTargetLayoutType() {
        return Accordion.class;
    }
}
