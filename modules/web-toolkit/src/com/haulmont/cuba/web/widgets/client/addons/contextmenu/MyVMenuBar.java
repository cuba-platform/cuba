/*
 * Copyright 2000-2018 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.haulmont.cuba.web.widgets.client.addons.contextmenu;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Command;
import com.vaadin.client.ui.VMenuBar;
import com.vaadin.client.ui.VOverlay;

/**
 * This is just to overcome the issue of application connection. Not needed
 * later, after this issue is resolved in the framework.
 */
public class MyVMenuBar extends VMenuBar {

    // FIXME: this should be properly set for all context menus
    private boolean isContextMenu = true;

    public MyVMenuBar() {
    }

    public MyVMenuBar(boolean subMenu, VMenuBar parentMenu) {
        super(subMenu, parentMenu);
    }

    @Override
    protected VOverlay createOverlay() {
        return new MyVOverlay(true, false);
    }

    // overridden to be visible for the connector
    @Override
    protected void showChildMenuAt(CustomMenuItem item, int top, int left) {
        super.showChildMenuAt(item, top, left);
    }

    // this method has a couple lines added, marked with FIXME
    @Override
    public boolean handleNavigation(int keycode, boolean ctrl, boolean shift) {

        // If tab or shift+tab close menus
        if (keycode == KeyCodes.KEY_TAB) {
            setSelected(null);
            hideChildren();
            menuVisible = false;
            return false;
        }

        if (ctrl || shift || !enabled) {
            // Do not handle tab key, nor ctrl keys
            return false;
        }

        if (keycode == getNavigationLeftKey()) {
            if (getSelected() == null) {
                // If nothing is selected then select the last item
                setSelected(items.get(items.size() - 1));
                if (!getSelected().isSelectable()) {
                    handleNavigation(keycode, ctrl, shift);
                }
            } else if (visibleChildMenu == null && getParentMenu() == null) {
                // If this is the root menu then move to the left
                int idx = items.indexOf(getSelected());
                if (idx > 0) {
                    setSelected(items.get(idx - 1));
                } else {
                    setSelected(items.get(items.size() - 1));
                }

                if (!getSelected().isSelectable()) {
                    handleNavigation(keycode, ctrl, shift);
                }
            } else if (visibleChildMenu != null) {
                // Redirect all navigation to the submenu
                visibleChildMenu.handleNavigation(keycode, ctrl, shift);

            } else if (getParentMenu().getParentMenu() == null) {

                // FIXME: this line added
                if (isContextMenu) {
                    return true;
                }

                // Inside a sub menu, whose parent is a root menu item
                VMenuBar root = getParentMenu();

                root.getSelected().getSubMenu().setSelected(null);
                // #15255 - disable animate-in/out when hide popup
                root.hideChildren(false, false);

                // Get the root menus items and select the previous one
                int idx = root.getItems().indexOf(root.getSelected());
                idx = idx > 0 ? idx : root.getItems().size();
                CustomMenuItem selected = root.getItems().get(--idx);

                while (selected.isSeparator() || !selected.isEnabled()) {
                    idx = idx > 0 ? idx : root.getItems().size();
                    selected = root.getItems().get(--idx);
                }

                root.setSelected(selected);
                openMenuAndFocusFirstIfPossible(selected);
            } else {
                getParentMenu().getSelected().getSubMenu().setSelected(null);
                getParentMenu().hideChildren();
            }

            return true;

        } else if (keycode == getNavigationRightKey()) {

            if (getSelected() == null) {
                // If nothing is selected then select the first item
                setSelected(items.get(0));
                if (!getSelected().isSelectable()) {
                    handleNavigation(keycode, ctrl, shift);
                }
            } else if (visibleChildMenu == null && getParentMenu() == null) {
                // If this is the root menu then move to the right
                int idx = items.indexOf(getSelected());

                if (idx < items.size() - 1) {
                    setSelected(items.get(idx + 1));
                } else {
                    setSelected(items.get(0));
                }

                if (!getSelected().isSelectable()) {
                    handleNavigation(keycode, ctrl, shift);
                }
            } else if (visibleChildMenu == null
                    && getSelected().getSubMenu() != null) {
                // If the item has a submenu then show it and move the selection
                // there
                showChildMenu(getSelected());
                menuVisible = true;
                visibleChildMenu.handleNavigation(keycode, ctrl, shift);
            } else if (visibleChildMenu == null && !isContextMenu /* FIXME */) {

                // Get the root menu
                VMenuBar root = getParentMenu();
                while (root.getParentMenu() != null) {
                    root = root.getParentMenu();
                }

                // Hide the submenu (#15255 - disable animate-in/out when hide
                // popup)
                root.hideChildren(false, false);

                // Get the root menus items and select the next one
                int idx = root.getItems().indexOf(root.getSelected());
                idx = idx < root.getItems().size() - 1 ? idx : -1;
                CustomMenuItem selected = root.getItems().get(++idx);

                while (selected.isSeparator() || !selected.isEnabled()) {
                    idx = idx < root.getItems().size() - 1 ? idx : -1;
                    selected = root.getItems().get(++idx);
                }

                root.setSelected(selected);
                openMenuAndFocusFirstIfPossible(selected);
            } else if (visibleChildMenu != null) {
                // Redirect all navigation to the submenu
                visibleChildMenu.handleNavigation(keycode, ctrl, shift);
            }

            return true;

        } else if (keycode == getNavigationUpKey()) {

            if (getSelected() == null) {
                // If nothing is selected then select the last item
                setSelected(items.get(items.size() - 1));
                if (!getSelected().isSelectable()) {
                    handleNavigation(keycode, ctrl, shift);
                }
            } else if (visibleChildMenu != null) {
                // Redirect all navigation to the submenu
                visibleChildMenu.handleNavigation(keycode, ctrl, shift);
            } else {
                // Select the previous item if possible or loop to the last item
                int idx = items.indexOf(getSelected());
                if (idx > 0) {
                    setSelected(items.get(idx - 1));
                } else {
                    setSelected(items.get(items.size() - 1));
                }

                if (!getSelected().isSelectable()) {
                    handleNavigation(keycode, ctrl, shift);
                }
            }

            return true;

        } else if (keycode == getNavigationDownKey()) {

            if (getSelected() == null) {
                // If nothing is selected then select the first item
                selectFirstItem();
            } else if (visibleChildMenu == null && getParentMenu() == null) {
                // If this is the root menu the show the child menu with arrow
                // down, if there is a child menu
                openMenuAndFocusFirstIfPossible(getSelected());
            } else if (visibleChildMenu != null) {
                // Redirect all navigation to the submenu
                visibleChildMenu.handleNavigation(keycode, ctrl, shift);
            } else {
                // Select the next item if possible or loop to the first item
                int idx = items.indexOf(getSelected());
                if (idx < items.size() - 1) {
                    setSelected(items.get(idx + 1));
                } else {
                    setSelected(items.get(0));
                }

                if (!getSelected().isSelectable()) {
                    handleNavigation(keycode, ctrl, shift);
                }
            }
            return true;

        } else if (keycode == getCloseMenuKey()) {
            setSelected(null);
            hideChildren();
            menuVisible = false;

        } else if (isNavigationSelectKey(keycode)) {
            if (getSelected() == null) {
                // If nothing is selected then select the first item
                selectFirstItem();
            } else if (visibleChildMenu != null) {
                // Redirect all navigation to the submenu
                visibleChildMenu.handleNavigation(keycode, ctrl, shift);
                menuVisible = false;
            } else if (visibleChildMenu == null
                    && getSelected().getSubMenu() != null) {
                // If the item has a sub menu then show it and move the
                // selection there
                openMenuAndFocusFirstIfPossible(getSelected());
            } else {
                final Command command = getSelected().getCommand();

                setSelected(null);
                hideParents(true);

                // #17076 keyboard selected menuitem without children: do
                // not leave menu to visible ("hover open") mode
                menuVisible = false;

                Scheduler.get().scheduleDeferred(new ScheduledCommand() {
                    @Override
                    public void execute() {
                        if (command != null) {
                            command.execute();
                        }
                    }
                });
            }
        }

        return false;
    }

    private void selectFirstItem() {
        for (int i = 0; i < items.size(); i++) {
            CustomMenuItem item = items.get(i);
            if (item.isSelectable()) {
                setSelected(item);
                break;
            }
        }
    }

    private void openMenuAndFocusFirstIfPossible(CustomMenuItem menuItem) {
        MyVMenuBar subMenu = (MyVMenuBar) menuItem.getSubMenu();
        if (subMenu == null) {
            // No child menu? Nothing to do
            return;
        }

        MyVMenuBar parentMenu = (MyVMenuBar) menuItem.getParentMenu();
        parentMenu.showChildMenu(menuItem);

        menuVisible = true;
        // Select the first item in the newly open submenu
        subMenu.selectFirstItem();

    }

    public boolean isPopupShowing() {
        return menuVisible;
    }
}
