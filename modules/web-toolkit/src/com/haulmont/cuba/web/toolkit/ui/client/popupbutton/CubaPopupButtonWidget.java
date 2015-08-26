/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.popupbutton;

import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;
import com.haulmont.cuba.web.toolkit.ui.client.jqueryfileupload.CubaFileUploadWidget;
import com.vaadin.client.ui.VButton;
import com.vaadin.client.ui.VUpload;
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

                    VButton button = null;
                    if (contentChild instanceof CubaFileUploadWidget) {
                        button = ((CubaFileUploadWidget) contentChild).getSubmitButton();
                    } else if (contentChild instanceof VUpload) {
                        button = ((VUpload) contentChild).submitButton;
                    } else if (contentChild instanceof VButton) {
                        button = (VButton) contentChild;
                    }

                    if (button != null) {
                        final VButton finalButton = button;
                        button.addFocusHandler(new FocusHandler() {
                            @Override
                            public void onFocus(FocusEvent event) {
                                childWidgetFocused(finalButton);
                            }
                        });

                        // sink mouse over
                        DOM.sinkEvents(button.getElement(), Event.ONMOUSEOVER | DOM.getEventsSunk(button.getElement()));
                    }
                }
            }
        }
    }

    protected void childWidgetFocused(Widget target) {
        resetSelectedItem();

        target.addStyleName(SELECTED_ITEM_STYLE);
    }

    protected void resetSelectedItem() {
        for (Widget popupChild : getPopup()) {
            if (popupChild instanceof VAbstractOrderedLayout) {
                VAbstractOrderedLayout content = (VAbstractOrderedLayout) popupChild;
                for (Widget slot : content) {
                    Widget contentChild = ((Slot) slot).getWidget();

                    VButton button = null;
                    if (contentChild instanceof CubaFileUploadWidget) {
                        button = ((CubaFileUploadWidget) contentChild).getSubmitButton();
                    } else if (contentChild instanceof VButton) {
                        button = (VButton) contentChild;
                    } else if (contentChild instanceof VUpload) {
                        button = ((VUpload) contentChild).submitButton;
                    }

                    if (button != null && button.getStyleName().contains(SELECTED_ITEM_STYLE)) {
                        button.removeStyleName(SELECTED_ITEM_STYLE);
                    }
                }
            }
        }
    }

    @Override
    protected boolean handleKeyboardEvents(Event event) {
        int type = DOM.eventGetType(event);
        // Synthesize clicks based on keyboard events AFTER the normal key
        // handling.
        if ((event.getTypeInt() & Event.KEYEVENTS) != 0) {
            switch (type) {
                case Event.ONKEYDOWN:
                    // Button should not react on shortcuts with ENTER and SPACE
                    if (isShortcut(event)
                            && (event.getKeyCode() == KeyCodes.KEY_ENTER
                            || event.getKeyCode() == KeyCodes.KEY_SPACE)) {
                        return true;
                    }
                    // Stop propagation when the user starts pressing a button that
                    // we are handling to prevent actions from getting triggered
                    if (event.getKeyCode() == KeyCodes.KEY_SPACE) {
                        isFocusing = true;
                        event.preventDefault();
                        event.stopPropagation();
                        return true;
                    } else if (event.getKeyCode() == KeyCodes.KEY_ENTER) {
                        isFocusing = true;
                        event.stopPropagation();
                        return true;
                    } else if (event.getKeyCode() == KeyCodes.KEY_DOWN) {
                        isFocusing = true;
                        event.stopPropagation();
                    }
                    break;

                // CAUTION IE sometimes does not generate ONKEYPRESS for ENTER, so we override default Vaadin behavior
                case Event.ONKEYUP:
                    if (isFocusing) {
                        if (event.getKeyCode() == KeyCodes.KEY_SPACE) {
                            isFocusing = false;
                            onClick();
                            event.stopPropagation();
                            event.preventDefault();
                            return true;
                        } else if (event.getKeyCode() == KeyCodes.KEY_ENTER) {
                            isFocusing = false;
                            onClick();
                            event.stopPropagation();
                            event.preventDefault();
                            return true;
                        } else if (event.getKeyCode() == KeyCodes.KEY_DOWN) {
                            isFocusing = false;
                            onClick();
                            event.stopPropagation();
                            event.preventDefault();
                            return true;
                        }
                    } else if (isShortcut(event)
                            && (event.getKeyCode() == KeyCodes.KEY_ENTER
                            || event.getKeyCode() == KeyCodes.KEY_SPACE)) {
                        return true;
                    }
                    break;
                case Event.ONKEYPRESS: {
                    // Button should not react on shortcuts with ENTER and SPACE
                    if (isShortcut(event)
                            && (event.getKeyCode() == KeyCodes.KEY_ENTER
                            || event.getKeyCode() == KeyCodes.KEY_SPACE)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    protected boolean isShortcut(Event event) {
        return event.getShiftKey() || event.getAltKey() || event.getCtrlKey() || event.getMetaKey();
    }
}