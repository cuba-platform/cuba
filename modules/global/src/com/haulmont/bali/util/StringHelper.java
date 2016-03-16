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
package com.haulmont.bali.util;

/**
 */
public final class StringHelper {

    private StringHelper() {
    }

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