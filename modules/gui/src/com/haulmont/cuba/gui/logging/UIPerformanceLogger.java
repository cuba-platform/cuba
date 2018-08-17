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

package com.haulmont.cuba.gui.logging;

/**
 * Logger class for UI performance stats.
 * Contains constants for screen life cycle.
 */
public final class UIPerformanceLogger {

    private UIPerformanceLogger() {
    }

    public enum LifeCycle {
        LOAD("load", "#load"),
        XML("xml", "#xml"),
        INIT("init", "#init"),
        READY("ready", "#ready"),
        SET_ITEM("setItem", "#setItem"),
        UI_PERMISSIONS("uiPermissions", "#uiPermissions"),
        INJECTION("inject", "#inject"),
        COMPANION("companion", "#companion");

        private String name;
        private String suffix;

        LifeCycle(String name, String suffix) {
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
}