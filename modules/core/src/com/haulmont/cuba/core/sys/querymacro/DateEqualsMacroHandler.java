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

import com.haulmont.bali.datastruct.Pair;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.TimeZones;
import com.haulmont.cuba.core.global.UserSessionSource;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Pattern;

@Component("cuba_DateEqualsQueryMacroHandler")
@Scope("prototype")
public class DateEqualsMacroHandler extends AbstractQueryMacroHandler {

    private UserSessionSource userSessionSource = AppBeans.get(UserSessionSource.NAME);
    private TimeZones timeZones = AppBeans.get(TimeZones.NAME);

    protected static final Pattern MACRO_PATTERN = Pattern.compile("@dateEquals\\s*\\(([^)]+)\\)");

    protected Map<String, Object> namedParameters;
    protected List<Pair<String, String>> paramNames = new ArrayList<>();

    public DateEqualsMacroHandler() {
        super(MACRO_PATTERN);
    }

    @Override
    public void setQueryParams(Map<String, Object> namedParameters) {
        this.namedParameters = namedParameters;
    }

    @Override
    protected String doExpand(String macro) {
        count++;
        String[] args = macro.split(",");
        if (args.length != 2)
            throw new RuntimeException("Invalid macro: " + macro);

        String field = args[0].trim();
        String param1 = args[1].trim().substring(1);
        String param2 = field.replace(".", "_") + "_" + count;
        paramNames.add(new Pair<>(param1, param2));

        return String.format("(%s >= :%s and %s < :%s)", field, param1, field, param2);
    }

    @Override
    public Map<String, Object> getParams() {
        Map<String, Object> params = new HashMap<>();
        for (Pair<String, String> pair : paramNames) {
            Date date1 = (Date) namedParameters.get(pair.getFirst());
            if (date1 == null)
                throw new RuntimeException("Parameter " + pair.getFirst() + " not found for macro");

            TimeZone truncationTimeZone = getTruncationTimeZone();
            TimeZone defaultTimeZone = TimeZone.getDefault();

            Date toTruncate = timeZones.convert(date1, defaultTimeZone, truncationTimeZone);

            Date truncatedStart = DateUtils.truncate(toTruncate, Calendar.DAY_OF_MONTH);
            Date truncatedEnd = DateUtils.addDays(truncatedStart, 1);

            params.put(pair.getFirst(), timeZones.convert(truncatedStart, truncationTimeZone, defaultTimeZone));
            params.put(pair.getSecond(), timeZones.convert(truncatedEnd, truncationTimeZone, defaultTimeZone));
        }
        return params;
    }

    @Override
    public String replaceQueryParams(String queryString, Map<String, Object> params) {
        return queryString;
    }

    private TimeZone getTruncationTimeZone()
    {
        if (userSessionSource == null ||
            userSessionSource.getUserSession() == null ||
            userSessionSource.getUserSession().getTimeZone() == null
            ) {
            TimeZone.getDefault();
        }

        return userSessionSource.getUserSession().getTimeZone();
    }

}