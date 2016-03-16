/*
 * Copyright (c) 2008-2016 Haulmont.
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
 *
 */

package com.haulmont.cuba.gui.components;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates errors found during components validation.
 *
 */
public class ValidationErrors {

    public static class Item {
        public final Component component;
        public final String description;

        public Item(Component component, String description) {
            this.component = component;
            this.description = description;
        }
    }

    private List<Item> items = new ArrayList<>();

    /**
     * Add an error without reference to component causing it.
     * @param description   error description
     */
    public void add(String description) {
        add(null, description);
    }

    /**
     * Add an error.
     * @param component     component causing the error
     * @param description   error description
     */
    public void add(@Nullable Component component, String description) {
        items.add(new Item(component, description));
    }

    /**
     * @return errors list
     */
    public List<Item> getAll() {
        return items;
    }

    /**
     * @return  true if there are no errors
     */
    public boolean isEmpty() {
        return items.isEmpty();
    }
}