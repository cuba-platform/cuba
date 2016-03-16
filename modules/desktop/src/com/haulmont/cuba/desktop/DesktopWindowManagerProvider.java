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

package com.haulmont.cuba.desktop;

import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.WindowManagerProvider;

import org.springframework.stereotype.Component;
import javax.swing.*;

/**
 * Naive implementation of WindowManagerProvider. <br/>
 * Always returns WindowManager of main application frame.
 *
 */
@Component(WindowManagerProvider.NAME)
public class DesktopWindowManagerProvider implements WindowManagerProvider {

    @Override
    public WindowManager get() {
        if (!SwingUtilities.isEventDispatchThread())
            throw new IllegalStateException("Could not access to WindowManager outside Event Dispatch Thread");

        TopLevelFrame topFrame = App.getInstance().getMainFrame();

        return topFrame.getWindowManager();
    }
}