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
 */

package com.haulmont.cuba.gui.components.filter.dateinterval;

import com.google.common.base.Strings;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Messages;
import com.haulmont.cuba.gui.components.filter.dateinterval.predefined.PredefinedDateInterval;
import com.haulmont.cuba.gui.components.filter.dateinterval.predefined.PredefinedDateIntervalsFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Class that is used for storing the value of the date interval for the "In interval" condition of the generic filter.
 * The class is responsible for converting the value into the JPQL expression.
 */
@Component(DateIntervalValue.NAME)
@Scope("prototype")
public class DateIntervalValue {

    public static final String NAME = "cuba_FilterDateIntervalValue";

    protected Type type;
    protected TimeUnit timeUnit;
    protected Integer number;
    protected boolean includingCurrent;
    protected PredefinedDateInterval predefinedDateInterval;

    protected static final String INCLUDING_CURRENT_DESCR = "including_current";

    protected static final Pattern NEXT_LAST_PATTERN = Pattern.compile("(NEXT|LAST)\\s+\\d+\\s+(DAY|MONTH|MINUTE|HOUR)(\\s+including_current)?");
    protected static final Pattern PREDEFINED_PATTERN = Pattern.compile("PREDEFINED\\s+\\w+");

    public DateIntervalValue() {}

    public DateIntervalValue(String description) {
        if (Strings.isNullOrEmpty(description)) return;

        if (!NEXT_LAST_PATTERN.matcher(description).matches() &&
                !PREDEFINED_PATTERN.matcher(description).matches()) {
            throw new IllegalArgumentException("Wrong filter date interval description format");
        }

        String[] parts = description.split("\\s+");
        type = Type.valueOf(parts[0]);
        switch (type) {
            case PREDEFINED:
                predefinedDateInterval = getIntervalByName(parts[1]);
                break;
            default:
                number = Integer.valueOf(parts[1]);
                timeUnit = TimeUnit.valueOf(parts[2]);
                includingCurrent = parts.length == 4 && INCLUDING_CURRENT_DESCR.equals(parts[3]);
        }
    }

    @Nullable
    protected PredefinedDateInterval getIntervalByName(String name) {
        Optional<PredefinedDateInterval> result = AppBeans.getAll(PredefinedDateInterval.class).values()
                .stream()
                .filter(interval -> name.equals(interval.getName()))
                .findFirst();
        return (result.isPresent()) ? result.get() : null;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public boolean isIncludingCurrent() {
        return includingCurrent;
    }

    public void setIncludingCurrent(boolean includingCurrent) {
        this.includingCurrent = includingCurrent;
    }

    public PredefinedDateInterval getPredefinedDateInterval() {
        return predefinedDateInterval;
    }

    public void setPredefinedDateInterval(PredefinedDateInterval predefinedDateInterval) {
        this.predefinedDateInterval = predefinedDateInterval;
    }

    @Nullable
    public String getDescription() {
        if (type == null) return null;
        switch (type) {
            case PREDEFINED:
                return type.name() + " " + predefinedDateInterval.getName();
            case NEXT:
            case LAST:
                return type.name() + " " +
                        number + " " +
                        timeUnit.name() +
                        (includingCurrent ? " " + INCLUDING_CURRENT_DESCR : "");
            default:
                throw new IllegalStateException("Unknown date interval type: " + type);
        }
    }

    @Nullable
    public String getLocalizedValue() {
        if (type == null) return null;
        Messages messages = AppBeans.get(Messages.class);
        switch (type) {
            case LAST:
            case NEXT:
                return messages.getMessage(type) +
                        " " +
                        number +
                        " " +
                        messages.getMessage(timeUnit).toLowerCase() +
                        (includingCurrent ? " " + messages.getMessage(DateIntervalValue.class, "includingCurrent") : "");
            case PREDEFINED:
                return predefinedDateInterval.getLocalizedCaption();
            default:
                throw new IllegalStateException("Unknown date interval type: " + type);
        }
    }

    public String toJPQL(String propertyName) {
        if (type == null) return null;
        if (type == Type.PREDEFINED) {
            return predefinedDateInterval.getJPQL(propertyName);
        }

        String moment1 = "";
        String moment2 = "";
        switch (type) {
            case LAST:
                moment1 = "now - " + number;
                moment2 = includingCurrent ? "now + 1" : "now";
                break;
            case NEXT:
                moment1 = includingCurrent ? "now" : "now + 1";
                moment2 = "now + " + number;
                break;

            default:
                // no action
                break;
        }
        return String.format("@between(%s.%s, %s, %s, %s)", "{E}", propertyName, moment1, moment2, timeUnit.name());
    }

    public enum Type {
        LAST,
        NEXT,
        PREDEFINED,
    }

    public enum TimeUnit {
        DAY,
        HOUR,
        MINUTE,
        MONTH
    }
}
