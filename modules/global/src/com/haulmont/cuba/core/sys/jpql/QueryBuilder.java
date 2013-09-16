/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys.jpql;

/**
 * Author: Alexander Chevelev
 * Date: 29.03.2011
 * Time: 1:06:23
 */
public class QueryBuilder {
    private StringBuilder sb = new StringBuilder();

    public void appendSpace() {
        if (sb.length() != 0 && sb.charAt(sb.length() - 1) != ' ')
            sb.append(' ');
    }

    public void appendChar(char c) {
        if (sb.length() != 0 && getLast() == ' ' && (c == ' ' || c == ')' || c == ',')) {
            deleteLast();
        }
        sb.append(c);
    }

    public void appendString(String s) {
        if (s != null) {
            if (sb.length() != 0 && getLast() == ' ' && (s.charAt(0) == ' ' || s.charAt(0) == ')' || s.charAt(0) == ',')) {
                deleteLast();
            }
            sb.append(s);
        }
    }

    public char getLast() {
        return sb.charAt(sb.length() - 1);
    }

    public void deleteLast() {
        sb.deleteCharAt(sb.length() - 1);
    }

    @Override
    public String toString() {
        return sb.toString();
    }
}
