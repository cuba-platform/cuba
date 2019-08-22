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

package com.haulmont.cuba.core.jmx;

import com.haulmont.cuba.core.app.MiddlewareStatisticsAccumulator;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.sys.AppContext;
import com.haulmont.cuba.core.sys.connectionpoolinfo.CommonsConnectionPoolInfo;
import com.haulmont.cuba.core.sys.connectionpoolinfo.ConnectionPoolInfo;
import com.haulmont.cuba.core.sys.connectionpoolinfo.HikariConnectionPoolInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.management.*;
import java.lang.management.ManagementFactory;

@Component("cuba_StatisticsCounterMBean")
public class StatisticsCounter implements StatisticsCounterMBean {

    private static final Logger log = LoggerFactory.getLogger(StatisticsCounter.class);
    protected volatile boolean dbConnPoolNotFound;

    @Inject
    protected MiddlewareStatisticsAccumulator accumulator;

    @Inject
    protected GlobalConfig globalConfig;

    protected volatile ConnectionPoolInfo connectionPoolInfo;

    @Override
    public Long getActiveTransactionsCount() {
        return accumulator.getActiveTransactionsCount();
    }

    @Override
    public double getStartedTransactionsCount() {
        return accumulator.getStartedTransactionsCount();
    }

    @Override
    public Long getCommittedTransactionsCount() {
        return accumulator.getCommittedTransactionsCount();
    }

    @Override
    public Long getRolledBackTransactionsCount() {
        return accumulator.getRolledBackTransactionsCount();
    }

    @Override
    public double getTransactionsPerSecond() {
        return accumulator.getTransactionsPerSecond();
    }

    @Override
    public Long getMiddlewareRequestsCount() {
        return accumulator.getMiddlewareRequestsCount();
    }

    @Override
    public double getMiddlewareRequestsPerSecond() {
        return accumulator.getMiddlewareRequestsPerSecond();
    }

    @Override
    public Long getCubaScheduledTasksCount() {
        return accumulator.getCubaScheduledTasksCount();
    }

    @Override
    public double getCubaScheduledTasksPerSecond() {
        return accumulator.getCubaScheduledTasksPerSecond();
    }

    @Override
    public Long getSpringScheduledTasksCount() {
        return accumulator.getSpringScheduledTasksCount();
    }

    @Override
    public double getSpringScheduledTasksPerSecond() {
        return accumulator.getSpringScheduledTasksPerSecond();
    }

    @Override
    public Long getImplicitFlushCount() {
        return accumulator.getImplicitFlushCount();
    }

    @Override
    public int getDbConnectionPoolNumActive() {
        connectionPoolInfo = getConnectionPoolInfo();
        return connectionPoolInfo == null ? 0 : getDbConnectionPoolMBeanAttr(connectionPoolInfo.getActiveConnectionsAttrName());
    }

    @Override
    public int getDbConnectionPoolNumIdle() {
        connectionPoolInfo = getConnectionPoolInfo();
        return connectionPoolInfo == null ? 0 : getDbConnectionPoolMBeanAttr(connectionPoolInfo.getIdleConnectionsAttrName());
    }

    @Override
    public int getDbConnectionPoolMaxTotal() {
        connectionPoolInfo = getConnectionPoolInfo();
        return connectionPoolInfo == null ? 0 : getDbConnectionPoolMBeanAttr(connectionPoolInfo.getTotalConnectionsAttrName());
    }

    protected ConnectionPoolInfo getConnectionPoolInfo() {
        if (dbConnPoolNotFound) {
            return null;
        }

        String dsProvider = AppContext.getProperty("cuba.dataSourceProvider");
        ConnectionPoolInfo connectionPoolInfo = new CommonsConnectionPoolInfo();
        if ("application".equals(dsProvider)) {
            connectionPoolInfo = new HikariConnectionPoolInfo();
        }

        if (connectionPoolInfo.getRegisteredMBeanName() == null) {
            log.warn("No one connection pool was found for statistics counting!");
            dbConnPoolNotFound = true;
            return null;
        }
        return connectionPoolInfo;
    }

    private int getDbConnectionPoolMBeanAttr(String attrName) {
        ObjectName registeredMbeanPoolName = connectionPoolInfo.getRegisteredMBeanName();
        try {
            return (Integer) ManagementFactory.getPlatformMBeanServer().getAttribute(registeredMbeanPoolName, attrName);
        } catch (JMException e) {
            log.warn(String.format("Can't get MBean attribute %s from %s pool!", attrName, registeredMbeanPoolName.getCanonicalName()), e);
        }
        return 0;
    }

    @Override
    public double getAvgDbConnectionPoolNumActive() {
        return accumulator.getAvgDbConnectionPoolNumActive();
    }

    @Override
    public double getAvgDbConnectionPoolNumIdle() {
        return accumulator.getAvgDbConnectionPoolNumIdle();
    }

    @Override
    public double getAvgActiveTransactions() {
        return accumulator.getAvgActiveTransactionsCount();
    }

    @Override
    public double getAvgUserSessions() {
        return accumulator.getAvgUserSessions();
    }

    @Override
    public double getAvgHeapMemoryUsage() {
        return accumulator.getAvgHeapMemoryUsage();
    }

    @Override
    public double getAvgNonHeapMemoryUsage() {
        return accumulator.getAvgNonHeapMemoryUsage();
    }

    @Override
    public double getAvgFreePhysicalMemorySize() {
        return accumulator.getAvgFreePhysicalMemorySize();
    }

    @Override
    public double getAvgFreeSwapSpaceSize() {
        return accumulator.getAvgFreeSwapSpaceSize();
    }

    @Override
    public double getAvgSystemCpuLoad() {
        return accumulator.getAvgSystemCpuLoad();
    }

    @Override
    public double getAvgProcessCpuLoad() {
        return accumulator.getAvgProcessCpuLoad();
    }

    @Override
    public double getAvgThreadCount() {
        return accumulator.getAvgThreadCount();
    }
}