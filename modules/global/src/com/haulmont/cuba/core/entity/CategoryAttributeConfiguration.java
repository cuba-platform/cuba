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

package com.haulmont.cuba.core.entity;

import com.google.common.base.Strings;
import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.app.dynamicattributes.DynamicAttributesTools;
import com.haulmont.cuba.core.entity.annotation.SystemLevel;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.DataManager;

import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@MetaClass(name = "sys$CategoryAttributeConfiguration")
@SystemLevel
public class CategoryAttributeConfiguration extends BaseGenericIdEntity<String> {

    private static final long serialVersionUID = 2670605418267938507L;

    protected transient CategoryAttribute categoryAttribute;

    protected String id;

    @MetaProperty
    protected Integer minInt;

    @MetaProperty
    protected Double minDouble;

    @MetaProperty
    protected BigDecimal minDecimal;

    @MetaProperty
    protected Integer maxInt;

    @MetaProperty
    protected Double maxDouble;

    @MetaProperty
    protected BigDecimal maxDecimal;

    @MetaProperty
    protected String validatorGroovyScript;

    @MetaProperty
    protected String columnName;

    @MetaProperty
    protected String columnAlignment;

    @MetaProperty
    protected Integer columnWidth;

    @MetaProperty
    protected String numberFormatPattern;

    @MetaProperty
    protected String recalculationGroovyScript;

    @MetaProperty
    @Transient
    protected transient Collection<CategoryAttribute> dependentCategoryAttributes;

    @Transient
    protected List<UUID> dependsOnCategoryAttributesIds;

    @MetaProperty
    @Transient
    protected transient List<CategoryAttribute> dependsOnCategoryAttributes;

    public Integer getMinInt() {
        return minInt;
    }

    public void setMinInt(Integer minInt) {
        this.minInt = minInt;
    }

    public Double getMinDouble() {
        return minDouble;
    }

    public void setMinDouble(Double minDouble) {
        this.minDouble = minDouble;
    }

    public BigDecimal getMinDecimal() {
        return minDecimal;
    }

    public void setMinDecimal(BigDecimal minDecimal) {
        this.minDecimal = minDecimal;
    }

    public Integer getMaxInt() {
        return maxInt;
    }

    public void setMaxInt(Integer maxInt) {
        this.maxInt = maxInt;
    }

    public Double getMaxDouble() {
        return maxDouble;
    }

    public void setMaxDouble(Double maxDouble) {
        this.maxDouble = maxDouble;
    }

    public BigDecimal getMaxDecimal() {
        return maxDecimal;
    }

    public void setMaxDecimal(BigDecimal maxDecimal) {
        this.maxDecimal = maxDecimal;
    }

    public Number getMinValue() {
        if (categoryAttribute.getDataType() != null) {
            switch (categoryAttribute.getDataType()) {
                case INTEGER: return minInt;
                case DOUBLE: return minDouble;
                case DECIMAL: return minDecimal;
                default: return null;
            }
        }
        return null;
    }

    public Number getMaxValue() {
        if (categoryAttribute.getDataType() != null) {
            switch (categoryAttribute.getDataType()) {
                case INTEGER: return maxInt;
                case DOUBLE: return maxDouble;
                case DECIMAL: return maxDecimal;
                default: return null;
            }
        }
        return null;
    }

    public String getValidatorGroovyScript() {
        return validatorGroovyScript;
    }

    public void setValidatorGroovyScript(String validatorGroovyScript) {
        this.validatorGroovyScript = validatorGroovyScript;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnAlignment() {
        return columnAlignment;
    }

    public void setColumnAlignment(String columnAlignment) {
        this.columnAlignment = columnAlignment;
    }

    public Integer getColumnWidth() {
        return columnWidth;
    }

    public void setColumnWidth(Integer columnWidth) {
        this.columnWidth = columnWidth;
    }

    public CategoryAttribute getCategoryAttribute() {
        return categoryAttribute;
    }

    public void setCategoryAttribute(CategoryAttribute categoryAttribute) {
        this.categoryAttribute = categoryAttribute;
    }

    public String getNumberFormatPattern() {
        return numberFormatPattern;
    }

    public void setNumberFormatPattern(String numberFormatPattern) {
        this.numberFormatPattern = numberFormatPattern;
    }

    public String getRecalculationGroovyScript() {
        return recalculationGroovyScript;
    }

    public void setRecalculationGroovyScript(String recalculationGroovyScript) {
        this.recalculationGroovyScript = recalculationGroovyScript;
    }

    public Collection<CategoryAttribute> getDependentCategoryAttributes() {
        if (dependentCategoryAttributes == null) {
            dependentCategoryAttributes = getDynamicAttributesTools().getDependentCategoryAttributes(categoryAttribute);
        }

        return dependentCategoryAttributes;
    }

    public List<CategoryAttribute> getDependsOnCategoryAttributes() {
        if (dependsOnCategoryAttributesIds == null || dependsOnCategoryAttributesIds.isEmpty()) {
            return Collections.emptyList();
        }

        if (dependsOnCategoryAttributes == null) {
            dependsOnCategoryAttributes = getDataManager().load(CategoryAttribute.class)
                    .ids(dependsOnCategoryAttributesIds)
                    .list();
        }

        return dependsOnCategoryAttributes;
    }

    public void setDependsOnCategoryAttributes(List<CategoryAttribute> dependsOnCategoryAttributes) {
        if (dependsOnCategoryAttributes == null) {
            this.dependsOnCategoryAttributesIds = null;
            this.dependsOnCategoryAttributes = null;
            return;
        }

        this.dependsOnCategoryAttributesIds = dependsOnCategoryAttributes.stream()
                .map(BaseUuidEntity::getId)
                .collect(Collectors.toList());

        this.dependsOnCategoryAttributes = dependsOnCategoryAttributes;
    }

    public Boolean isReadOnly() {
        return !Strings.isNullOrEmpty(recalculationGroovyScript);
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        if (id == null) {
            return categoryAttribute.getId().toString() + "-Configuration";
        }
        return id;
    }

    private DataManager getDataManager() {
        return AppBeans.get(DataManager.NAME);
    }

    private DynamicAttributesTools getDynamicAttributesTools() {
        return AppBeans.get(DynamicAttributesTools.NAME);
    }
}
