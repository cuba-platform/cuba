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

package com.haulmont.cuba.gui.sys;

import com.haulmont.cuba.gui.screen.Screen;

public class RouteDefinition {

    private final String path;
    private final Class<? extends Screen> parent;

    public RouteDefinition(String path, Class<? extends Screen> parent) {
        this.path = path;
        this.parent = parent;
    }

    public String getPath() {
        return path;
    }

    public Class<? extends Screen> getParent() {
        if (parent == Screen.class) {
            return null;
        }
        return parent;
    }

    @Override
    public String toString() {
        return "RouteDefinition{" +
                "path='" + path + '\'' +
                (Screen.class == parent ? ""
                        : ", parent='" + parent.getName() + '\'') +
                '}';
    }
}
