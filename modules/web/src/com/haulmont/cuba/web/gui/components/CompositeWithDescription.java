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

package com.haulmont.cuba.web.gui.components;

import com.haulmont.cuba.gui.components.Component;

/**
 * {@link CompositeComponent} having a description.
 * Default implementations delegate their execution to {@link CompositeComponent#getComposition()}.
 */
public interface CompositeWithDescription extends Component.HasDescription {

    @Override
    default String getDescription() {
        Component.HasDescription hasDescription =
                (Component.HasCaption) ((CompositeComponent) this).getCompositionNN();
        return hasDescription.getDescription();
    }

    @Override
    default void setDescription(String description) {
        Component.HasDescription hasDescription =
                (Component.HasDescription) ((CompositeComponent) this).getCompositionNN();
        hasDescription.setDescription(description);
    }
}
