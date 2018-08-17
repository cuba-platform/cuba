/*
 * Copyright (c) 2008-2018 Haulmont.
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

package com.haulmont.cuba.gui;

import com.haulmont.cuba.gui.config.WindowInfo;
import com.haulmont.cuba.gui.screen.FrameOwner;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.ScreenOptions;

/**
 * JavaDoc
 */
public interface Screens {

    String NAME = "cuba_Screens";

    default <T extends Screen> T create(Class<T> screenClass, LaunchMode launchMode) {
        return create(screenClass, launchMode, FrameOwner.NO_OPTIONS);
    }

    default Screen create(WindowInfo windowInfo, LaunchMode launchMode) {
        return create(windowInfo, launchMode, FrameOwner.NO_OPTIONS);
    }

    /**
     * JavaDoc
     */
    <T extends Screen> T create(Class<T> screenClass, LaunchMode launchMode, ScreenOptions options);

    /**
     * JavaDoc
     */
    Screen create(WindowInfo windowInfo, LaunchMode launchMode, ScreenOptions options);

    /**
     * JavaDoc
     */
    void show(Screen screen);

    /**
     * Removes window from UI and releases all the resources of screen.
     *
     * @param screen screen
     */
    void remove(Screen screen);

    /**
     * JavaDoc
     */
    void removeAll();

    /**
     * JavaDoc
     *
     * @return true if there are windows with unsaved changes
     */
    boolean hasUnsavedChanges();

    /**
     * JavaDoc
     */
    interface LaunchMode {
    }
}