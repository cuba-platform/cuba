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

public interface ScreenData {

    String NAME = "cuba_ScreenData";

    DataContext getDataContext();

    <T extends InstanceContainer> T getContainer(String id);

    <T extends DataLoader> T getLoader(String id);

    void registerContainer(String id, InstanceContainer container);

    void registerLoader(String id, DataLoader loader);
}
