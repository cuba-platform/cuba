/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.portal.restapi;

/**
 * @author krivopustov
 * @version $Id$
 */
public interface MyJSON {
    /**
     * Render into a string buffer.
     *
     * @param level level at which this instance is being rendered
     * @return a mutable buffer
     */
    public StringBuilder asString(int level);

    public static final char FIELD_SEPARATOR  = ',';
    public static final char MEMBER_SEPARATOR = ',';
    public static final char VALUE_SEPARATOR  = ':';
    public static final char IOR_SEPARTOR     = '-';
    public static final char QUOTE            = '"';
    public static final char SPACE            = ' ';
    public static final char OBJECT_START     = '{';
    public static final char OBJECT_END       = '}';
    public static final char ARRAY_START      = '[';
    public static final char ARRAY_END        = ']';

    public static final String NEWLINE        = "\r\n";
    public static final String NULL_LITERAL   = "null";
    public static final String REF_MARKER     = "ref";
    public static final String ID_MARKER      = "id";
    public static final String ARRAY_EMPTY    = "[]";
}