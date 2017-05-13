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

import org.apache.commons.lang.time.DateUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Pattern;

@Component("cuba_DateAfterQueryMacroHandler")
@Scope("prototype")
public class DateAfterMacroHandler extends AbstractQueryMacroHandler {

    protected static final Pattern MACRO_PATTERN = Pattern.compile("@dateAfter\\s*\\(([^)]+)\\)");

    protected Map<String, Object> namedParameters;
    protected List<String> paramNames = new ArrayList<>();

    public DateAfterMacroHandler() {
        super(MACRO_PATTERN);
    }

    @Override
    protected String doExpand(String macro) {
        count++;
        String[] args = macro.split(",");
        if (args.length != 2)
            throw new RuntimeException("Invalid macro: " + macro);

        String field = args[0].trim();
        String param = args[1].trim().substring(1);
        paramNames.add(param);

        return String.format("(%s >= :%s)", field, param);
    }

    @Override
    public void setQueryParams(Map<String, Object> namedParameters) {
        this.namedParameters = namedParameters;
    }

    @Override
    public Map<String, Object> getParams() {
        Map<String, Object> params = new HashMap<>();
        for (String paramName : paramNames) {
            Date date = (Date) namedParameters.get(paramName);
            if (date == null)
                throw new RuntimeException("Parameter " + paramName + " not found for macro");

            Date d = DateUtils.truncate(date, Calendar.DAY_OF_MONTH);
            params.put(paramName, d);
        }
        return params;
    }

    @Override
    public String replaceQueryParams(String queryString, Map<String, Object> params) {
        return queryString;
    }
}