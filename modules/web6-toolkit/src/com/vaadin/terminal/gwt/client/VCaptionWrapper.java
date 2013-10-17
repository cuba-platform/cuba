/* 
 * Copyright 2008 IT Mill Ltd.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.vaadin.terminal.gwt.client;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

public class VCaptionWrapper extends FlowPanel {

    public static final String CLASSNAME = "v-captionwrapper";
    protected VCaption caption;
    protected Paintable widget;

    protected final Element widgetWrapper = DOM.createDiv();

    ApplicationConnection client;

    public VCaptionWrapper(Paintable toBeWrapped, ApplicationConnection client) {
        this.client = client;
        setStyleName(CLASSNAME);

        DOM.appendChild(getElement(), widgetWrapper);

        widget = toBeWrapped;

        add((Widget) widget, getWidgetContainer());
    }

    public void updateCaption(UIDL uidl) {
        if (VCaption.isNeeded(uidl)) {
            // We need a caption

            VCaption newCaption = caption;

            if (newCaption == null) {
                newCaption = new VCaption(widget, client);
            }

            boolean positionChanged = newCaption.updateCaption(uidl);

            if (newCaption != caption || positionChanged) {
                setCaption(newCaption);
            }

        } else {
            // Caption is not needed
            if (caption != null) {
                remove(caption);

                Util.setFloat(getWidgetContainer(), ""); //Remove float style from the widget container
            }

        }

        setVisible(!uidl.getBooleanAttribute("invisible"));
    }

    protected void setCaption(VCaption newCaption) {
        // Detach new child.
        if (newCaption != null) {
            newCaption.removeFromParent();
        }

        // Remove old child.
        if (caption != null && newCaption != caption) {
            remove(caption);
        }

        // Logical attach.
        caption = newCaption;

        if (caption != null) {
            // Physical attach.
            if (caption.shouldBePlacedAfterComponent()) {
                Util.setFloat(getWidgetContainer(), "left");
                Util.setFloat(caption.getElement(), "left");
                getElement().appendChild(caption.getElement());
            } else {
                Util.setFloat(getWidgetContainer(), "");
                Util.setFloat(caption.getElement(), "");
                getElement().insertBefore(caption.getElement(), getWidgetContainer());
            }

            adopt(caption);
        }
    }

    public Paintable getPaintable() {
        return widget;
    }

    public Element getWidgetContainer() {
        return  widgetWrapper;
    }
}