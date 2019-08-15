/*
 * Copyright (c) 2008-2019 Haulmont.
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

package com.haulmont.cuba.web.widgets.client.popupview;

public enum PopupStyleName {
    TOP_LEFT("popup-top-left"),
    TOP_CENTER("popup-top-center"),
    TOP_RIGHT("popup-top-right"),

    MIDDLE_LEFT("popup-middle-left"),
    MIDDLE_CENTER("popup-middle-center"),
    MIDDLE_RIGHT("popup-middle-right"),

    BOTTOM_LEFT("popup-bottom-left"),
    BOTTOM_CENTER("popup-bottom-center"),
    BOTTOM_RIGHT("popup-bottom-right");

    protected String styleName;

    PopupStyleName(String styleName) {
        this.styleName = styleName;
    }

    public String getStyleName() {
        return styleName;
    }
}
