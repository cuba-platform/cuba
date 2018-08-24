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
import com.haulmont.cuba.gui.util.OperationResult;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class StandardLookup<T extends Entity> extends Screen implements LookupScreen<T> {
    private Consumer<Collection<T>> selectHandler;
    private Predicate<ValidationContext<T>> selectValidator;

    @Override
    public Consumer<Collection<T>> getSelectHandler() {
        return selectHandler;
    }

    @Override
    public void setSelectHandler(Consumer<Collection<T>> selectHandler) {
        this.selectHandler = selectHandler;
    }

    @Override
    public Predicate<ValidationContext<T>> getSelectValidator() {
        return selectValidator;
    }

    @Override
    public void setSelectValidator(Predicate<ValidationContext<T>> selectValidator) {
        this.selectValidator = selectValidator;
    }

    protected void select(Collection<T> items) {
        boolean valid = true;
        if (selectValidator != null) {
            valid = selectValidator.test(new ValidationContext<>(this, items));
        }

        if (valid) {
            OperationResult result = close(LOOKUP_SELECT_CLOSE_ACTION);
            if (selectHandler != null) {
                result.then(() -> selectHandler.accept(items));
            }
        }
    }
}