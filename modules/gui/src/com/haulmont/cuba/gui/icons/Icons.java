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
import java.util.regex.Pattern;

/**
 * A bean that resolves icon sources for icon sets defined for the project.
 * <p>
 * Examples:
 * <pre>
 * excelButton.setIcon(icons.get(CubaIcon.EXCEL_ACTION));
 * helpButton.setIcon(icons.get(CubaIcon.INFO));
 * </pre>
 *
 * @see Icon
 */
public interface Icons {

    String NAME = "cuba_Icons";

    Pattern ICON_NAME_REGEX = Pattern.compile("[A-Z_0-9]+");

    /**
     * For the given {@link Icon} instance, returns the icon source that can be used to set this icon to components.
     * <p>
     * Example:
     * <pre>
     * createButton.setIcon(icons.get(CubaIcon.CREATE));
     * </pre>
     *
     * @param icon {@link Icons.Icon} instance
     * @return icon source (see {@link Icon#source()})
     */
    @Nullable
    String get(@Nullable Icon icon);

    /**
     * For the given {@link Icon#iconName()}, returns the icon source that can be used to set this icon to components.
     * <p>
     * Example:
     * <pre>
     * importButton.setIcon(icons.get("IMPORT"));
     * </pre>
     *
     * @param iconName icon name that contains only uppercase letters and underscores
     * @return icon source (see {@link Icon#source()})
     */
    @Nullable
    String get(@Nullable String iconName);

    /**
     * Interface for enumerations that represent icon sets.
     */
    interface Icon {
        /**
         * @return icon source: "font-icon:ADD", "theme://createIcon", etc
         */
        String source();

        /**
         * @return icon name: "ADD", "CREATE", etc
         */
        String iconName();
    }
}
