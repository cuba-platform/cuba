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

package com.haulmont.cuba.gui.components.mainwindow;

import com.haulmont.cuba.gui.components.Component;
import com.haulmont.cuba.gui.components.VBoxLayout;

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