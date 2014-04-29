/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.web.toolkit.ui;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.Layout;

import java.util.LinkedList;

/**
 * @author gorodnov
 * @version $Id$
 */
@SuppressWarnings("serial")
public class Tree extends com.vaadin.ui.Tree {
    private boolean doubleClickMode = false;

    protected Layout comboBoxMenu;
    protected boolean sendHideContextMenu = false;

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        super.paintContent(target);

        if (isDoubleClickMode()) {
            target.addAttribute("doubleClickMode", isDoubleClickMode());
        }

        if (comboBoxMenu != null) {
            target.startTag("cm");
            comboBoxMenu.paint(target);
            target.endTag("cm");
        }

        if (sendHideContextMenu) {
            target.addAttribute("hideContextMenu", true);
            sendHideContextMenu = false;
        }
    }

    @Override
    protected void paintItem(PaintTarget target, Object itemId, LinkedList<String> selectedKeys,
                             LinkedList<String> expandedKeys) throws PaintException {
        super.paintItem(target, itemId, selectedKeys, expandedKeys);
        if (areChildrenAllowed(itemId) && hasChildren(itemId)) {
            target.addAttribute("hasChildren", true);
        }
    }

    public boolean isDoubleClickMode() {
        return doubleClickMode;
    }

    public void setDoubleClickMode(boolean doubleClickMode) {
        this.doubleClickMode = doubleClickMode;
        requestRepaint();
    }

    public Layout getComboBoxMenu() {
        return comboBoxMenu;
    }

    public void setComboBoxMenu(Layout comboBoxMenu) {
        this.comboBoxMenu = comboBoxMenu;
    }

    public void hideContextMenu() {
        sendHideContextMenu = true;
        requestRepaint();
    }
}
