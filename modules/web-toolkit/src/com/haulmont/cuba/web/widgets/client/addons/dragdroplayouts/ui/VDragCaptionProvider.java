/*
 * Copyright 2017 Nikita Petunin, Yuriy Artamonov
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

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.PreElement;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.Util;
import com.vaadin.client.ui.AbstractConnector;
import com.vaadin.client.ui.Icon;
import com.vaadin.shared.ui.ContentMode;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.interfaces.DDLayoutState;
import com.haulmont.cuba.web.widgets.client.addons.dragdroplayouts.ui.interfaces.DragAndDropAwareState;

public class VDragCaptionProvider {
    private final AbstractConnector root;

    public VDragCaptionProvider(AbstractConnector root) {
        this.root = root;
    }

    public Element getDragCaptionElement(Widget w) {
        ComponentConnector component = Util.findConnectorFor(w);
        DDLayoutState state = ((DragAndDropAwareState) root.getState()).getDragAndDropState();
        DragCaptionInfo dci = state.dragCaptions.get(component);

        Document document = Document.get();

        Element dragCaptionImage = document.createDivElement();
        Element dragCaption = document.createSpanElement();

        String dragCaptionText = dci.caption;
        if (dragCaptionText != null) {
            if (dci.contentMode == ContentMode.TEXT) {
                dragCaption.setInnerText(dragCaptionText);
            } else if (dci.contentMode == ContentMode.HTML) {
                dragCaption.setInnerHTML(dragCaptionText);
            } else if (dci.contentMode == ContentMode.PREFORMATTED) {
                PreElement preElement = document.createPreElement();
                preElement.setInnerText(dragCaptionText);
                dragCaption.appendChild(preElement);
            }
        }

        String dragIconKey = state.dragCaptions.get(component).iconKey;
        if (dragIconKey != null) {
            String resourceUrl = root.getResourceUrl(dragIconKey);
            Icon icon = component.getConnection().getIcon(resourceUrl);
            dragCaptionImage.appendChild(icon.getElement());
        }

        dragCaptionImage.appendChild(dragCaption);

        return dragCaptionImage;
    }
}