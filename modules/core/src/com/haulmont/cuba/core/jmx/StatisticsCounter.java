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
import com.haulmont.cuba.core.sys.AppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;
import javax.inject.Inject;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.Set;
import java.util.regex.Pattern;

/**
 */
@Component("cuba_StatisticsCounterMBean")
public class StatisticsCounter implements StatisticsCounterMBean {

    private Logger log = LoggerFactory.getLogger(StatisticsCounter.class);

    protected final Pattern DS_MBEAN_PATTERN;

    @Inject
    protected MiddlewareStatisticsAccumulator accumulator;

    protected volatile ObjectName dbConnPoolObjectName;

    protected volatile boolean dbConnPoolNotFound;

    public StatisticsCounter() {
        String name = "CubaDS";
        String jndiName = AppContext.getProperty("cuba.dataSourceJndiName");
        if (jndiName != null) {
            String[] parts = jndiName.split("/");
            name = parts[parts.length - 1];
        }
        String re = "Catalina:type=DataSource,.*,class=javax.sql.DataSource,name=\".*" + name + "\"";
        DS_MBEAN_PATTERN = Pattern.compile(re);
    }

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
    public double getMiddlewareRequestsCount() {
        return accumulator.getMiddlewareRequestsCount();
    }

    @Override
    public double getMiddlewareRequestsPerSecond() {
        return accumulator.getMiddlewareRequestsPerSecond();
    }

    @Override
    public double getCubaScheduledTasksCount() {
        return accumulator.getCubaScheduledTasksCount();
    }

    @Override
    public double getCubaScheduledTasksPerSecond() {
        return accumulator.getCubaScheduledTasksPerSecond();
    }

    @Override
    public double getSpringScheduledTasksCount() {
        return accumulator.getSpringScheduledTasksCount();
    }

    @Override
    public double getSpringScheduledTasksPerSecond() {
        return accumulator.getSpringScheduledTasksPerSecond();
    }

    private int getDbConnectionPoolMBeanAttr(String attrName) {
        if (dbConnPoolNotFound)
            return 0;
        try {
            MBeanServerConnection connection = ManagementFactory.getPlatformMBeanServer();
            if (dbConnPoolObjectName == null) {
                Set<ObjectName> names = connection.queryNames(null, null);
                for (ObjectName name : names) {
                    if (DS_MBEAN_PATTERN.matcher(name.toString()).matches()) {
                        dbConnPoolObjectName = name;
                        break;
                    }
                }
            }
            if (dbConnPoolObjectName != null) {
                return (int) connection.getAttribute(dbConnPoolObjectName, attrName);
            } else {
                dbConnPoolNotFound = true;
            }
        } catch (Exception e) {
            log.warn("Error DB connection pool attribute " + attrName + ": " + e);
        }
        return 0;
    }

    @Override
    public int getDbConnectionPoolNumActive() {
        return getDbConnectionPoolMBeanAttr("numActive");
    }

    @Override
    public int getDbConnectionPoolNumIdle() {
        return getDbConnectionPoolMBeanAttr("numIdle");
    }

    @Override
    public int getDbConnectionPoolMaxTotal() {
        return getDbConnectionPoolMBeanAttr("maxTotal");
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
