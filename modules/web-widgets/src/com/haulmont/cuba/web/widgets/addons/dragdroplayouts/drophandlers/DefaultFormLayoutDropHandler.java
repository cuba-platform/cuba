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
import com.vaadin.event.dd.acceptcriteria.Or;
import com.vaadin.event.dd.acceptcriteria.TargetDetailIs;
import com.vaadin.shared.ui.dd.VerticalDropLocation;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.SingleComponentContainer;

import com.haulmont.cuba.web.widgets.addons.dragdroplayouts.DDFormLayout;
import com.haulmont.cuba.web.widgets.addons.dragdroplayouts.DDFormLayout.FormLayoutTargetDetails;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.Constants;
import com.haulmont.cuba.web.widgets.addons.dragdroplayouts.events.LayoutBoundTransferable;
import com.haulmont.cuba.web.widgets.addons.dragdroplayouts.events.VerticalLocationIs;

/**
 * Default drop handler for Form layouts
 * 
 * @author John Ahlroos / www.jasoft.fi
 * @since 0.8.0
 */
public class DefaultFormLayoutDropHandler
        extends AbstractDefaultLayoutDropHandler {

    private Alignment dropAlignment;

    /**
     * Constructor
     * 
     */
    public DefaultFormLayoutDropHandler() {

    }

    /**
     * Constructor
     * 
     * @param dropCellAlignment
     *            The cell alignment of the component after it has been dropped
     */
    public DefaultFormLayoutDropHandler(Alignment dropCellAlignment) {
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
        LayoutBoundTransferable transferable = (LayoutBoundTransferable) event
                .getTransferable();
        FormLayoutTargetDetails details = (FormLayoutTargetDetails) event
                .getTargetDetails();
        DDFormLayout layout = (DDFormLayout) details.getTarget();

        Component comp = transferable.getComponent();
        int idx = details.getOverIndex();
        int oldIdx = layout.getComponentIndex(comp);

        if (idx == oldIdx) {
            // Dropping on myself
            return;
        }

        // Detach
        layout.removeComponent(comp);
        if (idx > 0 && idx > oldIdx) {
            idx--;
        }

        // Increase index if component is dropped after or above a previous
        // component
        VerticalDropLocation loc = details.getDropLocation();
        if (loc == VerticalDropLocation.MIDDLE
                || loc == VerticalDropLocation.BOTTOM) {
            idx++;
        }

        // Add component
        if (idx >= 0) {
            layout.addComponent(comp, idx);
        } else {
            layout.addComponent(comp);
        }

        // Add component alignment if given
        if (dropAlignment != null) {
            layout.setComponentAlignment(comp, dropAlignment);
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
        FormLayoutTargetDetails details = (FormLayoutTargetDetails) event
                .getTargetDetails();
        AbstractOrderedLayout layout = (AbstractOrderedLayout) details
                .getTarget();
        Component source = event.getTransferable().getSourceComponent();
        int idx = details.getOverIndex();
        Component comp = transferable.getComponent();

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

        // Increase index if component is dropped after or above a
        // previous
        // component
        VerticalDropLocation loc = (details).getDropLocation();
        if (loc == VerticalDropLocation.MIDDLE
                || loc == VerticalDropLocation.BOTTOM) {
            idx++;
        }

        // Add component
        if (idx >= 0) {
            layout.addComponent(comp, idx);
        } else {
            layout.addComponent(comp);
        }

        // Add component alignment if given
        if (dropAlignment != null) {
            layout.setComponentAlignment(comp, dropAlignment);
        }
    }

    @Override
    protected void handleHTML5Drop(DragAndDropEvent event) {
        FormLayoutTargetDetails details = (FormLayoutTargetDetails) event
                .getTargetDetails();
        int idx = details.getOverIndex();
        AbstractOrderedLayout layout = (AbstractOrderedLayout) details
                .getTarget();

        // Increase index if component is dropped after or above a
        // previous component
        VerticalDropLocation loc = details.getDropLocation();
        if (loc == VerticalDropLocation.MIDDLE
                || loc == VerticalDropLocation.BOTTOM) {
            idx++;
        }

        // Add component
        if (idx >= 0) {
            layout.addComponent(resolveComponentFromHTML5Drop(event), idx);
        } else {
            layout.addComponent(resolveComponentFromHTML5Drop(event));
        }

        // Add component alignment if given
        if (dropAlignment != null) {
            layout.setComponentAlignment(resolveComponentFromHTML5Drop(event),
                    dropAlignment);
        }
    }

    @Override
    public AcceptCriterion getAcceptCriterion() {
        TargetDetailIs isOverEmptyLayout = new TargetDetailIs(
                Constants.DROP_DETAIL_TO, "-1");
        return new Or(isOverEmptyLayout, VerticalLocationIs.TOP,
                VerticalLocationIs.BOTTOM);
    }

    @Override
    public Class<FormLayout> getTargetLayoutType() {
        return FormLayout.class;
    }
}
