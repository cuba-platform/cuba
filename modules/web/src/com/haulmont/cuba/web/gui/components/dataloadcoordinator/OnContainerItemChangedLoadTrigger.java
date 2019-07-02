/*
 * Copyright (c) 2008-2019 Haulmont.
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

package com.haulmont.cuba.web.gui.components.dataloadcoordinator;

import com.haulmont.cuba.gui.components.DataLoadCoordinator;
import com.haulmont.cuba.gui.model.DataLoader;
import com.haulmont.cuba.gui.model.InstanceContainer;

public class OnContainerItemChangedLoadTrigger implements DataLoadCoordinator.Trigger {

    private final DataLoader loader;
    private final InstanceContainer container;
    private final String param;

    public OnContainerItemChangedLoadTrigger(DataLoader loader, InstanceContainer container, String param) {
        this.loader = loader;
        this.container = container;
        this.param = param;
        //noinspection unchecked
        container.addItemChangeListener(event -> load());
    }

    private void load() {
        loader.setParameter(param, container.getItemOrNull());
        loader.load();
    }

    @Override
    public DataLoader getLoader() {
        return loader;
    }
}
