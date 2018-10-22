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

package com.haulmont.cuba.gui.screen;

import com.haulmont.cuba.gui.model.DataLoader;
import com.haulmont.cuba.gui.model.ScreenData;

/**
 * {@link Subscribe} and {@link Install} target type.
 */
public enum Target {
    /**
     * UI component if id of component specified in the corresponding annotation.
     *
     * Default option.
     */
    COMPONENT,

    /**
     * UI controller
     */
    CONTROLLER,

    /**
     * Parent UI controller
     */
    PARENT_CONTROLLER,

    /**
     * Window or Fragment of UI controller
     */
    FRAME,

    /**
     * {@link DataLoader} defined in {@link ScreenData}.
     */
    DATA_LOADER,

    /**
     * Data container defined in {@link ScreenData}.
     */
    DATA_CONTAINER,

    /**
     * {@code DataContext} provided by {@link ScreenData}.
     */
    DATA_CONTEXT
}