/*
 * Copyright (c) 2008-2019 Haulmont.
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

package com.haulmont.cuba.core.app.dynamicattributes;

import com.google.common.base.Strings;
import com.haulmont.cuba.core.entity.BaseGenericIdEntity;
import com.haulmont.cuba.core.entity.CategoryAttribute;
import com.haulmont.cuba.core.entity.CategoryAttributeValue;
import com.haulmont.cuba.core.global.GlobalConfig;
import com.haulmont.cuba.core.global.MetadataTools;
import com.haulmont.cuba.core.global.Scripting;
import groovy.lang.Binding;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.*;

@Component(DynamicAttributesRecalculationTools.NAME)
public class DynamicAttributesRecalculationTools {

    public static final String NAME = "cuba_DynamicAttributesRecalculationTools";

    @Inject
    protected Scripting scripting;

    @Inject
    protected MetadataTools metadataTools;

    @Inject
    protected DynamicAttributesTools dynamicAttributesTools;

    @Inject
    protected GlobalConfig config;

    /**
     * Performs recalculation for all dependent dynamic attributes. Recalculation is performed hierarchically.
     *
     * Recalculation level limited by
     * {@code cuba.dynamicAttributes.maxRecalculationLevel} application property. If this property is not defined
     * then the default value is used (default value is 10).
     *
     * @param entity entity with loaded dynamic attributes.
     * @param attribute an attribute from which the recalculation begins. Value for this attribute won't be changed,
     *                  it is assumed that this attribute was updated before
     */
    public void recalculateDynamicAttributes(BaseGenericIdEntity entity, CategoryAttribute attribute) {

        if (attribute == null || attribute.getConfiguration().getDependentAttributes() == null
                || attribute.getConfiguration().getDependentAttributes().isEmpty()) {
            return;
        }

        Set<CategoryAttribute> needToRecalculate = new HashSet<>(attribute.getConfiguration().getDependentAttributes());
        int recalculationLevel = 1;

        while (!needToRecalculate.isEmpty()) {

            if (recalculationLevel > config.getMaxRecalculationLevel()) {
                throw new IllegalStateException(String.format("Recalculation level has reached the maximum allowable value: %d. " +
                        "Check Dynamic Attributes configuration.", config.getMaxRecalculationLevel()));
            }

            Set<CategoryAttribute> nextLevelAttributes = new HashSet<>();

            for (CategoryAttribute dependentAttribute : needToRecalculate) {
                String groovyScript = dependentAttribute.getConfiguration().getRecalculationScript();

                if (Strings.isNullOrEmpty(groovyScript)) {
                    continue;
                }

                String attributeCode = DynamicAttributesUtils.encodeAttributeCode(dependentAttribute.getCode());

                Object oldValue = entity.getValue(attributeCode);
                Object newValue = evaluateGroovyScript(entity, groovyScript);

                if ((oldValue == null && newValue == null)
                        || (oldValue != null && oldValue.equals(newValue))) {
                    continue;
                }

                entity.setValue(attributeCode, newValue);

                if (dependentAttribute.getConfiguration().getDependentAttributes() != null) {
                    nextLevelAttributes.addAll(dependentAttribute.getConfiguration().getDependentAttributes());
                }
            }

            needToRecalculate = nextLevelAttributes;
            recalculationLevel++;
        }
    }

    /**
     * Performs recalculation for all dynamic attributes.
     *
     * Recalculation level limited by
     * {@code cuba.dynamicAttributes.maxRecalculationLevel} application property. If this property is not defined
     * then the default value is used (default value is 10).
     *
     * @param entity entity with loaded dynamic attributes.
     */
    public void recalculateDynamicAttributes(BaseGenericIdEntity entity) {
        Collection<CategoryAttribute> independentAttributes = dynamicAttributesTools.getIndependentCategoryAttributes(entity);

        if (independentAttributes == null || independentAttributes.isEmpty()) {
            return;
        }

        for (CategoryAttribute attribute : independentAttributes) {
            recalculateDynamicAttributes(entity, attribute);
        }
    }

    protected Object evaluateGroovyScript(BaseGenericIdEntity entity, String groovyScript) {

        //noinspection unchecked
        Map<String, CategoryAttributeValue> dynamicAttributes = (Map<String, CategoryAttributeValue>) entity.getDynamicAttributes();
        Map<String, Object> dynamicAttributesValues = new HashMap<>();

        if (dynamicAttributes != null) {
            for (Map.Entry<String, CategoryAttributeValue> entry : dynamicAttributes.entrySet()) {
                dynamicAttributesValues.put(entry.getKey(), entry.getValue().getValue());
            }
        }

        Binding binding = new Binding();
        binding.setVariable("entity", entity);
        binding.setVariable("dynamicAttributes", dynamicAttributesValues);

        return scripting.evaluateGroovy(groovyScript, binding);
    }
}
