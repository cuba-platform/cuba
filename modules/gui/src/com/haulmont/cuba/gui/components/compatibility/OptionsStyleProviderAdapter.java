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

package com.haulmont.cuba.gui.components.compatibility;

import com.haulmont.cuba.gui.components.Component;

import java.util.function.Function;

@Deprecated
public class OptionsStyleProviderAdapter<I> implements Function<I, String> {

    protected final Component component;
    protected final Component.OptionsStyleProvider delegate;

    public OptionsStyleProviderAdapter(Component component, Component.OptionsStyleProvider delegate) {
        this.component = component;
        this.delegate = delegate;
    }

    @Override
    public String apply(I item) {
        return delegate.getItemStyleName(component, item);
    }

    public Component.OptionsStyleProvider getDelegate() {
        return delegate;
    }
}