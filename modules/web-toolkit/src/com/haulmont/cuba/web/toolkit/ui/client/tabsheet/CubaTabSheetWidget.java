/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.tabsheet;

import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.haulmont.cuba.web.toolkit.ui.client.appui.ValidationErrorHolder;
import com.vaadin.client.ui.VTabsheet;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaTabSheetWidget extends VTabsheet {

    protected TabContextMenuHandler tabContextMenuHandler;

    @Override
    protected void onTabContextMenu(final int tabIndex, ContextMenuEvent event) {
        if (tabContextMenuHandler != null) {
            tabContextMenuHandler.onContextMenu(tabIndex, event);
        }
    }

    public interface TabContextMenuHandler {
        void onContextMenu(final int tabIndex, ContextMenuEvent event);
    }

    @Override
    protected VTabsheet.TabBar createTabBar() {
        return new CubaTabBar(this);
    }

    public static class CubaTabBar extends TabBar {
        public CubaTabBar(VTabsheet tabsheet) {
            super(tabsheet);
        }

        @Override
        protected Tab createTab() {
            return new CubaTab(this);
        }
    }

    public static class CubaTab extends Tab {
        public CubaTab(TabBar tabBar) {
            super(tabBar);
        }

        @Override
        public void onClose() {
            if (ValidationErrorHolder.hasValidationErrors()) {
                return;
            }

            super.onClose();
        }
    }
}