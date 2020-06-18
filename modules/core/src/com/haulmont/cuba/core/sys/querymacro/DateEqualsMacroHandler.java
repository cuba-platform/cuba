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
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.core.sys.querymacro.macroargs.MacroArgsDateEquals;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component("cuba_DateEqualsQueryMacroHandler")
@Scope("prototype")
public class DateEqualsMacroHandler extends AbstractQueryMacroHandler {

    protected static final Pattern MACRO_PATTERN = Pattern.compile("@dateEquals\\s*\\(([^)]+)\\)");
    protected static final Pattern NOW_PARAM_PATTERN = Pattern.compile("(now)\\s*([\\d\\s+-]*)");

    @Inject
    protected DateTimeTransformations transformations;
    @Inject
    protected TimeSource timeSource;
    @Inject
    protected Scripting scripting;

    protected Map<String, Object> namedParameters;

    protected List<MacroArgsDateEquals> paramArgs = new ArrayList<>();

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
        String param1 = args[1].trim();
        String param2;
        TimeZone timeZone = getTimeZoneFromArgs(args, 2);

        Matcher matcher = NOW_PARAM_PATTERN.matcher(param1);
        if (matcher.find()) {
            int offset;
            try {
                String expr = matcher.group(2);
                offset = evaluateExpression(expr, scripting);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Invalid macro argument: " + param1, e);
            }
            param1 = args[0].trim().replace(".", "_") + "_" + count + "_" + 1;
            param2 = args[0].trim().replace(".", "_") + "_" + count + "_" + 2;
            paramArgs.add(new MacroArgsDateEquals(param1, param2, timeZone, offset, true));
        } else {
            param1 = param1.substring(1);
            param2 = field.replace(".", "_") + "_" + count;
            paramArgs.add(new MacroArgsDateEquals(param1, param2, timeZone));
        }

        return String.format("(%s >= :%s and %s < :%s)", field, param1, field, param2);
    }

    @Override
    public Map<String, Object> getParams() {
        Map<String, Object> params = new HashMap<>();
        for (MacroArgsDateEquals paramArg : paramArgs) {
            Class javaType;
            ZonedDateTime zonedDateTime;
            String firstparamName = paramArg.getParamName();
            String secondParamName = paramArg.getSecondParamName();
            TimeZone timeZone = paramArg.getTimeZone();
            if (timeZone == null)
                timeZone = TimeZone.getDefault();
            if (paramArg.isNow()) {
                zonedDateTime = timeSource.now();
                javaType = expandedParamTypes.get(firstparamName);
                if (javaType == null)
                    throw new RuntimeException(String.format("Type of parameter %s not resolved", firstparamName));
            } else {
                Object date = namedParameters.get(firstparamName);
                if (date == null)
                    throw new RuntimeException(String.format("Parameter %s not found for macro", firstparamName));
                javaType = date.getClass();
                zonedDateTime = transformations.transformToZDT(date);
            }
            if (transformations.isDateTypeSupportsTimeZones(javaType)) {
                zonedDateTime = zonedDateTime.withZoneSameInstant(timeZone.toZoneId());
            }
            zonedDateTime = zonedDateTime.plusDays(paramArg.getOffset()).truncatedTo(ChronoUnit.DAYS);
            ZonedDateTime firstZonedDateTime = zonedDateTime.truncatedTo(ChronoUnit.DAYS);
            ZonedDateTime secondZonedDateTime = firstZonedDateTime.plusDays(1);
            params.put(firstparamName, transformations.transformFromZDT(firstZonedDateTime, javaType));
            params.put(secondParamName, transformations.transformFromZDT(secondZonedDateTime, javaType));
        }
        return params;
    }

}