/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.sys.vcl;

import sun.awt.CausedFocusEvent;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.util.Set;

/**
 * <p>Handle focus traversing in table</p>
 * <p>Supports focus forward, backward, up and down navigation</p>
 * <p>Keys:
 * <li>TAB - next cell/control</li>
 * <li>SHIFT+TAB - previous cell/control</li>
 * <li>CTRL+TAB - next component after table</li>
 * <li>CTRL+SHIFT+TAB - previous component before table</li>
 * </p>
 *
 * @author artamonov
 * @version $Id$
 */
public class TableFocusManager {

    protected JTable impl;

    public TableFocusManager(JTable impl) {
        this.impl = impl;
    }

    public boolean isDisabledKeys(KeyEvent e) {
        return (e.getModifiers() & KeyEvent.CTRL_MASK) > 0 && (e.getModifiers() & KeyEvent.SHIFT_MASK) > 0;
    }

    public boolean processKeyBinding(KeyStroke ks, KeyEvent e, int condition, boolean pressed) {
        Set<AWTKeyStroke> forwardKeys = KeyboardFocusManager.getCurrentKeyboardFocusManager().getDefaultFocusTraversalKeys(
                KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS);
        Set<AWTKeyStroke> backwardKeys = KeyboardFocusManager.getCurrentKeyboardFocusManager().getDefaultFocusTraversalKeys(
                KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS);
        if (forwardKeys.contains(ks)) {
            if ((e.getModifiers() & KeyEvent.CTRL_MASK) > 0)
                moveFocusToNextControl();
            else
                nextFocusElement();
            return true;
        } else if (backwardKeys.contains(ks)) {
            if ((e.getModifiers() & KeyEvent.CTRL_MASK) > 0)
                moveFocusToPrevControl();
            else
                prevFocusElement();
            return true;
        } else if (e.getModifiers() == 0) {
//            if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_UP ) {
//                impl.getSelectionModel().setValueIsAdjusting(pressed);
//            }

            return processExtraKeyBinding(ks, e, condition, pressed);
        }

        return false;
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
            impl.requestFocus();
            impl.editingCanceled(new ChangeEvent(this));
            // allow handle ESCAPE in window
            return false;
        } else {
            return false;
        }
    }

    public void processFocusEvent(FocusEvent e) {
        if (e.getID() == FocusEvent.FOCUS_GAINED) {
            if (e instanceof CausedFocusEvent) {
                if (((CausedFocusEvent) e).getCause() == CausedFocusEvent.Cause.TRAVERSAL_FORWARD) {
                    if (impl.getModel().getRowCount() > 0) {
                        // if focus from cell editor
                        if (e.getSource() == impl && impl.getSelectedRow() >= 0) {
                            int selectedColumn = impl.getSelectedColumn();
                            focusTo(impl.getSelectedRow(), selectedColumn >= 0 ? selectedColumn : 0);
                        } else
                            moveToStart(0, 0);
                    } else
                        impl.transferFocus();

                } else if (((CausedFocusEvent) e).getCause() == CausedFocusEvent.Cause.TRAVERSAL_BACKWARD) {
                    if (impl.getModel().getRowCount() > 0) {
                        moveToEnd(impl.getRowCount() - 1, impl.getColumnCount() - 1);
                    } else
                        impl.transferFocusBackward();
                }
            }
        }
    }

    /**
     * Navigate down throw Table rows
     */
    protected void nextDownElement() {
        int editingColumn = getActiveColumn();
        int editingRow = getActiveRow();
        int nextRow = editingRow + 1;
        if (editingRow == -1) {
            return;
        }
        if (editingColumn == -1) {
            editingColumn = 0;
        }
        if (nextRow > impl.getRowCount() - 1) {
            nextRow = 0;
        }
        moveToStart(nextRow, editingColumn);
    }

    /**
     * Navigate up throw Table rows
     */
    protected void nextUpElement() {
        int editingColumn = getActiveColumn();
        int editingRow = getActiveRow();
        int nextRow = editingRow - 1;
        if (editingRow == -1) {
            return;
        }
        if (editingColumn == -1) {
            editingColumn = 0;
        }
        if (nextRow == -1) {
            nextRow = impl.getRowCount() - 1;
        }
        moveToStart(nextRow, editingColumn);
    }

    /**
     * Navigate to prev active control or cell in table
     */
    public void prevFocusElement() {
        int selectedColumn = getActiveColumn();
        int selectedRow = getActiveRow();
        int prevColumn = selectedColumn - 1;
        int prevRow = selectedRow;
        if (selectedColumn == -1) {
            selectedColumn = 0;
        }

        if (selectedRow == -1) {
            if (impl.getModel().getRowCount() > 0) {
                moveToEnd(impl.getRowCount() - 1, impl.getColumnCount() - 1);
            } else
                moveFocusToPrevControl();
            return;
        }

        if (selectedColumn == 0) {
            prevColumn = impl.getColumnCount() - 1;
            prevRow = selectedRow - 1;
        }

        JComponent activeComponent = getActiveComponent();
        boolean wasMoved = false;
        if (activeComponent != null) {
            wasMoved = moveFocusPrevIntoComponent(activeComponent);
        }

        if (!wasMoved) {
            if (prevRow < 0)
                impl.transferFocusBackward();
            else
                moveToEnd(prevRow, prevColumn);
        }
    }

    /**
     * Navigate to previous active control or cell in table
     */
    public void nextFocusElement() {
        int selectedColumn = getActiveColumn();
        int selectedRow = getActiveRow();
        int nextColumn = selectedColumn + 1;
        int nextRow = selectedRow;
        if (selectedColumn == -1) {
            selectedColumn = 0;
        }

        if (selectedRow == -1) {
            if (impl.getModel().getRowCount() > 0) {
                moveToStart(0, 0);
            } else
                moveFocusToNextControl();

            return;
        }
        if (selectedColumn == impl.getColumnCount() - 1) {
            nextColumn = 0;
            nextRow = selectedRow + 1;
        }

        JComponent activeComponent = getActiveComponent();
        boolean wasMoved = false;
        if (activeComponent != null) {
            wasMoved = moveFocusNextIntoComponent(activeComponent);
        }

        if (!wasMoved) {
            if (nextRow > impl.getRowCount() - 1)
                impl.transferFocus();
            else
                moveToStart(nextRow, nextColumn);
        }
    }

    /**
     * Focus first cell in specified row
     *
     * @param selectedRow Focused row
     */
    public void focusSelectedRow(int selectedRow) {
        if (impl.getModel().getRowCount() > 0) {
            focusTo(selectedRow, 0);
        } else {
            moveFocusToNextControl();
        }
    }

    /**
     * Scroll to first cell in specified row
     *
     * @param selectedRow row
     */
    public void scrollToSelectedRow(int selectedRow) {
        if (impl.getModel().getRowCount() > 0) {
            scrollTo(selectedRow, 0);
        }
    }

    protected void moveTo(int row, int col) {
        Component editorComp = impl.getEditorComponent();

        if (editorComp != null) {
            editorComp.dispatchEvent(new FocusEvent(editorComp, FocusEvent.FOCUS_LOST, false, impl));
        }
        impl.scrollRectToVisible(impl.getCellRect(row, col, true));

        if (row >= 0 && col >= 0)
            impl.requestFocus();

        impl.getSelectionModel().setSelectionInterval(row, row);
        impl.getColumnModel().getSelectionModel().setSelectionInterval(col, col);
        impl.editCellAt(
                impl.getSelectedRow(),
                impl.getSelectedColumn()
        );
    }

    protected void focusTo(int row, int col) {
        if (row >= 0) {
            impl.requestFocus();

            impl.getSelectionModel().setSelectionInterval(row, row);
            impl.getColumnModel().getSelectionModel().setSelectionInterval(col, col);

            scrollTo(row, col);
        }
    }

    protected void scrollTo(int row, int col) {
        if (row >= 0) {
            Rectangle cellRect = impl.getCellRect(row, col, true);
            impl.scrollRectToVisible(cellRect);
        }
    }

    protected void moveToStart(int row, int col) {
        moveTo(row, col);
        JComponent newEditorComp = (JComponent) impl.getEditorComponent();

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
        JComponent newEditorComp = (JComponent) impl.getEditorComponent();

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
        return (JComponent) impl.getEditorComponent();
    }

    protected int getActiveColumn() {
        int editingColumn = impl.getEditingColumn();
        int selectedColumn = impl.getSelectedColumn();
        if (editingColumn < 0)
            return selectedColumn;
        else
            return editingColumn;
    }

    protected int getActiveRow() {
        int editingRow = impl.getEditingColumn() == -1 ? -1 : impl.getEditingRow();
        int selectedRow = impl.getSelectedRow();
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
            if (focusOwner == impl) {
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
            if (focusOwner == impl) {
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