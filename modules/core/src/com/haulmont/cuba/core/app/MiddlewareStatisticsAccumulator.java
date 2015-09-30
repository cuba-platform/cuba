/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.app;

import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.sys.StatisticsAccumulator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;
import javax.inject.Inject;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author krivenko
 * @version $Id$
 */
@Component(MiddlewareStatisticsAccumulator.NAME)
public class MiddlewareStatisticsAccumulator extends StatisticsAccumulator {

    public static final String NAME = "cuba_MiddlewareStatisticsAccumulator";

    protected Logger log = LoggerFactory.getLogger(MiddlewareStatisticsAccumulator.class);

    protected AtomicLong startedTransactionsCount = new AtomicLong();
    protected AtomicLong committedTransactionsCount = new AtomicLong();
    protected AtomicLong rolledBackTransactionsCount = new AtomicLong();
    protected AtomicLong middlewareRequestsCount = new AtomicLong();
    protected AtomicLong cubaScheduledTasksCount = new AtomicLong();

    protected Parameter dbConnectionPoolNumActive = new Parameter();
    protected Parameter dbConnectionPoolNumIdle = new Parameter();

    protected Parameter activeTransactions = new Parameter();
    protected Parameter userSessions = new Parameter();

    @Inject
    protected GlobalConfig config;

    @Override
    public void gatherParameters() {
        super.gatherParameters();
        try {
            activeTransactions.register(getActiveTransactionsCount());

            MBeanServerConnection connection = ManagementFactory.getPlatformMBeanServer();
            String appName = getAppName();

            dbConnectionPoolNumActive.register((int) connection.getAttribute(
                    new ObjectName(appName + ".cuba:type=StatisticsCounter"), "DbConnectionPoolNumActive"));

            dbConnectionPoolNumIdle.register((int) connection.getAttribute(
                    new ObjectName(appName + ".cuba:type=StatisticsCounter"), "DbConnectionPoolNumIdle"));

            userSessions.register((int) connection.getAttribute(
                    new ObjectName(appName + ".cuba:type=UserSessions"), "Count"));

        } catch (Exception e) {
            log.warn("Unable to gather system statistics: " + e);
        }
    }

    protected String getAppName() {
        return config.getWebContextName();
    }

    public void incStartedTransactionsCount() {
        startedTransactionsCount.incrementAndGet();
    }

    public void incCommittedTransactionsCount() {
        committedTransactionsCount.incrementAndGet();
    }

    public void incRolledBackTransactionsCount() {
        rolledBackTransactionsCount.incrementAndGet();
    }

    public void incMiddlewareRequestsCount() {
        middlewareRequestsCount.incrementAndGet();
    }

    public void incCubaScheduledTasksCount() {
        cubaScheduledTasksCount.incrementAndGet();
    }

    public Long getActiveTransactionsCount() {
        return (startedTransactionsCount.get() - committedTransactionsCount.get() - rolledBackTransactionsCount.get());
    }

    public Long getStartedTransactionsCount() {
        return startedTransactionsCount.get();
    }

    public Long getCommittedTransactionsCount() {
        return committedTransactionsCount.get();
    }

    public Long getRolledBackTransactionsCount() {
        return rolledBackTransactionsCount.get();
    }

    public Long getMiddlewareRequestsCount() {
        return middlewareRequestsCount.get();
    }

    public Long getCubaScheduledTasksCount() {
        return cubaScheduledTasksCount.get();
    }

    public double getCubaScheduledTasksPerSecond() {
        return getCubaScheduledTasksCount() / ((System.currentTimeMillis() - startTime) / 1000.0);
    }

    public double getAvgDbConnectionPoolNumActive() {
        return dbConnectionPoolNumActive.getValue();
    }

    public double getAvgDbConnectionPoolNumIdle() {
        return dbConnectionPoolNumIdle.getValue();
    }

    public double getAvgActiveTransactionsCount() {
        return activeTransactions.getValue();
    }

    public double getTransactionsPerSecond() {
        return getStartedTransactionsCount() / ((System.currentTimeMillis() - startTime) / 1000.0);
    }

    public double getAvgUserSessions() {
        return userSessions.getValue();
    }

    public double getMiddlewareRequestsPerSecond() {
        return getMiddlewareRequestsCount() / ((System.currentTimeMillis() - startTime) / 1000.0);
    }
}
