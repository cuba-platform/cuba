/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys;

import com.haulmont.cuba.core.global.LoadContext;
import org.apache.commons.lang.ObjectUtils;

import java.io.Serializable;

/**
 * @author artamonov
 * @version $Id$
 */
public class QueryHolder implements Serializable {

    private static final long serialVersionUID = -6055610488135337366L;

    public final LoadContext.Query query;

    public QueryHolder(LoadContext.Query query) {
        this.query = query;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QueryHolder that = (QueryHolder) o;

        if (query == null || that.query == null) return false;
        if (!ObjectUtils.equals(query.getQueryString(), that.query.getQueryString())) return false;
        if (!ObjectUtils.equals(query.getParameters(), that.query.getParameters())) return false;

        return true;
    }
}