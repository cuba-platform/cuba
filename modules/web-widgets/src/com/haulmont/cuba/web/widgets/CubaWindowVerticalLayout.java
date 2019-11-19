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

package com.haulmont.cuba.web.widgets;

import com.vaadin.ui.Dependency;
import com.vaadin.ui.HasDependencies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CubaWindowVerticalLayout extends CubaVerticalActionsLayout implements HasDependencies {

    protected List<ClientDependency> dependencies;

    @Override
    public List<ClientDependency> getDependencies() {
        return dependencies != null ? dependencies : Collections.emptyList();
    }

    public void setDependencies(List<ClientDependency> dependencies) {
        this.dependencies = dependencies;
    }

    public void addDependency(String path, Dependency.Type type) {
        if (dependencies == null) {
            dependencies = new ArrayList<>();
        }

        dependencies.add(new ClientDependency(path, type));
    }

    public void addDependencies(String... dependencies) {
        if (this.dependencies == null) {
            this.dependencies = new ArrayList<>();
        }

        for (String path : dependencies) {
            this.dependencies.add(new ClientDependency(path));
        }
    }
}
