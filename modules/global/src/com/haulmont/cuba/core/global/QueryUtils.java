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

package com.haulmont.cuba.core.global;

/**
 */
public class QueryUtils {
    
    public static final String ESCAPE_CHARACTER = "\\";

    /**
     * Escapes a parameter value for a 'like' operation in JPQL query
     * @param value parameter value
     * @return escaped parameter value
     */
    public static String escapeForLike(String value) {
        return escapeForLike(value, ESCAPE_CHARACTER);
    }

    /**
     * Escapes a parameter value for a 'like' operation in JPQL query
     * @param value parameter value
     * @param escapeCharacter escape character
     * @return escaped parameter value
     */
    public static String escapeForLike(String value, String escapeCharacter) {
        return value.replace(escapeCharacter, escapeCharacter + escapeCharacter)
                .replace("%", escapeCharacter + "%")
                .replace("_", escapeCharacter + "_");
    }

}
