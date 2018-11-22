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

public class MacroArgs {
    protected String paramName;
    protected TimeZone timeZone;
    protected int offset;
    protected boolean isNow;

    public MacroArgs(String paramName, TimeZone timeZone, int offset, boolean isNow) {
        this.paramName = paramName;
        this.timeZone = timeZone;
        this.offset = offset;
        this.isNow = isNow;
    }

    public MacroArgs(String paramName, TimeZone timeZone) {
        this.paramName = paramName;
        this.timeZone = timeZone;
        this.offset = 0;
        this.isNow = false;
    }

    public String getParamName() {
        return paramName;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public int getOffset() {
        return offset;
    }

    public boolean isNow() {
        return isNow;
    }
}