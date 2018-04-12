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
 * A component that is marked with this interface allows to manage additional style names for options displayed
 * by this component.
 *
 * todo extract
 */
public interface HasOptionsStyleProvider {
    /**
     * Sets the given {@code optionsStyleProvider} to the component.
     *
     * @param optionsStyleProvider {@link OptionsStyleProvider} instance that will be user by this component
     */
    void setOptionsStyleProvider(OptionsStyleProvider optionsStyleProvider);

    /**
     * @return {@link OptionsStyleProvider} instance that is used by this component
     */
    OptionsStyleProvider getOptionsStyleProvider();
}