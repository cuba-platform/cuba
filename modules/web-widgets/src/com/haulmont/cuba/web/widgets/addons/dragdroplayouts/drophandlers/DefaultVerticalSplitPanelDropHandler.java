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
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.SingleComponentContainer;
import com.vaadin.ui.VerticalSplitPanel;

import com.haulmont.cuba.web.widgets.addons.dragdroplayouts.DDVerticalSplitPanel;
import com.haulmont.cuba.web.widgets.addons.dragdroplayouts.DDVerticalSplitPanel.VerticalSplitPanelTargetDetails;
import com.haulmont.cuba.web.widgets.addons.dragdroplayouts.events.LayoutBoundTransferable;
import com.haulmont.cuba.web.widgets.addons.dragdroplayouts.events.VerticalLocationIs;

/**
 * Default drop handler for vertical splitpanels
 * 
 * @author John Ahlroos / www.jasoft.fi
 * @since 0.6.0
 */
@SuppressWarnings("serial")
public class DefaultVerticalSplitPanelDropHandler
        extends AbstractDefaultLayoutDropHandler {

    @Override
    public AcceptCriterion getAcceptCriterion() {
        // Only allow dropping in slots, not on the center bar
        return new Not(VerticalLocationIs.MIDDLE);
    }

    @Override
    protected void handleComponentReordering(DragAndDropEvent event) {
        handleDropFromLayout(event);
    }

    @Override
    protected void handleDropFromLayout(DragAndDropEvent event) {
        LayoutBoundTransferable transferable = (LayoutBoundTransferable) event
                .getTransferable();
        VerticalSplitPanelTargetDetails details = (VerticalSplitPanelTargetDetails) event
                .getTargetDetails();
        Component component = transferable.getComponent();
        DDVerticalSplitPanel panel = (DDVerticalSplitPanel) details.getTarget();
        ComponentContainer source = (ComponentContainer) transferable
                .getSourceComponent();

        // Detach from old source
        if (source instanceof ComponentContainer) {
            ((ComponentContainer) source).removeComponent(component);
        } else if (source instanceof SingleComponentContainer) {
            ((SingleComponentContainer) source).setContent(null);
        }

        if (details.getDropLocation() == VerticalDropLocation.TOP) {
            // Dropped in the left area
            panel.setFirstComponent(component);

        } else if (details.getDropLocation() == VerticalDropLocation.BOTTOM) {
            // Dropped in the right area
            panel.setSecondComponent(component);
        }
    }

    @Override
    protected void handleHTML5Drop(DragAndDropEvent event) {
        VerticalSplitPanelTargetDetails details = (VerticalSplitPanelTargetDetails) event
                .getTargetDetails();
        DDVerticalSplitPanel panel = (DDVerticalSplitPanel) details.getTarget();

        if (details.getDropLocation() == VerticalDropLocation.TOP) {
            // Dropped in the left area
            panel.setFirstComponent(resolveComponentFromHTML5Drop(event));

        } else if (details.getDropLocation() == VerticalDropLocation.BOTTOM) {
            // Dropped in the right area
            panel.setSecondComponent(resolveComponentFromHTML5Drop(event));
        }
    }

    @Override
    public Class<VerticalSplitPanel> getTargetLayoutType() {
        return VerticalSplitPanel.class;
    }
}
