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

import org.springframework.util.StringUtils;

public class RouteDefinition {

    protected final String path;
    protected final String parentPrefix;
    protected final boolean root;

    public RouteDefinition(String path, String parentPrefix, boolean root) {
        this.path = path;
        this.parentPrefix = parentPrefix;
        this.root = root;
    }

    public String getPath() {
        return path;
    }

    public String getParentPrefix() {
        return parentPrefix;
    }

    public boolean isRoot() {
        return root;
    }

    @Override
    public String toString() {
        return "RouteDefinition{" +
                "path='" + path + '\'' +
                (StringUtils.isEmpty(parentPrefix) ? ""
                        : ", parentPrefix='" + parentPrefix + '\'') +
                "root='" + root + '\'' +
                '}';
    }
}
