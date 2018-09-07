/*
 * Copyright (c) 2008-2018 Haulmont.
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

package com.haulmont.cuba.gui.components.data;

import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.cuba.core.entity.annotation.IgnoreUserTimeZone;
import com.haulmont.cuba.core.global.MessageTools;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.components.DateField;
import com.haulmont.cuba.gui.components.HasRange;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.persistence.TemporalType;
import javax.validation.constraints.Future;
import javax.validation.constraints.Past;
import java.time.*;
import java.util.Date;
import java.util.TimeZone;

@Component(DateComponents.NAME)
public class DateComponents {
    public static final String NAME = "cuba_DataComponents";

    @Inject
    protected MessageTools messageTools;

    @Inject
    protected MetadataTools metadataTools;

    @Inject
    protected UserSessionSource userSessionSource;

    public void setupDateRange(HasRange component, EntityValueSource valueSource) {
        MetaProperty metaProperty = valueSource.getMetaPropertyPath().getMetaProperty();
        Class javaType = metaProperty.getRange().asDatatype().getJavaClass();

        if (metaProperty.getAnnotations().get(Past.class.getName()) != null) {
            LocalDateTime dateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
            //noinspection unchecked
            component.setRangeEnd(convertFromLocalDateTime(dateTime, javaType));
        } else if (metaProperty.getAnnotations().get(Future.class.getName()) != null) {
            LocalDateTime dateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN).plusDays(1);
            //noinspection unchecked
            component.setRangeEnd(convertFromLocalDateTime(dateTime, javaType));
        }
    }

    public void setupZoneId(DateField component, EntityValueSource valueSource) {
        if (component.getZoneId() == null) {
            MetaProperty metaProperty = valueSource.getMetaPropertyPath().getMetaProperty();
            Class javaType = metaProperty.getRange().asDatatype().getJavaClass();
            if (isSupportsZoneId(javaType)) {
                Boolean ignoreUserTimeZone = metadataTools.getMetaAnnotationValue(metaProperty, IgnoreUserTimeZone.class);
                if (!Boolean.TRUE.equals(ignoreUserTimeZone)) {
                    TimeZone timeZone = userSessionSource.getUserSession().getTimeZone();
                    component.setTimeZone(timeZone);
                }
            }
        }
    }

    public void setupDateFormat(DateField component, EntityValueSource valueSource) {
        MetaProperty metaProperty = valueSource.getMetaPropertyPath().getMetaProperty();
        Class javaType = metaProperty.getRange().asDatatype().getJavaClass();

        TemporalType temporalType = null;

        if (java.sql.Date.class.equals(javaType) || LocalDate.class.equals(javaType)) {
            temporalType = TemporalType.DATE;
        } else if (metaProperty.getAnnotations() != null) {
            temporalType = (TemporalType) metaProperty.getAnnotations().get(MetadataTools.TEMPORAL_ANN_NAME);
        }

        component.setResolution(temporalType == TemporalType.DATE
                ? DateField.Resolution.DAY
                : DateField.Resolution.MIN);

        String formatStr = messageTools.getDefaultDateFormat(temporalType);
        component.setDateFormat(formatStr);
    }

    public LocalDateTime convertToLocalDateTime(Object date) {
        return convertToLocalDateTime(date, ZoneId.systemDefault());
    }

    public LocalDateTime convertToLocalDateTime(Object date, ZoneId zoneId) {
        zoneId = zoneId == null ? ZoneId.systemDefault() : zoneId;
        if (date instanceof java.sql.Date) {
            return java.time.LocalDateTime.of(((java.sql.Date) date)
                    .toLocalDate(), LocalTime.MIDNIGHT);
        } else if (date instanceof Date) {
            return ((Date) date).toInstant()
                    .atZone(zoneId)
                    .toLocalDateTime();
        } else if (date instanceof LocalDate) {
            return java.time.LocalDateTime.of((LocalDate) date, LocalTime.MIDNIGHT);
        } else if (date instanceof LocalDateTime) {
            return ((LocalDateTime) date);
        } else if (date instanceof OffsetDateTime) {
            return ((OffsetDateTime) date).atZoneSameInstant(zoneId)
                    .toLocalDateTime();
        } else if (date != null) {
            throw new IllegalArgumentException(String.format("Date component doesn't support type %s", date.getClass()));
        }
        return null;
    }

    public Object convertFromLocalDateTime(LocalDateTime localDateTime, Class javaType) {
        return convertFromLocalDateTime(localDateTime, ZoneId.systemDefault(), javaType);
    }

    public Object convertFromLocalDateTime(LocalDateTime localDateTime, ZoneId fromZoneId, Class javaType) {
        fromZoneId = fromZoneId == null ? ZoneId.systemDefault() : fromZoneId;
        if (java.sql.Date.class.equals(javaType)) {
            return java.sql.Date.valueOf(localDateTime.toLocalDate());
        } else if (Date.class.equals(javaType)) {
            return Date.from(localDateTime.atZone(fromZoneId).toInstant());
        } else if (LocalDate.class.equals(javaType)) {
            return localDateTime.toLocalDate();
        } else if (LocalDateTime.class.equals(javaType)) {
            return localDateTime;
        } else if (OffsetDateTime.class.equals(javaType)) {
            return localDateTime.atZone(fromZoneId).withZoneSameInstant(ZoneId.systemDefault())
                    .toOffsetDateTime();
        } else {
            throw new IllegalArgumentException(String.format("Date component doesn't support type %s", javaType));
        }
    }

    public LocalTime convertToLocalTime(Object date) {
        if (date instanceof java.sql.Time) {
            return ((java.sql.Time) date).toLocalTime();
        } else if (date instanceof Date) {
            return ((Date) date).toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalTime();
        } else if (date instanceof LocalTime) {
            return (LocalTime) date;
        } else if (date instanceof OffsetTime) {
            return ((OffsetTime) date).toLocalTime();
        } else if (date != null) {
            throw new IllegalArgumentException(String.format("Time component doesn't support type %s", date.getClass()));
        }
        return null;
    }

    public Object convertFromLocalTime(LocalTime localTime, Class javaType) {
        if (java.sql.Time.class.equals(javaType)) {
            return java.sql.Time.valueOf(localTime);
        } else if (Date.class.equals(javaType)) {
            return new Date(java.sql.Time.valueOf(localTime).getTime());
        } else if (LocalTime.class.equals(javaType)) {
            return localTime;
        } else if (OffsetTime.class.equals(javaType)) {
            return new Date(java.sql.Time.valueOf(localTime).getTime())
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toOffsetDateTime()
                    .toOffsetTime();
        } else {
            throw new IllegalArgumentException(String.format("Date component doesn't support type %s", javaType));
        }
    }

    protected boolean isSupportsZoneId(Class javaType) {
        return Date.class.equals(javaType) || OffsetDateTime.class.equals(javaType);
    }
}
