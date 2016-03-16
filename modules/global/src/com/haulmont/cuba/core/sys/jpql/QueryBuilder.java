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
