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
 *
 */
package com.haulmont.cuba.core.sys.querymacro;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.TimeSource;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Pattern;

@Component("cuba_TimeTodayQueryMacroHandler")
@Scope("prototype")
public class TimeTodayQueryMacroHandler extends AbstractQueryMacroHandler {

    private static final Pattern MACRO_PATTERN = Pattern.compile("@today\\s*\\(([^\\)]+)\\)");

    private int count;
    private Map<String, Object> params = new HashMap<>();

    public TimeTodayQueryMacroHandler() {
        super(MACRO_PATTERN);
    }

    @Override
    public void setQueryParams(Map<String, Object> namedParameters) {
    }

    @Override
    public Map<String, Object> getParams() {
        return params;
    }

    @Override
    public String replaceQueryParams(String queryString, Map<String, Object> params) {
        return queryString;
    }

    @Override
    protected String doExpand(String macro) {
        count++;
        String[] args = macro.split(",");
        if (args.length != 1 && args.length != 2)
            throw new RuntimeException("Invalid macro: " + macro);
        String field = args[0].trim();
        String param1 = field.replace(".", "_") + "_" + count + "_1";
        String param2 = field.replace(".", "_") + "_" + count + "_2";

        TimeZone timeZone = getTimeZoneFromArgs(args, 1);
        if (timeZone == null) {
            timeZone = TimeZone.getDefault();
        }
        Calendar cal = Calendar.getInstance(timeZone);
        cal.setTime(AppBeans.get(TimeSource.class).currentTimestamp());

        params.put(param1, DateUtils.truncate(cal, Calendar.DAY_OF_MONTH).getTime());

        cal.add(Calendar.DAY_OF_MONTH, 1);
        params.put(param2, DateUtils.truncate(cal, Calendar.DAY_OF_MONTH).getTime());

        return String.format("(%s >= :%s and %s < :%s)", field, param1, field, param2);
    }
}