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

package com.haulmont.cuba.core.sys.querymacro.macroargs;

import java.util.TimeZone;

public class MacroArgsTimeBetween extends MacroArgs {
    protected String unit;

    public MacroArgsTimeBetween(String paramName, TimeZone timeZone, String unit, int offset, boolean isNow) {
        super(paramName, timeZone, offset, isNow);
        this.unit = unit;
    }

    public MacroArgsTimeBetween(String paramName, TimeZone timeZone, String unit, int offset) {
        super(paramName, timeZone, offset, true);
        this.unit = unit;
    }

    public MacroArgsTimeBetween(String paramName, TimeZone timeZone, String unit) {
        super(paramName, timeZone);
        this.unit = unit;
    }

    public String getUnit() {
        return unit;
    }
}
