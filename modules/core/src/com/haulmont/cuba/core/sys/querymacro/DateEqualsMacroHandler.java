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
import com.haulmont.cuba.core.global.UserSessionSource;
import groovy.lang.Binding;
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
    protected Scripting scripting;
    @Inject
    protected TimeSource timeSource;

    protected Map<String, Object> namedParameters;

    protected List<String> paramNames = new ArrayList<>();
    protected boolean isNow = false;
    protected TimeZone timeZone;
    protected int offset = 0;

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
        timeZone = getTimeZoneFromArgs(args, 2);

        Matcher matcher = NOW_PARAM_PATTERN.matcher(param1);
        if (matcher.find()) {
            isNow = true;
            try {
                String expr = matcher.group(2);
                if (!Strings.isNullOrEmpty(expr)) {
                    offset = scripting.evaluateGroovy(expr, new Binding());
                }
            } catch (NumberFormatException e) {
                throw new RuntimeException("Invalid macro argument: " + param1, e);
            }
            param1 = args[0].trim().replace(".", "_") + "_" + count + "_" + 1;
            param2 = args[0].trim().replace(".", "_") + "_" + count + "_" + 2;
        } else {
            param1 = param1.substring(1);
            param2 = field.replace(".", "_") + "_" + count;
        }
        paramNames.add(param1);
        paramNames.add(param2);

        return String.format("(%s >= :%s and %s < :%s)", field, param1, field, param2);
    }

    @Override
    public Map<String, Object> getParams() {
        Map<String, Object> params = new HashMap<>();
        Class javaType;
        ZonedDateTime zonedDateTime;
        if (paramNames.isEmpty())
            return params;
        if (timeZone == null)
            timeZone = TimeZone.getDefault();
        if (isNow) {
            zonedDateTime = timeSource.now();
            javaType = expandedParamTypes.get(paramNames.get(0));
            if (javaType == null)
                throw new RuntimeException(String.format("Type of parameter %s not resolved", paramNames.get(0)));
        } else {
            Object date = namedParameters.get(paramNames.get(0));
            if (date == null)
                throw new RuntimeException(String.format("Parameter %s not found for macro", paramNames.get(0)));
            javaType = date.getClass();
            zonedDateTime = transformations.transformToZDT(date);
        }
        if (transformations.isDateTypeSupportsTimeZones(javaType)) {
            zonedDateTime = zonedDateTime.withZoneSameInstant(timeZone.toZoneId());
        }
        zonedDateTime = zonedDateTime.plusDays(offset).truncatedTo(ChronoUnit.DAYS);
        ZonedDateTime firstZonedDateTime = zonedDateTime.truncatedTo(ChronoUnit.DAYS);
        ZonedDateTime secondZonedDateTime = firstZonedDateTime.plusDays(1);
        params.put(paramNames.get(0), transformations.transformFromZDT(firstZonedDateTime, javaType));
        params.put(paramNames.get(1), transformations.transformFromZDT(secondZonedDateTime, javaType));
        return params;
    }

    @Override
    public String replaceQueryParams(String queryString, Map<String, Object> params) {
        return queryString;
    }
}