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

package com.haulmont.cuba.gui.components;

import com.google.common.base.Strings;

public enum SizeUnit {
    /**
     * Unit code representing pixels.
     */
    PIXELS("px"),
    /**
     * Unit code representing in percentage of the containing element
     * defined by terminal.
     */
    PERCENTAGE("%");

    private String symbol;

    SizeUnit(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }

    public static SizeUnit getUnitFromSymbol(String symbol) {
        if (Strings.isNullOrEmpty(symbol)) {
            return SizeUnit.PIXELS; // Defaults to pixels
        }

        for (SizeUnit unit : SizeUnit.values()) {
            if (symbol.equals(unit.getSymbol())) {
                return unit;
            }
        }

        throw new IllegalArgumentException("Passed symbol cannot be recognized as known SizeUnit");
    }
}