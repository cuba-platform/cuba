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
package com.haulmont.cuba.gui.components;

import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.ArrayList;

/**
 * Utility class to format and parse component paths
 *
 */
public class ValuePathHelper {

    public static String format(String[] elements) {
        StringBuilder builder = new StringBuilder();

        int i = 1;
        for (String element : elements) {
            builder.append(element.contains(".") ? "[" + element + "]" : element);
            if (i != elements.length) builder.append(".");
            i++;
        }

        return builder.toString();
    }

    public static String[] parse(String path) {
        if (!path.contains(".") && !path.contains("["))
            return new String[] {path};

        List<String> elements = new ArrayList<>();

        int bracketCount = 0;

        StringBuilder buffer = new StringBuilder();

        for (int i = 0; i < path.length(); i++) {
            char c = path.charAt(i);
            if (c == '[') {
                bracketCount++;
                continue;
            }

            if (c == ']') {
                bracketCount--;
                continue;
            }

            if ('.' != c || bracketCount > 0)
                buffer.append(c);

            if ('.' == c && bracketCount == 0) {
                String element = buffer.toString();
                if (!StringUtils.isEmpty(element)) {
                    elements.add(element);
                } else {
                    throw new IllegalStateException("Wrong value path format");
                }
                buffer = new StringBuilder();
            }
        }
        elements.add(buffer.toString());

        return elements.toArray(new String[elements.size()]);
    }
}