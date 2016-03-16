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

package com.haulmont.cuba.gui.app.core.dev;

/**
 */
public class LayoutTip {

    public final LayoutTipType errorType;
    public final String componentPath;
    public final String message;

    public LayoutTip(LayoutTipType errorType, String componentPath, String message) {
        this.errorType = errorType;
        this.componentPath = componentPath;
        this.message = message;
    }

    public static LayoutTip warn(String componentPath, String message) {
        return new LayoutTip(LayoutTipType.WARN, componentPath, message);
    }

    public static LayoutTip warn(String componentPath, String message, Object... params) {
        return new LayoutTip(LayoutTipType.WARN, componentPath, String.format(message, params));
    }

    public static LayoutTip error(String componentPath, String message) {
        return new LayoutTip(LayoutTipType.ERROR, componentPath, message);
    }

    public static LayoutTip error(String componentPath, String message, Object... params) {
        return new LayoutTip(LayoutTipType.ERROR, componentPath, String.format(message, params));
    }
}