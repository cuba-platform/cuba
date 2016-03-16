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

package com.haulmont.cuba.desktop.sys.vcl;

import java.awt.*;

/**
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
