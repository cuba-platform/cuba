/*
 * Copyright (c) 2008-2017 Haulmont.
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

package com.haulmont.cuba.web.widgets;

import javax.annotation.Nullable;

/**
 * Factory that generates components for {@link CubaGrid} editor.
 */
public interface CubaGridEditorFieldFactory {

    /**
     * Generates component for {@link CubaGrid} editor.
     *
     * @param itemId     editing item id
     * @param propertyId editing item property id
     * @return generated component or {@code null}
     */
    @Nullable
    com.vaadin.v7.ui.Field<?> createField(Object itemId, Object propertyId);
}
