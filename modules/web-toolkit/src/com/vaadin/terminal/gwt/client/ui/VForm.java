/* 
 * Copyright 2010 IT Mill Ltd.
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

package com.vaadin.terminal.gwt.client.ui;

import java.util.Set;

import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.BrowserInfo;
import com.vaadin.terminal.gwt.client.Container;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.RenderInformation;
import com.vaadin.terminal.gwt.client.RenderSpace;
import com.vaadin.terminal.gwt.client.UIDL;
import com.vaadin.terminal.gwt.client.Util;
import com.vaadin.terminal.gwt.client.VConsole;
import com.vaadin.terminal.gwt.client.VErrorMessage;

/**
 * VForm
 * <br/>
 * [Compatible with Vaadin 6.6]
 */
public class VForm extends ComplexPanel implements Container, KeyDownHandler {

    protected String id;

    private String height = "";

    private String width = "";

    public static final String CLASSNAME = "v-form";

    protected Container lo;
    protected Element legend = DOM.createLegend();
    protected Element caption = DOM.createSpan();
    protected Element errorIndicatorElement = DOM.createDiv();
    protected Element desc = DOM.createDiv();
    protected Icon icon;
    private VErrorMessage errorMessage = new VErrorMessage();

    protected Element fieldContainer = DOM.createDiv();

    protected Element footerContainer = DOM.createDiv();

    protected Element fieldSet = DOM.createFieldSet();

    protected Container footer;

    protected ApplicationConnection client;

    private RenderInformation renderInformation = new RenderInformation();

    private int borderPaddingHorizontal = -1;

    private boolean rendering = false;

    protected ShortcutActionHandler shortcutHandler;

    private HandlerRegistration keyDownRegistration;

    public VForm() {
        setElement(DOM.createDiv());
        DOM.appendChild(getElement(), fieldSet);
        setStyleName(CLASSNAME);
        DOM.appendChild(fieldSet, legend);
        DOM.appendChild(legend, caption);
        DOM.setElementProperty(errorIndicatorElement, "className",
                "v-errorindicator");
        DOM.setStyleAttribute(errorIndicatorElement, "display", "none");
        DOM.setInnerText(errorIndicatorElement, " "); // needed for IE
        DOM.setElementProperty(desc, "className", CLASSNAME + "-description");
        DOM.appendChild(fieldSet, desc);
        DOM.appendChild(fieldSet, fieldContainer);
        errorMessage.setVisible(false);
        errorMessage.setStyleName(CLASSNAME + "-errormessage");
        DOM.appendChild(fieldSet, errorMessage.getElement());
        DOM.appendChild(fieldSet, footerContainer);
    }

    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        rendering = true;
        this.client = client;
        id = uidl.getId();

        if (client.updateComponent(this, uidl, false)) {
            rendering = false;
            return;
        }

        renderDOM(uidl, client);

        updateSize();
        // TODO Check if this is needed
        client.runDescendentsLayout(this);
        renderFooter(uidl, client);


        renderContent(uidl, client);

        // We may have actions attached
        if (uidl.getChildCount() > 1) {
            UIDL childUidl = uidl.getChildByTagName("actions");
            if (childUidl != null) {
                if (shortcutHandler == null) {
                    shortcutHandler = new ShortcutActionHandler(id, client);
                    keyDownRegistration = addDomHandler(this, KeyDownEvent
                            .getType());
                }
                shortcutHandler.updateActionMap(childUidl);
            }
        } else if (shortcutHandler != null) {
            keyDownRegistration.removeHandler();
            shortcutHandler = null;
            keyDownRegistration = null;
        }

