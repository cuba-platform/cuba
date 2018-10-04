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

package com.haulmont.cuba.gui.components;

/**
 * Factory to create actions declared as {@link ActionType}.
 *
 * @see Action
 */
public interface Actions {

    String NAME = "cuba_Actions";

    Action create(String actionTypeId);

    Action create(String actionTypeId, String id);

    <T extends Action> T create(Class<T> actionTypeClass);

    <T extends Action> T create(Class<T> actionTypeClass, String id);
}