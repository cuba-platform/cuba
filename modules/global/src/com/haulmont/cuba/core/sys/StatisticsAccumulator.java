/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.core.sys;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;
import java.lang.management.ManagementFactory;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author krivopustov
 * @version $Id$
 */
public class StatisticsAccumulator {

    protected Parameter heapMemoryUsage = new Parameter();
    protected Parameter nonHeapMemoryUsage = new Parameter();
    protected Parameter freePhysicalMemorySize = new Parameter();
    protected Parameter freeSwapSpaceSize = new Parameter();
    protected Parameter systemCpuLoad = new Parameter();
    protected Parameter processCpuLoad = new Parameter();
    protected Parameter threadCount = new Parameter();

    private AtomicLong springScheduledTasksCount = new AtomicLong();

    protected final long startTime = System.currentTimeMillis();

    private Log log = LogFactory.getLog(StatisticsAccumulator.class);

    public void gatherParameters() {
        try {
            MBeanServerConnection connection = ManagementFactory.getPlatformMBeanServer();

            CompositeData data = (CompositeData) connection.getAttribute(
                    new ObjectName("java.lang:type=Memory"), "HeapMemoryUsage");
            heapMemoryUsage.register((Long) data.get("used"));

            data = (CompositeData) connection.getAttribute(
                    new ObjectName("java.lang:type=Memory"), "NonHeapMemoryUsage");
            nonHeapMemoryUsage.register((Long) data.get("used"));

            freePhysicalMemorySize.register((Long) connection.getAttribute(
                    new ObjectName("java.lang:type=OperatingSystem"), "FreePhysicalMemorySize"));

            freeSwapSpaceSize.register((Long) connection.getAttribute(
                    new ObjectName("java.lang:type=OperatingSystem"), "FreeSwapSpaceSize"));

            systemCpuLoad.register((double) connection.getAttribute(
                    new ObjectName("java.lang:type=OperatingSystem"), "SystemCpuLoad"));

            processCpuLoad.register((double) connection.getAttribute(
                    new ObjectName("java.lang:type=OperatingSystem"), "ProcessCpuLoad"));

            threadCount.register((int) connection.getAttribute(
                    new ObjectName("java.lang:type=Threading"), "ThreadCount"));
        } catch (Exception e) {
            log.warn("Unable to gather system statistics: " + e);
        }
    }

    public double getAvgHeapMemoryUsage() {
        return heapMemoryUsage.getValue();
    }

    public double getAvgNonHeapMemoryUsage() {
        return nonHeapMemoryUsage.getValue();
    }

    public double getAvgFreePhysicalMemorySize() {
        return freePhysicalMemorySize.getValue();
    }

    public double getAvgFreeSwapSpaceSize() {
        return freeSwapSpaceSize.getValue();
    }

    public double getAvgSystemCpuLoad() {
        return systemCpuLoad.getValue();
    }

    public double getAvgProcessCpuLoad() {
        return processCpuLoad.getValue();
    }

    public double getAvgThreadCount() {
        return threadCount.getValue();
    }

    public void incSpringScheduledTasksCount() {
        springScheduledTasksCount.incrementAndGet();
    }

    public Long getSpringScheduledTasksCount() {
        return springScheduledTasksCount.get();
    }

    public double getSpringScheduledTasksPerSecond() {
        return getSpringScheduledTasksCount() / ((System.currentTimeMillis() - startTime) / 1000.0);
    }

    public static class Parameter {
        long count;
        double sum;
        ReadWriteLock lock = new ReentrantReadWriteLock();

        public void register(double value) {
            lock.writeLock().lock();
            try {
                sum += value;
                count++;
            } finally {
                lock.writeLock().unlock();
            }
        }

        public double getValue() {
            lock.readLock().lock();
            try {
                return sum / count;
            } finally {
                lock.readLock().unlock();
            }
        }
    }
}
