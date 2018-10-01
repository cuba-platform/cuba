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

import com.google.common.base.Strings;
import com.haulmont.cuba.core.global.DateTimeTransformations;
import com.haulmont.cuba.core.global.Scripting;
import com.haulmont.cuba.core.global.TimeSource;
import groovy.lang.Binding;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component("cuba_TimeBetweenQueryMacroHandler")
@Scope("prototype")
public class TimeBetweenQueryMacroHandler extends AbstractQueryMacroHandler {

    protected static final Pattern MACRO_PATTERN = Pattern.compile("@between\\s*\\(([^)]+)\\)");
    protected static final Pattern PARAM_PATTERN = Pattern.compile("(now)\\s*([\\d\\s+-]*)");
    protected static final Pattern QUERY_PARAM_PATTERN = Pattern.compile(":(\\w+)");

    protected static final Map<String, BiFunction<ZonedDateTime, Integer, ZonedDateTime>> units = new HashMap<>();

    static {
        units.put("year", (zdt, num) -> zdt.plusYears(num).withDayOfYear(1).truncatedTo(ChronoUnit.DAYS));
        units.put("month", (zdt, num) -> zdt.plusMonths(num).withDayOfMonth(1).truncatedTo(ChronoUnit.DAYS));
        units.put("day", (zdt, num) -> zdt.plusDays(num).truncatedTo(ChronoUnit.DAYS));
        units.put("hour", (zdt, num) -> zdt.plusHours(num).truncatedTo(ChronoUnit.HOURS));
        units.put("minute", (zdt, num) -> zdt.plusMinutes(num).truncatedTo(ChronoUnit.MINUTES));
        units.put("second", (zdt, num) -> zdt.plusSeconds(num).truncatedTo(ChronoUnit.SECONDS));
    }

    @Inject
    protected DateTimeTransformations transformations;
    @Inject
    protected Scripting scripting;
    @Inject
    protected TimeSource timeSource;

    protected List<ArgDef> argDefs = new ArrayList<>();

    public TimeBetweenQueryMacroHandler() {
        super(MACRO_PATTERN);
    }

    @Override
    public void setQueryParams(Map<String, Object> namedParameters) {
    }

    @Override
    public String replaceQueryParams(String queryString, Map<String, Object> params) {
        Matcher matcher = MACRO_PATTERN.matcher(queryString);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String macros = matcher.group(0);
            macros = replaceParamsInMacros(macros, params);
            matcher.appendReplacement(sb, macros);
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    protected String replaceParamsInMacros(String macros, Map<String, Object> params) {
        Matcher matcher = QUERY_PARAM_PATTERN.matcher(macros);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String paramName = matcher.group(1);
            if (params.containsKey(paramName)) {
                matcher.appendReplacement(sb, params.get(paramName).toString());
                params.remove(paramName);
            }
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    @Override
    protected String doExpand(String macro) {
        count++;
        String[] args = macro.split(",");
        if (args.length != 4 && args.length != 5)
            throw new RuntimeException("Invalid macro: " + macro);

        String field = args[0];
        TimeZone timeZone = getTimeZoneFromArgs(args, 4);
        if (timeZone == null) {
            timeZone = TimeZone.getDefault();
        }
        String param1 = getParam(args, 1, timeZone);
        String param2 = getParam(args, 2, timeZone);

        return String.format("(%s >= :%s and %s < :%s)", field, param1, field, param2);
    }

    @Override
    public Map<String, Object> getParams() {
        Map<String, Object> params = new HashMap<>();
        for (ArgDef argDef : argDefs) {
            ZonedDateTime zonedDateTime = timeSource.now();
            Class javaType = expandedParamTypes.get(argDef.paramName);
            if (javaType == null)
                throw new RuntimeException(String.format("Type of parameter %s not resolved", argDef.paramName));
            if (transformations.isDateTypeSupportsTimeZones(javaType)) {
                zonedDateTime = zonedDateTime.withZoneSameInstant(argDef.timeZone.toZoneId());
            }
            BiFunction<ZonedDateTime, Integer, ZonedDateTime> calc = units.get(argDef.unit);
            if (calc == null)
                throw new RuntimeException(String.format("Invalid macro argument: %s", argDef.unit));
            zonedDateTime = calc.apply(zonedDateTime, argDef.num);

            params.put(argDef.paramName, transformations.transformFromZDT(zonedDateTime, javaType));
        }
        return params;
    }

    protected String getParam(String[] args, int idx, TimeZone timeZone) {
        String arg = args[idx].trim();
        String unit = args[3].trim();

        Matcher matcher = PARAM_PATTERN.matcher(arg);
        if (!matcher.find())
            throw new RuntimeException("Invalid macro argument: " + arg);

        int num = 0;
        try {
            String expr = matcher.group(2);
            if (!Strings.isNullOrEmpty(expr)) {
                num = scripting.evaluateGroovy(expr, new Binding());
            }
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid macro argument: " + arg, e);
        }

        String paramName = args[0].trim().replace(".", "_") + "_" + count + "_" + idx;
        argDefs.add(new ArgDef(paramName, num, unit, timeZone));

        return paramName;
    }

    protected static class ArgDef {
        protected String paramName;
        protected int num;
        protected String unit;
        protected TimeZone timeZone;

        public ArgDef(String paramName, int num, String unit, TimeZone timeZone) {
            this.paramName = paramName;
            this.num = num;
            this.unit = unit;
            this.timeZone = timeZone;
        }
    }
}