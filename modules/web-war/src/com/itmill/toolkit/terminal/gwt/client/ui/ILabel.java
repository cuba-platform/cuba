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

package com.itmill.toolkit.terminal.gwt.client.ui;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.PreElement;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HTML;
import com.itmill.toolkit.terminal.gwt.client.ApplicationConnection;
import com.itmill.toolkit.terminal.gwt.client.ITooltip;
import com.itmill.toolkit.terminal.gwt.client.Paintable;
import com.itmill.toolkit.terminal.gwt.client.UIDL;
import com.itmill.toolkit.terminal.gwt.client.Util;
import com.haulmont.cuba.toolkit.gwt.client.Tools;

public class ILabel extends HTML implements Paintable {

    public static final String CLASSNAME = "i-label";
    private static final String CLASSNAME_UNDEFINED_WIDTH = "i-label-undef-w";

    private ApplicationConnection client;
    private int verticalPaddingBorder = 0;
    private int horizontalPaddingBorder = 0;

    public ILabel() {
        super();
        setStyleName(CLASSNAME);
        sinkEvents(ITooltip.TOOLTIP_EVENTS);
    }

    public ILabel(String text) {
        super(Tools.format(text));
        setStyleName(CLASSNAME);
        sinkEvents(ITooltip.TOOLTIP_EVENTS);
    }

    @Override
    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);
        if (event.getTypeInt() == Event.ONLOAD) {
            Util.notifyParentOfSizeChange(this, true);
            event.cancelBubble(true);
            return;
        }
        if (client != null) {
            client.handleTooltipEvent(event, this);
        }
    }

    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        //todo: gorodnoff would need think about text formating on update
        if (client.updateComponent(this, uidl, true)) {
            return;
        }

        this.client = client;

        boolean sinkOnloads = false;

        final String mode = uidl.getStringAttribute("mode");
        if (mode == null || "text".equals(mode)) {
            setHTML(Tools.format(uidl.getChildString(0)));
        } else if ("pre".equals(mode)) {
            PreElement preElement = Document.get().createPreElement();
            preElement.setInnerText(uidl.getChildUIDL(0).getChildString(0));
            // clear existing content
            setHTML("");
            // add preformatted text to dom
            getElement().appendChild(preElement);
        } else if ("uidl".equals(mode)) {
            setHTML(uidl.getChildrenAsXML());
        } else if ("xhtml".equals(mode)) {
            UIDL content = uidl.getChildUIDL(0).getChildUIDL(0);
            if (content.getChildCount() > 0) {
                setHTML(content.getChildString(0));
            } else {
                setHTML("");
            }
            sinkOnloads = true;
        } else if ("xml".equals(mode)) {
            setHTML(uidl.getChildUIDL(0).getChildString(0));
        } else if ("raw".equals(mode)) {
            setHTML(uidl.getChildUIDL(0).getChildString(0));
            sinkOnloads = true;
        } else {
            setText("");
        }
        if (sinkOnloads) {
            sinkOnloadsForContainedImgs();
        }
    }

    private void sinkOnloadsForContainedImgs() {
        NodeList<Element> images = getElement().getElementsByTagName("img");
        for (int i = 0; i < images.getLength(); i++) {
            Element img = images.getItem(i);
            DOM.sinkEvents((com.google.gwt.user.client.Element) img,
                    Event.ONLOAD);
        }

    }

    @Override
    public void setHeight(String height) {
        verticalPaddingBorder = Util.setHeightExcludingPaddingAndBorder(this,
                height, verticalPaddingBorder);
    }

    @Override
    public void setWidth(String width) {
        horizontalPaddingBorder = Util.setWidthExcludingPaddingAndBorder(this,
                width, horizontalPaddingBorder);
        if (width == null || width.equals("")) {
            setStyleName(getElement(), CLASSNAME_UNDEFINED_WIDTH, true);
        } else {
            setStyleName(getElement(), CLASSNAME_UNDEFINED_WIDTH, false);
        }
    }
}