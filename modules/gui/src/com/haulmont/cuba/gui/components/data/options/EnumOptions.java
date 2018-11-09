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

package com.haulmont.cuba.gui.components.data.options;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import java.util.Arrays;

/**
 * Options based on an enumeration class.
 *
 * @param <I> item type
 */
public class EnumOptions<I extends EnumClass> extends ListOptions<I> {
    private Class<I> enumClass;

    public EnumOptions(Class<I> enumClass) {
        super(Arrays.asList(enumClass.getEnumConstants()));
        this.enumClass = enumClass;
    }

    public Class<I> getEnumClass() {
        return enumClass;
    }
}