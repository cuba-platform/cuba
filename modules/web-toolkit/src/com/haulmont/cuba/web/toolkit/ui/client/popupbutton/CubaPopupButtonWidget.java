/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.popupbutton;

import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.ui.VButton;
import com.vaadin.client.ui.orderedlayout.Slot;
import com.vaadin.client.ui.orderedlayout.VAbstractOrderedLayout;
import org.vaadin.hene.popupbutton.widgetset.client.ui.VPopupButton;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaPopupButtonWidget extends VPopupButton {

    public static final String SELECTED_ITEM_STYLE = "v-selected";

    protected boolean customLayout = false;

    @Override
    protected void onPopupOpened() {
        super.onPopupOpened();

        if (customLayout) {
            return;
        }

        // find button, assign .v-selected style
        for (Widget popupChild : getPopup()) {
            if (popupChild instanceof VAbstractOrderedLayout) {
                VAbstractOrderedLayout content = (VAbstractOrderedLayout) popupChild;
                for (Widget slot : content) {
                    Widget contentChild = ((Slot) slot).getWidget();

                    if (contentChild instanceof VButton) {
                        VButton button = (VButton) contentChild;

                        if (button.isEnabled() && !button.getStyleName().contains(SELECTED_ITEM_STYLE)) {
                            button.addStyleName(SELECTED_ITEM_STYLE);
                            button.setFocus(true);
                            break;
                        }
                    }
                }
            }
        }

        // add focus handler
        for (Widget popupChild : getPopup()) {
            if (popupChild instanceof VAbstractOrderedLayout) {
                VAbstractOrderedLayout content = (VAbstractOrderedLayout) popupChild;
                for (Widget slot : content) {
                    Widget contentChild = ((Slot) slot).getWidget();

                    if (contentChild instanceof VButton) {
                        final VButton button = (VButton) contentChild;

                        button.addFocusHandler(new FocusHandler() {
                            @Override
                            public void onFocus(FocusEvent event) {
                                childButtonFocused(button);
                            }
                        });

                        // sink mouse over
                        DOM.sinkEvents(button.getElement(),
                                Event.ONMOUSEOVER | DOM.getEventsSunk(button.getElement()));
                    }
                }
            }
        }
    }

    protected void childButtonFocused(VButton targetButton) {
        resetSelectedItem();

        targetButton.addStyleName(SELECTED_ITEM_STYLE);
    }

    protected void resetSelectedItem() {
        for (Widget popupChild : getPopup()) {
            if (popupChild instanceof VAbstractOrderedLayout) {
                VAbstractOrderedLayout content = (VAbstractOrderedLayout) popupChild;
                for (Widget slot : content) {
                    Widget contentChild = ((Slot) slot).getWidget();

                    if (contentChild instanceof VButton) {
                        VButton button = (VButton) contentChild;

                        if (button.getStyleName().contains(SELECTED_ITEM_STYLE)) {
                            button.removeStyleName(SELECTED_ITEM_STYLE);
                        }
                    }
                }
            }
        }
    }
}