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

package com.haulmont.cuba.web.widgets.client.orderedactionslayout;

import com.haulmont.cuba.web.widgets.CubaOrderedActionsLayout;
import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.Paintable;
import com.vaadin.client.UIDL;
import com.vaadin.client.ui.*;
import com.vaadin.client.ui.aria.AriaHelper;
import com.vaadin.client.ui.orderedlayout.AbstractOrderedLayoutConnector;
import com.vaadin.client.ui.orderedlayout.CaptionPosition;
import com.vaadin.shared.ComponentConstants;
import com.vaadin.shared.communication.URLReference;
import com.vaadin.shared.ui.Connect;

import java.util.List;

@Connect(CubaOrderedActionsLayout.class)
public class CubaOrderedActionsLayoutConnector extends AbstractOrderedLayoutConnector implements Paintable {

    @Override
    public CubaOrderedActionsLayoutWidget getWidget() {
        return (CubaOrderedActionsLayoutWidget) super.getWidget();
    }

    @Override
    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        final int cnt = uidl.getChildCount();
        for (int i = 0; i < cnt; i++) {
            UIDL childUidl = uidl.getChildUIDL(i);
            if (childUidl.getTag().equals("actions")) {
                if (getWidget().getShortcutHandler() == null) {
                    getWidget().setShortcutHandler(new ShortcutActionHandler(uidl.getId(), client));
                }
                getWidget().getShortcutHandler().updateActionMap(childUidl);
            }
        }
    }

    @Override
    protected void updateCaptionInternal(ComponentConnector child) {
        // CAUTION copied from superclass
        CubaOrderedLayoutSlot slot = (CubaOrderedLayoutSlot) getWidget().getSlot(child.getWidget());

        String caption = child.getState().caption;
        URLReference iconUrl = child.getState().resources
                .get(ComponentConstants.ICON_RESOURCE);
        String iconUrlString = iconUrl != null ? iconUrl.getURL() : null;
        Icon icon = child.getConnection().getIcon(iconUrlString);

        List<String> styles = child.getState().styles;
        String error = child.getState().errorMessage;
        boolean showError = error != null;
        if (child instanceof HasErrorIndicator) {
            showError = ((HasErrorIndicator) child).isErrorIndicatorVisible();
        }
        boolean required = false;
        if (child instanceof HasRequiredIndicator) {
            required = ((HasRequiredIndicator) child)
                    .isRequiredIndicatorVisible();
        }
        // For compatibility components
        if (child instanceof com.vaadin.v7.client.ui.AbstractFieldConnector) {
            required = ((com.vaadin.v7.client.ui.AbstractFieldConnector) child).isRequiredIndicatorVisible();
        }
        boolean enabled = child.isEnabled();

        if (slot.hasCaption() && null == caption) {
            slot.setCaptionResizeListener(null);
        }

        // Haulmont API
        boolean contextHelpIconEnabled = isContextHelpIconEnabled(child.getState());

        // Haulmont API
        slot.setCaption(caption, contextHelpIconEnabled, icon, styles, error, showError, required,
                enabled, child.getState().captionAsHtml);

        AriaHelper.handleInputRequired(child.getWidget(), required);
        AriaHelper.handleInputInvalid(child.getWidget(), showError);
        AriaHelper.bindCaption(child.getWidget(), slot.getCaptionElement());

        if (slot.hasCaption()) {
            CaptionPosition pos = slot.getCaptionPosition();
            slot.setCaptionResizeListener(slotCaptionResizeListener);
            if (child.isRelativeHeight()
                    && (pos == CaptionPosition.TOP || pos == CaptionPosition.BOTTOM)) {
                getWidget().updateCaptionOffset(slot.getCaptionElement());
            } else if (child.isRelativeWidth()
                    && (pos == CaptionPosition.LEFT || pos == CaptionPosition.RIGHT)) {
                getWidget().updateCaptionOffset(slot.getCaptionElement());
            }
        }
    }
}