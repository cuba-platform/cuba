/*
 * Copyright (c) 2008-2016 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
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

@Component(MiddlewareStatisticsAccumulator.NAME)
public class MiddlewareStatisticsAccumulator extends StatisticsAccumulator {

    public static final String NAME = "cuba_MiddlewareStatisticsAccumulator";

    private static final Logger log = LoggerFactory.getLogger(MiddlewareStatisticsAccumulator.class);

    protected AtomicLong startedTransactionsCount = new AtomicLong();
    protected AtomicLong committedTransactionsCount = new AtomicLong();
    protected AtomicLong rolledBackTransactionsCount = new AtomicLong();
    protected AtomicLong middlewareRequestsCount = new AtomicLong();
    protected AtomicLong cubaScheduledTasksCount = new AtomicLong();
    protected AtomicLong implicitFlushCount = new AtomicLong();

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

    public void reset() {
        startedTransactionsCount.set(0);
        committedTransactionsCount.set(0);
        rolledBackTransactionsCount.set(0);
        middlewareRequestsCount.set(0);
        cubaScheduledTasksCount.set(0);
        implicitFlushCount.set(0);
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

    public void incImplicitFlushCount() {
        implicitFlushCount.incrementAndGet();
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

    public Long getImplicitFlushCount() {
        return implicitFlushCount.get();
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