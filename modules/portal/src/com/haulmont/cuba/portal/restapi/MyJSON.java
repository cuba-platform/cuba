/*
 * Based on JEST, part of the OpenJPA framework.
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
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