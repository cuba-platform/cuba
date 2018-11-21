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
import com.haulmont.cuba.core.entity.annotation.CaseConversion;
import com.haulmont.cuba.core.entity.annotation.ConversionType;
import com.haulmont.cuba.core.entity.annotation.IgnoreUserTimeZone;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.components.DateField;
import com.haulmont.cuba.gui.components.HasRange;
import com.haulmont.cuba.gui.components.TextInputField;
import com.haulmont.cuba.gui.components.data.meta.EntityValueSource;
import org.apache.commons.collections4.MapUtils;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.persistence.TemporalType;
import javax.validation.constraints.Future;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.*;
import java.util.Map;
import java.util.TimeZone;

/**
 * todo JavaDoc
 */
@Component(DataAwareComponentsTools.NAME)
public class DataAwareComponentsTools {

    public static final String NAME = "cuba_DataAwareComponentsTools";

    @Inject
    protected UserSessionSource userSessionSource;

    @Inject
    protected MessageTools messageTools;

    @Inject
    protected TimeSource timeSource;

    @Inject
    protected DateTimeTransformations dateTimeTransformations;

    @Inject
    protected MetadataTools metadataTools;

    /**
     * todo JavaDoc
     *
     * @param component
     * @param valueSource
     */
    public void setupCaseConversion(TextInputField.CaseConversionSupported component, EntityValueSource valueSource) {
        MetaProperty metaProperty = valueSource.getMetaPropertyPath().getMetaProperty();
        Map<String, Object> annotations = metaProperty.getAnnotations();

        String caseConversionAnnotation = CaseConversion.class.getName();
        //noinspection unchecked
        Map<String, Object> caseConversion = (Map<String, Object>) annotations.get(caseConversionAnnotation);
        if (MapUtils.isNotEmpty(caseConversion)) {
            ConversionType conversionType = (ConversionType) caseConversion.get("type");
            TextInputField.CaseConversion conversion = TextInputField.CaseConversion.valueOf(conversionType.name());

            component.setCaseConversion(conversion);
        }
    }

    /**
     * todo JavaDoc
     *
     * @param component
     * @param valueSource
     */
    public void setupMaxLength(TextInputField.MaxLengthLimited component, EntityValueSource valueSource) {
        MetaProperty metaProperty = valueSource.getMetaPropertyPath().getMetaProperty();
        Map<String, Object> annotations = metaProperty.getAnnotations();

        Integer maxLength = (Integer) annotations.get("length");
        if (maxLength != null) {
            component.setMaxLength(maxLength);
        }

        Integer sizeMax = (Integer) annotations.get(Size.class.getName() + "_max");
        if (sizeMax != null) {
            component.setMaxLength(sizeMax);
        }

        Integer lengthMax = (Integer) annotations.get(Length.class.getName() + "_max");
        if (lengthMax != null) {
            component.setMaxLength(lengthMax);
        }
    }

    public void setupDateRange(HasRange component, EntityValueSource valueSource) {
        MetaProperty metaProperty = valueSource.getMetaPropertyPath().getMetaProperty();
        Class javaType = metaProperty.getRange().asDatatype().getJavaClass();
        TemporalType temporalType = getTemporalType(metaProperty, javaType);

        if (metaProperty.getAnnotations().get(Past.class.getName()) != null) {
            LocalDateTime dateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
            ZonedDateTime zonedDateTime = ZonedDateTime.of(dateTime, ZoneId.systemDefault());
            //noinspection unchecked
            component.setRangeEnd(dateTimeTransformations.transformFromZDT(zonedDateTime, javaType));
        } else if (metaProperty.getAnnotations().get(Future.class.getName()) != null) {
            LocalDateTime dateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
            // In case of date and time we can select the current date with future time,
            // so we start from the next day only if the time isn't displayed
            if (temporalType == TemporalType.DATE) {
                dateTime = dateTime.plusDays(1);
            }
            ZonedDateTime zonedDateTime = ZonedDateTime.of(dateTime, ZoneId.systemDefault());
            //noinspection unchecked
            component.setRangeStart(dateTimeTransformations.transformFromZDT(zonedDateTime, javaType));
        }
    }

    public void setupZoneId(DateField component, EntityValueSource valueSource) {
        if (component.getZoneId() == null) {
            MetaProperty metaProperty = valueSource.getMetaPropertyPath().getMetaProperty();
            Class javaType = metaProperty.getRange().asDatatype().getJavaClass();
            if (dateTimeTransformations.isDateTypeSupportsTimeZones(javaType)) {
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

        TemporalType temporalType = getTemporalType(metaProperty, javaType);

        component.setResolution(temporalType == TemporalType.DATE
                ? DateField.Resolution.DAY
                : DateField.Resolution.MIN);

        String formatStr = messageTools.getDefaultDateFormat(temporalType);
        component.setDateFormat(formatStr);
    }

    protected TemporalType getTemporalType(MetaProperty metaProperty, Class javaType) {
        TemporalType temporalType = null;

        if (java.sql.Date.class.equals(javaType) || LocalDate.class.equals(javaType)) {
            temporalType = TemporalType.DATE;
        } else if (metaProperty.getAnnotations() != null) {
            temporalType = (TemporalType) metaProperty.getAnnotations().get(MetadataTools.TEMPORAL_ANN_NAME);
        }
        return temporalType;
    }
}