/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app;

/**
 * @author krivenko
 * @version $Id$
 */
public interface StatisticsCounterAPI {
    final String NAME = "cuba_StatisticsCounter";

    void incStartedTransactionsCount();

    void incCommittedTransactionsCount();

    void incRolledBackTransactionsCount();

    void incMiddlewareRequestsCount();

    void incSchedulersCallsCount();

    Long getActiveTransactionsCount();

    Long getStartedTransactionsCount();

    Long getCommittedTransactionsCount();

    Long getRolledBackTransactionsCount();

    Long getMiddlewareRequestsCount();

    Long getSchedulersCallsCount();
}
