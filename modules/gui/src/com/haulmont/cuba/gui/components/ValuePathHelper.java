/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Dmitry Abramov
 * Created: 19.01.2009 11:31:13
 * $Id$
 */
package com.haulmont.cuba.gui.components;

import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.ArrayList;

public class ValuePathHelper {
    public static String format(String[] elements) {
        StringBuffer buffer = new StringBuffer();

        int i = 1;
        for (String element : elements) {
            buffer.append(element.contains(".") ? "[" + element + "]" : element);
            if (i != elements.length) buffer.append(".");
            i++;
        }

        return buffer.toString();
    }

    public static String[] parse(String path) {
        List<String> elements = new ArrayList<String>();

        int breaketCount = 0;

        StringBuilder buffer = new StringBuilder();

        for (int i = 0; i < path.length(); i++) {
            char c = path.charAt(i);
            if (c == '[') {
                breaketCount++;
                continue;
            }

            if (c == ']') {
                breaketCount--;
                continue;
            }

            if ('.' != c || breaketCount > 0)
                buffer.append(c);

            if ('.' == c && breaketCount == 0) {
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

        return elements.toArray(new String[]{});
    }
}
