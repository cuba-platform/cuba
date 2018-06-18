/*
 * Copyright 2015 Nikita Petunin, Yuriy Artamonov
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
package com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts;

import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.Util;
import com.vaadin.client.VCaption;
import com.vaadin.client.ui.VAccordion;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.interfaces.DDLayoutState;

public class VGrabFilter {
    protected final DDLayoutState state;

    public VGrabFilter(DDLayoutState state) {
        this.state = state;
    }

    public boolean canBeGrabbed(Widget root, Widget widget) {
        if (state.nonGrabbable != null) {
            return canBeGrabbedRecursive(root, widget);
        }
        return true;
    }

    protected boolean canBeGrabbedRecursive(Widget root, Widget widget) {
        if (widget == root) {
            return true;
        }

        ComponentConnector connector;
        if (!isCaptionForAccordion(widget)) {
            connector = Util.findConnectorFor(widget);
        } else {
            connector = findConnectorForAccordionCaption(widget);
        }

        if (connector != null && state.nonGrabbable.contains(connector)) {
            return false;
        }

        Widget parent = widget.getParent();
        if (parent == null || parent == root) {
            return true;
        }

        return canBeGrabbedRecursive(root, parent);
    }

    protected ComponentConnector findConnectorForAccordionCaption(Widget widget) {
        VAccordion.StackItem parent = (VAccordion.StackItem) widget.getParent();
        return Util.findConnectorFor(parent.getChildWidget());
    }

    protected boolean isCaptionForAccordion(Widget widget) {
        if (widget == null) {
            return false;
        }
        if (!(widget instanceof VCaption)) {
            return false;
        }
        Widget parent = widget.getParent();
        return parent instanceof VAccordion.StackItem;
    }
}