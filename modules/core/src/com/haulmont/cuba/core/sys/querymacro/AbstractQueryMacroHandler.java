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

import com.google.common.base.Strings;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Scripting;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.core.sys.QueryMacroHandler;
import com.haulmont.cuba.security.global.UserSession;
import groovy.lang.Binding;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractQueryMacroHandler implements QueryMacroHandler {

    protected static final Pattern QUERY_PARAM_PATTERN = Pattern.compile(":(\\w+)");
    protected static final Pattern NEGATIVE_PARAM_EXPR_PATTERN = Pattern.compile("[+-]\\s*[-]\\s*\\d+.*");

    protected int count;
    protected final Pattern macroPattern;
    protected Map<String, Class> expandedParamTypes;

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

    protected String replaceParamsInMacros(String macros, Map<String, Object> params) {
        Matcher matcher = QUERY_PARAM_PATTERN.matcher(macros);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String paramName = matcher.group(1);
            if (params.get(paramName) instanceof Number) {
                matcher.appendReplacement(sb, params.get(paramName).toString());
                params.remove(paramName);
            }
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    @Override
    public String replaceQueryParams(String queryString, Map<String, Object> params) {
        Matcher matcher = macroPattern.matcher(queryString);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String macros = matcher.group(0);
            macros = replaceParamsInMacros(macros, params);
            matcher.appendReplacement(sb, macros);
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * Calculates value of expression.
     *
     * @return value of expression or 0 if expression is null or empty.
     * @throws NumberFormatException in case of malformed expression
     */
    protected int evaluateExpression(@Nullable String expression, Scripting scripting) throws NumberFormatException {
        int val = 0;
        if (!Strings.isNullOrEmpty(expression)) {
            if (NEGATIVE_PARAM_EXPR_PATTERN.matcher(expression).matches())
                expression = '0' + expression; //workaround for expression == "+ -1 " (where "+" is operation from query, "-1" - parameter value)
            val = scripting.evaluateGroovy(expression, new Binding());
        }
        return val;
    }

    protected abstract String doExpand(String macro);

    @Override
    public void setExpandedParamTypes(Map<String, Class> expandedParamTypes) {
        this.expandedParamTypes = expandedParamTypes;
    }

    protected TimeZone getTimeZoneFromArgs(String[] args, int pos) {
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
