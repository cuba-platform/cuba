/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */
package com.haulmont.cuba.core.sys;

import java.util.Map;

public interface QueryMacroHandler {

    String expandMacro(String queryString);

    void setQueryParams(Map<String, Object> namedParameters);

    Map<String, Object> getParams();
}
