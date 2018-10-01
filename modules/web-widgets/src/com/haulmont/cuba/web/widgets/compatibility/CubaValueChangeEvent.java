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

package com.haulmont.cuba.web.widgets.compatibility;

import com.vaadin.event.HasUserOriginated;
import com.vaadin.v7.ui.Field;

@Deprecated
public class CubaValueChangeEvent extends Field.ValueChangeEvent implements HasUserOriginated {
    protected final boolean userOriginated;

    /**
     * Constructs a new event object with the specified source field object.
     *
     * @param source the field that caused the event.
     */
    public CubaValueChangeEvent(Field source, boolean userOriginated) {
        super(source);
        this.userOriginated = userOriginated;
    }

    @Override
    public boolean isUserOriginated() {
        return userOriginated;
    }
}
