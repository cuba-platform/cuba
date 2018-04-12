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

package com.haulmont.cuba.gui.components;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Component which can contain other components and provides indexed access to children.
 */
public interface OrderedContainer extends ComponentContainer {
    void add(Component childComponent, int index);
    int indexOf(Component component);

    /**
     * Returns the component at the given position.
     *
     * @param index component index
     * @return the component at the given index or null.
     */
    @Nullable
    Component getComponent(int index);

    /**
     * Returns the component at the given position.
     *
     * @param index component index
     * @return the component at the given index. Throws exception if not found.
     */
    @Nonnull
    default Component getComponentNN(int index) {
        Component component = getComponent(index);
        if (component == null) {
            throw new IllegalArgumentException(
                    String.format("Not found component by index %s", index)
            );
        }

        return component;
    }
}