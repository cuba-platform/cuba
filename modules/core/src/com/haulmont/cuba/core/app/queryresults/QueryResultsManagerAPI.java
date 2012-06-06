/*
 * Copyright (c) 2012 Haulmont Technology Ltd. All Rights Reserved.
 * Haulmont Technology proprietary and confidential.
 * Use is subject to license terms.
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
