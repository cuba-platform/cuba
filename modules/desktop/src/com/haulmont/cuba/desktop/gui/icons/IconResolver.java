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

package com.haulmont.cuba.desktop.gui.icons;

import javax.swing.*;

/**
 * A bean that is intended for resolving icon resources from {@link com.haulmont.cuba.desktop.DesktopResources}
 * by the given {@code iconPath}.
 */
public interface IconResolver {
    String NAME = "cuba_IconResolver";

    /**
     * Returns an instance of {@link Icon} by the given {@code iconPath}.
     *
     * @param iconPath icon path
     * @return {@link Icon} instance
     */
    Icon getIconResource(String iconPath);
}
