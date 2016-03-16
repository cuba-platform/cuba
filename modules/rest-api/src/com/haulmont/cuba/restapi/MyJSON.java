/*
 * Based on JEST, part of the OpenJPA framework.
 *
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

package com.haulmont.cuba.restapi;

/**
 */
public interface MyJSON {
    /**
     * Render into a string buffer.
     *
     * @param level level at which this instance is being rendered
     * @return a mutable buffer
     */
    StringBuilder asString(int level);

    char FIELD_SEPARATOR  = ',';
    char MEMBER_SEPARATOR = ',';
    char VALUE_SEPARATOR  = ':';
    char IOR_SEPARTOR     = '-';
    char QUOTE            = '"';
    char SPACE            = ' ';
    char OBJECT_START     = '{';
    char OBJECT_END       = '}';
    char ARRAY_START      = '[';
    char ARRAY_END        = ']';

    String NEWLINE        = "\r\n";
    String NULL_LITERAL   = "null";
    String REF_MARKER     = "ref";
    String ID_MARKER      = "id";
    String ARRAY_EMPTY    = "[]";
}