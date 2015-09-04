/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.gui.components.mainwindow;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.VBoxLayout;

/**
 * @author artamonov
 * @version $Id$
 */
public interface AppWorkArea extends Component.BelongToFrame {

    String NAME = "workArea";

    enum Mode {
        /**
         * If the main window is in TABBED mode, it creates the Tabsheet inside
         * and opens screens with {@link com.haulmont.cuba.gui.WindowManager.OpenType#NEW_TAB} as tabs.
         */
        TABBED,

        /**
         * In SINGLE mode each new screen opened with {@link com.haulmont.cuba.gui.WindowManager.OpenType#NEW_TAB}
         * opening type will replace the current screen.
         */
        SINGLE
    }

    enum State {
        INITIAL_LAYOUT,
        WINDOW_CONTAINER
    }

    Mode getMode();
    void setMode(Mode mode);

    State getState();

    VBoxLayout getInitialLayout();
    void setInitialLayout(VBoxLayout initialLayout);

    void addStateChangeListener(StateChangeListener listener);
    void removeStateChangeListener(StateChangeListener listener);

    interface StateChangeListener {
        void stateChanged(State newState);
    }
}