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
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.TimeSource;
import com.google.common.base.Strings;
import com.haulmont.cuba.core.global.Scripting;
import com.haulmont.cuba.core.sys.QueryMacroHandler;
import groovy.lang.Binding;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.context.annotation.Scope;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 */
@Component("cuba_TimeBetweenQueryMacroHandler")
@Scope("prototype")
public class TimeBetweenQueryMacroHandler implements QueryMacroHandler {

    protected static final Pattern MACRO_PATTERN = Pattern.compile("@between\\s*\\(([^\\)]+)\\)");
    protected static final Pattern PARAM_PATTERN = Pattern.compile("(now)\\s*([\\d\\s+-]*)");
    protected static final Pattern QUERY_PARAM_PATTERN = Pattern.compile(":(\\w+)");

    protected static final Map<String, Object> units = new HashMap<>();
    static {
        units.put("year", Calendar.YEAR);
        units.put("month", Calendar.MONTH);
        units.put("day", Calendar.DAY_OF_MONTH);
        units.put("hour", Calendar.HOUR_OF_DAY);
        units.put("minute", Calendar.MINUTE);
        units.put("second", Calendar.SECOND);
    }

    protected int count;
    protected Map<String, Object> params = new HashMap<>();

    @Override
    public String expandMacro(String queryString) {
        count = 0;
        Matcher matcher = MACRO_PATTERN.matcher(queryString);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, doExpand(matcher.group(1)));
        }
        matcher.appendTail(sb);
        return sb.toString();
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

    protected String doExpand(String macro) {
        count++;
        String[] args = macro.split(",");
        if (args.length != 4)
            throw new RuntimeException("Invalid macro: " + macro);

        String field = args[0];
        String param1 = getParam(args, 1);
        String param2 = getParam(args, 2);

        return String.format("(%s >= :%s and %s < :%s)", field, param1, field, param2);
    }

    protected String getParam(String[] args, int idx) {
        String arg = args[idx].trim();
        String unit = args[3].trim();

        Matcher matcher = PARAM_PATTERN.matcher(arg);
        if (!matcher.find())
            throw new RuntimeException("Invalid macro argument: " + arg);

        int num = 0;
        try {
            String expr = matcher.group(2);
            if (!Strings.isNullOrEmpty(expr)) {
                Scripting scripting = AppBeans.get(Scripting.class);
                num = scripting.evaluateGroovy(expr, new Binding());
            }
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid macro argument: " + arg, e);
        }

        Date date = computeDate(num, unit);

        String paramName = args[0].trim().replace(".", "_") + "_" + count + "_" + idx;
        params.put(paramName, date);

        return paramName;
    }

    protected Date computeDate(int num, String unit) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(AppBeans.get(TimeSource.class).currentTimestamp());
        int calField1 = getCalendarField(unit);
        if (num != 0) {
            cal.add(calField1, num);
        }
        int calField = calField1;
        Date date = DateUtils.truncate(cal.getTime(), calField);
        return date;
    }

    protected int getCalendarField(String unit) {
        Integer calField = (Integer) units.get(unit.toLowerCase());
        if (calField == null)
            throw new RuntimeException("Invalid macro argument: " + unit);
        return calField;
    }
}