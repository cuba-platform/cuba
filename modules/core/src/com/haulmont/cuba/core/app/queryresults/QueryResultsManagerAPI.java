/*
 * Copyright (c) 2008-2013 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app.queryresults;

import com.haulmont.cuba.core.global.LoadContext;

import java.util.List;

/**
 * Supports functionality that allows queries from previously selected results.
 *
 * @author krivopustov
 * @version $Id$
 */
public interface QueryResultsManagerAPI {

    String NAME = "cuba_QueryResultsManager";

    void savePreviousQueryResults(LoadContext loadContext);

    void deleteForCurrentSession();

    void deleteForInactiveSessions();
}
