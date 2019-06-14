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

import com.haulmont.chile.core.datatypes.Datatype;
import com.haulmont.chile.core.model.MetaProperty;
import com.haulmont.chile.core.model.MetaPropertyPath;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesUtils;
import com.haulmont.cuba.core.app.dynamicattributes.PropertyType;
import com.haulmont.cuba.core.entity.CategoryAttribute;
import com.haulmont.cuba.core.entity.annotation.CaseConversion;
import com.haulmont.cuba.core.entity.annotation.ConversionType;
import com.haulmont.cuba.core.entity.annotation.IgnoreUserTimeZone;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.gui.components.DateField;
import com.haulmont.cuba.gui.components.HasRange;
import com.haulmont.cuba.gui.components.OptionsField;
import com.haulmont.cuba.gui.components.TextInputField;
import com.haulmont.cuba.gui.components.data.meta.EntityValueSource;
import com.haulmont.cuba.gui.components.data.options.EnumOptions;
import org.apache.commons.collections4.MapUtils;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.persistence.TemporalType;
import javax.validation.constraints.*;
import java.time.*;
import java.util.Map;
import java.util.TimeZone;

/**
 * Utillity bean that provides typical data aware operations with UI components.
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
     * Sets case conversion using {@link CaseConversion} annotation on entity property.
     *
     * @param component UI component
     * @param valueSource value source
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
     * Sets max length for textual UI component using Entity metadata.
     *
     * @param component UI component
     * @param valueSource value source
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

    @SuppressWarnings("unchecked")
    public void setupDateRange(HasRange component, EntityValueSource valueSource) {
        MetaProperty metaProperty = valueSource.getMetaPropertyPath().getMetaProperty();
        Class javaType = metaProperty.getRange().asDatatype().getJavaClass();
        TemporalType temporalType = getTemporalType(metaProperty, javaType);

        if (metaProperty.getAnnotations().get(Past.class.getName()) != null
                || metaProperty.getAnnotations().get(PastOrPresent.class.getName()) != null) {
            LocalDateTime dateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
            ZonedDateTime zonedDateTime = ZonedDateTime.of(dateTime, ZoneId.systemDefault());
            component.setRangeEnd(dateTimeTransformations.transformFromZDT(zonedDateTime, javaType));
        } else if (metaProperty.getAnnotations().get(Future.class.getName()) != null) {
            LocalDateTime dateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
            // In case of date and time we can select the current date with future time,
            // so we start from the next day only if the time isn't displayed
            if (temporalType == TemporalType.DATE) {
                dateTime = dateTime.plusDays(1);
            }
            ZonedDateTime zonedDateTime = ZonedDateTime.of(dateTime, ZoneId.systemDefault());
            component.setRangeStart(dateTimeTransformations.transformFromZDT(zonedDateTime, javaType));
        } else if (metaProperty.getAnnotations().get(FutureOrPresent.class.getName()) != null) {
            LocalDateTime dateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
            ZonedDateTime zonedDateTime = ZonedDateTime.of(dateTime, ZoneId.systemDefault());
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

    /**
     * Throws IllegalArgumentException if component's {@link ValueSource} and {@link Datatype} have different types.
     *
     * @param datatype    datatype
     * @param valueSource component's value source
     */
    public void checkValueSourceDatatypeMismatch(Datatype datatype, ValueSource valueSource) {
        if (valueSource != null && datatype != null) {
            if (!valueSource.getType().equals(datatype.getJavaClass())) {
                throw new IllegalArgumentException("ValueSource and Datatype have different types. ValueSource:"
                        + valueSource.getType() + "; Datatype: " + datatype.getJavaClass());
            }
        }
    }

    public void setupOptions(OptionsField optionsField, EntityValueSource valueSource) {
        MetaPropertyPath propertyPath = valueSource.getMetaPropertyPath();
        MetaProperty metaProperty = propertyPath.getMetaProperty();

        if (metaProperty.getRange().isEnum()) {
            //noinspection unchecked
            optionsField.setOptions(new EnumOptions(metaProperty.getRange().asEnumeration().getJavaClass()));
        } else if (DynamicAttributesUtils.isDynamicAttribute(metaProperty)) {
            CategoryAttribute categoryAttribute = DynamicAttributesUtils.getCategoryAttribute(metaProperty);

            if (categoryAttribute != null
                    && categoryAttribute.getDataType() == PropertyType.ENUMERATION) {

                //noinspection unchecked
                optionsField.setOptionsMap(categoryAttribute.getLocalizedEnumerationMap());
            }
        }
    }
}