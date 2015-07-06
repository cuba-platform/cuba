/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.sys;

import java.util.Map;

/**
 * Interface to be implemented by JPQL macro handlers.
 * <p>The implementation must be a managed bean with "prototype" scope.</p>
 *
 * @author krivopustov
 * @version $Id$
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