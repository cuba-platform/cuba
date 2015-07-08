/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.toolkit.ui.client.tabsheet;

import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.user.client.ui.Widget;
import com.haulmont.cuba.web.toolkit.ui.client.appui.ValidationErrorHolder;
import com.vaadin.client.ui.VTabsheet;
import com.vaadin.shared.ui.tabsheet.TabState;

/**
 * @author artamonov
 * @version $Id$
 */
public class CubaTabSheetWidget extends VTabsheet {

    protected TabContextMenuHandler tabContextMenuHandler;
    protected CubaTabBar tabBar;

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
        tabBar = new CubaTabBar(this);
        return tabBar;
    }

    @Override
    public void renderTab(TabState tabState, int index) {
        super.renderTab(tabState, index);

        Tab tab = tabBar.getTab(index);
        if (tab.isHiddenOnServer()) {
            tab.removeStyleName("cuba-tab-visible");
            tab.addStyleName("cuba-tab-hidden");
        } else {
            tab.removeStyleName("cuba-tab-hidden");
            tab.addStyleName("cuba-tab-visible");
        }
    }

    public void assignAdditionalCellStyles() {
        assignAdditionalCellStyles(activeTabIndex);
    }

    public void assignAdditionalCellStyles(int navIndex) {
        if (navIndex >=0) {
            int i = 0;
            boolean firstVisibleAfterSelection = false;
            for (Widget widget : tabBar) {
                Tab t = (Tab) widget;
                t.removeStyleName("cuba-tab-sibling-visible");

                if (!firstVisibleAfterSelection
                        && i > navIndex
                        && !t.isHiddenOnServer()) {
                    t.addStyleName("cuba-tab-sibling-visible");
                    firstVisibleAfterSelection = true;
                }

                i++;
            }
        }
    }

    public class CubaTabBar extends TabBar {
        public CubaTabBar(VTabsheet tabsheet) {
            super(tabsheet);
        }

        @Override
        protected Tab createTab() {
            return new CubaTab(this);
        }

        @Override
        public Tab navigateTab(int fromIndex, int toIndex) {
            Tab navigateTab = super.navigateTab(fromIndex, toIndex);
            if (navigateTab != null) {
                assignAdditionalCellStyles(toIndex);
            }
            return navigateTab;
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