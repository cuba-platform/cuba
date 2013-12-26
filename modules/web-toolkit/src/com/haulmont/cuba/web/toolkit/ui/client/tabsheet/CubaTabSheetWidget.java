/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.tabsheet;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.vaadin.client.UIDL;
import com.vaadin.client.ui.VTabsheet;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaTabSheetWidget extends VTabsheet {

    protected TabContextMenuHandler tabContextMenuHandler;

    @Override
    protected void onTabContextMenu(final int tabIndex, ContextMenuEvent event) {
        if (tabContextMenuHandler != null)
            tabContextMenuHandler.onContextMenu(tabIndex, event);
    }

    public interface TabContextMenuHandler {
        void onContextMenu(final int tabIndex, ContextMenuEvent event);
    }

    @Override
    public void onFocus(FocusEvent event) {
        super.onFocus(event);

        addStyleDependentName("focus");
    }

    @Override
    public void onBlur(BlurEvent event) {
        super.onBlur(event);

        removeStyleDependentName("focus");
    }

    @Override
    protected void updateAdditionalProperties(UIDL tabUidl, Tab tab) {
        super.updateAdditionalProperties(tabUidl, tab);

        if (tabUidl.hasAttribute("testId")) {
            tab.getElement().setId(tabUidl.getStringAttribute("testId"));
        }
    }


    @Override
    protected boolean onTabSelected(int tabIndex) {
        boolean result = super.onTabSelected(tabIndex);
        if (waitingForResponse)
            addStyleName("adjusting");
        return result;
    }
}