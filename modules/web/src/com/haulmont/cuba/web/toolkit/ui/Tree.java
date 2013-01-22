/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Nikolay Gorodnov
 * Created: 09.06.2010 15:13:21
 *
 * $Id$
 */
package com.haulmont.cuba.web.toolkit.ui;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;

import java.util.LinkedList;

@SuppressWarnings("serial")
public class Tree extends com.vaadin.ui.Tree {
    private boolean doubleClickMode = false;

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        super.paintContent(target);

        if (isDoubleClickMode()) {
            target.addAttribute("doubleClickMode", isDoubleClickMode());
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
}
