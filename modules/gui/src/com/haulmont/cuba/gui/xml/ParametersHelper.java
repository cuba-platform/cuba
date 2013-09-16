/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.gui.xml;

import com.haulmont.cuba.gui.filter.QueryFilter;

import javax.annotation.Nullable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;

import org.apache.commons.lang.StringUtils;

public class ParametersHelper {

    public static final String QUERY_PARAMETERS_RE = ":(\\(\\?i\\))?([\\w\\.\\$]+)";
    public static final Pattern QUERY_PARAMETERS_PATTERN = Pattern.compile(QUERY_PARAMETERS_RE);
    public static final String CASE_INSENSITIVE_MARKER = "(?i)";

    public static final Pattern TEMPL_CLAUSE_PATTERN = Pattern.compile("<#[^>]*>");

    public static Pattern TEMPL_PARAM_PATTERN; // "((component)|(param)|(ds)|(session)|(custom))\\$[\\w\\.]+"

    static {
        StringBuilder sb = new StringBuilder("(");
        ParameterInfo.Type[] values = ParameterInfo.Type.values();
        for (int i = 0, valuesLength = values.length; i < valuesLength; i++) {
            sb.append("(").append(values[i].getPrefix()).append(")");
            if (i < valuesLength - 1) {
                sb.append("|");
            }
        }
        sb.append(")").append("\\$[\\w\\.]+");
        TEMPL_PARAM_PATTERN = Pattern.compile(sb.toString());
    }

    public static Set<String> extractNames(String text) {
        Set<String> set = new HashSet<String>();
        
        Matcher matcher = QUERY_PARAMETERS_PATTERN.matcher(text);
        while (matcher.find()) {
            set.add(matcher.group(2));
        }

        return set;
    }

    public static Set<ParameterInfo> parseQuery(String query) {
        Set<ParameterInfo> infos = new HashSet<ParameterInfo>();

        Matcher matcher = QUERY_PARAMETERS_PATTERN.matcher(query);
        while (matcher.find()) {
            final ParameterInfo info = parse(matcher);
            infos.add(info);
        }

        return infos;
    }

    public static ParameterInfo[] parseQuery(String query, @Nullable QueryFilter filter) {
        Set<ParameterInfo> infos = new HashSet<ParameterInfo>();

        Matcher matcher = QUERY_PARAMETERS_PATTERN.matcher(query);
        while (matcher.find()) {
            final ParameterInfo info = parse(matcher);
            infos.add(info);
        }

        // Add parameters used by freemarker clauses
        Matcher templMatcher = TEMPL_CLAUSE_PATTERN.matcher(query);
        while (templMatcher.find()) {
            String templClause = templMatcher.group();

            Matcher paramMatcher = TEMPL_PARAM_PATTERN.matcher(templClause);
            while (paramMatcher.find()) {
                String param = paramMatcher.group();
                infos.add(parse(param, false));
            }
        }

        if (filter != null) {
            infos.addAll(filter.getParameters());
        }

        return infos.toArray(new ParameterInfo[infos.size()]);
    }

    private static ParameterInfo parse(Matcher matcher) {
        boolean caseInsensitive = !StringUtils.isBlank(matcher.group(1));
        final String param = matcher.group(2);

        return parse(param, caseInsensitive);
    }

    private static ParameterInfo parse(String param, boolean caseInsensitive) {
        final String[] strings = param.split("\\$");
        if (strings.length != 2) {
            throw new IllegalStateException(String.format("Illegal parameter info '%s'", param));
        }
        final String source = strings[0];
        final String name = strings[1];

        for (ParameterInfo.Type type : ParameterInfo.Type.values()) {
            if (type.getPrefix().equals(source)) {
                return new ParameterInfo(name, type, caseInsensitive);
            }
        }
        throw new IllegalStateException(String.format("Illegal parameter info '%s'", param));
    }
}
