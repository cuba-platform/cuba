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

package com.haulmont.cuba.gui.components.data;

import com.haulmont.bali.events.Subscription;
import com.haulmont.cuba.gui.components.Component;

import java.util.function.Consumer;

/**
 * vaadin8 document
 *
 * @param <V> todo
 */
public interface ValueSource<V> extends Component.ValueChangeNotifier {
    V getValue();
    void setValue(V value);

    boolean isReadOnly();

    Class<V> getType();

    ValueSourceState getStatus();

    Subscription addStateChangeListener(Consumer<ValueSourceStateChangeEvent<V>> listener);
}