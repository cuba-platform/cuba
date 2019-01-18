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

package com.haulmont.cuba.gui.logging;

public enum ScreenLifeCycle {
    CREATE("create", "#create"),
    LOAD("load", "#load"),
    XML("xml", "#xml"),
    INIT("init", "#init"),
    BEFORE_SHOW("afterShow", "#beforeShow"),
    AFTER_SHOW("afterShow", "#afterShow"),
    UI_PERMISSIONS("uiPermissions", "#uiPermissions"),
    INJECTION("inject", "#inject"),
    COMPANION("companion", "#companion");

    private String name;
    private String suffix;

    ScreenLifeCycle(String name, String suffix) {
        this.name = name;
        this.suffix = suffix;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public String getSuffix() {
        return suffix;
    }
}