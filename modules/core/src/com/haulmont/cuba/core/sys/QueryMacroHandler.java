/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.sys;

import java.util.Map;

/**
 * @author krivopustov
 * @version $Id$
 */
public interface QueryMacroHandler {

    String expandMacro(String queryString);

    void setQueryParams(Map<String, Object> namedParameters);

    Map<String, Object> getParams();

    /**
     * Replaces param names in {@code queryString} with its values from {@code params} parameter
     * @return modified query string
     */
    String replaceQueryParams(String queryString, Map<String, Object> params);
}