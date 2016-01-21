/*
 * Copyright (c) 2008-2016 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.global;

/**
 * @author Maxim Gorbunkov
 * @version $Id$
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
