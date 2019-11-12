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

package com.haulmont.cuba.gui.app.core.bulk;

import javax.annotation.Nullable;

/**
 * Defines the number of editor columns in bulk editor.
 */
public enum ColumnsMode {

    ONE_COLUMN(1),
    TWO_COLUMNS(2);

    protected final int columnsCount;

    ColumnsMode(int columnsCount) {
        this.columnsCount = columnsCount;
    }

    public int getColumnsCount() {
        return columnsCount;
    }

    /**
     * @param count number of columns
     * @return null if there is no enum value for the given count
     */
    @Nullable
    public static ColumnsMode fromColumnsCount(int count) {
        for (ColumnsMode mode : ColumnsMode.values()) {
            if (mode.getColumnsCount() == count) {
                return mode;
            }
        }
        return null;
    }
}
