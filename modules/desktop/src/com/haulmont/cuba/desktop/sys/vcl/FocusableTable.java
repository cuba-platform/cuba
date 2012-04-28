/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.desktop.sys.vcl;

import org.jdesktop.swingx.JXTable;
import sun.awt.CausedFocusEvent;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.util.Set;

/**
 * @author artamonov
 * @version $Id$
 */
public class FocusableTable extends JXTable {

    public FocusableTable() {
    }

    @Override
    protected boolean processKeyBinding(KeyStroke ks, KeyEvent e, int condition, boolean pressed) {

        Set<AWTKeyStroke> forwardKeys = KeyboardFocusManager.getCurrentKeyboardFocusManager().getDefaultFocusTraversalKeys(
                KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS);
        Set<AWTKeyStroke> backwardKeys = KeyboardFocusManager.getCurrentKeyboardFocusManager().getDefaultFocusTraversalKeys(
                KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS);
        if (forwardKeys.contains(ks)) {
            nextFocusElement();
            return true;
        } else if (backwardKeys.contains(ks)) {
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

    @Override
    protected void processFocusEvent(FocusEvent e) {
        if (e.getID() == FocusEvent.FOCUS_GAINED) {
            if (e instanceof CausedFocusEvent) {
                if (((CausedFocusEvent) e).getCause() == CausedFocusEvent.Cause.TRAVERSAL_FORWARD) {
                    if (getModel().getRowCount() > 0) {
                        moveToStart(0, 0);
                    } else
                        transferFocus();

                } else if (((CausedFocusEvent) e).getCause() == CausedFocusEvent.Cause.TRAVERSAL_BACKWARD) {
                    if (getModel().getRowCount() > 0) {
                        moveToEnd(getRowCount() - 1, getColumnCount() - 1);
                    } else
                        transferFocusBackward();
                }
            }
        }

        super.processFocusEvent(e);
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
        moveToStart(nextRow, editingColumn);
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
        moveToStart(nextRow, editingColumn);
    }

    protected void prevFocusElement() {
        int selectedColumn = getActiveColumn();
        int selectedRow = getActiveRow();
        int prevColumn = selectedColumn - 1;
        int prevRow = selectedRow;

        if (selectedColumn == -1 || selectedRow == -1) {
            if (getModel().getRowCount() > 0) {
                moveToEnd(getRowCount() - 1, getColumnCount() - 1);
            } else
                moveFocusToPrevControl();
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
            if (prevRow < 0)
                transferFocusBackward();
            else
                moveToEnd(prevRow, prevColumn);
        }
    }

    protected void nextFocusElement() {
        int selectedColumn = getActiveColumn();
        int selectedRow = getActiveRow();
        int nextColumn = selectedColumn + 1;
        int nextRow = selectedRow;
        if (selectedColumn == -1 || selectedRow == -1) {
            if (getModel().getRowCount() > 0) {
                moveToStart(0, 0);
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
            if (nextRow > getRowCount() - 1)
                transferFocus();
            else
                moveToStart(nextRow, nextColumn);
        }
    }

    private void moveTo(int row, int col) {
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
    }

    protected void moveToStart(int row, int col) {
        moveTo(row, col);
        JComponent newEditorComp = (JComponent) getEditorComponent();

        if (newEditorComp != null) {
            newEditorComp.requestFocusInWindow();
            KeyboardFocusManager focusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
            FocusTraversalPolicy defaultFocusTraversalPolicy = focusManager.getDefaultFocusTraversalPolicy();
            Component component = defaultFocusTraversalPolicy.getFirstComponent(newEditorComp);

            if (component != null)
                component.requestFocus();
        }
    }

    protected void moveToEnd(int row, int col) {
        moveTo(row, col);
        JComponent newEditorComp = (JComponent) getEditorComponent();

        if (newEditorComp != null) {
            newEditorComp.requestFocusInWindow();
            KeyboardFocusManager focusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
            FocusTraversalPolicy defaultFocusTraversalPolicy = focusManager.getDefaultFocusTraversalPolicy();
            Component component = defaultFocusTraversalPolicy.getLastComponent(newEditorComp);

            if (component != null)
                component.requestFocus();
        }
    }

    protected JComponent getActiveComponent() {
        return (JComponent) getEditorComponent();
    }

    protected int getActiveColumn() {
        int editingColumn = getEditingColumn();
        int selectedColumn = getSelectedColumn();
        if (editingColumn < 0)
            return selectedColumn;
        else
            return editingColumn;
    }

    protected int getActiveRow() {
        int editingRow = getEditingRow();
        int selectedRow = getSelectedRow();
        if (editingRow < 0)
            return selectedRow;
        else
            return editingRow;
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

            return true;
        }

        return false;
    }

    protected void moveFocusToNextControl() {
        FocusHelper.moveFocusToNextControl();
    }

    protected void moveFocusToPrevControl() {
        FocusHelper.moveFocusToPrevControl();
    }
}
