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
import com.haulmont.cuba.core.entity.annotation.ConversionType;
import com.haulmont.cuba.core.global.TimeSource;
import com.haulmont.cuba.core.global.UserSessionSource;
import com.haulmont.cuba.gui.components.HasRange;
import com.haulmont.cuba.gui.components.TextInputField;
import org.apache.commons.collections4.MapUtils;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.validation.constraints.Future;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * todo JavaDoc
 */
@Component(DataAwareComponentsTools.NAME)
public class DataAwareComponentsTools {

    public static final String NAME = "cuba_DataAwareComponentsTools";

    @Inject
    protected UserSessionSource sessionSource;

    @Inject
    protected TimeSource timeSource;

    /**
     * todo JavaDoc
     *
     * @param component
     * @param valueSource
     */
    public void setupCaseConversion(TextInputField.CaseConversionSupported component, EntityValueSource valueSource) {
        MetaProperty metaProperty = valueSource.getMetaPropertyPath().getMetaProperty();
        Map<String, Object> annotations = metaProperty.getAnnotations();

        String caseConversionAnnotation = com.haulmont.cuba.core.entity.annotation.CaseConversion.class.getName();
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

        if (metaProperty.getAnnotations().get(Past.class.getName()) != null) {
            Date currentTimestamp = timeSource.currentTimestamp();

            Calendar calendar = Calendar.getInstance(sessionSource.getLocale());
            calendar.setTime(currentTimestamp);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 999);

            component.setRangeEnd(calendar.getTime());
        } else if (metaProperty.getAnnotations().get(Future.class.getName()) != null) {
            Date currentTimestamp = timeSource.currentTimestamp();

            Calendar calendar = Calendar.getInstance(sessionSource.getLocale());
            calendar.setTime(currentTimestamp);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.add(Calendar.DATE, 1);

            component.setRangeStart(calendar.getTime());
        }
    }
}