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

import com.haulmont.cuba.core.global.DateTimeTransformations;
import com.haulmont.cuba.core.global.TimeSource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Pattern;

@Component("cuba_TimeTodayQueryMacroHandler")
@Scope("prototype")
public class TimeTodayQueryMacroHandler extends AbstractQueryMacroHandler {

    private static final Pattern MACRO_PATTERN = Pattern.compile("@today\\s*\\(([^\\)]+)\\)");

    @Inject
    protected DateTimeTransformations transformations;
    @Inject
    protected TimeSource timeSource;

    private int count;
    private List<ArgDef> argDefs = new ArrayList<>();

    public TimeTodayQueryMacroHandler() {
        super(MACRO_PATTERN);
    }

    @Override
    public void setQueryParams(Map<String, Object> namedParameters) {
    }

    @Override
    public Map<String, Object> getParams() {
        Map<String, Object> params = new HashMap<>();
        for (ArgDef argDef : argDefs) {
            Class javaType = expandedParamTypes.get(argDef.firstParamName);
            if (javaType == null)
                throw new RuntimeException(String.format("Type of parameter %s not resolved", argDef.firstParamName));
            ZonedDateTime zonedDateTime = timeSource.now();
            if (transformations.isDateTypeSupportsTimeZones(javaType)) {
                zonedDateTime = zonedDateTime.withZoneSameInstant(argDef.timeZone.toZoneId());
            }
            ZonedDateTime firstZonedDateTime = zonedDateTime.truncatedTo(ChronoUnit.DAYS);
            ZonedDateTime secondZonedDateTime = firstZonedDateTime.plusDays(1);

            params.put(argDef.firstParamName, transformations.transformFromZDT(firstZonedDateTime, javaType));
            params.put(argDef.secondParamName, transformations.transformFromZDT(secondZonedDateTime, javaType));
        }
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

        argDefs.add(new ArgDef(param1, param2, timeZone));

        return String.format("(%s >= :%s and %s < :%s)", field, param1, field, param2);
    }

    protected static class ArgDef {
        protected String firstParamName;
        protected String secondParamName;
        protected TimeZone timeZone;

        public ArgDef(String firstParamName, String secondParamName, TimeZone timeZone) {
            this.firstParamName = firstParamName;
            this.secondParamName = secondParamName;
            this.timeZone = timeZone;
        }
    }
}