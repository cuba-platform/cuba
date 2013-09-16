/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.desktop.sys.vcl;

import java.awt.*;

/**
 * @author artamonov
 * @version $Id$
 */
public abstract class FocusHelper {

    public static void moveFocusToNextControl() {
        Component focusOwner = getFocusedComponent();
        moveFocusToNextControl(focusOwner);
    }

    public static Component getFocusedComponent() {
        KeyboardFocusManager focusManager =
                KeyboardFocusManager.getCurrentKeyboardFocusManager();

        return focusManager.getFocusOwner();
    }

    public static void moveFocusToNextControl(Component focusOwner) {
        KeyboardFocusManager focusManager =
                KeyboardFocusManager.getCurrentKeyboardFocusManager();

        Container currentFocusCycleRoot =
                focusManager.getCurrentFocusCycleRoot();

        FocusTraversalPolicy policy =
                focusManager.getDefaultFocusTraversalPolicy();

        if (currentFocusCycleRoot != null || focusOwner != null) {
            Component componentAfter =
                    policy.getComponentAfter(currentFocusCycleRoot, focusOwner);
            componentAfter.requestFocus();
        }
    }

    public static void moveFocusToPrevControl(Component focusOwner) {
        KeyboardFocusManager focusManager =
                KeyboardFocusManager.getCurrentKeyboardFocusManager();

        Container currentFocusCycleRoot =
                focusManager.getCurrentFocusCycleRoot();

        FocusTraversalPolicy policy =
                focusManager.getDefaultFocusTraversalPolicy();

        if (currentFocusCycleRoot != null || focusOwner != null) {
            Component componentBefore =
                    policy.getComponentBefore(currentFocusCycleRoot, focusOwner);
            componentBefore.requestFocus();
        }
    }

    public static void moveFocusToPrevControl() {
        Component focusOwner = getFocusedComponent();
        moveFocusToPrevControl(focusOwner);
    }
}
