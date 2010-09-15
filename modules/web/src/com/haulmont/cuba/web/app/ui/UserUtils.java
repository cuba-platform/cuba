/*
 * Copyright (c) 2008 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.

 * Author: Valery Novikov
 * Created: 13.09.2010 15:27:27
 *
 * $Id$
 */

package com.haulmont.cuba.web.app.ui;

import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

public class UserUtils {

    static public String formatName(String pattern, String firstName, String lastName, String middleName) throws ParseException {
        if (pattern == null || pattern.length() == 0)
            throw new ParseException("Pattern error", 0);
        if (firstName == null || firstName == "null")
            firstName = "";
        if (lastName == null || lastName == "null")
            lastName = "";
        if (middleName == null || middleName == "null")
            middleName = "";
        String[] params = StringUtils.substringsBetween(pattern, "{", "}");
        int i;
        for (i = 0; i < params.length; i++) {
            pattern = StringUtils.replace(pattern, "{" + params[i] + "}", "{" + i + "}", 1);
            params[i] = parseParam(params[i], firstName, lastName, middleName);
        }
        for (i = 0; i < params.length; i++) {
            pattern = StringUtils.replace(pattern, "{" + i + "}", params[i], 1);
        }
        return pattern;
    }

    static private String parseParam(String param, String firstName, String lastName, String middleName) throws ParseException {
        if (param == null || param.length() == 0)
            throw new ParseException("Pattern error", 0);
        String last = StringUtils.substringAfter(param, "|");
        String first = StringUtils.upperCase(StringUtils.substringBefore(param, "|"));
        if (first == null || first.length() == 0)
            throw new ParseException("Pattern error", 0);
        char type = first.charAt(0);
        boolean all = true;
        int length = 0;
        if (first.length() > 1) {
            char ch = first.charAt(1);
            switch (ch) {
                case 'F':
                case 'L':
                case 'M':
                    if (first.length() != 2 || type != ch)
                        throw new ParseException("Pattern error", 2);
                    break;
                default:
                    length = Integer.parseInt(first.substring(1, first.length()));
                    break;
            }
        } else {
            all = false;
            length = 1;
        }
        switch (type) {
            case 'F':
                first = firstName;
                break;
            case 'L':
                first = lastName;
                break;
            case 'M':
                first = middleName;
                break;
            default:
                throw new ParseException("Pattern error", 0);
        }
        if (!all) {
            first = StringUtils.left(first, length);
        }
        return (first.length() > 0) ? first + last : "";
    }
}
