/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 26.12.2008 10:02:53
 * $Id$
 */
package com.haulmont.cuba.gui.xml;

import com.haulmont.cuba.gui.filter.QueryFilter;

import javax.annotation.Nullable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;

import org.apache.commons.lang.StringUtils;

public class ParametersHelper {

    private static final Pattern QUERY_PARAMETERS_PATTERN = Pattern.compile(":(\\(\\?i\\))?([\\w\\.\\$]+)");
    public static final String CASE_INSENSITIVE_MARKER = "(?i)";

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

        if (filter != null) {
            infos.addAll(filter.getParameters());
        }

        return infos.toArray(new ParameterInfo[infos.size()]);
    }

    private static ParameterInfo parse(Matcher matcher) {
        boolean caseInsensitive = !StringUtils.isBlank(matcher.group(1));
        final String param = matcher.group(2);

        final String[] strings = param.split("\\$");
        if (strings.length != 2) {
            throw new IllegalStateException(String.format("Illegal parameter info '%s'", matcher.group()));
        }
        final String source = strings[0];
        final String name = strings[1];

        for (ParameterInfo.Type type : ParameterInfo.Type.values()) {
            if (type.getPrefix().equals(source)) {
                return new ParameterInfo(name, type, caseInsensitive);
            }
        }
        throw new IllegalStateException(String.format("Illegal parameter info '%s'", matcher.group()));
    }

//    public static ParameterInfo parse(String parameterInfo) {
//        if (parameterInfo.startsWith(":")) {
//            final String param = parameterInfo.substring(1);
//
//            final String[] strings = param.split("\\$");
//            if (strings.length != 2) {
//                throw new IllegalStateException(String.format("Illegal parameter info '%s'", parameterInfo));
//            }
//            final String source = strings[0];
//            final String name = strings[1];
//
//            for (ParameterInfo.Type type : ParameterInfo.Type.values()) {
//                if (type.prefix.equals(source)) {
//                    return new ParameterInfo(name, type);
//                }
//            }
//
//            throw new IllegalStateException(String.format("Illegal parameter info '%s'", parameterInfo));
//        } else {
//            throw new IllegalStateException(String.format("Illegal parameter info '%s'", parameterInfo));
//        }
//    }
}
