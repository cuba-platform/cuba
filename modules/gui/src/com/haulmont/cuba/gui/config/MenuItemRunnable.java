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
 */

package com.haulmont.cuba.gui.config;

import com.haulmont.cuba.gui.screen.FrameOwner;

/**
 * Interface for runnable classes of a menu item in menu config.
 */
public interface MenuItemRunnable {
    /**
     * Runs action of menu item. Called by the menu UI component when menu item triggered by a user.
     *
     * @param origin   origin screen
     * @param menuItem menu item
     */
    void run(FrameOwner origin, MenuItem menuItem);
}