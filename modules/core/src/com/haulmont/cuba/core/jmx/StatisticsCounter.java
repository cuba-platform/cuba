/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.jmx;

import com.haulmont.cuba.core.sys.jmx.StatisticsCounterMBean;

import javax.annotation.ManagedBean;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.SimpleType;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author krivenko
 * @version $Id$
 */
@ManagedBean("cuba_StatisticsCounterMBean")
public class StatisticsCounter implements StatisticsCounterMBean {

    private AtomicLong startedTransactionsCount = new AtomicLong();
    private AtomicLong committedTransactionsCount = new AtomicLong();
    private AtomicLong rolledBackTransactionsCount = new AtomicLong();
    private AtomicLong middlewareRequestsCount = new AtomicLong();
    private AtomicLong schedulersCallsCount = new AtomicLong();

    @Override
    public void incStartedTransactionsCount() {
        startedTransactionsCount.incrementAndGet();
    }

    @Override
    public void incCommittedTransactionsCount() {
        committedTransactionsCount.incrementAndGet();
    }

    @Override
    public void incRolledBackTransactionsCount() {
        rolledBackTransactionsCount.incrementAndGet();
    }

    @Override
    public void incMiddlewareRequestsCount() {
        middlewareRequestsCount.incrementAndGet();
    }

    @Override
    public void incSchedulersCallsCount() {
        schedulersCallsCount.incrementAndGet();
    }

    @Override
    public Long getActiveTransactionsCount() {
        return (startedTransactionsCount.get()-committedTransactionsCount.get()-rolledBackTransactionsCount.get());
    }

    @Override
    public Long getStartedTransactionsCount() {
        return startedTransactionsCount.get();
    }

    @Override
    public Long getCommittedTransactionsCount() {
        return committedTransactionsCount.get();
    }

    @Override
    public Long getRolledBackTransactionsCount() {
        return rolledBackTransactionsCount.get();
    }

    @Override
    public Long getMiddlewareRequestsCount() {
        return middlewareRequestsCount.get();
    }

    @Override
    public Long getSchedulersCallsCount() {
        return schedulersCallsCount.get();
    }

}
