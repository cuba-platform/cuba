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

package com.haulmont.cuba.gui.model;

import javax.annotation.Nullable;
import java.util.Set;

public interface ScreenData {

    String NAME = "cuba_ScreenData";

    DataContext getDataContext();

    void loadAll();

    <T extends InstanceContainer> T getContainer(String id);

    <T extends DataLoader> T getLoader(String id);

    Set<String> getContainerIds();

    Set<String> getLoaderIds();

    @Nullable
    <T extends DataLoader> T findLoaderOf(InstanceContainer container);

    void registerContainer(String id, InstanceContainer container);

    void registerLoader(String id, DataLoader loader);
}
