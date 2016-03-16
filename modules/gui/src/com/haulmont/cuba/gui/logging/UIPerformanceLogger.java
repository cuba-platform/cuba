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
 * Logger class for UI performance stats
 * Contains constants for screen life cycle
 *
 */
public interface UIPerformanceLogger {

    enum LifeCycle {
        LOAD("load"),
        XML("xml"),
        INIT("init"),
        READY("ready"),
        SET_ITEM("setItem"),
        UI_PERMISSIONS("uiPermissions"),
        INJECTION("inject"),
        COMPANION("companion");

        private String name;

        LifeCycle(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}