package com.haulmont.cuba.gui.components.sys;

import com.haulmont.cuba.gui.components.Window;
import com.haulmont.cuba.gui.screen.Screen;

/**
 * Internal. Provides API for WindowManager implementations.
 */
public interface WindowImplementation extends Window, FrameImplementation {
    void setFrameOwner(Screen screen);
}