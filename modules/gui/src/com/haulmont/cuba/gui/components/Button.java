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
package com.haulmont.cuba.gui.components;

public interface Button extends Component, Component.HasCaption, Component.BelongToFrame, Component.ActionOwner,
                                Component.HasIcon, Component.Focusable {
    String NAME = "button";

    /**
     * Determines if a button is automatically disabled when clicked. If this is
     * set to true the button will be automatically disabled when clicked,
     * typically to prevent (accidental) extra clicks on a button.
     *
     * @param disableOnClick disable on click option.
     */
    void setDisableOnClick(boolean disableOnClick);
    /**
     * @return true if the button is disabled when clicked.
     */
    boolean isDisableOnClick();
}