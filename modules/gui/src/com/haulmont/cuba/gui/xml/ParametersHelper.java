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

public class ParametersHelper {
    private static final Pattern QUERY_PARAMETERS_PATTERN = Pattern.compile(":([\\w\\.\\$]+)");

    public static Set<String> extractNames(String text) {
        Set<String> set = new HashSet<String>();
        
        Matcher matcher = QUERY_PARAMETERS_PATTERN.matcher(text);
        while (matcher.find()) {
            set.add(matcher.group(1));
        }

        return set;
    }

    public static ParameterInfo[] parseQuery(String query, @Nullable QueryFilter filter) {
        Set<ParameterInfo> infos = new HashSet<ParameterInfo>();

        Matcher matcher = QUERY_PARAMETERS_PATTERN.matcher(query);
        while (matcher.find()) {
            final String parameterInfo = matcher.group();
            final ParameterInfo info = parse(parameterInfo);
            infos.add(info);
        }

        if (filter != null) {
            for (String s : filter.getParameters()) {
                ParameterInfo info = parse(":" + s);
                infos.add(info);
            }
        }

        return infos.toArray(new ParameterInfo[infos.size()]);
    }

    public static class ParameterInfo {
        public enum Type {
            DATASOURCE("ds"),
            COMPONENT("component"),
            PARAM("param"),
            SESSION("session"),
            CUSTOM("custom");

            private String prefix;

            Type(String prefix) {
                this.prefix = prefix;
            }

            public String getPrefix() {
                return prefix;
            }
        }

        private Type type;
        private String path;

        ParameterInfo(String name, Type type) {
            this.path = name;
            this.type = type;
        }

        public Type getType() {
            return type;
        }

        public String getPath() {
            return path;
        }

        public String getName() {
            return (type.getPrefix() + "$" + path);
        }

        public String getFlatName() {
            return (type.getPrefix() + "." + path).replaceAll("\\.", "_");
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ParameterInfo that = (ParameterInfo) o;

            return path.equals(that.path) && type == that.type;
        }

        @Override
        public int hashCode() {
            int result = type.hashCode();
            result = 31 * result + path.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return type + " : " + path;
        }
    }

    public static ParameterInfo parse(String parameterInfo) {
        if (parameterInfo.startsWith(":")) {
            final String param = parameterInfo.substring(1);

            final String[] strings = param.split("\\$");
            if (strings.length != 2) {
                throw new IllegalStateException(String.format("Illegal parameter info '%s'", parameterInfo));
            }
            final String source = strings[0];
            final String name = strings[1];

            for (ParameterInfo.Type type : ParameterInfo.Type.values()) {
                if (type.prefix.equals(source)) {
                    return new ParameterInfo(name, type);
                }
            }

            throw new IllegalStateException(String.format("Illegal parameter info '%s'", parameterInfo));
        } else {
            throw new IllegalStateException(String.format("Illegal parameter info '%s'", parameterInfo));
        }
    }
}
