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
import com.haulmont.cuba.gui.icons.Icons;

/**
 * {@link CompositeComponent} having an icon.
 * Default implementations delegate their execution to {@link CompositeComponent#getComposition()}.
 */
public interface CompositeWithIcon extends Component.HasIcon {

    @Override
    default String getIcon() {
        Component.HasIcon hasIcon = (Component.HasIcon) ((CompositeComponent) this).getCompositionNN();
        return hasIcon.getIcon();
    }

    @Override
    default void setIcon(String icon) {
        Component.HasIcon hasIcon = (Component.HasIcon) ((CompositeComponent) this).getCompositionNN();
        hasIcon.setIcon(icon);
    }

    @Override
    default void setIconFromSet(Icons.Icon icon) {
        Component.HasIcon hasIcon = (Component.HasIcon) ((CompositeComponent) this).getCompositionNN();
        hasIcon.setIconFromSet(icon);
    }
}
