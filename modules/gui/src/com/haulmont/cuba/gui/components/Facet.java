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

package com.haulmont.cuba.gui.components;

import com.haulmont.cuba.gui.meta.PropertyType;
import com.haulmont.cuba.gui.meta.StudioProperty;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.ScreenFragment;

import javax.annotation.Nullable;

/**
 * Non-visual component of a {@link Screen} or {@link ScreenFragment}.
 */
public interface Facet {

    /**
     * Sets facet ID.
     *
     * @param id id
     */
    @StudioProperty(type = PropertyType.COMPONENT_ID)
    void setId(String id);
    /**
     * @return ID as defined in {@code id} attribute
     */
    String getId();

    /**
     * Sets owner frame ({@link Window} or {@link Fragment}) to the facet.
     *
     * @param owner owner frame
     */
    void setOwner(@Nullable Frame owner);
    /**
     * @return owner frame
     */
    @Nullable
    Frame getOwner();
}