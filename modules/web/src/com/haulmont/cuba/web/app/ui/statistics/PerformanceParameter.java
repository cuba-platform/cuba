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

package com.haulmont.cuba.web.app.ui.statistics;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.AbstractNotPersistentEntity;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import com.haulmont.cuba.gui.components.Formatter;

@MetaClass(name = "stat$PerformanceParameter")
@SystemLevel
@SuppressWarnings("unused")
public class PerformanceParameter extends AbstractNotPersistentEntity {
    private static final long serialVersionUID = 7529837429932823943L;

    @MetaProperty
    private String parameterName;

    @MetaProperty
    private String displayName;

    @MetaProperty
    private String parameterGroup;

    private Double current;

    private Double average;

    private Double recent;

    private Boolean showRecent;

    private Long refreshCount;

    private double sum;

    private Formatter<Double> formatter;

    @MetaProperty
    public String getCurrentStringValue() {
        if (current == null)
            return "";

        if (formatter == null)
            return current.toString();

        return formatter.format(current);
    }

    @MetaProperty
    public String getRecentStringValue() {
        if (recent == null)
            return "";

        if (formatter == null)
            return recent.toString();

        return formatter.format(recent);
    }

    @MetaProperty
    public String getAverageStringValue() {
        if (average == null)
            return "";

        if (formatter == null)
            return average.toString();

        return formatter.format(average);
    }

    public Long getRefreshCount() {
        return refreshCount;
    }

    public void setRefreshCount(Long refreshCount) {
        this.refreshCount = refreshCount;
    }

    public Boolean getShowRecent() {
        return showRecent;
    }

    public void setShowRecent(Boolean showRecent) {
        this.showRecent = showRecent;
    }

    public Double getAverage() {
        return average;
    }

    public void setAverage(Double average) {
        this.average = average;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public String getDisplayName() {
        return displayName == null ? parameterName : displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getParameterGroup() {
        return parameterGroup;
    }

    public void setParameterGroup(String parameterGroup) {
        this.parameterGroup = parameterGroup;
    }

    public Double getCurrent() {
        return current;
    }

    public void setCurrent(Double current) {
        this.current = current;
        calcRecent();
    }

    public Long getCurrentLong() {
        return current == null ? null : current.longValue();
    }

    public void setCurrentLong(Long current) {
        this.current = current == null ? null : current.doubleValue();
        calcRecent();
    }

    private void calcRecent() {
        if (showRecent) {
            sum += current;
            setRecent(sum / refreshCount);
        }
    }

    public Double getRecent() {
        return recent;
    }

    public void setRecent(Double average1m) {
        this.recent = average1m;
    }

    public Formatter<Double> getFormatter() {
        return formatter;
    }

    public void setFormatter(Formatter<Double> formatter) {
        this.formatter = formatter;
    }
}