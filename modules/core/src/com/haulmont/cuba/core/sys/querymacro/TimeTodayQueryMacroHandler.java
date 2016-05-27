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

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.core.sys.QueryMacroHandler;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.context.annotation.Scope;

import org.springframework.stereotype.Component;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component("cuba_TimeTodayQueryMacroHandler")
@Scope("prototype")
public class TimeTodayQueryMacroHandler implements QueryMacroHandler {

    private static final Pattern MACRO_PATTERN = Pattern.compile("@today\\s*\\(([^\\)]+)\\)");

    private int count;
    private Map<String, Object> params = new HashMap<>();

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
        return queryString;
    }

    private String doExpand(String macro) {
        count++;
        String param1 = macro.replace(".", "_") + "_" + count + "_1";
        String param2 = macro.replace(".", "_") + "_" + count + "_2";

        Calendar cal = Calendar.getInstance();
        cal.setTime(AppBeans.get(TimeSource.class).currentTimestamp());
//        cal.setTime(new Date());
        params.put(param1, DateUtils.truncate(cal.getTime(), Calendar.DAY_OF_MONTH));

        cal.add(Calendar.DAY_OF_MONTH, 1);
        params.put(param2, DateUtils.truncate(cal.getTime(), Calendar.DAY_OF_MONTH));

        return String.format("(%s >= :%s and %s < :%s)", macro, param1, macro, param2);
    }
}