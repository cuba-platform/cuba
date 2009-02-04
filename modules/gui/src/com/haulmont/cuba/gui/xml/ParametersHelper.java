/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 26.12.2008 10:02:53
 * $Id$
 */
package com.haulmont.cuba.gui.xml;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;

public class ParametersHelper {
    private static final Pattern QUERY_PARAMETERS_PATTERN = Pattern.compile(":([\\w\\.\\$]+)");

    public static ParameterInfo[] parseQuery(String query) {
        Set<ParameterInfo> infos = new HashSet<ParameterInfo>();

        Matcher matcher = QUERY_PARAMETERS_PATTERN.matcher(query);
        while (matcher.find()) {
            final String parameterInfo = matcher.group();
            final ParameterInfo info = parse(parameterInfo);
            infos.add(info);
        }

        return infos.toArray(new ParameterInfo[infos.size()]);
    }

    public static class ParameterInfo {
        public enum Type {
            DATASOURCE("ds"),
            COMPONENT("component"),
            CONTEXT("context");

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

            if (ParameterInfo.Type.DATASOURCE.prefix.equals(source)) {
                return new ParameterInfo(name, ParameterInfo.Type.DATASOURCE);
            } else if (ParameterInfo.Type.CONTEXT.prefix.equals(source)) {
                return new ParameterInfo(name, ParameterInfo.Type.CONTEXT);
            } else if (ParameterInfo.Type.COMPONENT.prefix.equals(source)) {
                return new ParameterInfo(name, ParameterInfo.Type.COMPONENT);
            } else
                throw new IllegalStateException(String.format("Illegal parameter info '%s'", parameterInfo));
        } else {
            throw new IllegalStateException(String.format("Illegal parameter info '%s'", parameterInfo));
        }
    }
}
