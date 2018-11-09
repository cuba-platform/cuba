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
import com.haulmont.cuba.gui.components.data.Options;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Options based on a simple collection.
 *
 * @param <I> item type
 */
public class ListOptions<I> implements Options<I> {
    protected Collection<I> options;

    public ListOptions(Collection<I> options) {
        Preconditions.checkNotNullArgument(options);

        this.options = options;
    }

    @SafeVarargs
    public static <V> ListOptions<V> of(V v, V... vs) {
        List<V> options = new ArrayList<>();
        options.add(v);

        if (vs != null) {
            CollectionUtils.addAll(options, vs);
        }

        return new ListOptions<>(options);
    }

    public static <V> ListOptions<V> empty() {
        return new ListOptions<>(Collections.emptyList());
    }

    public Collection<I> getItemsCollection() {
        return options;
    }

    @Override
    public Stream<I> getOptions() {
        return options.stream();
    }

    @Override
    public BindingState getState() {
        return BindingState.ACTIVE;
    }

    @Override
    public Subscription addStateChangeListener(Consumer<StateChangeEvent> listener) {
        return VoidSubscription.INSTANCE;
    }

    @Override
    public Subscription addOptionsChangeListener(Consumer<OptionsChangeEvent<I>> listener) {
        return VoidSubscription.INSTANCE;
    }
}