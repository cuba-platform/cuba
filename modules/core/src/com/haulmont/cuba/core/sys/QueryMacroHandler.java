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
package com.haulmont.cuba.core.sys;

import java.util.Map;

/**
 * Interface to be implemented by JPQL macro handlers.
 * <p>The implementation must be a managed bean with "prototype" scope.</p>
 *
 */
public interface QueryMacroHandler {

    /**
     * Replaces macro with real JPQL code. If macro is not found, the source query string is returned.
     *
     * @param queryString   source query string
     * @return              query string with macros replaced
     */
    String expandMacro(String queryString);

    /**
     * Sets parameters of the expanded query to the macro handler.
     *
     * @param namedParameters   all named parameters of the expanded query
     */
    void setQueryParams(Map<String, Object> namedParameters);

    /**
     * @return  all named parameters of the expanded query augmented with the macro parameters
     */
    Map<String, Object> getParams();

    /**
     * Replaces param names in {@code queryString} with its values from the {@code params} parameter.
     * If macro is not found, the source query string is returned.
     *
     * @return modified query string
     */
    String replaceQueryParams(String queryString, Map<String, Object> params);
}