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

/**
 * A class that implements this interface can have indentation outside the border.
 */
public interface HasOuterMargin {
    /**
     * Enables or disables margins on all sides simultaneously.
     *
     * @param enable if true, enables margins on all sides. If false, disables margins on all sides.
     */
    default void setOuterMargin(boolean enable) {
        setOuterMargin(new MarginInfo(enable, enable, enable, enable));
    }

    /**
     * Sets margins on all sides individually.
     *
     * @param top    enable or disable top margin
     * @param right  enable or disable right margin
     * @param bottom enable or disable bottom margin
     * @param left   enable or disable left margin
     */
    default void setOuterMargin(boolean top, boolean right, boolean bottom, boolean left) {
        setOuterMargin(new MarginInfo(top, right, bottom, left));
    }

    /**
     * Sets margins on all sides according to the passed {@link MarginInfo} object.
     *
     * @param marginInfo the {@link MarginInfo} object that describes the
     *                   margin settings for each side of a Component.
     */
    void setOuterMargin(MarginInfo marginInfo);

    /**
     * @return the {@link MarginInfo} object that describes the
     * margin settings for each side of a Component.
     */
    MarginInfo getOuterMargin();
}