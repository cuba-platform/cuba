/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.sys.vcl;

import org.jdesktop.swingx.JXTable;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Set;

/**
 * @author artamonov
 * @version $Id$
 */
public class FocusableTable extends JXTable {

    protected boolean needClearSelection = false;

    protected boolean needFocus = false;

    @Override
    protected boolean processKeyBinding(KeyStroke ks, KeyEvent e, int condition, boolean pressed) {

        Set<AWTKeyStroke> forwardKeys = KeyboardFocusManager.getCurrentKeyboardFocusManager().getDefaultFocusTraversalKeys(
                KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS);
        Set<AWTKeyStroke> backwordKeys = KeyboardFocusManager.getCurrentKeyboardFocusManager().getDefaultFocusTraversalKeys(
                KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS);
        if (forwardKeys.contains(ks)) {
            needFocus = true;
            nextFocusElement();
            return true;
        } else if (backwordKeys.contains(ks)) {
            needFocus = true;
            prevFocusElement();
            return true;
        } else if (e.getModifiers() == 0) {
            return processExtraKeyBinding(ks, e, condition, pressed);
        }

        return super.processKeyBinding(ks, e, condition, pressed);
    }

    protected boolean processExtraKeyBinding(KeyStroke ks, KeyEvent e, int condition, boolean pressed) {
        if (e.getKeyCode() == KeyEvent.VK_UP && pressed) {
            nextUpElement();
            return true;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN && pressed) {
            nextDownElement();
            return true;
        } else if (e.getKeyCode() == KeyEvent.VK_ENTER && pressed) {
            nextFocusElement();
            return true;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT && pressed) {
            prevFocusElement();
            return true;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && pressed) {
            nextFocusElement();
            return true;
        } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE && pressed) {
            requestFocus();
            editingCanceled(new ChangeEvent(this));
            return true;
        } else {
            return super.processKeyBinding(ks, e, condition, pressed);
        }
    }

    protected void nextDownElement() {
        int editingColumn = getActiveColumn();
        int editingRow = getActiveRow();
        int nextRow = editingRow + 1;
        if (editingColumn == -1 || editingRow == -1) {
            return;
        }
        if (nextRow > getRowCount() - 1) {
            nextRow = 0;
        }
        moveTo(nextRow, editingColumn);
    }

    protected void nextUpElement() {
        int editingColumn = getActiveColumn();
        int editingRow = getActiveRow();
        int nextRow = editingRow - 1;
        if (editingColumn == -1 || editingRow == -1) {
            return;
        }
        if (nextRow == -1) {
            nextRow = getRowCount() - 1;
        }
        moveTo(nextRow, editingColumn);
    }

    protected void prevFocusElement() {
        int selectedColumn = getActiveColumn();
        int selectedRow = getActiveRow();
        int prevColumn = selectedColumn - 1;
        int prevRow = selectedRow;
        if (selectedColumn == -1 || selectedRow == -1) {
            if (getModel().getRowCount() > 0) {
                moveTo(getRowCount() - 1, getColumnCount() - 1);
                JComponent activeComponent = getActiveComponent();
                if (activeComponent != null)
                    moveFocusPrevIntoComponent(activeComponent);
            } else
                moveFocusToPreviousControl();
            return;
        }
        if (selectedColumn == 0) {
            prevColumn = getColumnCount() - 1;
            prevRow = selectedRow - 1;
        }

        JComponent activeComponent = getActiveComponent();
        boolean wasMoved = false;
        if (activeComponent != null) {
            wasMoved = moveFocusPrevIntoComponent(activeComponent);
        }

        if (!wasMoved) {
            if (prevRow < 0) {
                transferFocusBackward();
                needClearSelection = true;
            } else {
                moveTo(prevRow, prevColumn);
                activeComponent = getActiveComponent();
                if (activeComponent != null)
                    moveFocusPrevIntoComponent(activeComponent);
            }
        }
    }

    protected void moveTo(int row, int col) {
        if (editorComp != null) {
            editorComp.dispatchEvent(new FocusEvent(editorComp, FocusEvent.FOCUS_LOST, false, this));
        }
        scrollRectToVisible(getCellRect(row, col, true));

        if (row >= 0 && col >= 0)
            requestFocus();

        getSelectionModel().setSelectionInterval(row, row);
        getColumnModel().getSelectionModel().setSelectionInterval(col, col);
        editCellAt(
                getSelectedRow(),
                getSelectedColumn()
        );
        JComponent newEditorComp = (JComponent) getEditorComponent();

        if (newEditorComp != null) {
            newEditorComp.requestFocusInWindow();
            moveFocusNextIntoComponent(newEditorComp);
        }
    }

    protected int getActiveColumn() {
        return getSelectedColumn();
    }

    protected int getActiveRow() {
        return getSelectedRow();
    }

    private boolean shouldIgnore(MouseEvent e) {
        return e.isConsumed() ||
                (isEnabled() &&
                        e.getID() != MouseEvent.MOUSE_PRESSED);
    }

    @Override
    protected void processMouseEvent(MouseEvent e) {
        if (shouldIgnore(e)) {
            if (!SwingUtilities.isLeftMouseButton(e) &&
                    !SwingUtilities.isRightMouseButton(e)) {
                super.processMouseEvent(e);
            }
            return;
        }
        Point p = e.getPoint();
        int row = rowAtPoint(p);
        int column = columnAtPoint(p);
        moveTo(row, column);
    }

    @Override
    protected void processFocusEvent(FocusEvent e) {
        if (e.getID() == FocusEvent.FOCUS_GAINED) {
            if (needClearSelection) {
                clearSelection();
                needClearSelection = false;
            }

            if (editorComp != null) {
                if (!editorComp.hasFocus()) {
                    editorComp.requestFocus();
                }
            }
        } else {
            if (e.getID() == FocusEvent.FOCUS_LOST) {
                Component oppositeComponent = e.getOppositeComponent();
                boolean isParent = oppositeComponent != null && isParent(this, oppositeComponent);
                if (!isParent) {
                    needClearSelection = true;
                }
            }
            super.processFocusEvent(e);
        }
    }

    protected void moveFocusToPreviousControl() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().focusPreviousComponent(getParent());
    }

    protected void nextFocusElement() {
        int selectedColumn = getActiveColumn();
        int selectedRow = getActiveRow();
        int nextColumn = selectedColumn + 1;
        int nextRow = selectedRow;
        if (selectedColumn == -1 || selectedRow == -1) {
            if (getModel().getRowCount() > 0) {
                moveTo(0, 0);
                JComponent activeComponent = getActiveComponent();
                if (activeComponent != null)
                    moveFocusNextIntoComponent(activeComponent);
            } else
                moveFocusToNextControl();

            return;
        }
        if (selectedColumn == getColumnCount() - 1) {
            nextColumn = 0;
            nextRow = selectedRow + 1;
        }

        JComponent activeComponent = getActiveComponent();
        boolean wasMoved = false;
        if (activeComponent != null) {
            wasMoved = moveFocusNextIntoComponent(activeComponent);
        }

        if (!wasMoved) {
            if (nextRow > getRowCount() - 1) {
                transferFocus();
                needClearSelection = true;
            } else {
                moveTo(nextRow, nextColumn);
                activeComponent = getActiveComponent();
                if (activeComponent != null)
                    moveFocusNextIntoComponent(activeComponent);
            }
        }
    }

    protected boolean moveFocusNextIntoComponent(Container activeComponent) {
        KeyboardFocusManager focusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        Component focusOwner = focusManager.getFocusOwner();
        FocusTraversalPolicy defaultFocusTraversalPolicy = focusManager.getDefaultFocusTraversalPolicy();
        Component lastComponent = defaultFocusTraversalPolicy.getLastComponent(activeComponent);
        if (focusOwner != null &&
                lastComponent != null &&
                lastComponent != focusOwner) {
            if (focusOwner == this) {
                Component component = defaultFocusTraversalPolicy.getFirstComponent(activeComponent);
                if (component != null)
                    component.requestFocus();
                else
                    moveFocusToNextControl();
            } else {
                moveFocusToNextControl();
            }

            activeComponent.invalidate();
            activeComponent.repaint();

            return true;
        }

        return false;
    }

    protected boolean moveFocusPrevIntoComponent(JComponent activeComponent) {
        KeyboardFocusManager focusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        Component focusOwner = focusManager.getFocusOwner();
        FocusTraversalPolicy defaultFocusTraversalPolicy = focusManager.getDefaultFocusTraversalPolicy();
        Component firstComponent = defaultFocusTraversalPolicy.getFirstComponent(activeComponent);
        if (focusOwner != null &&
                firstComponent != null &&
                firstComponent != focusOwner) {
            if (focusOwner == this) {
                Component component = defaultFocusTraversalPolicy.getLastComponent(activeComponent);
                if (component != null)
                    component.requestFocus();
                else
                    moveFocusToPrevControl();
            } else
                moveFocusToPrevControl();

            activeComponent.invalidate();
            activeComponent.repaint();

            return true;
        }

        return false;
    }

    protected JComponent getActiveComponent() {
        return (JComponent) getEditorComponent();
    }

    protected void moveFocusToNextControl() {
        FocusHelper.moveFocusToNextControl();
    }

    protected void moveFocusToPrevControl() {
        FocusHelper.moveFocusToPrevControl();
    }

    protected boolean isParent(Component activeComponent, Component focusOwner) {
        while (focusOwner.getParent() != null) {
            if (activeComponent == focusOwner.getParent()) {
                return true;
            }
            focusOwner = focusOwner.getParent();
        }
        return false;
    }
}
