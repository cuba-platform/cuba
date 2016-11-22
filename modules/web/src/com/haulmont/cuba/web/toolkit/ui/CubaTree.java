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

package com.haulmont.cuba.web.toolkit.ui;

import com.google.common.base.Preconditions;
import com.haulmont.cuba.web.toolkit.ShortcutActionManager;
import com.haulmont.cuba.web.toolkit.ui.client.tree.CubaTreeClientRpc;
import com.haulmont.cuba.web.toolkit.ui.client.tree.CubaTreeState;
import com.vaadin.event.Action;
import com.vaadin.event.ActionManager;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.PaintException;
import com.vaadin.server.PaintTarget;
import com.vaadin.server.Resource;
import com.vaadin.ui.Component;
import com.vaadin.ui.HasComponents;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Tree;

import java.util.*;

public class CubaTree extends Tree implements HasComponents {

    protected Runnable beforePaintListener;

    public CubaTree() {
        setValidationVisible(false);
        setShowBufferedSourceException(false);
    }

    /**
     * Keeps track of the ShortcutListeners added to this component, and manages the painting and handling as well.
     */
    protected ActionManager shortcutActionManager;
    protected ItemIconProvider itemIconProvider;

    @Override
    protected CubaTreeState getState() {
        return (CubaTreeState) super.getState();
    }

    @Override
    protected CubaTreeState getState(boolean markAsDirty) {
        return (CubaTreeState) super.getState(markAsDirty);
    }

    public void setContextMenuPopup(Layout contextMenu) {
        getState().contextMenu = contextMenu;
    }

    public void hideContextMenuPopup() {
        getRpcProxy(CubaTreeClientRpc.class).hideContextMenuPopup();
    }

    public void setDoubleClickMode(boolean doubleClickMode) {
        if (getState(false).doubleClickMode != doubleClickMode) {
            getState().doubleClickMode = doubleClickMode;
        }
    }

    public boolean isDoubleClickMode() {
        return getState(false).doubleClickMode;
    }

    public void setNodeCaptionsAsHtml(boolean nodeCaptionsAsHtml) {
        if (getState(false).nodeCaptionsAsHtml != nodeCaptionsAsHtml) {
            getState().nodeCaptionsAsHtml = nodeCaptionsAsHtml;
        }
    }

    public boolean isNodeCaptionsAsHtml() {
        return getState(false).nodeCaptionsAsHtml;
    }

    @Override
    public void changeVariables(Object source, Map<String, Object> variables) {
        super.changeVariables(source, variables);

        if (shortcutActionManager != null) {
            shortcutActionManager.handleActions(variables, this);
        }
    }

    @Override
    public void addShortcutListener(ShortcutListener shortcut) {
        if (shortcutActionManager == null) {
            shortcutActionManager = new ShortcutActionManager(this);
        }

        shortcutActionManager.addAction(shortcut);
    }

    @Override
    public void removeShortcutListener(ShortcutListener shortcut) {
        if (shortcutActionManager != null) {
            shortcutActionManager.removeAction(shortcut);
        }
    }

    @Override
    protected void paintActions(PaintTarget target, Set<Action> actionSet) throws PaintException {
        super.paintActions(target, actionSet);

        if (shortcutActionManager != null) {
            shortcutActionManager.paintActions(null, target);
        }
    }

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        if (beforePaintListener != null) {
            beforePaintListener.run();
        }

        if (isNodeCaptionsAsHtml()) {
            target.addAttribute("nodeCaptionsAsHtml", true);
        }
        super.paintContent(target);
    }

    @Override
    public Iterator<Component> iterator() {
        if (getState(false).contextMenu != null) {
            return Collections.singleton((Component)getState(false).contextMenu).iterator();
        }
        return Collections.emptyIterator();
    }

    public void expandAll() {
        for (Object id : getItemIds()) {
            expandItemRecursively(id);
        }
    }

    public void expandItemRecursively(Object id) {
        expandItem(id);
        if (hasChildren(id)) {
            for (Object childId: getChildren(id)) {
                expandItemRecursively(childId);
            }
        }
    }

    public void expandItemWithParents(Object id) {
        Object currentId = id;
        while (currentId != null) {
            expandItem(currentId);

            currentId = getParent(currentId);
        }
    }

    public void collapseItemRecursively(Object id) {
        if (hasChildren(id)) {
            for (Object childId: getChildren(id)) {
                collapseItemRecursively(childId);
            }
        }
        collapseItem(id);
    }

    public void collapseAll() {
        for (Object id : getItemIds()) {
            collapseItemRecursively(id);
        }
    }

    public void expandUpTo(int level) {
        Preconditions.checkArgument(level > 0, "level should be greater than 0");

        List<Object> currentLevelItemIds = new ArrayList<>(getItemIds());

        int i = 0;
        while (i < level && !currentLevelItemIds.isEmpty()) {
            for (Object itemId : new ArrayList<>(currentLevelItemIds)) {
                expandItem(itemId);
                currentLevelItemIds.remove(itemId);
                currentLevelItemIds.addAll(getChildren(itemId));
            }
            i++;
        }
    }

    @Override
    public Resource getItemIcon(Object itemId) {
        if (itemIconProvider != null) {
            Resource itemIcon = itemIconProvider.getItemIcon(itemId);
            if (itemIcon != null) {
                return itemIcon;
            }
        }

        return super.getItemIcon(itemId);
    }

    public ItemIconProvider getItemIconProvider() {
        return itemIconProvider;
    }

    public void setItemIconProvider(ItemIconProvider itemIconProvider) {
        if (this.itemIconProvider != itemIconProvider) {
            this.itemIconProvider = itemIconProvider;
            markAsDirty();
        }
    }

    public interface ItemIconProvider {
        Resource getItemIcon(Object itemId);
    }

    public void setBeforePaintListener(Runnable beforePaintListener) {
        this.beforePaintListener = beforePaintListener;
    }
}