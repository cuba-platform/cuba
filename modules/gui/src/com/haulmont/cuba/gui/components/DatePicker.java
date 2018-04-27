/*
 * Copyright (c) 2008-2016 Haulmont.
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

import java.util.Date;

public interface DatePicker<V extends Date> extends Field<V>, Component.Focusable, HasRange {
    String NAME = "datePicker";

    enum Resolution {
        DAY,
        MONTH,
        YEAR
    }

    /**
     * Return resolution of the DatePicker.
     *
     * @return Resolution
     */
    Resolution getResolution();
    /**
     * Set resolution of the DatePicker.
     *
     * @param resolution resolution
     */
    void setResolution(Resolution resolution);

    @SuppressWarnings("unchecked")
    @Override
    V getValue();
}