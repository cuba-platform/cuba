/*
 * Copyright (c) 2008-2017 Haulmont.
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

package com.haulmont.cuba.gui.icons;

import javax.annotation.Nullable;

/**
 * A bean that gives an ability to set icons from icon sets - {@link Icon} inheritors.
 * <p>
 * It automatically resolves icons from icon sets that override the default icon set - {@link CubaIcon} or icons that
 * are overridden for a current theme in {@code theme-name-theme.properties} (e.g. halo-theme.properties) file.
 * <p>
 * Examples:
 * <pre><code>
 * excelButton.setIcon(icons.get(CubaIcon.EXCEL_ACTION));
 *
 * helpButton.setIcon(icons.get(CubaIcon.INFO));
 * </code></pre>
 */
public interface Icons {
    String NAME = "cuba_Icons";

    /**
     * Returns icon path for the given {@link Icon} instance that can be used to set this icon to components.
     * <p>
     * Example:
     * <pre><code>
     * createButton.setIcon(icons.get(CubaIcon.CREATE));
     * </code></pre>
     *
     * @param icon {@link Icons.Icon} instance
     * @return actual icon path
     */
    String get(@Nullable Icon icon);

    /**
     * Returns icon path for the given {@code iconName} that can be used to set this icon to components.
     * <p>
     * Example:
     * <pre><code>
     * importButton.setIcon(icons.get("IMPORT"));
     * </code></pre>
     *
     * @param iconName icon name that contains only uppercase letters and underscores
     * @return icon full string path
     */
    String get(@Nullable String iconName);

    /**
     * Marker interface for special enumerations - icon sets.
     */
    interface Icon {
        /**
         * @return icon source: "font-icon:ADD", "theme://createIcon", etc
         */
        String id();

        /**
         * @return icon name: "ADD", "CREATE", etc
         */
        String name();
    }
}
