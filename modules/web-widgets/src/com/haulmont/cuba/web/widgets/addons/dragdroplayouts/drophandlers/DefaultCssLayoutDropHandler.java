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
import com.vaadin.shared.ui.dd.HorizontalDropLocation;
import com.vaadin.shared.ui.dd.VerticalDropLocation;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.SingleComponentContainer;

import com.haulmont.cuba.web.widgets.addons.dragdroplayouts.DDCssLayout;
import com.haulmont.cuba.web.widgets.addons.dragdroplayouts.DDCssLayout.CssLayoutTargetDetails;
import com.haulmont.cuba.web.widgets.addons.dragdroplayouts.events.LayoutBoundTransferable;

/**
 * Default CSS Layout drop handler
 * 
 * @author John Ahlroos / www.jasoft.fi
 * @since 0.7.0
 * 
 */
@SuppressWarnings("serial")
public class DefaultCssLayoutDropHandler
        extends AbstractDefaultLayoutDropHandler {

    @Override
    protected void handleComponentReordering(DragAndDropEvent event) {
        // Component re-ordering
        LayoutBoundTransferable transferable = (LayoutBoundTransferable) event
                .getTransferable();
        CssLayoutTargetDetails details = (CssLayoutTargetDetails) event
                .getTargetDetails();
        DDCssLayout layout = (DDCssLayout) details.getTarget();
        Component comp = transferable.getComponent();
        int idx = details.getOverIndex();
        Component over = details.getOverComponent();

        // Detach from old source
        Component source = transferable.getSourceComponent();
        if (source instanceof ComponentContainer) {
            ((ComponentContainer) source).removeComponent(comp);
        } else if (source instanceof SingleComponentContainer) {
            ((SingleComponentContainer) source).setContent(null);
        }

        // Add component
        if (idx >= 0 && idx < layout.getComponentCount()) {
            layout.addComponent(comp, idx);
        } else {
            layout.addComponent(comp);
        }
    }

    @Override
    protected void handleDropFromLayout(DragAndDropEvent event) {
        LayoutBoundTransferable transferable = (LayoutBoundTransferable) event
                .getTransferable();
        CssLayoutTargetDetails details = (CssLayoutTargetDetails) event
                .getTargetDetails();
        DDCssLayout layout = (DDCssLayout) details.getTarget();
        HorizontalDropLocation hl = details.getHorizontalDropLocation();
        VerticalDropLocation vl = details.getVerticalDropLocation();
        Component source = event.getTransferable().getSourceComponent();
        int idx = (details).getOverIndex();
        Component comp = transferable.getComponent();
        Component over = details.getOverComponent();

        if (over == layout) {
            if (vl == VerticalDropLocation.TOP
                    || hl == HorizontalDropLocation.LEFT) {
                idx = 0;
            } else if (vl == VerticalDropLocation.BOTTOM
                    || hl == HorizontalDropLocation.RIGHT) {
                idx = -1;
            }
        } else {
            if (vl == VerticalDropLocation.BOTTOM
                    || hl == HorizontalDropLocation.RIGHT) {
                idx++;
            }
        }

        // Check that we are not dragging an outer layout into an inner
        // layout
        Component parent = layout.getParent();
        while (parent != null) {
            if (parent == comp) {
                return;
            }
            parent = parent.getParent();
        }

        // If source is an instance of a component container then remove
        // it
        // from there,
        // the component cannot have two parents.
        if (source instanceof ComponentContainer) {
            ComponentContainer sourceLayout = (ComponentContainer) source;
            sourceLayout.removeComponent(comp);
        }

        // Add component
        if (idx >= 0 && idx < layout.getComponentCount()) {
            layout.addComponent(comp, idx);
        } else {
            layout.addComponent(comp);
        }
    }

    @Override
    protected void handleHTML5Drop(DragAndDropEvent event) {
        CssLayoutTargetDetails details = (CssLayoutTargetDetails) event
                .getTargetDetails();
        Component over = details.getOverComponent();
        DDCssLayout layout = (DDCssLayout) details.getTarget();
        int idx = (details).getOverIndex();
        HorizontalDropLocation hl = details.getHorizontalDropLocation();
        VerticalDropLocation vl = details.getVerticalDropLocation();

        if (over == layout) {
            if (vl == VerticalDropLocation.TOP
                    || hl == HorizontalDropLocation.LEFT) {
                idx = 0;
            } else if (vl == VerticalDropLocation.BOTTOM
                    || hl == HorizontalDropLocation.RIGHT) {
                idx = -1;
            }
        } else {
            if (vl == VerticalDropLocation.BOTTOM
                    || hl == HorizontalDropLocation.RIGHT) {
                idx++;
            }
        }

        if (idx >= 0 && idx < layout.getComponentCount()) {
            layout.addComponent(resolveComponentFromHTML5Drop(event), idx);
        } else {
            layout.addComponent(resolveComponentFromHTML5Drop(event));
        }
    }

    @Override
    public Class<CssLayout> getTargetLayoutType() {
        return CssLayout.class;
    }
}
