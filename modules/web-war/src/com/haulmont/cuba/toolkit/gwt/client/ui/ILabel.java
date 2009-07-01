/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 01.07.2009 11:39:56
 *
 * $Id$
 */
package com.haulmont.cuba.toolkit.gwt.client.ui;

import com.itmill.toolkit.terminal.gwt.client.UIDL;
import com.itmill.toolkit.terminal.gwt.client.ApplicationConnection;
import com.itmill.toolkit.terminal.gwt.client.ui.Icon;
import com.google.gwt.dom.client.PreElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

public class ILabel
        extends com.itmill.toolkit.terminal.gwt.client.ui.ILabel
{
    protected Icon icon;

    protected Element contentElement;

    public ILabel() {
        super();
        contentElement = getElement();
    }

    public ILabel(String text) {
        super(text);
        contentElement = getElement();
    }

    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        if (client.updateComponent(this, uidl, true)) {
            return;
        }

        this.client = client;

        boolean hasIcon = uidl.hasAttribute("icon");
        if (hasIcon && getContentElement() == getElement()) {
            setContentElement(DOM.createSpan());
        }

        boolean sinkOnloads = false;

        final String mode = uidl.getStringAttribute("mode");
        if (mode == null || "text".equals(mode)) {
            setText(uidl.getChildString(0));
        } else if ("pre".equals(mode)) {
            PreElement preElement = Document.get().createPreElement();
            preElement.setInnerText(uidl.getChildUIDL(0).getChildString(0));
            // clear existing content
            setHTML("");
            // add preformatted text to dom
            getContentElement().appendChild(preElement);
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
        
        if (hasIcon) {
            if (icon == null) {
                icon = new Icon(client);
                DOM.insertChild(getElement(), icon.getElement(), 0);
            }
            icon.setUri(uidl.getStringAttribute("icon"));
        }

        if (sinkOnloads) {
            sinkOnloadsForContainedImgs();
        }
    }

    @Override
    public void setText(String text) {
        getContentElement().setInnerText(text);
    }

    @Override
    public void setHTML(String html) {
        getContentElement().setInnerHTML(html);
    }

    protected Element getContentElement() {
        return contentElement;
    }

    protected void setContentElement(Element element) {
        contentElement = element;
        if (getElement() != element) {
            getElement().appendChild(element);
        }
    }

}
