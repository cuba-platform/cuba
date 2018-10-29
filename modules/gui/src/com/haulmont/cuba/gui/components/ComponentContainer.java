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

import com.haulmont.cuba.gui.ComponentsHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

/**
 * Component which can contain other components.
 */
public interface ComponentContainer extends Component {
    /**
     * Adds a component to this container.
     *
     * @param childComponent a component to add
     */
    void add(Component childComponent);

    /**
     * Sequentially adds components to this container.
     *
     * @param childComponents components to add
     */
    default void add(Component... childComponents) {
        for (Component component : childComponents) {
            add(component);
        }
    }

    /**
     * Removes a component from this container.
     *
     * @param childComponent a component to remove
     */
    void remove(Component childComponent);

    /**
     * Sequentially removes components from this container.
     *
     * @param childComponents components to remove
     */
    default void remove(Component... childComponents) {
        for (Component component : childComponents) {
            remove(component);
        }
    }

    /**
     * Removes all components from this container.
     */
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

    /**
     * Focuses the first {@link Focusable} component, if present.
     */
    default void focusFirstComponent() {
        ComponentsHelper.walkComponents(this, component -> {
            if (component instanceof Focusable) {
                ((Focusable) component).focus();
                return true;
            }
            return false;
        });
    }
}