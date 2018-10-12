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

package com.haulmont.cuba.gui.components.data.value;

import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.gui.components.data.ValueSource;
import com.haulmont.cuba.gui.components.data.ValueSourceProvider;
import com.haulmont.cuba.gui.model.InstanceContainer;

public class ContainerValueSourceProvider<E extends Entity> implements ValueSourceProvider {

    protected final InstanceContainer<E> container;

    public ContainerValueSourceProvider(InstanceContainer<E> container) {
        this.container = container;
    }

    public InstanceContainer<E> getContainer() {
        return container;
    }

    @Override
    public ValueSource<?> getValueSource(String property) {
        return new ContainerValueSource<>(container, property);
    }
}
