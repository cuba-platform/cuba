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

package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.client.ClientConfig;

/**
 * Describes the default ways a window was closed.
 */
public enum CloseOriginType implements Window.CloseOrigin {

    /**
     * A screen is closed by clicking on the breadcrumbs link.
     */
    BREADCRUMBS,

    /**
     * A screen is closed with one of the following approaches:
     * screen's close button, TabSheet tabs' close actions (Close, Close All, Close Others).
     */
    CLOSE_BUTTON,

    /**
     * A screen is closed with {@link ClientConfig#getCloseShortcut()}.
     */
    SHORTCUT
}
