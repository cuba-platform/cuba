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
 * A class that describes a component and its spanning.
 */
public class ComponentPosition {
    protected Component component;
    protected int colSpan;
    protected int rowSpan;

    public ComponentPosition(Component component, int colSpan, int rowSpan) {
        Preconditions.checkArgument(colSpan >= 1, "'colspan' can't be less than 1");
        Preconditions.checkArgument(rowSpan >= 1, "'rowspan' can't be less than 1");

        this.component = component;
        this.colSpan = colSpan;
        this.rowSpan = rowSpan;
    }

    public Component getComponent() {
        return component;
    }

    public int getColSpan() {
        return colSpan;
    }

    public int getRowSpan() {
        return rowSpan;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ComponentPosition)) return false;
        ComponentPosition that = (ComponentPosition) o;
        return Objects.equal(component, that.component);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(component);
    }
}