        rendering = false;
    }

    protected void renderContent(UIDL uidl, ApplicationConnection client) {
        final UIDL layoutUidl = uidl.getChildUIDL(0);
        Container newLo = (Container) client.getPaintable(layoutUidl);
        if (lo == null) {
            lo = newLo;
            add((Widget) lo, fieldContainer);
        } else if (lo != newLo) {
            client.unregisterPaintable(lo);
            remove((Widget) lo);
            lo = newLo;
            add((Widget) lo, fieldContainer);
        }
        lo.updateFromUIDL(layoutUidl, client);
    }

    protected void renderFooter(UIDL uidl, ApplicationConnection client) {
        // first render footer so it will be easier to handle relative height of
        // main layout
        if (uidl.getChildCount() > 1
                && !uidl.getChildUIDL(1).getTag().equals("actions")) {
            // render footer
            Container newFooter = (Container) client.getPaintable(uidl
                    .getChildUIDL(1));
            if (footer == null) {
                add((Widget) newFooter, footerContainer);
                footer = newFooter;
            } else if (newFooter != footer) {
                remove((Widget) footer);
                client.unregisterPaintable(footer);
                add((Widget) newFooter, footerContainer);
            }
            footer = newFooter;
            footer.updateFromUIDL(uidl.getChildUIDL(1), client);
            updateSize();
        } else {
            if (footer != null) {
                remove((Widget) footer);
                client.unregisterPaintable(footer);
                updateSize();
            }
        }
    }

    protected void renderDOM(UIDL uidl, ApplicationConnection client) {
        boolean legendEmpty = true;
        if (uidl.hasAttribute("caption")) {
            DOM.setInnerText(caption, uidl.getStringAttribute("caption"));
            legendEmpty = false;
        } else {
            DOM.setInnerText(caption, "");
        }
        if (uidl.hasAttribute("icon")) {
            if (icon == null) {
                icon = new Icon(client);
                DOM.insertChild(legend, icon.getElement(), 0);
            }
            icon.setUri(uidl.getStringAttribute("icon"));
            legendEmpty = false;
        } else {
            if (icon != null) {
                DOM.removeChild(legend, icon.getElement());
            }
        }
        if (legendEmpty) {
            addStyleDependentName("nocaption");
        } else {
            removeStyleDependentName("nocaption");
        }

        if (uidl.hasAttribute("error")) {
            final UIDL errorUidl = uidl.getErrors();
            errorMessage.updateFromUIDL(errorUidl);
            errorMessage.setVisible(true);

        } else {
            errorMessage.setVisible(false);
        }

        if (uidl.hasAttribute("description")) {
            DOM.setInnerHTML(desc, uidl.getStringAttribute("description"));
            removeStyleDependentName("nodescription");
        } else {
            DOM.setInnerHTML(desc, "");
            addStyleDependentName("nodescription");
        }
    }

    public void updateSize() {

        renderInformation.updateSize(getElement());

        renderInformation.setContentAreaHeight(renderInformation
                .getRenderedSize().getHeight() - getSpaceConsumedVertically());
        if (BrowserInfo.get().isIE6()) {
            getElement().getStyle().setProperty("overflow", "hidden");
        }
        renderInformation.setContentAreaWidth(renderInformation
                .getRenderedSize().getWidth() - borderPaddingHorizontal);
    }

    public RenderSpace getAllocatedSpace(Widget child) {
        if (child == lo) {
            return renderInformation.getContentAreaSize();
        } else if (child == footer) {
            return new RenderSpace(renderInformation.getContentAreaSize()
                    .getWidth(), 0);
        } else {
            VConsole.error("Invalid child requested RenderSpace information");
            return null;
        }
    }

    public boolean hasChildComponent(Widget component) {
        return component != null && (component == lo || component == footer);
    }

    public void replaceChildComponent(Widget oldComponent, Widget newComponent) {
        if (!hasChildComponent(oldComponent)) {
            throw new IllegalArgumentException(
                    "Old component is not inside this Container");
        }
        remove(oldComponent);
        if (oldComponent == lo) {
            lo = (Container) newComponent;
            add((Widget) lo, fieldContainer);
        } else {
            footer = (Container) newComponent;
            add((Widget) footer, footerContainer);
        }

    }

    public boolean requestLayout(Set<Paintable> child) {

        if (height != null && !"".equals(height) && width != null
                && !"".equals(width)) {
            /*
             * If the height and width has been specified the child components
             * cannot make the size of the layout change
             */

            return true;
        }

        if (renderInformation.updateSize(getElement())) {
            return false;
        } else {
            return true;
        }

    }

    public void updateCaption(Paintable component, UIDL uidl) {
        // NOP form don't render caption for neither field layout nor footer
        // layout
    }

    @Override
    public void setHeight(String height) {
        if (this.height.equals(height)) {
            return;
        }

        this.height = height;
        super.setHeight(height);

        updateSize();
    }

    /**
     * @return pixels consumed by decoration, captions, descrioptiosn etc.. In
     *         other words space, not used by the actual layout in form.
     */
    private int getSpaceConsumedVertically() {
        int offsetHeight2 = fieldSet.getOffsetHeight();
        int offsetHeight3 = fieldContainer.getOffsetHeight();
        int borderPadding = offsetHeight2 - offsetHeight3;
        return borderPadding;
    }

    @Override
    public void setWidth(String width) {
        if (borderPaddingHorizontal < 0) {
            // measure excess size lazyly after stylename setting, but before
            // setting width
            int ow = getOffsetWidth();
            int dow = desc.getOffsetWidth();
            borderPaddingHorizontal = ow - dow;
        }
        if (Util.equals(this.width, width)) {
            return;
        }

        this.width = width;
        super.setWidth(width);

        updateSize();

        if (!rendering && height.equals("")) {
            // Width might affect height
            Util.updateRelativeChildrenAndSendSizeUpdateEvent(client, this);
        }
    }

    public void onKeyDown(KeyDownEvent event) {
        shortcutHandler.handleKeyboardEvent(Event.as(event.getNativeEvent()));
    }
}
