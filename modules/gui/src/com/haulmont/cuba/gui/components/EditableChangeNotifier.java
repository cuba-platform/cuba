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

import com.haulmont.bali.events.Subscription;

import java.util.EventObject;
import java.util.function.Consumer;

/**
 * Component that fires EditableChangeEvent events.
 */
public interface EditableChangeNotifier {

    Subscription addEditableChangeListener(Consumer<EditableChangeEvent> listener);
    @Deprecated
    void removeEditableChangeListener(Consumer<EditableChangeEvent> listener);

    class EditableChangeEvent extends EventObject {
        public EditableChangeEvent(Component.Editable source) {
            super(source);
        }

        @Override
        public Component.Editable getSource() {
            return (Component.Editable) super.getSource();
        }
    }
}