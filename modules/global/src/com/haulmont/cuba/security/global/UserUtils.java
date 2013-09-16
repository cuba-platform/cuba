/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.security.global;

import org.apache.commons.lang.StringUtils;

import java.text.ParseException;

/**
 * Utility class for {@link com.haulmont.cuba.security.entity.User} full name creation.
 *
 * @author Novikov
 * @version $Id$
 */
public class UserUtils {

    public static String formatName(String pattern, String firstName, String lastName, String middleName) throws ParseException {
        if (pattern == null || pattern.length() == 0)
            throw new ParseException("Pattern error", 0);
        if (firstName == null || firstName.equals("null"))
            firstName = "";
        if (lastName == null || lastName.equals("null"))
            lastName = "";
        if (middleName == null || middleName.equals("null"))
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

    private static String parseParam(String param, String firstName, String lastName, String middleName) throws ParseException {
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
