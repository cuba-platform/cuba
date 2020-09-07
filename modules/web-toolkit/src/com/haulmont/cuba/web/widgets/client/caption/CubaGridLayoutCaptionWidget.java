/*
 * Copyright (c) 2008-2020 Haulmont.
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

package com.haulmont.cuba.web.widgets.client.caption;

import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.WidgetUtil;
import com.vaadin.client.ui.Icon;
import com.vaadin.client.ui.ImageIcon;

public class CubaGridLayoutCaptionWidget extends CubaCaptionWidget {

    public static final String INLINE_ICON_CLASSNAME = "v-caption-inline-icon";

    public CubaGridLayoutCaptionWidget(ComponentConnector component, ApplicationConnection client) {
        super(component, client);
    }

    public Icon getIcon() {
        return icon;
    }

    public boolean hasInlineIcon() {
        return getStyleName().contains(INLINE_ICON_CLASSNAME) && icon != null;
    }

    @Override
    public int getRenderedWidth() {
        int width = 0;

        if (icon != null && !hasInlineIcon()) {
            // Updating the caption resets the image icon size and it will acquire the appropriate size later on a
            // ONLOAD browser event. In order to get the width of the image icon, we first set an undefined width,
            // calculate the width and then reset the width value.
            if (icon instanceof ImageIcon && icon.getOffsetWidth() == 0) {
                icon.setWidth("");
                width += WidgetUtil.getRequiredWidth(icon.getElement());
                icon.setWidth("0");
            } else {
                width += WidgetUtil.getRequiredWidth(icon.getElement());
            }
        }

        if (captionText != null) {
            width += WidgetUtil.getRequiredWidth(captionText);
        }
        if (requiredFieldIndicator != null && requiredFieldIndicator.getParentElement() == getElement()) {
            width += WidgetUtil.getRequiredWidth(requiredFieldIndicator);
        }
        if (errorIndicatorElement != null && errorIndicatorElement.getParentElement() == getElement()) {
            width += WidgetUtil.getRequiredWidth(errorIndicatorElement);
        }
        if (contextHelpIndicatorElement != null && contextHelpIndicatorElement.getParentElement() == getElement()) {
            width += WidgetUtil.getRequiredWidth(contextHelpIndicatorElement);
        }
        return width;
    }

    @Override
    public int getRequiredWidth() {
        return getRenderedWidth();
    }

    @Override
    protected int getInsertPosition(InsertPosition element) {
        int pos = 0;
        if (InsertPosition.ICON.equals(element)) {
            return pos;
        }
        if (icon != null) {
            pos++;
        }

        if (InsertPosition.CAPTION.equals(element)) {
            return pos;
        }

        if (captionText != null) {
            pos++;
        }

        if (InsertPosition.REQUIRED.equals(element)) {
            return pos;
        }
        if (requiredFieldIndicator != null) {
            pos++;
        }

        if (contextHelpIndicatorElement != null) {
            pos++;
        }

        return pos;
    }
}
