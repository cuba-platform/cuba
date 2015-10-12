/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.jmx;

import org.springframework.jmx.export.annotation.ManagedResource;

/**
 * @author krivenko
 * @version $Id$
 */
@ManagedResource(description = "Provides various information about middleware performance statistics")
@SuppressWarnings("unused")
public interface StatisticsCounterMBean {

    Long getActiveTransactionsCount();

    double getStartedTransactionsCount();

    Long getCommittedTransactionsCount();

    Long getRolledBackTransactionsCount();

    double getTransactionsPerSecond();

    double getMiddlewareRequestsCount();

    double getMiddlewareRequestsPerSecond();

    double getCubaScheduledTasksCount();

    double getCubaScheduledTasksPerSecond();

    double getSpringScheduledTasksCount();

    double getSpringScheduledTasksPerSecond();

    int getDbConnectionPoolNumActive();

    int getDbConnectionPoolNumIdle();

    int getDbConnectionPoolMaxTotal();

    double getAvgDbConnectionPoolNumActive();

    double getAvgDbConnectionPoolNumIdle();

    double getAvgActiveTransactions();

    double getAvgUserSessions();

    double getAvgHeapMemoryUsage();

    double getAvgNonHeapMemoryUsage();

    double getAvgFreePhysicalMemorySize();

    double getAvgFreeSwapSpaceSize();

    double getAvgSystemCpuLoad();

    double getAvgProcessCpuLoad();

    double getAvgThreadCount();
}
