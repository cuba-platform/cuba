/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.haulmont.cuba.web.toolkit.ui.client.groupbox;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.DOM;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HasEnabled;
import com.haulmont.cuba.web.toolkit.ui.client.Tools;
import com.vaadin.client.ui.VPanel;

/**
 */
public class CubaGroupBoxWidget extends VPanel implements HasEnabled {

    public static final String CLASSNAME = "cuba-groupbox";

    protected boolean expanded = true;

    protected boolean collapsable = false;

    private boolean enabled = true;

    protected ExpandHandler expandHandler;

    public Element captionWrap;

    public Element expander = DOM.createSpan();

    public Element captionStartDeco = DOM.createDiv();
    public Element captionEndDeco = DOM.createDiv();
    public Element captionTextNode;

    public CubaGroupBoxWidget(String primaryStyleName) {
        setStylePrimaryName(primaryStyleName);
        setStyleName(primaryStyleName);

        captionWrap = captionNode.getParentElement().cast();

        captionNode.setClassName(primaryStyleName + "-caption");
        contentNode.setClassName(primaryStyleName + "-content");
        bottomDecoration.setClassName(primaryStyleName + "-deco");
        expander.setClassName(primaryStyleName + "-expander");

        contentNode.getStyle().clearPosition();

        setExpanded(true);

        captionStartDeco.appendChild(DOM.createDiv());
        captionStartDeco.setClassName(primaryStyleName + "-caption-start-deco");
        captionWrap.insertFirst(captionStartDeco);

        captionEndDeco.appendChild(DOM.createDiv());
        captionEndDeco.setClassName(primaryStyleName + "-caption-end-deco");
        captionWrap.appendChild(captionEndDeco);

        captionNode.insertFirst(expander);

        captionTextNode = (Element) captionNode.getChild(1);
        captionTextNode.setClassName(primaryStyleName + "-caption-text");

        DOM.sinkEvents(expander, Event.ONCLICK);
        DOM.sinkEvents(captionTextNode, Event.ONCLICK);
    }

    public CubaGroupBoxWidget() {
        this(CLASSNAME);
    }

    @Override
    public void setCaption(String text) {
        if (text == null || text.equals("")) {
            addStyleDependentName("nocaption");
        } else {
            removeStyleDependentName("nocaption");
        }

        super.setCaption(text);
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        if (expanded) {
            expander.addClassName("expanded");
            getElement().removeClassName("collapsed");
            getElement().addClassName("expanded");
        } else {
            expander.removeClassName("expanded");
            getElement().removeClassName("expanded");
            getElement().addClassName("collapsed");
        }

        this.expanded = expanded;
    }

    public boolean isCollapsable() {
        return collapsable;
    }

    public void setCollapsable(boolean collapsable) {
        Style expanderStyle = expander.getStyle();
        if (collapsable) {
            expanderStyle.clearProperty("display");
            removeStyleDependentName("nocollapsable");
        } else {
            addStyleDependentName("nocollapsable");
            expanderStyle.setDisplay(Style.Display.NONE);
        }

        Tools.textSelectionEnable(captionNode, !collapsable);

        this.collapsable = collapsable;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (isEnabled() != enabled) {
            this.enabled = enabled;
        }
    }

    @Override
    public void onBrowserEvent(Event event) {
        if (collapsable && DOM.eventGetType(event) == Event.ONCLICK && isEnabled()
                && (DOM.eventGetTarget(event) == expander || DOM.eventGetTarget(event) == captionNode.getChild(1))) {
            toggleExpanded(event);
        } else {
            super.onBrowserEvent(event);
        }
    }

    protected void toggleExpanded(Event event) {
        setExpanded(!expanded);

        if (collapsable && expandHandler != null) {
            if (expanded) {
                expandHandler.expand();
            } else {
                expandHandler.collapse();
            }
        }
        DOM.eventCancelBubble(event, true);
    }

    public interface ExpandHandler {
        void expand();

        void collapse();
    }
}