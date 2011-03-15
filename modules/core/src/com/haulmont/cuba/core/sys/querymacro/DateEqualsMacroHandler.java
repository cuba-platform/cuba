/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Konstantin Krivopustov
 * Created: 14.03.11 16:23
 *
 * $Id$
 */
package com.haulmont.cuba.core.sys.querymacro;

import com.haulmont.bali.datastruct.Pair;
import com.haulmont.cuba.core.sys.QueryMacroHandler;
import org.apache.commons.lang.time.DateUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateEqualsMacroHandler implements QueryMacroHandler {

    protected static final Pattern MACRO_PATTERN = Pattern.compile("@dateEquals\\(([^\\)]+)\\)");

    protected int count;
    protected Map<String, Object> namedParameters;
    protected List<Pair<String, String>> paramNames = new ArrayList<Pair<String, String>>();

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

    public void setQueryParams(Map<String, Object> namedParameters) {
        this.namedParameters = namedParameters;
    }

    protected String doExpand(String macro) {
        count++;
        String[] args = macro.split(",");
        if (args.length != 2)
            throw new RuntimeException("Invalid macro: " + macro);

        String field = args[0].trim();
        String param1 = args[1].trim().substring(1);
        String param2 = field.replace(".", "_") + "_" + count;
        paramNames.add(new Pair<String, String>(param1, param2));

        return String.format("(%s >= :%s and %s < :%s)", field, param1, field, param2);
    }

    public Map<String, Object> getParams() {
        Map<String, Object> params = new HashMap<String, Object>();
        for (Pair<String, String> pair : paramNames) {
            Date date1 = (Date) namedParameters.get(pair.getFirst());
            if (date1 == null)
                throw new RuntimeException("Parameter " + pair.getFirst() + " not found for macro");
            date1 = DateUtils.truncate(date1, Calendar.DAY_OF_MONTH);
            Date date2 = DateUtils.addDays(date1, 1);

            params.put(pair.getFirst(), date1);
            params.put(pair.getSecond(), date2);
        }
        return params;
    }
}
