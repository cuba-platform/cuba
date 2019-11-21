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

package com.haulmont.cuba.web.widgets.client.gridlayout;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Widget;
import com.haulmont.cuba.web.widgets.client.button.CubaButtonWidget;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ui.ManagedLayout;
import com.vaadin.client.ui.layout.ComponentConnectorLayoutSlot;

import javax.annotation.Nullable;

public class CubaGridLayoutSlot extends ComponentConnectorLayoutSlot {

    public CubaGridLayoutSlot(String baseClassName, ComponentConnector child, ManagedLayout layout) {
        super(baseClassName, child, layout);
    }

    @Override
    public int getWidgetHeight() {
        Widget widget = getChild().getWidget();
        if (widget instanceof CubaButtonWidget) {
            return getButtonHeight((CubaButtonWidget) widget);
        }

        return super.getWidgetHeight();
    }

    protected int getWidgetHeight(Widget widget) {
        return getLayoutManager()
                .getOuterHeight(widget.getElement());
    }

    protected int getButtonHeight(CubaButtonWidget widget) {
        Element element = widget.getElement();
        if (!element.hasClassName("link")) {
            return getWidgetHeight(widget);
        }

        Element captionElement = findLinkButtonCaptionElement(element);
        if (captionElement == null) {
            return getWidgetHeight(widget);
        }
        // The LinkButton component has an ability to wrap caption onto multiple rows.
        // Thus, at the first recalculation of the heights for the slots, the "getWidgetHeight()" call
        // returns an invalid multiline component height.
        // In order to calculate the really required caption height, regardless of the 'white-space' mode,
        // we need to explicitly set it to 'nowrap'. After calculation, it's reverted back.
        Style style = captionElement.getStyle();
        String prevWhiteSpace = style.getWhiteSpace();
        style.setWhiteSpace(Style.WhiteSpace.NOWRAP);
        int buttonHeight = element.getOffsetHeight();

        if (prevWhiteSpace != null && !prevWhiteSpace.isEmpty()) {
            style.setWhiteSpace(Style.WhiteSpace.valueOf(prevWhiteSpace));
        } else {
            style.clearWhiteSpace();
        }

        return buttonHeight;
    }

    @Nullable
    protected Element findLinkButtonCaptionElement(Element linkButtonElement) {
        Element buttonWrapElement = findElementByClassName(linkButtonElement, "v-button-wrap");
        if (buttonWrapElement != null) {
            return findElementByClassName(buttonWrapElement, "v-button-caption");
        }
        return null;
    }

    @Nullable
    protected Element findElementByClassName(Element element, String className) {
        if (element == null) {
            return null;
        }

        if (element.getClassName().contains(className)) {
            return element;
        }

        NodeList<Node> childNodes = element.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node child = childNodes.getItem(i);
            if (child instanceof Element) {
                Element childElement = findElementByClassName((Element) child, className);
                if (childElement != null) {
                    return childElement;
                }
            }
        }
        return null;
    }
}