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

import com.haulmont.cuba.gui.screen.FrameOwner;
import com.haulmont.cuba.gui.screen.ScreenFragment;
import com.haulmont.cuba.gui.screen.ScreenOptions;

/**
 * Interface defining methods for creation and displaying of reusable screen parts that have their own UI controller.
 * <br>
 * Usage example (this - Screen controller):
 * <pre>{@code
 *    Fragment editorActions = fragments.create(this, EditorActionsFragment.class)
 *             .init()
 *             .getFragment();
 *
 *    this.getWindow().add(editorActions);
 * }</pre>
 */
public interface Fragments {

    /**
     * Creates a screen fragment by its controller class.
     *
     * @param parent              parent UI controller
     * @param screenFragmentClass screen controller class
     */
    default <T extends ScreenFragment> T create(FrameOwner parent, Class<T> screenFragmentClass) {
        return create(parent, screenFragmentClass, FrameOwner.NO_OPTIONS);
    }

    /**
     * Creates a screen fragment by its id.
     *
     * @param parent           parent UI controller
     * @param screenFragmentId id of screen fragment
     */
    default ScreenFragment create(FrameOwner parent, String screenFragmentId) {
        return create(parent, screenFragmentId, FrameOwner.NO_OPTIONS);
    }

    /**
     * Creates a screen fragment by its controller class.
     *
     * @param parent              parent UI controller
     * @param screenFragmentClass screen controller class
     * @param options             screen parameters
     */
    <T extends ScreenFragment> T create(FrameOwner parent, Class<T> screenFragmentClass, ScreenOptions options);

    /**
     * Creates a screen fragment by its id.
     *
     * @param parent           parent UI controller
     * @param screenFragmentId id of screen fragment
     * @param options          screen parameters
     */
    ScreenFragment create(FrameOwner parent, String screenFragmentId, ScreenOptions options);

    /**
     * Perform fragment initialization. Clients must always perform init.
     *
     * @param fragment fragment
     */
    void init(ScreenFragment fragment);
}