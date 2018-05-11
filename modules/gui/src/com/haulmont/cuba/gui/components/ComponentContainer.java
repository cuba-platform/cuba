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
import java.util.Collection;

/**
 * Component which can contain other components.
 */
public interface ComponentContainer extends Component {
    void add(Component childComponent);
    default void add(Component... childComponents) {
        for (Component component : childComponents) {
            add(component);
        }
    }

    void remove(Component childComponent);
    default void remove(Component... childComponents) {
        for (Component component : childComponents) {
            remove(component);
        }
    }

    void removeAll();

    /**
     * Get component directly owned by this container.
     * @return component or null if not found
     */
    @Nullable
    Component getOwnComponent(String id);

    /**
     * Get component belonging to the whole components tree below this container.
     * @return component or null if not found
     */
    @Nullable
    Component getComponent(String id);

    /**
     * Get component belonging to the whole components tree below this container.
     *
     * @return component. Throws exception if not found.
     */
    @Nonnull
    default Component getComponentNN(String id) {
        Component component = getComponent(id);
        if (component == null) {
            throw new IllegalArgumentException(String.format("Not found component with id '%s'", id));
        }
        return component;
    }

    /** Get all components directly owned by this container */
    Collection<Component> getOwnComponents();

    /** Get all components belonging to the whole components tree below this container */
    Collection<Component> getComponents();
}