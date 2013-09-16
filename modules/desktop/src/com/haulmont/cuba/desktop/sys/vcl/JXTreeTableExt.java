/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.sys.vcl;

import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.TreeTableModel;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.util.Enumeration;
import java.util.HashSet;

/**
 * @author artamonov
 * @version $Id$
 */
public class JXTreeTableExt extends JXTreeTable implements FocusableTable {
    protected HashSet<TreePath> expandedPaths;

    protected TableFocusManager focusManager = new TableFocusManager(this);

    @Override
    protected boolean processKeyBinding(KeyStroke ks, KeyEvent e, int condition, boolean pressed) {
        if (focusManager.processKeyBinding(ks, e, condition, pressed))
            return true;
        else
            return super.processKeyBinding(ks, e, condition, pressed);
    }

    @Override
    protected void processFocusEvent(FocusEvent e) {
        focusManager.processFocusEvent(e);
        super.processFocusEvent(e);
    }

    public void backupExpandedNodes() {
        expandedPaths = new HashSet<TreePath>();

        TreeTableModel treeTableModel = getTreeTableModel();
        TreePath rootPath = new TreePath(treeTableModel.getRoot());
        Enumeration<?> expandedDescendants = getExpandedDescendants(rootPath);

        if (expandedDescendants != null) {
            while (expandedDescendants.hasMoreElements()) {
                expandedPaths.add((TreePath) expandedDescendants.nextElement());
            }
        }
    }

    public void restoreExpandedNodes() {
        if ((expandedPaths != null) && (!expandedPaths.isEmpty())) {
            for (TreePath expandedPath : expandedPaths) {
                int pathRow = getRowForPath(expandedPath);
                if (pathRow >= 0) {
                    expandPath(expandedPath);
                }
            }
        }
        expandedPaths = null;
    }

    @Override
    public TableFocusManager getFocusManager() {
        return focusManager;
    }
}
