/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.jmx;

import com.haulmont.cuba.core.app.StatisticsCounterAPI;
import com.haulmont.cuba.core.sys.jmx.StatisticsCounterMBean;

import javax.annotation.ManagedBean;
import javax.inject.Inject;

/**
 * @author krivenko
 * @version $Id$
 */
@ManagedBean("cuba_StatisticsCounterMBean")
public class StatisticsCounter implements StatisticsCounterMBean {

    @Inject
    protected StatisticsCounterAPI statisticsCounterAPI;

    @Override
    public Long getActiveTransactionsCount() {
        return statisticsCounterAPI.getActiveTransactionsCount();
    }

    @Override
    public Long getStartedTransactionsCount() {
        return statisticsCounterAPI.getStartedTransactionsCount();
    }

    @Override
    public Long getCommittedTransactionsCount() {
        return statisticsCounterAPI.getCommittedTransactionsCount();
    }

    @Override
    public Long getRolledBackTransactionsCount() {
        return statisticsCounterAPI.getRolledBackTransactionsCount();
    }

    @Override
    public Long getMiddlewareRequestsCount() {
        return statisticsCounterAPI.getMiddlewareRequestsCount();
    }

    @Override
    public Long getSchedulersCallsCount() {
        return statisticsCounterAPI.getSchedulersCallsCount();
    }

}
