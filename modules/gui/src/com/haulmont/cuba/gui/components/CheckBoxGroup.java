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

import com.haulmont.cuba.gui.components.data.Options;

import java.util.Set;
import java.util.function.Function;

/**
 * A group of Checkboxes. Individual checkboxes are made from items supplied by {@link Options}.
 *
 * @param <I> item type
 */
public interface CheckBoxGroup<I> extends OptionsField<Set<I>, I>, LookupComponent, Component.Focusable, HasOrientation {

    String NAME = "checkBoxGroup";

    /**
     * Set the icon provider for the LookupField.
     *
     * @param optionIconProvider provider which provides icons for options
     */
    void setOptionIconProvider(Function<? super I, String> optionIconProvider);
    /**
     * @return icon provider of the LookupField.
     */
    Function<? super I, String> getOptionIconProvider();
}