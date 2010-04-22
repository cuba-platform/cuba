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
import org.apache.commons.lang.time.DateUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeTodayQueryMacroHandler implements QueryMacroHandler {

    private static final Pattern MACRO_PATTERN = Pattern.compile("@today\\(([^\\)]+)\\)");

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
        String param1 = macro.replace(".", "_") + "_" + count + "_1";
        String param2 = macro.replace(".", "_") + "_" + count + "_2";

        Calendar cal = Calendar.getInstance();
        cal.setTime(TimeProvider.currentTimestamp());
//        cal.setTime(new Date());
        params.put(param1, DateUtils.truncate(cal.getTime(), Calendar.DAY_OF_MONTH));

        cal.add(Calendar.DAY_OF_MONTH, 1);
        params.put(param2, DateUtils.truncate(cal.getTime(), Calendar.DAY_OF_MONTH));

        return String.format("(%s >= :%s and %s < :%s)", macro, param1, macro, param2);
    }
}