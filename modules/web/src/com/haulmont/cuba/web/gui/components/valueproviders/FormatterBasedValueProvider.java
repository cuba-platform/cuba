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

package com.haulmont.cuba.web.gui.components.valueproviders;

import com.vaadin.data.ValueProvider;

import java.util.function.Function;

public class FormatterBasedValueProvider<T> implements ValueProvider<T, String> {

    protected Function<? super T, String> formatter;

    public FormatterBasedValueProvider(Function<? super T, String> formatter) {
        this.formatter = formatter;
    }

    @Override
    public String apply(T value) {
        return formatter.apply(value);
    }

    @SuppressWarnings("unchecked")
    public Function<T, String> getFormatter() {
        return (Function<T, String>) formatter;
    }
}