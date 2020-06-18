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

import com.google.common.collect.ImmutableMap;
import com.haulmont.cuba.core.global.DateTimeTransformations;
import com.haulmont.cuba.core.global.Scripting;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.core.sys.querymacro.macroargs.MacroArgsTimeBetween;
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

    protected static final Map<String, BiFunction<ZonedDateTime, Integer, ZonedDateTime>> UNITS =
            new ImmutableMap.Builder<String, BiFunction<ZonedDateTime, Integer, ZonedDateTime>>()
                    .put("year", (zdt, num) ->
                            zdt.plusYears(num).withDayOfYear(1).truncatedTo(ChronoUnit.DAYS)
                    )
                    .put("month", (zdt, num) ->
                            zdt.plusMonths(num).withDayOfMonth(1).truncatedTo(ChronoUnit.DAYS)
                    )
                    .put("day", (zdt, num) ->
                            zdt.plusDays(num).truncatedTo(ChronoUnit.DAYS)
                    )
                    .put("hour", (zdt, num) ->
                            zdt.plusHours(num).truncatedTo(ChronoUnit.HOURS)
                    )
                    .put("minute", (zdt, num) ->
                            zdt.plusMinutes(num).truncatedTo(ChronoUnit.MINUTES)
                    )
                    .put("second", (zdt, num) ->
                            zdt.plusSeconds(num).truncatedTo(ChronoUnit.SECONDS)
                    )
                    .build();

    @Inject
    protected DateTimeTransformations transformations;
    @Inject
    protected TimeSource timeSource;
    @Inject
    protected Scripting scripting;

    protected List<MacroArgsTimeBetween> macroArgs = new ArrayList<>();

    public TimeBetweenQueryMacroHandler() {
        super(MACRO_PATTERN);
    }

    @Override
    public void setQueryParams(Map<String, Object> namedParameters) {
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
        for (MacroArgsTimeBetween macroArg : macroArgs) {
            ZonedDateTime zonedDateTime = timeSource.now();
            Class javaType = expandedParamTypes.get(macroArg.getParamName());
            if (javaType == null)
                throw new RuntimeException(String.format("Type of parameter %s not resolved", macroArg.getParamName()));
            if (transformations.isDateTypeSupportsTimeZones(javaType)) {
                zonedDateTime = zonedDateTime.withZoneSameInstant(macroArg.getTimeZone().toZoneId());
            }
            if (macroArg.getUnit() == null) {
                throw new RuntimeException("Empty date/time unit");
            }
            BiFunction<ZonedDateTime, Integer, ZonedDateTime> calc = UNITS.get(macroArg.getUnit().toLowerCase(Locale.ROOT));
            if (calc == null) {
                throw new RuntimeException(String.format("Invalid macro argument: %s", macroArg.getUnit()));
            }
            zonedDateTime = calc.apply(zonedDateTime, macroArg.getOffset());

            params.put(macroArg.getParamName(), transformations.transformFromZDT(zonedDateTime, javaType));
        }
        return params;
    }

    protected String getParam(String[] args, int idx, TimeZone timeZone) {
        String arg = args[idx].trim();
        String unit = args[3].trim();

        Matcher matcher = PARAM_PATTERN.matcher(arg);
        if (!matcher.find())
            throw new RuntimeException("Invalid macro argument: " + arg);

        int num;
        try {
            String expr = matcher.group(2);
            num = evaluateExpression(expr, scripting);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid macro argument: " + arg, e);
        }

        String paramName = args[0].trim().replace(".", "_") + "_" + count + "_" + idx;
        macroArgs.add(new MacroArgsTimeBetween(paramName, timeZone, unit, num));

        return paramName;
    }
}