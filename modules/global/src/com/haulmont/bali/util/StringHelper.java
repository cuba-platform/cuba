/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.bali.util;

public class StringHelper {

    /**
     * Removes extra (more than one) whitespace characters from any place of the string.<br>
     * Examples:<br>
     * " aaa  bbb   ccc ddd " becomes "aaa bbb ccc ddd"
     *
    */
    public static String removeExtraSpaces(String str) {
        StringBuilder sb = new StringBuilder();

        int pos = 0;
        boolean prevWS = true;

        for (int i = 0; i < str.length(); i++) {
            if (Character.isWhitespace(str.charAt(i)) || i == str.length() - 1) {
                if (!prevWS) {
                    sb.append(str.substring(pos, i)).append(str.charAt(i));
                }
                prevWS = true;
            } else {
                if (prevWS)
                    pos = i;
                prevWS = false;
            }
        }
        if (Character.isWhitespace(sb.charAt(sb.length() - 1)))
            sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }
}
