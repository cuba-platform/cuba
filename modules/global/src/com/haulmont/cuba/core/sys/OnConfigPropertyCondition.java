/*
 * Copyright (c) 2008-2017 Haulmont.
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

package com.haulmont.cuba.core.sys;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotatedTypeMetadata;

import javax.annotation.Nonnull;
import java.util.Map;

public class OnConfigPropertyCondition implements Condition {
    @Override
    public boolean matches(@Nonnull ConditionContext context, @Nonnull AnnotatedTypeMetadata metadata) {
        Map<String, Object> attributes
                = metadata.getAnnotationAttributes(ConditionalOnAppProperty.class.getName());

        if (attributes != null) {
            String configPropertyName = (String) attributes.get("property");
            String configPropertyValue = (String) attributes.get("value");
            String configPropertyDefaultValue = (String) attributes.get("defaultValue");

            return isConditionSatisfied(configPropertyName, configPropertyValue, configPropertyDefaultValue);
        } else {
            Map<String, Object> valueMap =
                    metadata.getAnnotationAttributes(ConditionalOnAppProperties.class.getName());
            if (valueMap == null) {
                return true;
            }

            AnnotationAttributes[] properties = (AnnotationAttributes[]) valueMap.get("value");

            for (AnnotationAttributes propertyCondition : properties) {
                String configPropertyName = (String) propertyCondition.get("property");
                String configPropertyValue = (String) propertyCondition.get("value");
                String configPropertyDefaultValue = (String) propertyCondition.get("defaultValue");

                if (!isConditionSatisfied(configPropertyName, configPropertyValue, configPropertyDefaultValue)) {
                    return false;
                }
            }
        }

        return true;
    }

    protected boolean isConditionSatisfied(String configPropertyName, String configPropertyValue,
                                           String configPropertyDefaultValue) {
        String propertyValue = AppContext.getProperty(configPropertyName);
        if (propertyValue == null && StringUtils.isNotEmpty(configPropertyDefaultValue)) {
            propertyValue = configPropertyDefaultValue;
        }

        return configPropertyValue.equals(propertyValue);
    }
}