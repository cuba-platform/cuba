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

package com.haulmont.cuba.gui.screen;

import com.haulmont.cuba.core.entity.Entity;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static com.haulmont.cuba.gui.components.Window.SELECT_ACTION_ID;

/**
 * JavaDoc
 *
 * @param <T>
 */
public interface LookupScreen<T extends Entity> {
    CloseAction LOOKUP_SELECT_CLOSE_ACTION = new StandardCloseAction(SELECT_ACTION_ID);

    Consumer<Collection<T>> getSelectHandler();
    void setSelectHandler(Consumer<Collection<T>> selectHandler);

    Predicate<ValidationContext<T>> getSelectValidator();
    void setSelectValidator(Predicate<ValidationContext<T>> selectValidator);

    class ValidationContext<T extends Entity> {
        private Screen screen;
        private Collection<T> selectedItems;

        public ValidationContext(Screen screen, Collection<T> selectedItems) {
            this.screen = screen;
            this.selectedItems = selectedItems;
        }

        public Screen getScreen() {
            return screen;
        }

        public Collection<T> getSelectedItems() {
            return selectedItems;
        }
    }
}