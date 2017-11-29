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

import com.haulmont.cuba.core.global.UserSessionSource;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.*;
import java.util.regex.Pattern;

@Component("cuba_DateEqualsQueryMacroHandler")
@Scope("prototype")
public class DateEqualsMacroHandler extends AbstractQueryMacroHandler {

    protected static final Pattern MACRO_PATTERN = Pattern.compile("@dateEquals\\s*\\(([^)]+)\\)");

    protected Map<String, Object> namedParameters;
    protected List<MacroArgs> paramNames = new ArrayList<>();

    @Inject
    protected UserSessionSource userSessionSource;

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
        if (args.length != 2 && args.length != 3)
            throw new RuntimeException("Invalid macro: " + macro);

        String field = args[0].trim();
        String param1 = args[1].trim().substring(1);
        String param2 = field.replace(".", "_") + "_" + count;
        TimeZone timeZone = awareTimeZoneFromArgs(args, 2);
        paramNames.add(new MacroArgs(param1, param2, timeZone));

        return String.format("(%s >= :%s and %s < :%s)", field, param1, field, param2);
    }

    @Override
    public Map<String, Object> getParams() {
        Map<String, Object> params = new HashMap<>();
        for (MacroArgs macroArgs : paramNames) {
            TimeZone timeZone = macroArgs.timeZone == null ? TimeZone.getDefault() : macroArgs.timeZone;
            Date date1 = (Date) namedParameters.get(macroArgs.firstParamName);
            if (date1 == null) {
                throw new RuntimeException(String.format("Parameter %s not found for macro",
                        macroArgs.firstParamName));
            }
            Calendar calendar1 = Calendar.getInstance(timeZone);
            calendar1.setTime(date1);
            calendar1 = DateUtils.truncate(calendar1, Calendar.DAY_OF_MONTH);

            Calendar calendar2 = Calendar.getInstance(timeZone);
            calendar2.setTime(calendar1.getTime());
            calendar2.add(Calendar.DAY_OF_MONTH, 1);

            params.put(macroArgs.firstParamName, calendar1.getTime());
            params.put(macroArgs.secondParamName, calendar2.getTime());
        }
        return params;
    }

    @Override
    public String replaceQueryParams(String queryString, Map<String, Object> params) {
        return queryString;
    }

    protected static class MacroArgs {
        protected String firstParamName;
        protected String secondParamName;
        protected TimeZone timeZone;

        public MacroArgs(String firstParamName, String secondParamName, TimeZone timeZone) {
            this.firstParamName = firstParamName;
            this.secondParamName = secondParamName;
            this.timeZone = timeZone;
        }
    }
}