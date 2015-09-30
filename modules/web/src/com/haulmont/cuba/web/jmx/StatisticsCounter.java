/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.jmx;

import com.haulmont.cuba.web.app.WebStatisticsAccumulator;

import org.springframework.stereotype.Component;
import javax.inject.Inject;

/**
 * @author krivenko
 * @version $Id$
 */
@Component("cuba_StatisticsCounterMBean")
public class StatisticsCounter implements StatisticsCounterMBean {

    @Inject
    protected WebStatisticsAccumulator accumulator;

    @Override
    public double getWebRequestsCount() {
        return accumulator.getWebRequestsCount();
    }

    @Override
    public double getWebRequestsPerSecond() {
        return accumulator.getWebRequestsPerSecond();
    }

    @Override
    public double getSpringScheduledTasksCount() {
        return accumulator.getSpringScheduledTasksCount();
    }

    @Override
    public double getSpringScheduledTasksPerSecond() {
        return accumulator.getSpringScheduledTasksPerSecond();
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
