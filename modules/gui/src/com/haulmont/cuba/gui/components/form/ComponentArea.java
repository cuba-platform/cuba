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

package com.haulmont.cuba.gui.components.form;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.haulmont.cuba.gui.components.Component;

/**
 * A class that describes a component and its area, i.e. top left and bottom right coordinates.
 */
public class ComponentArea {
    protected Component component;
    protected int startColumn;
    protected int startRow;
    protected int endColumn;
    protected int endRow;

    public ComponentArea(Component component,
                         int startColumn, int startRow, int endColumn, int endRow) {
        Preconditions.checkArgument(startColumn >= 0 && startRow >= 0 && endColumn >= 0 && endRow >= 0,
                "Component's coordinates can't be less than 0: [%s, %s] - [%s, %s]",
                startColumn, endColumn, startRow, endRow);
        Preconditions.checkArgument(endColumn >= startColumn && endRow >= startRow,
                "Illegal coordinates for the component: %s > %s, %s > %s", startColumn, endColumn, startRow, endRow);

        this.component = component;
        this.startColumn = startColumn;
        this.startRow = startRow;
        this.endColumn = endColumn;
        this.endRow = endRow;
    }

    public Component getComponent() {
        return component;
    }

    public int getStartColumn() {
        return startColumn;
    }

    public int getStartRow() {
        return startRow;
    }

    public int getEndColumn() {
        return endColumn;
    }

    public int getEndRow() {
        return endRow;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ComponentArea)) return false;
        ComponentArea that = (ComponentArea) o;
        return Objects.equal(component, that.component);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(component);
    }
}
