/*
 * Copyright (c) 2008-2015 Haulmont. All rights reserved.
 * Use is subject to license terms, see http://www.cuba-platform.com/license for details.
 */

package com.haulmont.cuba.web.app.ui.statistics;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.AbstractNotPersistentEntity;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author krivenko
 * @version $Id$
 */
@MetaClass(name = "stat$PerformanceParameter")
@SystemLevel
@SuppressWarnings("unused")
public class PerformanceParameter extends AbstractNotPersistentEntity {
    private static final long serialVersionUID = 7529837429932823943L;

    @MetaProperty
    private String parameterName;

    @MetaProperty
    private String parameterGroup;

    @MetaProperty
    private String currentStringValue;

    private Long currentLongValue;

    private Double currentDoubleValue;

    @MetaProperty
    private Double averageForUptime;

    private Double average1m;

    @MetaProperty
    private String average1mStringValue;

    private Boolean showUptime;

    private Boolean showAverage;

    private Long uptime;

    private Integer averageInterval;

    private Queue<Long> longValues;

    private Queue<Double> doubleValues;

    public Integer getAverageInterval() {
        return averageInterval;
    }

    public void setAverageInterval(Integer averageInterval) {
        this.averageInterval = averageInterval;
        longValues = new ArrayBlockingQueue<>(averageInterval);
        doubleValues = new ArrayBlockingQueue<>(averageInterval);
    }

    public String getAverage1mStringValue() {
        return average1mStringValue != null ? average1mStringValue :
                (average1m != null ? average1m.toString() : "");
    }

    public void setAverage1mStringValue(String average1mStringValue) {
        this.average1mStringValue = average1mStringValue;
    }

    public Long getUptime() {
        return uptime;
    }

    public void setUptime(Long uptime) {
        this.uptime = uptime;
    }

    public Boolean getShowUptime() {
        return showUptime;
    }

    public void setShowUptime(Boolean showUptime) {
        this.showUptime = showUptime;
    }

    public Boolean getShowAverage() {
        return showAverage;
    }

    public void setShowAverage(Boolean showAverage) {
        this.showAverage = showAverage;
    }

    public Double getAverageForUptime() {
        return averageForUptime;
    }

    public void setAverageForUptime(Double averageForUptime) {
        this.averageForUptime = averageForUptime;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public String getParameterGroup() {
        return parameterGroup;
    }

    public void setParameterGroup(String parameterGroup) {
        this.parameterGroup = parameterGroup;
    }

    public String getCurrentStringValue() {
        return currentStringValue != null ? currentStringValue :
                (currentLongValue != null ? currentLongValue.toString() :
                        (currentDoubleValue != null ? currentDoubleValue.toString() : ""));
    }

    public void setCurrentStringValue(String currentStringValue) {
        this.currentStringValue = currentStringValue;
    }

    public Long getCurrentLongValue() {
        return currentLongValue;
    }


    public void setCurrentLongValue(Long currentLongValue) {
        this.currentLongValue = currentLongValue;

        calcLongAverage(currentLongValue);
    }

    private void calcLongAverage(Long currentLongValue) {
        if (showUptime) {
            setAverageForUptime((double) currentLongValue / (uptime != null ? uptime : 1));
        }
        if (showAverage) {
            if (!longValues.offer(currentLongValue)) {
                longValues.poll();
                longValues.offer(currentLongValue);
            }

            long sum = 0;
            for (Long v : longValues) {
                sum += v;
            }
            setAverage1m((double) sum / longValues.size());
        }
    }

    public Double getCurrentDoubleValue() {
        return currentDoubleValue;
    }

    public void setCurrentDoubleValue(Double currentDoubleValue) {
        this.currentDoubleValue = currentDoubleValue;
        calcDoubleAverage(currentDoubleValue);
    }

    private void calcDoubleAverage(Double currentDoubleValue) {
        if (showUptime) {
            setAverageForUptime(currentDoubleValue / (uptime != null ? uptime : 1));
        }
        if (showAverage) {
            boolean added = doubleValues.offer(currentDoubleValue);
            if (!added) {
                doubleValues.poll();
                doubleValues.offer(currentDoubleValue);
            }
            double sum = 0;
            for (Double v : doubleValues) {
                sum += v;
            }
            setAverage1m(sum / doubleValues.size());
        }
    }

    public Double getAverage1m() {
        return average1m;
    }

    public void setAverage1m(Double average1m) {
        this.average1m = average1m;
    }

}
