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
 */

package com.haulmont.cuba.core.sys.querymacro;

import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.core.sys.QueryMacroHandler;
import com.haulmont.cuba.security.global.UserSession;

import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractQueryMacroHandler implements QueryMacroHandler {

    protected int count;
    private final Pattern macroPattern;

    protected AbstractQueryMacroHandler(Pattern macroPattern) {
        this.macroPattern = macroPattern;
    }

    @Override
    public String expandMacro(String queryString) {
        count = 0;
        Matcher matcher = macroPattern.matcher(queryString);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, doExpand(matcher.group(1)));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    protected abstract String doExpand(String macro);

    protected TimeZone awareTimeZoneFromArgs(String[] args, int pos) {
        if (pos < args.length) {
            if ("USER_TIMEZONE".equalsIgnoreCase(args[pos].trim())) {
                UserSessionSource userSessionSource = AppBeans.get(UserSessionSource.NAME);
                if (userSessionSource.checkCurrentUserSession()) {
                    UserSession userSession = userSessionSource.getUserSession();
                    return userSession.getTimeZone();
                }
            }
        }
        return null;
    }
}
