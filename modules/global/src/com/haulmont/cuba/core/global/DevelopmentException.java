/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.global;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author krivopustov
 * @version $Id$
 */
@SupportedByClient
public class DevelopmentException extends RuntimeException {

    protected final Map<String, Object> params = new LinkedHashMap<>(1);

    public DevelopmentException(String message) {
        super(message);
    }

    public DevelopmentException(String message, String paramKey, Object paramValue) {
        super(message);
        params.put(paramKey, paramValue);
    }

    public DevelopmentException(String message, Map<String, Object> params) {
        super(message);
        this.params.putAll(params);
    }

    public Map<String, Object> getParams() {
        return params;
    }

    @Override
    public String toString() {
        return super.toString() + (params.isEmpty() ? "" : ", params=" + params);
    }
}
