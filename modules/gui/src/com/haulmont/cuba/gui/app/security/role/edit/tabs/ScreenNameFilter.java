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

package com.haulmont.cuba.gui.app.security.role.edit.tabs;

import com.haulmont.cuba.gui.app.security.entity.AssignableTarget;
import com.haulmont.cuba.gui.components.TextField;

import java.util.function.Predicate;

public class ScreenNameFilter<T extends AssignableTarget> implements Predicate<T> {

    protected final TextField<String> screenFilter;

    public ScreenNameFilter(TextField<String> screenFilter) {
        this.screenFilter = screenFilter;
    }

    @Override
    public boolean test(T target) {
        if (target != null) {
            String filterTerm = screenFilter.getValue();
            if (filterTerm == null || filterTerm.isEmpty()) {
                return true;
            }

            return target.getCaption().toLowerCase()
                    .contains(filterTerm.toLowerCase());
        }
        return false;
    }
}
