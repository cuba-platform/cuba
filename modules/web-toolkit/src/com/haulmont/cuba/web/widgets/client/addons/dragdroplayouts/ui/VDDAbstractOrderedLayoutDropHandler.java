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
package com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.user.client.DOM;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.WidgetUtil;
import com.vaadin.client.ui.orderedlayout.Slot;
import com.vaadin.client.ui.orderedlayout.VAbstractOrderedLayout;

public abstract class VDDAbstractOrderedLayoutDropHandler<W extends VAbstractOrderedLayout>
        extends VDDAbstractDropHandler<W> {

    public VDDAbstractOrderedLayoutDropHandler(ComponentConnector connector) {
        super(connector);
    }

    protected abstract Slot getSlot(Element e, NativeEvent event);

    protected Slot findSlotAtPosition(int clientX, int clientY,
            NativeEvent event) {
        com.google.gwt.dom.client.Element elementUnderMouse = WidgetUtil
                .getElementFromPoint(clientX, clientY);
        if (getLayout().getElement() != elementUnderMouse) {
            return getSlot(DOM.asOld(elementUnderMouse), event);
        }
        return null;
    }

    protected Slot findSlotHorizontally(int spacerSize, NativeEvent event) {
        int counter = 0;
        Slot slotLeft, slotRight;
        int clientX = event.getClientX();
        int clientY = event.getClientY();
        while (counter < spacerSize) {
            counter++;
            slotRight = findSlotAtPosition(clientX + counter, clientY, event);
            slotLeft = findSlotAtPosition(clientX - counter, clientY, event);
            if (slotRight != null) {
                return slotRight;
            }
            if (slotLeft != null) {
                return slotLeft;
            }
        }
        return null;
    }

    protected Slot findSlotVertically(int spacerSize, NativeEvent event) {
        int counter = 0;
        Slot slotTop, slotBottom;
        int clientX = event.getClientX();
        int clientY = event.getClientY();
        while (counter < spacerSize) {
            counter++;
            slotBottom = findSlotAtPosition(clientX, clientY + counter, event);
            slotTop = findSlotAtPosition(clientX, clientY - counter, event);
            if (slotBottom != null) {
                return slotBottom;
            }
            if (slotTop != null) {
                return slotTop;
            }
        }
        return null;
    }
}
