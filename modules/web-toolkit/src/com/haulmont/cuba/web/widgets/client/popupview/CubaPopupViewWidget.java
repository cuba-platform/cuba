/*
 * Copyright (c) 2008-2019 Haulmont.
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
 */

package com.haulmont.cuba.web.widgets.client.popupview;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.RootPanel;
import com.vaadin.client.ui.VPopupView;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CubaPopupViewWidget extends VPopupView {

    protected PopupPosition popupPosition;
    protected int popupPositionLeft;
    protected int popupPositionTop;

    @Override
    public void center() {
        updatePopupPosition();
    }

    protected void setupPopupPosition(PopupPosition popupPosition) {
        this.popupPosition = popupPosition;
    }

    protected void setupPopupPositionLeft(int popupPositionLeft) {
        this.popupPositionLeft = popupPositionLeft;
    }

    protected void setupPopupPositionTop(int popupPositionTop) {
        this.popupPositionTop = popupPositionTop;
    }

    protected void updatePopupPosition() {
        resetPosition();

        if (popupPosition == null
                || popupPosition == PopupPosition.DEFAULT) {
            setPopupPosition(popupPositionTop, popupPositionLeft);
        } else {
            List<String> popupStyleNames = getPopupPositionStyleNames(popupPosition);
            setPopupPosition(popupStyleNames);
        }
    }

    protected void setPopupPosition(int popupPositionTop, int popupPositionLeft) {
        int top = getPositionTop(popupPositionTop);
        int left = getPositionLeft(popupPositionLeft);

        popup.setPopupPosition(left, top);
    }

    protected void setPopupPosition(List<String> styleNames) {
        styleNames.forEach(popup::addStyleName);
    }

    protected int getPositionTop(int popupPositionTop) {
        if (popupPositionTop != -1) {
            return popupPositionTop;
        }

        RootPanel rootPanel = RootPanel.get();
        int windowTop = rootPanel.getAbsoluteTop();
        int windowHeight = rootPanel.getOffsetHeight();
        int windowBottom = windowTop + windowHeight;
        int popupHeight = popup.getOffsetHeight();

        int top = getAbsoluteTop() + (getOffsetHeight() - popupHeight) / 2;

        if ((top + popupHeight) > windowBottom) {
            top -= (top + popupHeight) - windowBottom;
        }

        if (top < 0) {
            top = 0;
        }
        return top;
    }

    protected int getPositionLeft(int popupPositionLeft) {
        if (popupPositionLeft != -1) {
            return popupPositionLeft;
        }

        RootPanel rootPanel = RootPanel.get();
        int windowLeft = rootPanel.getAbsoluteLeft();
        int windowWidth = rootPanel.getOffsetWidth();
        int windowRight = windowLeft + windowWidth;
        int popupWidth = popup.getOffsetWidth();

        int left = getAbsoluteLeft() + (getOffsetWidth() - popupWidth) / 2;

        if ((left + popupWidth) > windowRight) {
            left -= (left + popupWidth) - windowRight;
        }

        if (left < 0) {
            left = 0;
        }
        return left;
    }

    protected List<String> getPopupPositionStyleNames(PopupPosition popupPosition) {
        return Arrays.stream(PopupStyleName.values())
                .filter(popupStyleName -> Objects.equals(popupPosition.name(), popupStyleName.name()))
                .map(PopupStyleName::getStyleName)
                .collect(Collectors.toList());
    }

    protected void resetPosition() {
        resetStyleNames();
        resetPopupPosition();
    }

    protected void resetStyleNames() {
        Arrays.stream(PopupStyleName.values())
                .map(PopupStyleName::getStyleName)
                .forEach(popup::removeStyleName);
    }

    protected void resetPopupPosition() {
        Style style = popup.getElement().getStyle();
        style.clearTop();
        style.clearLeft();
    }
}
