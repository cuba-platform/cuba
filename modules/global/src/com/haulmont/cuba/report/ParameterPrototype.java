/*
 * Copyright (c) 2011 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
 */

package com.haulmont.cuba.report;

import java.io.Serializable;
import java.util.Map;

/**
 * Report parameter for lazy data loading in core module
 * <p>$Id$</p>
 *
 * @author artamonov
 */
public class ParameterPrototype implements Serializable {

    private static final long serialVersionUID = 2654220919728705511L;

    private String paramName;

    private String queryString;

    private String viewName;

    private String metaClassName;

    private Map<String, Object> queryParams;

    public ParameterPrototype(String paramName) {
        this.paramName = paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getParamName() {
        return paramName;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public String getMetaClassName() {
        return metaClassName;
    }

    public void setMetaClassName(String metaClassName) {
        this.metaClassName = metaClassName;
    }

    public Map<String, Object> getQueryParams() {
        return queryParams;
    }

    public void setQueryParams(Map<String, Object> queryParams) {
        this.queryParams = queryParams;
    }
}