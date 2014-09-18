/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.orderedactionslayout;

import com.haulmont.cuba.web.toolkit.ui.CubaOrderedActionsLayout;
import com.vaadin.client.*;
import com.vaadin.client.ui.AbstractFieldConnector;
import com.vaadin.client.ui.Icon;
import com.vaadin.client.ui.ShortcutActionHandler;
import com.vaadin.client.ui.aria.AriaHelper;
import com.vaadin.client.ui.orderedlayout.AbstractOrderedLayoutConnector;
import com.vaadin.client.ui.orderedlayout.CaptionPosition;
import com.vaadin.shared.AbstractFieldState;
import com.vaadin.shared.ComponentConstants;
import com.vaadin.shared.communication.URLReference;
import com.vaadin.shared.ui.ComponentStateUtil;
import com.vaadin.shared.ui.Connect;

import java.util.List;

/**
 * @author devyatkin
 * @version $Id$
 */
@Connect(value = CubaOrderedActionsLayout.class, loadStyle = Connect.LoadStyle.EAGER)
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
        if (child.getState() instanceof AbstractFieldState) {
            AbstractFieldState abstractFieldState = (AbstractFieldState) child
                    .getState();
            showError = showError && !abstractFieldState.hideErrors;
        }
        boolean required = false;
        if (child instanceof AbstractFieldConnector) {
            required = ((AbstractFieldConnector) child).isRequired();
        }
        boolean enabled = child.isEnabled();

        if (slot.hasCaption() && null == caption) {
            slot.setCaptionResizeListener(null);
        }

        String description = null;
        if (ComponentStateUtil.hasDescription(child.getState())) {
            description = child.getState().description;
        }

        // Haulmont API
        slot.setCaption(caption, description, icon, styles, error, showError, required,
                enabled);

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