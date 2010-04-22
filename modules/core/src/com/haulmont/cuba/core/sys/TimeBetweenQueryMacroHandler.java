/*
 * Copyright (c) 2010 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 21.04.2010 17:10:42
 *
 * $Id$
 */
package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.global.TimeProvider;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeBetweenQueryMacroHandler implements QueryMacroHandler {

    private static final Pattern MACRO_PATTERN = Pattern.compile("@between\\(([^\\)]+)\\)");
    private static final Pattern PARAM_PATTERN = Pattern.compile("(now)\\s*([+-]*)\\s*(\\d*)");
    
    private static Map<String, Integer> units = new HashMap<String, Integer>();

    static {
        units.put("year", Calendar.YEAR);
        units.put("month", Calendar.MONTH);
        units.put("day", Calendar.DAY_OF_MONTH);
        units.put("hour", Calendar.HOUR_OF_DAY);
        units.put("minute", Calendar.MINUTE);
        units.put("second", Calendar.SECOND);
    }

    private int count;
    private Map<String, Object> params = new HashMap<String, Object>();

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

    public Map<String, Object> getParams() {
        return params;
    }

    private String doExpand(String macro) {
        count++;
        String[] args = macro.split(",");
        if (args.length != 4)
            throw new RuntimeException("Invalid macro: " + macro);

        String field = args[0];
        String param1 = getParam(args, 1);
        String param2 = getParam(args, 2);

        return String.format("(%s >= :%s and %s < :%s)", field, param1, field, param2);
    }

    private String getParam(String[] args, int idx) {
        String arg = args[idx];
        String unit = args[3].trim();

        Matcher matcher = PARAM_PATTERN.matcher(arg);
        if (!matcher.find())
            throw new RuntimeException("Invalid macro argument: " + arg);

        String op = matcher.group(2);
        int num = 0;
        if (!StringUtils.isBlank(op)) {
            try {
                num = Integer.valueOf(matcher.group(3));
            } catch (NumberFormatException e) {
                throw new RuntimeException("Invalid macro argument: " + arg, e);
            }
            if (op.equals("-"))
                num = num * (-1);
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(TimeProvider.currentTimestamp());
//        cal.setTime(new Date());
        int calField = getCalendarField(unit);
        if (num != 0) {
            cal.add(calField, num);
        }
        Date date = DateUtils.truncate(cal.getTime(), calField);

        String paramName = args[0].replace(".", "_") + "_" + count + "_" + idx;
        params.put(paramName, date);

        return paramName;
    }

    private int getCalendarField(String unit) {
        Integer calField = units.get(unit.toLowerCase());
        if (calField == null)
            throw new RuntimeException("Invalid macro argument: " + unit);
        return calField;
    }

}
