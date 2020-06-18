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
import com.haulmont.cuba.core.global.Scripting;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.core.sys.querymacro.macroargs.MacroArgs;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component("cuba_DateBeforeQueryMacroHandler")
@Scope("prototype")
public class DateBeforeMacroHandler extends AbstractQueryMacroHandler {

    protected static final Pattern MACRO_PATTERN = Pattern.compile("@dateBefore\\s*\\(([^)]+)\\)");
    protected static final Pattern NOW_PARAM_PATTERN = Pattern.compile("(now)\\s*([\\d\\s+-]*)");

    @Inject
    protected DateTimeTransformations transformations;
    @Inject
    protected TimeSource timeSource;
    @Inject
    protected Scripting scripting;

    protected Map<String, Object> namedParameters;
    protected List<MacroArgs> paramArgs = new ArrayList<>();

    public DateBeforeMacroHandler() {
        super(MACRO_PATTERN);
    }

    @Override
    protected String doExpand(String macro) {
        count++;
        String[] args = macro.split(",");
        if (args.length != 2 && args.length != 3)
            throw new RuntimeException("Invalid macro: " + macro);

        String field = args[0].trim();
        String param = args[1].trim();
        TimeZone timeZone = getTimeZoneFromArgs(args, 2);
        String paramName;

        Matcher matcher = NOW_PARAM_PATTERN.matcher(param);
        if (matcher.find()) {
            int offset;
            try {
                String expr = matcher.group(2);
                offset = evaluateExpression(expr, scripting);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Invalid macro argument: " + param, e);
            }
            paramName = args[0].trim().replace(".", "_") + "_" + count + "_" + 1;
            paramArgs.add(new MacroArgs(paramName, timeZone, offset, true));
        } else {
            paramName = param.substring(1);
            paramArgs.add(new MacroArgs(paramName, timeZone));
        }

        return String.format("(%s < :%s)", field, paramName);
    }

    @Override
    public void setQueryParams(Map<String, Object> namedParameters) {
        this.namedParameters = namedParameters;
    }

    @Override
    public Map<String, Object> getParams() {
        Map<String, Object> params = new HashMap<>();
        for (MacroArgs paramArg : paramArgs) {
            Class javaType;
            ZonedDateTime zonedDateTime;
            TimeZone timeZone = paramArg.getTimeZone();
            String paramName = paramArg.getParamName();;
            if (timeZone == null)
                timeZone = TimeZone.getDefault();
            if (paramArg.isNow()) {
                zonedDateTime = timeSource.now();
                javaType = expandedParamTypes.get(paramName);
                if (javaType == null)
                    throw new RuntimeException(String.format("Type of parameter %s not resolved", paramName));
            } else {
                Object date = namedParameters.get(paramName);
                if (date == null)
                    throw new RuntimeException(String.format("Parameter %s not found for macro", paramName));
                javaType = date.getClass();
                zonedDateTime = transformations.transformToZDT(date);
            }
            if (transformations.isDateTypeSupportsTimeZones(javaType)) {
                zonedDateTime = zonedDateTime.withZoneSameInstant(timeZone.toZoneId());
            }
            zonedDateTime = zonedDateTime.plusDays(paramArg.getOffset()).truncatedTo(ChronoUnit.DAYS);
            Object paramValue = transformations.transformFromZDT(zonedDateTime, javaType);
            params.put(paramName, paramValue);
        }
        return params;
    }

}