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

package com.haulmont.cuba.desktop.theme;

import com.haulmont.cuba.desktop.DesktopResources;

import java.util.Set;

/**
 *
 */
public interface DesktopTheme {

    /**
     * @return name of theme
     */
    String getName();

    /**
     * Invoke this method before any UI components initialization.
     * Theme sets up look and feel, assigns UI defaults overrides.
     */
    void init();

    /**
     * Apply style to CUBA, swing or AWT component.
     *
     * @param component component
     * @param styleName space-separated list of styles to apply
     */
    void applyStyle(Object component, String styleName);

    /**
     * Apply style to CUBA, swing or AWT component.
     * This method is used by table style providers to reflect focus and selection states.
     *
     * @param component component
     * @param styleName space-separated list of styles to apply
     * @param state     set of strings describing internal swing component state
     */
    void applyStyle(Object component, String styleName, Set<String> state);

    /**
     * Return resources associated with theme.
     *
     * @return resources
     */
    DesktopResources getResources();
}
