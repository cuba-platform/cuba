/*
 * Copyright (c) 2008-2021 Haulmont.
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

import javax.annotation.Nullable;

/**
 * Interface to be implemented by components that enable to configure
 * {@code min-height} and {@code min-width} CSS properties.
 */
public interface HasMinSize {

    /**
     * @return {@code min-height} CSS property value of the component or {@code null} if not set
     */
    @Nullable
    Float getMinHeight();

    /**
     * @return {@code min-height} size unit
     */
    @Nullable
    SizeUnit getMinHeightSizeUnit();

    /**
     * Sets {@code min-height} CSS property value to the component.
     *
     * @param minHeight property value
     */
    void setMinHeight(@Nullable String minHeight);

    /**
     * @return {@code min-width} CSS property value of the component or {@code null} if not set
     */
    @Nullable
    Float getMinWidth();

    /**
     * @return {@code min-width} size unit
     */
    @Nullable
    SizeUnit getMinWidthSizeUnit();

    /**
     * Sets {@code min-width} CSS property value to the component.
     *
     * @param minWidth property value
     */
    void setMinWidth(@Nullable String minWidth);
}
