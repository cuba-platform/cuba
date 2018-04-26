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

package com.haulmont.cuba.gui.components.data.options;

import com.haulmont.bali.events.Subscription;
import com.haulmont.bali.events.sys.VoidSubscription;
import com.haulmont.bali.util.Preconditions;
import com.haulmont.cuba.gui.components.data.BindingState;
import com.haulmont.cuba.gui.components.data.OptionsSource;

import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class MapOptions<V> implements OptionsSource<V> {
    protected Map<String, V> options;

    public MapOptions(Map<String, V> options) {
        Preconditions.checkNotNullArgument(options);

        this.options = options;
    }

    public Map<String, V> getItemsCollection() {
        return options;
    }

    @Override
    public Stream<V> getOptions() {
        return options.values().stream();
    }

    @Override
    public BindingState getState() {
        return BindingState.ACTIVE;
    }

    @Override
    public Subscription addStateChangeListener(Consumer listener) {
        return VoidSubscription.INSTANCE;
    }

    @Override
    public Subscription addValueChangeListener(Consumer listener) {
        return VoidSubscription.INSTANCE;
    }

    @Override
    public Subscription addOptionsChangeListener(Consumer listener) {
        return VoidSubscription.INSTANCE;
    }
}